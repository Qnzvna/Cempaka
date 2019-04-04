package org.cempaka.cyclone.services;

import org.cempaka.cyclone.beans.TestRunConfiguration;

import java.util.UUID;

public interface TestRunnerService
{
    UUID startTest(final TestRunConfiguration testRunConfiguration);

    void abortTest(final UUID testId);
}
