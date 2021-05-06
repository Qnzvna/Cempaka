package org.cempaka.cyclone.tests;

import static org.cempaka.cyclone.core.utils.Optionals.areExclusive;
import static org.cempaka.cyclone.core.utils.Preconditions.checkArgument;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import org.immutables.value.Value;
import org.immutables.value.Value.Check;

@Value.Immutable
@JsonDeserialize(as = ImmutableTestExecutionProperties.class)
public interface TestExecutionProperties
{
    UUID getParcelId();

    String getTestName();

    Optional<Integer> getLoopCount();

    @JsonFormat(shape = Shape.STRING)
    Optional<Duration> getDuration();

    int getThreadsNumber();

    Map<String, String> getParameters();

    Set<String> getNodes();

    @Nullable
    String getJvmOptions();

    @Check
    default void validate()
    {
        checkArgument(areExclusive(getLoopCount(), getDuration()),
            "loopCount and duration are mutually exclusive");
    }
}
