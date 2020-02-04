package org.cempaka.cyclone.beans;

import com.google.common.collect.ImmutableSet;
import java.util.Set;

public final class TestState
{
    public static String INITIALIZED = "INITIALIZED";
    public static String STARTED = "STARTED";
    public static String ENDED = "ENDED";
    public static String ABORTED = "ABORTED";
    public static String ERROR = "ERROR";
    public static Set<String> ALL = ImmutableSet.of(INITIALIZED,
        STARTED,
        ENDED,
        ABORTED,
        ERROR);
}
