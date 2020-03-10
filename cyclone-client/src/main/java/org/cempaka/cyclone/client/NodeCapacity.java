package org.cempaka.cyclone.client;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value.Immutable;

@Immutable
@ImmutableStyle
@JsonDeserialize(as = ImmutableNodeCapacity.class)
public interface NodeCapacity
{
    int getIdleWorkers();

    int getRunningTests();
}
