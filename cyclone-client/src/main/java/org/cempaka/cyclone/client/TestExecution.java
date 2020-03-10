package org.cempaka.cyclone.client;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@ImmutableStyle
@JsonDeserialize(as = ImmutableTestExecution.class)
public interface TestExecution
{
    UUID getId();

    String getNode();

    String getState();

    TestExecutionProperties getProperties();
}
