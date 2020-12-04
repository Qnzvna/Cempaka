package org.cempaka.cyclone.resources;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.cempaka.cyclone.client.ImmutableTestExecutionProperties;
import org.cempaka.cyclone.client.TestExecutionProperties;

final class Tests
{
    static final String API = "http://localhost:8080/api";
    static final String NODE = "cyclone";
    static final String EXAMPLES = "target/examples.jar";
    private static final String EXAMPLE_TEST_FQDN = "org.cempaka.cyclone.examples.ExampleTest";
    private static final String TOO_BIG_PAYLOAD_TEST_FQDN = "org.cempaka.cyclone.examples.TooBigPayloadTest";
    private static final String LOG_TEST_FQDN = "org.cempaka.cyclone.examples.LoggingTest";
    private static final String METADATA_TEST_FQDN = "org.cempaka.cyclone.examples.MetadataParameterExampleTest";

    static TestExecutionProperties getExampleTest(final UUID parcelId,
                                                  final Set<String> nodes,
                                                  final Map<String, String> parameters)
    {
        return getTest(parcelId, nodes, EXAMPLE_TEST_FQDN, parameters);
    }

    static TestExecutionProperties getExampleTest(final UUID parcelId, final Set<String> nodes)
    {
        return getExampleTest(parcelId, nodes, ImmutableMap.of());
    }

    static TestExecutionProperties getLongCauseTest(final UUID parcelId, final Set<String> nodes)
    {
        return getTest(parcelId, nodes, TOO_BIG_PAYLOAD_TEST_FQDN, ImmutableMap.of());
    }

    static TestExecutionProperties getLoggingTest(final UUID parcelId, final Set<String> nodes)
    {
        return getTest(parcelId, nodes, LOG_TEST_FQDN, ImmutableMap.of());
    }

    static TestExecutionProperties getMetadataTest(final UUID parcelId, final Set<String> nodes)
    {
        return getTest(parcelId, nodes, METADATA_TEST_FQDN, ImmutableMap.of());
    }

    private static TestExecutionProperties getTest(final UUID parcelId,
                                                   final Set<String> nodes,
                                                   final String testName,
                                                   final Map<String, String> parameters)
    {
        return ImmutableTestExecutionProperties.builder()
            .parcelId(parcelId)
            .testName(testName)
            .threadsNumber(1)
            .loopCount(1)
            .nodes(nodes)
            .parameters(parameters)
            .build();
    }
}
