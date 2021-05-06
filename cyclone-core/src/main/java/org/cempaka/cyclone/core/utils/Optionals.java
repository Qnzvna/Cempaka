package org.cempaka.cyclone.core.utils;

import java.util.Optional;

public final class Optionals
{
    public static boolean areExclusive(final Optional<?>... optionals)
    {
        boolean present = false;
        for (Optional<?> optional : optionals) {
            if (present && optional.isPresent()) {
                return false;
            }
            present = present || optional.isPresent();
        }
        return true;
    }
}
