package org.cempaka.cyclone.core.log;

public interface LogDataSink
{
    void accept(LogMessage logMessage);
}
