package org.cempaka.cyclone.configurations;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import org.cempaka.cyclone.storage.repositories.JdbiLogMessageRepository;
import org.cempaka.cyclone.storage.repositories.JdbiNodeStateRepository;
import org.cempaka.cyclone.storage.repositories.JdbiParcelRepository;
import org.cempaka.cyclone.storage.repositories.JdbiTestExecutionRepository;
import org.cempaka.cyclone.storage.repositories.JdbiTestMetricRepository;
import org.cempaka.cyclone.storage.repositories.JdbiTestRepository;
import org.cempaka.cyclone.storage.repositories.LogMessageRepository;
import org.cempaka.cyclone.storage.repositories.NodeStateDataRepository;
import org.cempaka.cyclone.storage.repositories.ParcelRepository;
import org.cempaka.cyclone.storage.repositories.TestExecutionRepository;
import org.cempaka.cyclone.storage.repositories.TestMetricRepository;
import org.cempaka.cyclone.storage.repositories.TestRepository;

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
    @Valid
    @JsonProperty("nodeStateRepository")
    private TypedConfiguration<NodeStateDataRepository> nodeStateRepositoryConfiguration =
        new TypedConfiguration<>(JdbiNodeStateRepository.class);
    @Valid
    @JsonProperty("testRepository")
    private TypedConfiguration<TestRepository> testRepositoryConfiguration =
        new TypedConfiguration<>(JdbiTestRepository.class);
    @Valid
    @JsonProperty("metricsRepository")
    private TypedConfiguration<TestMetricRepository> metricsRepository =
        new TypedConfiguration<>(JdbiTestMetricRepository.class);
    @Valid
    @JsonProperty("logRepository")
    private TypedConfiguration<LogMessageRepository> logRepository =
        new TypedConfiguration<>(JdbiLogMessageRepository.class);

    public TypedConfiguration<ParcelRepository> getParcelRepositoryConfiguration()
    {
        return parcelRepositoryConfiguration;
    }

    public TypedConfiguration<TestExecutionRepository> getTestExecutionRepositoryConfiguration()
    {
        return testExecutionRepositoryConfiguration;
    }

    public TypedConfiguration<NodeStateDataRepository> getNodeStateRepositoryConfiguration()
    {
        return nodeStateRepositoryConfiguration;
    }

    public TypedConfiguration<TestRepository> getTestRepositoryConfiguration()
    {
        return testRepositoryConfiguration;
    }

    public TypedConfiguration<TestMetricRepository> getMetricsRepository()
    {
        return metricsRepository;
    }

    public TypedConfiguration<LogMessageRepository> getLogRepository()
    {
        return logRepository;
    }
}
