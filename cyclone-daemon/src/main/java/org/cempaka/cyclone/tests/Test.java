package org.cempaka.cyclone.tests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Set;
import java.util.UUID;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableTest.class)
public interface Test
{
    UUID getParcelId();

    String getName();

    Set<TestParameter> getParameters();
}
