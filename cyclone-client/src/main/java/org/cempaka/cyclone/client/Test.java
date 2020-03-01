package org.cempaka.cyclone.client;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Set;
import java.util.UUID;
import org.immutables.value.Value.Immutable;

@Immutable
@ImmutableStyle
@JsonDeserialize(as = ImmutableTest.class)
public interface Test
{
    UUID getParcelId();

    String getName();

    Set<TestParameter> getParameters();
}
