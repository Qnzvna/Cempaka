package org.cempaka.cyclone.log;

public interface LogDataSink
{
    void accept(LogMessage logMessage);
}
