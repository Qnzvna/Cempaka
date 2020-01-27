package org.cempaka.cyclone.beans;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableNodeCapacity.class)
public interface NodeCapacity
{
    int getIdleWorkers();

    int getRunningTests();
}
