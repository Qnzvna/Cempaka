package org.cempaka.cyclone.core.runners;

import static org.cempaka.cyclone.core.utils.Preconditions.checkNotNull;

import java.io.Closeable;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Context of test method execution.
 */
public class ExecutionContext implements Closeable
{
    private final Class<?> testClass;
    private final Method method;
    private final long startTime;

    private Throwable throwable;
    private long endTime;

    public ExecutionContext(final Class<?> testClass,
                            final Method method)
    {
        this.testClass = checkNotNull(testClass);
        this.method = checkNotNull(method);
        this.startTime = System.currentTimeMillis();
    }

    /**
     * @return executed class
     */
    public Class<?> getTestClass()
    {
        return testClass;
    }

    /**
     * @return executed method
     */
    public Method getMethod()
    {
        return method;
    }

    /**
     * @return {@code Throwable} if method fails or {@code null} when method returned successfully
     */
    public Throwable getThrowable()
    {
        return throwable;
    }

    /**
     * @return execution time of method in ms
     */
    public long getMillisExecutionTime()
    {
        return endTime - startTime;
    }

    @Override
    public void close()
    {
        endTime = endTime == 0 ? System.currentTimeMillis() : endTime;
    }

    public void close(final Throwable throwable)
    {
        this.throwable = throwable;
        close();
    }

    @Override
    public String toString()
    {
        return "ExecutionContext{" +
            "testClass=" + testClass +
            ", method=" + method +
            ", startTime=" + startTime +
            ", throwable=" + throwable +
            ", endTime=" + endTime +
            '}';
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final ExecutionContext that = (ExecutionContext) o;
        return startTime == that.startTime &&
            endTime == that.endTime &&
            Objects.equals(testClass, that.testClass) &&
            Objects.equals(method, that.method) &&
            Objects.equals(throwable, that.throwable);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(testClass, method, startTime, throwable, endTime);
    }
}
