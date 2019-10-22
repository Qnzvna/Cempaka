package org.cempaka.cyclone.invoker;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.cempaka.cyclone.measurements.CounterMeasurement;
import org.cempaka.cyclone.measurements.MeasurementRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MeasurementRegistrarTest
{
    @Mock
    private MeasurementRegistry measurementRegistry;

    private final MeasurementRegistrar measurementRegistrar = new MeasurementRegistrar();

    @Test
    public void shouldRegisterCounted() throws NoSuchMethodException
    {
        //given
        //when
        measurementRegistrar.registerAll(MeasurementsExample.class, measurementRegistry);
        //then
        verify(measurementRegistry, times(2))
            .register(eq(MeasurementsExample.class.getMethod("counted")), any(CounterMeasurement.class));
    }

    @Test
    public void shouldRegisterCustomMethodMeasurement() throws NoSuchMethodException
    {
        //given
        //when
        measurementRegistrar.registerAll(MeasurementsExample.class, measurementRegistry);
        //then
        verify(measurementRegistry, times(1))
            .register(eq(MeasurementsExample.class.getMethod("customCount")), any(CounterMeasurement.class));
    }
}