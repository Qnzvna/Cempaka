package org.cempaka.cyclone.log;

import java.util.UUID;
import org.cempaka.cyclone.core.ImmutableStyle;
import org.immutables.value.Value.Immutable;

@Immutable
@ImmutableStyle
public interface LogMessage
{
    UUID getTestExecutionId();

    String getLogLine();
}
