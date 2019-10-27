package org.cempaka.cyclone.configurations;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.Valid;
import org.cempaka.cyclone.storage.repositories.FileParcelRepository;
import org.cempaka.cyclone.storage.repositories.ParcelRepository;

public class StorageConfiguration
{
    @Valid
    @JsonProperty("parcelRepository")
    private TypedConfiguration<ParcelRepository> parcelRepositoryConfiguration =
        new TypedConfiguration<>(FileParcelRepository.class);

    public TypedConfiguration<ParcelRepository> getParcelRepositoryConfiguration()
    {
        return parcelRepositoryConfiguration;
    }
}
