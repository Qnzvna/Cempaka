package org.cempaka.cyclone.client;

public class DataProcessingException extends CycloneClientException
{
    DataProcessingException()
    {
    }

    DataProcessingException(final String message)
    {
        super(message);
    }

    DataProcessingException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    DataProcessingException(final Throwable cause)
    {
        super(cause);
    }
}
