package com.mchange.v2.c3p0.loom;

import java.util.Timer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import com.mchange.v2.async.*;
import com.mchange.v2.c3p0.*;
import com.mchange.v2.log.*;

public final class VirtualThreadPerTaskExecutorTaskRunnerFactory extends AbstractExecutorTaskRunnerFactory
{
    private final static MLogger logger = MLog.getLogger(VirtualThreadPerTaskExecutorTaskRunnerFactory.class);

    // for lazy initialization, called only on first-use
    protected Executor findCreateExecutor( TaskRunnerInit init )
    {
	if (logger.isLoggable(MLevel.INFO))
	    logger.log(MLevel.INFO, "Note: Setting the context ClassLoader or spawning threads with caller privileges cannot be supported on virtual threads.");
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
