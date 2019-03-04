package org.cempaka.cyclone.storage;

import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface TestRunStackTraceDataAcess
{
    @SqlUpdate("INSERT INTO test_run_stack_traces (test_run_id, stack_trace) VALUES (?, ?)")
    void insertEvent(String testRunId, String stackTrace);
}
