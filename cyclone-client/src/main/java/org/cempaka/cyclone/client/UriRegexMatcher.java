package org.cempaka.cyclone.client;

import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.http.HttpRequest;

class UriRegexMatcher implements RequestMatcher
{
    private final String regex;

    public static RequestMatcher ofRegex(final String regex)
    {
        return new UriRegexMatcher(regex);
    }

    public static RequestMatcher ofFormat(final String format)
    {
        return ofRegex(format.replaceAll("\\{[^/]*}", "[^\\/]*")
            .replaceAll("\\?", "\\\\?") + "$");
    }

    private UriRegexMatcher(final String regex)
    {
        this.regex = checkNotNull(regex);
    }

    @Override
    public boolean test(final HttpRequest request)
    {
        return request.getRequestLine().getUri().matches(regex);
    }
}
