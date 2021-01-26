package org.cempaka.cyclone.measurements;


import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import org.cempaka.cyclone.runners.MeasurementsExample;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MeasurementRegistrySmallTest
{
    private MeasurementRegistry measurementRegistry;

    @BeforeEach
    void setUp()
    {
        measurementRegistry = new MeasurementRegistry();
    }

    @Test
    void shouldRegisterMeasurement()
    {
        //given
        final String name = "noop";
        final Measurement measurement = new NoopMeasurement(name);
        //when
        measurementRegistry.register(measurement);
        //then
        assertThat(measurementRegistry.get(name)).isEqualTo(measurement);
    }

    @Test
    void shouldRegisterMeasurementOnMethod() throws NoSuchMethodException
    {
        //given
        final Method method = MeasurementsExample.class.getMethod("emptyThunderbolt");
        //when
        measurementRegistry.register(method, NoopMeasurement.INSTANCE);
        //then
        assertThat(measurementRegistry.get(method, NoopMeasurement.NAME)).isEqualTo(NoopMeasurement.INSTANCE);
    }

    @Test
    void shouldGetSnapshots() throws NoSuchMethodException
    {
        //given
        final Method method = MeasurementsExample.class.getMethod("emptyThunderbolt");
        final Measurement measurementA = new Measurement("A")
        {
            @Override
            public Map<String, Double> getSnapshot()
            {
                return Collections.singletonMap("valueA", 1D);
            }
        };
        final Measurement measurementB = new Measurement("B")
        {
            @Override
            public Map<String, Double> getSnapshot()
            {
                return Collections.singletonMap("valueB", 2D);
            }
        };
        //when
        measurementRegistry.register(measurementA);
        measurementRegistry.register(method, measurementB);
        //then
        assertThat(measurementRegistry.getSnapshots())
            .containsEntry(":A:valueA", 1D)
            .containsEntry("emptyThunderbolt:B:valueB", 2D)
            .hasSize(2);
    }

    @Test
    void shouldRegisterAllMeasurements()
    {
        //given
        //when
        measurementRegistry.registerAll(MeasurementsExample.class);
        //then
        System.out.println(measurementRegistry.getSnapshots());
        assertThat(measurementRegistry.getSnapshots())
            .containsKey(":global:count")
            .containsKey("measureScope::count")
            .containsKey("counted:failure:count")
            .containsKey("counted:success:count")
            .hasSize(4);
    }
}