package org.cempaka.cyclone.configurations;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.Valid;
import org.cempaka.cyclone.storage.repositories.JdbiParcelRepository;
import org.cempaka.cyclone.storage.repositories.JdbiTestExecutionRepository;
import org.cempaka.cyclone.storage.repositories.ParcelRepository;
import org.cempaka.cyclone.storage.repositories.TestExecutionRepository;

public class StorageConfiguration
{
    @Valid
    @JsonProperty("parcelRepository")
    private TypedConfiguration<ParcelRepository> parcelRepositoryConfiguration =
        new TypedConfiguration<>(JdbiParcelRepository.class);
    @Valid
    @JsonProperty("executionRepository")
    private TypedConfiguration<TestExecutionRepository> testExecutionRepositoryConfiguration =
        new TypedConfiguration<>(JdbiTestExecutionRepository.class);

    public TypedConfiguration<ParcelRepository> getParcelRepositoryConfiguration()
    {
        return parcelRepositoryConfiguration;
    }

    public TypedConfiguration<TestExecutionRepository> getTestExecutionRepositoryConfiguration()
    {
        return testExecutionRepositoryConfiguration;
    }
}
