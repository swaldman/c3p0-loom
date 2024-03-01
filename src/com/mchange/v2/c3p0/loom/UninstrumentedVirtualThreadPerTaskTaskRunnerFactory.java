package com.mchange.v2.c3p0.loom;

import com.mchange.v2.async.*;
import com.mchange.v2.c3p0.*;
import com.mchange.v2.log.*;
import com.mchange.v2.c3p0.impl.C3P0Defaults;
import javax.sql.ConnectionPoolDataSource;
import java.util.Timer;
import java.util.TimerTask;
import com.mchange.v2.lang.ObjectUtils;

public final class UninstrumentedVirtualThreadPerTaskTaskRunnerFactory implements TaskRunnerFactory
{
    //MT: thread-safe
    final static MLogger logger = MLog.getLogger( UninstrumentedVirtualThreadPerTaskTaskRunnerFactory.class );

    public ThreadPoolReportingAsynchronousRunner createTaskRunner(
        int num_threads_if_supported,
        int max_administrative_task_time_if_supported, // in seconds!
        String contextClassLoaderSourceIfSupported,
        boolean privilege_spawned_threads_if_supported,
        String threadLabelIfSupported,
        ConnectionPoolDataSource cpds,
        Timer timer
    )
    {
	if (logger.isLoggable(MLevel.INFO))
        {
            if (!ObjectUtils.eqOrBothNull( contextClassLoaderSourceIfSupported, C3P0Defaults.contextClassLoaderSource() ))
                logger.log(MLevel.INFO, "Nondefault contextClassLoaderSource detected, but setting the context ClassLoader cannot be supported on virtual threads.");
            if ( privilege_spawned_threads_if_supported )
                logger.log(MLevel.INFO, "privilege_spawned_threads is set to true, but spawning threads with caller privileges cannot be supported on virtual threads.");
            if ( num_threads_if_supported != C3P0Defaults.numHelperThreads() )
                logger.log(MLevel.INFO, "Nondefault numHelperThreads detected, but runner creates no thread pool that parameter could apply to.");
        }
        int matt_ms = max_administrative_task_time_if_supported * 1000;
        return new Runner( timer, matt_ms );
    }

    private static class Runner implements ThreadPoolReportingAsynchronousRunner
    {
        private Timer timer;
        int     matt_ms;

        Runner( Timer timer, int matt_ms )
        {
            this.timer = timer;
            this.matt_ms = matt_ms;
        }

        public void postRunnable(Runnable r)
        {
            final Thread t = Thread.ofVirtual().start(r);
            if (matt_ms > 0)
            {
                TimerTask tt = new TimerTask()
                {
                    public void run() { t.interrupt(); }
                };
                timer.schedule( tt, matt_ms );
            }
        }

        public void close( boolean skip_remaining_tasks ) {}
        public void close() {}

        public int getThreadCount() { return -1; }
        public int getActiveCount() { return -1; }
        public int getIdleCount() { return 0; }
        public int getPendingTaskCount() { return 0; }

        public String getStatus()
        { return "UninstrumentedVirtualThreadPerTaskTaskRunnerFactory.Runner (No further information)"; }

        public String getStackTraces()
        { return "[StackTraces not available]"; }
    }
}
