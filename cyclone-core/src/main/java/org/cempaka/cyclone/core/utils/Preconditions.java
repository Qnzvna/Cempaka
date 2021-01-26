package org.cempaka.cyclone.core.utils;

public final class Preconditions
{
    public static <T> T checkNotNull(final T object)
    {
        if (object == null) {
            throw new NullPointerException();
        }
        return object;
    }

    public static void checkArgument(final boolean expression, final String description)
    {
        if (!expression) {
            throw new IllegalArgumentException(description);
        }
    }

    public static void checkArgument(final boolean expression)
    {
        checkArgument(expression, "");
    }

    public static void checkState(final boolean expression, final String description)
    {
        if (!expression) {
            throw new IllegalStateException(description);
        }
    }
}
