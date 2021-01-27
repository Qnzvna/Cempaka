package org.cempaka.cyclone.client;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.function.Predicate;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

class StatusCodeValidator extends ResponseValidator
{
    private final int[] statusCodes;
    private final Predicate<HttpRequest> requestPredicate;

    public static ResponseValidator of(final int statusCode, Predicate<HttpRequest> requestPredicate)
    {
        return new StatusCodeValidator(new int[]{statusCode}, requestPredicate);
    }

    public static ResponseValidator of(final int[] statusCodes, Predicate<HttpRequest> requestPredicate)
    {
        return new StatusCodeValidator(statusCodes, requestPredicate);
    }

    private StatusCodeValidator(final int[] statusCodes,
                                final Predicate<HttpRequest> requestPredicate)
    {
        this.statusCodes = statusCodes;
        this.requestPredicate = checkNotNull(requestPredicate);
    }

    @Override
    public void runValidation(final HttpResponse response)
    {
        final int responseCode = response.getStatusLine().getStatusCode();
        for (int statusCode : statusCodes) {
            if (statusCode == responseCode) {
                return;
            }
        }
        try {
            final String message = response.getEntity() == null ?
                ""
                : EntityUtils.toString(response.getEntity());
            throw new InvalidResponseException(responseCode, message);
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override
    protected boolean isEnabled(final HttpRequest request)
    {
        return requestPredicate.test(request);
    }
}
