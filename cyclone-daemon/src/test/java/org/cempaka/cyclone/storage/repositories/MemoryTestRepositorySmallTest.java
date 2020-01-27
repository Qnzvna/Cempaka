package org.cempaka.cyclone.storage.repositories;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.UUID;
import org.junit.Test;

public class MemoryTestRepositorySmallTest
{
    private final TestRepository testRepository = new MemoryTestRepository();

    @Test
    public void shouldAddMultipleTestsWithSameParcelId()
    {
        //given
        final org.cempaka.cyclone.tests.Test test1 = mock(org.cempaka.cyclone.tests.Test.class);
        final org.cempaka.cyclone.tests.Test test2 = mock(org.cempaka.cyclone.tests.Test.class);
        final UUID parcelId = UUID.randomUUID();
        given(test1.getParcelId()).willReturn(parcelId);
        given(test2.getParcelId()).willReturn(parcelId);
        final Set<org.cempaka.cyclone.tests.Test> tests = ImmutableSet.<org.cempaka.cyclone.tests.Test>builder()
            .add(test1)
            .add(test2)
            .build();
        //when
        testRepository.putAll(tests);
        final Set<org.cempaka.cyclone.tests.Test> allTests = testRepository.getAll();
        //then
        assertThat(allTests).hasSize(2);
    }
}