package org.cempaka.cyclone.beans.exceptions;

public class ProcessFailureException extends TestFailureException
{
    public ProcessFailureException(final int exitCode)
    {
        super(String.format("Process exited abnormally with [%s] code.", exitCode));
    }
}
