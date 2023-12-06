package org.cempaka.cyclone.storage.repositories;

import static org.cempaka.cyclone.core.utils.Preconditions.checkNotNull;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.cempaka.cyclone.core.log.LogMessage;
import org.cempaka.cyclone.storage.jdbi.LogMessageDataAccess;

@Singleton
public class JdbiLogMessageRepository implements LogMessageRepository
{
    private final LogMessageDataAccess logMessageDataAccess;

    @Inject
    public JdbiLogMessageRepository(final LogMessageDataAccess logMessageDataAccess)
    {
        this.logMessageDataAccess = checkNotNull(logMessageDataAccess);
    }

    @Override
    public List<LogMessage> getNewerThan(final UUID id, final Instant timestamp)
    {
        checkNotNull(id);
        checkNotNull(timestamp);
        return logMessageDataAccess.getNewerThan(id.toString(), Timestamp.from(timestamp));
    }

    @Override
    public void put(final LogMessage logMessage)
    {
        checkNotNull(logMessage);
        logMessageDataAccess.insert(logMessage.getTestExecutionId().toString(),
            Timestamp.from(Instant.now()),
            logMessage.getLogLine());
    }

    @Override
    public void delete(final UUID id)
    {
        // TODO
    }
}
