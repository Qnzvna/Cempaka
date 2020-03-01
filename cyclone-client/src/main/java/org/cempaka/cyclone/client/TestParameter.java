package org.cempaka.cyclone.client;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value.Immutable;

@Immutable
@ImmutableStyle
@JsonDeserialize(as = ImmutableTestParameter.class)
public interface TestParameter
{
    String getName();

    String getType();

    String getDefaultValue();
}
