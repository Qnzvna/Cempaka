package org.cempaka.cyclone.tests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableTestParameter.class)
public interface TestParameter
{
    String getName();

    String getType();

    String getDefaultValue();
}
