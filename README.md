# c3p0-loom

c3p0-loom provides Java 21 "loom" virtual-thread based concurrency options to
the [c3p0](https://www.mchange.com/projects/c3p0/) Connection pooling and management library.

In particular, it provides implementations of [`com.mchange.v2.c3p0.TaskRunnerFactory`](https://www.mchange.com/projects/c3p0/apidocs/com/mchange/v2/c3p0/TaskRunnerFactory.html)
that you can use by setting the configuration property [`taskRunnerFactoryClassName`](https://www.mchange.com/projects/c3p0/#taskRunnerFactoryClassName).

Currently c3p0-loom includes two implementations of `TaskRunnerFactory`:

* [`VirtualThreadPerTaskExecutorTaskRunnerFactory`](src/com/mchange/v2/c3p0/loom/VirtualThreadPerTaskExecutorTaskRunnerFactory.java)
* [`UninstrumentedVirtualThreadPerTaskTaskRunnerFactory`](src/com/mchange/v2/c3p0/loom/UninstrumentedVirtualThreadPerTaskTaskRunnerFactory.java)

Conceptually, this project should really be a part of the [main c3p0 project](https://github.com/swaldman/c3p0), but main c3p0 is currently
built under Java 11 (targeting Java 7) output, while this project must be built under Java 21. So for now we are maintaining and building them
separately.

Releases of c3p0-loom share version numbers with main c3p0, however, and c3p0 is a transitive dependency of c3p0-loom. So if you wish to use
c3p0 with loom support, bringing **`com.mchange:c3p0-loom:<c3p0-version>`** should get you everything.

c3p0-loom is supported for c3p0 versions 0.10.0 and beyond.

