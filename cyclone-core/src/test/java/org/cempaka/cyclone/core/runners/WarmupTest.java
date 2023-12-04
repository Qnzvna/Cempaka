package org.cempaka.cyclone.core.runners;

import org.cempaka.cyclone.core.measurements.MeasurementRegistry;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class WarmupTest
{
    @Test
    void shouldWarmup()
    {
        //given
        final Runner simpleRunner = Runners.simpleRunner(
            Stream.of(TestExample.class).collect(Collectors.toList()),
            Collections.emptyMap(),
            Collections.emptyMap(),
            new MeasurementRegistry()
        );
        final Runner threadRunner = Runners.threadRunner(simpleRunner, 1, 3);
        //when
        threadRunner.run();
        //then

        assertThat(TestExample.getBeforeCounter()).isEqualTo(1);
        assertThat(TestExample.getThunderboltCounter()).isEqualTo(4);
        assertThat(TestExample.getAfterCounter()).isEqualTo(1);
    }
}