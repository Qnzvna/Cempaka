package org.cempaka.cyclone.storage.repositories;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.cempaka.cyclone.core.log.LogMessage;

public interface LogMessageRepository
{
    List<LogMessage> getNewerThan(UUID id, Instant timestamp);

    void put(LogMessage logMessage);

    void delete(UUID id);
}
