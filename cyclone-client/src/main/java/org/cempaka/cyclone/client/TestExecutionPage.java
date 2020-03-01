package org.cempaka.cyclone.client;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import org.immutables.value.Value.Immutable;

@Immutable
@ImmutableStyle
@JsonDeserialize(as = ImmutableTestExecutionPage.class)
public interface TestExecutionPage
{
    List<TestExecution> getTestExecutions();

    boolean isHasNext();
}
