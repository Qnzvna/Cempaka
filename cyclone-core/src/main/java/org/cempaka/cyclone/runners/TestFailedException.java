package org.cempaka.cyclone.runners;

public class TestFailedException extends RuntimeException
{
    public TestFailedException(final Throwable cause)
    {
        super(cause);
    }
}
