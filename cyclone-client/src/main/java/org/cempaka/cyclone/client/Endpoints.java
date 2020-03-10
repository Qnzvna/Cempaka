package org.cempaka.cyclone.client;

final class Endpoints
{
    private Endpoints()
    {
    }

    static final String CLUSTER_STATUS = "/cluster/status";
    static final String CLUSTER_NODE_CAPACITY = "/cluster/{0}/capacity";
    static final String PARCELS = "/parcels";
    static final String PARCEL = PARCELS + "/{0}";
    static final String TESTS = "/tests";
    static final String START_TEST = "/tests/start";
    static final String STOP_TEST = "/tests/{0}/stop";
    static final String TEST_EXECUTIONS = "/tests/executions";
    static final String TEST_EXECUTIONS_QUERY = "/tests/executions?{0}";
    static final String TEST_EXECUTION = "/tests/executions/{0}";
    static final String TEST_EXECUTION_METRICS = "/tests/executions/{0}/metrics";
    static final String TEST_EXECUTIONS_SEARCH = "/tests/executions/search?{0}";
    static final String TEST_EXECUTIONS_KEYS = "/tests/executions/keys";
}
