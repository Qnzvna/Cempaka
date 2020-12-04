package org.cempaka.cyclone.beans;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableMetadata.class)
public interface Metadata
{
    String getId();

    byte[] getValue();
}
