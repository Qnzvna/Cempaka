package org.cempaka.cyclone.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class Memoizer<T, U>
{
    private final Map<T, U> cache = new ConcurrentHashMap<>();

    private Memoizer()
    {
    }

    private Function<T, U> doMemoize(final Function<T, U> function)
    {
        return input -> cache.computeIfAbsent(input, function);
    }

    public static <T, U> Function<T, U> memoize(final Function<T, U> function)
    {
        return new Memoizer<T, U>().doMemoize(function);
    }

    public static <U> Supplier<U> memoize(final Supplier<U> supplier)
    {
        final Function<Unit, U> memoizedFunction = new Memoizer<Unit, U>()
            .doMemoize(aVoid -> supplier.get());
        return () -> memoizedFunction.apply(Unit.VOID);
    }

    private enum Unit
    {
        VOID
    }
}