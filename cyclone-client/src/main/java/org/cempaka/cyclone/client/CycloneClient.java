package org.cempaka.cyclone.client;

import java.io.File;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface CycloneClient
{
    Map<String, Boolean> getClusterStatus();

    NodeCapacity getNodeCapacity(String node);

    UUID uploadParcel(File parcel);

    UUID uploadParcel(ParcelUpload parcelUpload);

    void deleteParcel(UUID parcelId);

    Set<Test> getTests();

    UUID startTest(TestExecutionProperties testExecutionProperties);

    void stopTest(UUID testExecutionId);

    TestExecutionPage getTestExecutions();

    TestExecutionPage getTestExecutionsLimitedBy(int limit);

    TestExecutionPage getTestExecutionsOffsetBy(int offset);

    TestExecutionPage getTestExecutions(int limit, int offset);

    Set<MetricDataPoint> getTestExecutionMetrics(UUID testExecutionId);

    TestExecutionPage searchTestExecutionsByStates(Set<String> states);

    TestExecutionPage searchTestExecutionsByNames(Set<String> names);

    TestExecutionPage searchTestExecutions(Set<String> states, Set<String> names);

    Set<TestExecution> getTestExecutions(UUID testExecutionId);

    void deleteTestExecution(UUID testExecutionId);

    Set<UUID> getTestExecutionsIds();

    Set<String> getTestExecutionLogMessages(UUID testExecutionId, Instant from);
}
