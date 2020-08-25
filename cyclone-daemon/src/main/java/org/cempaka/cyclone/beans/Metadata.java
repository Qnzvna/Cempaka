package org.cempaka.cyclone.beans;

import org.immutables.value.Value.Immutable;

@Immutable
public interface Metadata
{
    String getId();

    byte[] getValue();
}
