package org.cempaka.cyclone.invoker;

import org.cempaka.cyclone.measurements.Measurement;
import org.cempaka.cyclone.measurements.MeasurementRegistry;
import org.cempaka.cyclone.utils.Reflections;

class MeasurementRegistrar
{
    void registerAll(final Class testClass, final MeasurementRegistry measurementRegistry)
    {
        Reflections.getThunderboltMethods(testClass)
            .forEach(method -> Reflections.getMeasureAnnotations(method)
                .forEach(measure -> {
                    final Measurement measurement = Reflections.newInstance(measure.value(), measure.name());
                    measurementRegistry.register(method, measurement);
                }));
    }
}
