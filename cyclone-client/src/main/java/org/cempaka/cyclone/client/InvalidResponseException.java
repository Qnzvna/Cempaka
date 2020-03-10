package org.cempaka.cyclone.client;

public class InvalidResponseException extends CycloneClientException
{
    InvalidResponseException(final int statusCode, final String message)
    {
        super(String.format("Invalid response from server with '%s' code. Reason: '%s'", statusCode, message));
    }
}
