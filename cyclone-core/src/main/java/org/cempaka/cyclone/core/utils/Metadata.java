package org.cempaka.cyclone.core.utils;

import java.util.regex.Pattern;

public final class Metadata
{
    public static final Pattern TEST_PATTERN = Pattern.compile(".*[Tt]est.*");
    public static final String SEPARATOR = ";";
    public static final String PARAMETER_SEPARATOR = ":";
    public static final String TEST_SEPARATOR = "\n";
}
