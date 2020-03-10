package org.cempaka.cyclone.client;

import java.util.function.Predicate;
import org.apache.http.HttpRequest;

public interface RequestMatcher extends Predicate<HttpRequest>
{
}
