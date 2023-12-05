package org.cempaka.cyclone.webjars;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

class AssetId
{
    public final String library;
    public final String resource;

    public AssetId(String library, String resource)
    {
        this.library = library;
        this.resource = resource;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof AssetId)) {
            return false;
        }

        AssetId id = (AssetId) obj;
        return Objects.equal(library, id.library) && Objects.equal(resource, id.resource);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(library, resource);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
            .add("library", library)
            .add("resource", resource)
            .toString();
    }
}