package org.cempaka.cyclone.beans.exceptions;

public class ProcessFailureException extends TestFailureException
{
    public ProcessFailureException(final int exitCode, final String errorMessage)
    {
        super(String.format("Process exited abnormally with [%s] code and message [%s].", exitCode, errorMessage));
    }
}
