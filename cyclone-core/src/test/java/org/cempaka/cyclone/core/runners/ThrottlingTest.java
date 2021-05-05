package org.cempaka.cyclone.core.runners;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.cempaka.cyclone.core.measurements.MeasurementRegistry;
import org.junit.jupiter.api.Test;

class ThrottlingTest
{
    @Test
    void shouldThrottleExecutions()
    {
        //given
        final Runner simpleRunner = Runners.simpleRunner(
            Stream.of(ThrottledExample.class).collect(Collectors.toList()),
            Collections.emptyMap(),
            Collections.emptyMap(),
            new MeasurementRegistry()
        );
        final Runner threadRunner = Runners.threadRunner(simpleRunner, 9);
        final Runner loopRunner = Runners.loopRunner(threadRunner, 10);
        //when
        final long startTime = System.currentTimeMillis();
        loopRunner.run();
        final long executionTime = System.currentTimeMillis() - startTime;
        //then
        assertThat(executionTime).isGreaterThan(1_000);
    }
}