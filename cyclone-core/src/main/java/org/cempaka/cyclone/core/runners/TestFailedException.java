package org.cempaka.cyclone.core.runners;

public class TestFailedException extends RuntimeException
{
    public TestFailedException(final Throwable cause)
    {
        super(cause);
    }
}
