package org.cempaka.cyclone.client;

public class ChannelException extends CycloneClientException
{
    ChannelException()
    {
    }

    ChannelException(final String message)
    {
        super(message);
    }

    ChannelException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    ChannelException(final Throwable cause)
    {
        super(cause);
    }
}
