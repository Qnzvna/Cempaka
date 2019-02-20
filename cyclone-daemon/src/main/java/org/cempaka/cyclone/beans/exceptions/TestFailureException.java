package org.cempaka.cyclone.beans.exceptions;

public class TestFailureException extends RuntimeException
{
    public TestFailureException()
    {
        super();
    }

    public TestFailureException(final String message)
    {
        super(message);
    }

    public TestFailureException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    public TestFailureException(final Throwable cause)
    {
        super(cause);
    }

    protected TestFailureException(final String message, final Throwable cause, final boolean enableSuppression,
                                   final boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
