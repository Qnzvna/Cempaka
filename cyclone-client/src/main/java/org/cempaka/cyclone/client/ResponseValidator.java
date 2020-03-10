package org.cempaka.cyclone.client;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

public abstract class ResponseValidator
{
    public void validate(final HttpRequest request, final HttpResponse response)
    {
        if (isEnabled(request)) {
            runValidation(response);
        }
    }

    protected abstract void runValidation(HttpResponse response);

    protected abstract boolean isEnabled(HttpRequest request);
}
