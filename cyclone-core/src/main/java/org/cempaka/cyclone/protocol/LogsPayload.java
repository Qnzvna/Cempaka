package org.cempaka.cyclone.protocol;

import static org.cempaka.cyclone.utils.Preconditions.checkNotNull;

import java.util.List;

public class LogsPayload implements Payload
{
    private final List<String> logs;

    public LogsPayload(final List<String> logs)
    {
        this.logs = checkNotNull(logs);
    }

    @Override
    public PayloadType getType()
    {
        return PayloadType.LOGS;
    }

    public List<String> getLogs()
    {
        return logs;
    }
}
