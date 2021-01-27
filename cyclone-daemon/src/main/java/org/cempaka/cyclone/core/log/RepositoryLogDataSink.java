package org.cempaka.cyclone.core.log;

import static org.cempaka.cyclone.core.utils.Preconditions.checkNotNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.storage.repositories.LogMessageRepository;

@Singleton
public class RepositoryLogDataSink implements LogDataSink
{
    private final LogMessageRepository logMessageRepository;

    @Inject
    public RepositoryLogDataSink(final LogMessageRepository logMessageRepository)
    {
        this.logMessageRepository = checkNotNull(logMessageRepository);
    }

    @Override
    public void accept(final LogMessage logMessage)
    {
        checkNotNull(logMessage);
        logMessageRepository.put(logMessage);
    }
}
