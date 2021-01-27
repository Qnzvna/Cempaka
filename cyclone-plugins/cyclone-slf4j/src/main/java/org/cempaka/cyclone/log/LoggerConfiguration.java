package org.cempaka.cyclone.log;

import static org.cempaka.cyclone.core.utils.Preconditions.checkNotNull;

import java.util.Objects;

public class LoggerConfiguration
{
    private final String level;

    public LoggerConfiguration(final String level)
    {
        this.level = checkNotNull(level);
    }

    public String getLevel()
    {
        return level;
    }

    @Override
    public String toString()
    {
        return "LoggerConfiguration{" +
            "level=" + level +
            '}';
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final LoggerConfiguration that = (LoggerConfiguration) o;
        return Objects.equals(level, that.level);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(level);
    }
}
