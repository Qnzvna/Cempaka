package org.cempaka.cyclone.resources;

import java.util.Set;
import java.util.UUID;
import org.cempaka.cyclone.tests.ImmutableTestExecutionProperties;
import org.cempaka.cyclone.tests.TestExecutionProperties;

final class Tests
{
    static final String PATH = "conf/local.yml";

    static TestExecutionProperties getExampleTest(final UUID parcelId, final Set<String> nodes)
    {
        return ImmutableTestExecutionProperties.builder()
            .parcelId(parcelId)
            .testName("org.cempaka.cyclone.examples.ExampleTest")
            .threadsNumber(1)
            .loopCount(1)
            .nodes(nodes)
            .build();
    }
}
