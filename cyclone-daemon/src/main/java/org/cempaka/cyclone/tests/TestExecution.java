package org.cempaka.cyclone.tests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.UUID;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableTestExecution.class)
public interface TestExecution
{
    UUID getId();

    String getNode();

    String getState();

    TestExecutionProperties getProperties();
}
