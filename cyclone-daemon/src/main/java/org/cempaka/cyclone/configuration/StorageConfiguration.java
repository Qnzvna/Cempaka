package org.cempaka.cyclone.configuration;

import org.hibernate.validator.constraints.NotEmpty;

public class StorageConfiguration
{
    @NotEmpty
    private String storagePath;

    public String getStoragePath()
    {
        return storagePath;
    }
}
