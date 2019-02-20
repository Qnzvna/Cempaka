package org.cempaka.cyclone.configuration;

import org.hibernate.validator.constraints.NotEmpty;

public class StorageConfiguration
{
    @NotEmpty
    private String storagePath;
    @NotEmpty
    private String parcelRepository;
    @NotEmpty
    private String parcelMetadataRepository;

    public String getStoragePath()
    {
        return storagePath;
    }

    public String getParcelRepository()
    {
        return parcelRepository;
    }

    public String getParcelMetadataRepository()
    {
        return parcelMetadataRepository;
    }
}
