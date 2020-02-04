package org.cempaka.cyclone.resources;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import org.cempaka.cyclone.tests.TestExecution;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableTestExecutionsPage.class)
public interface TestExecutionsPage
{
    List<TestExecution> getTestExecutions();

    boolean isHasNext();
}
