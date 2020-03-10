package org.cempaka.cyclone.client;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;
import org.immutables.value.Value.Immutable;

@Immutable
@ImmutableStyle
@JsonDeserialize(as = ImmutableClusterStatus.class)
public interface ClusterStatus
{
    @JsonUnwrapped
    Map<String, Boolean> getStatus();
}
