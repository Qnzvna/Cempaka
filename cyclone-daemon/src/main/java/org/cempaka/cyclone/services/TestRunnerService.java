package org.cempaka.cyclone.services;

import java.util.UUID;
import org.cempaka.cyclone.tests.TestExecutionProperties;

public interface TestRunnerService
{
    UUID startTest(final TestExecutionProperties testExecutionProperties);

    void abortTest(final UUID testId);
}
