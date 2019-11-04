package org.cempaka.cyclone.tests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableTestExecutionProperties.class)
public interface TestExecutionProperties
{
    UUID getParcelId();

    String getTestName();

    int getLoopCount();

    int getThreadsNumber();

    Map<String, String> getParameters();

    Set<String> getNodes();

    @Nullable
    String getJvmOptions();
}
