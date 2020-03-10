package org.cempaka.cyclone.client;

class CycloneClientException extends RuntimeException
{
    CycloneClientException()
    {
    }

    CycloneClientException(final String message)
    {
        super(message);
    }

    CycloneClientException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    CycloneClientException(final Throwable cause)
    {
        super(cause);
    }
}
