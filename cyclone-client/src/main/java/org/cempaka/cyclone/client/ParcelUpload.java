package org.cempaka.cyclone.client;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value.Immutable;

@Immutable
@ImmutableStyle
@JsonDeserialize(as = ImmutableParcelUpload.class)
public interface ParcelUpload
{
    String getLocation();
}
