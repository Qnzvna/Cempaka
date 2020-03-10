package org.cempaka.cyclone.client;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@ImmutableStyle
@JsonDeserialize(as = ImmutableTestExecutionProperties.class)
public interface TestExecutionProperties
{
    UUID getParcelId();

    String getTestName();

    int getLoopCount();

    int getThreadsNumber();

    Map<String, String> getParameters();

    Set<String> getNodes();

    Optional<String> getJvmOptions();
}
