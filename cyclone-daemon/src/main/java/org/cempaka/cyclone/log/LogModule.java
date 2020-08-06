package org.cempaka.cyclone.log;

import com.google.inject.PrivateModule;

public class LogModule extends PrivateModule
{
    @Override
    public void configure()
    {
        bind(LogDataSink.class).to(RepositoryLogDataSink.class);
        expose(LogDataSink.class);
    }
}
