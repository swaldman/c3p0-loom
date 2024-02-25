package com.mchange.v2.c3p0.loom;

import java.util.Timer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import com.mchange.v2.async.*;
import com.mchange.v2.c3p0.*;
import com.mchange.v2.log.*;

import com.mchange.v2.c3p0.impl.C3P0Defaults;
import com.mchange.v2.lang.ObjectUtils;

public final class VirtualThreadPerTaskExecutorTaskRunnerFactory extends AbstractExecutorTaskRunnerFactory
{
    private final static MLogger logger = MLog.getLogger(VirtualThreadPerTaskExecutorTaskRunnerFactory.class);

    // for lazy initialization, called only on first-use
    protected Executor findCreateExecutor( TaskRunnerInit init )
    {
	if (logger.isLoggable(MLevel.INFO))
        {
            if (!ObjectUtils.eqOrBothNull( init.contextClassLoaderSourceIfSupported, C3P0Defaults.contextClassLoaderSource() ))
                logger.log(MLevel.INFO, "Nondefault contextClassLoaderSource detected, but setting the context ClassLoader cannot be supported on virtual threads.");
            if ( init.privilege_spawned_threads_if_supported )
                logger.log(MLevel.INFO, "privilege_spawned_threads is set to true, but spawning threads with caller privileges cannot be supported on virtual threads.");
            if ( init.num_threads_if_supported != C3P0Defaults.numHelperThreads() )
                logger.log(MLevel.INFO, "Nondefault numHelperThreads detected, but VirtualThreadPerTaskExecutorAsynchronousRunner creates no thread pool that parameter could apply to.");
        }
	return Executors.newVirtualThreadPerTaskExecutor();
    }

    protected boolean taskRunnerOwnsExecutor() { return true; }

    protected ThreadPoolReportingAsynchronousRunner createTaskRunner( TaskRunnerInit init, Timer timer )
    { return new VirtualThreadPerTaskExecutorAsynchronousRunner( init, timer ); }

    protected final class VirtualThreadPerTaskExecutorAsynchronousRunner extends AbstractExecutorAsynchronousRunner
    {
        protected VirtualThreadPerTaskExecutorAsynchronousRunner( TaskRunnerInit init, Timer timer )
        { super( init, timer ); }
    }
}
