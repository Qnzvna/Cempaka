package org.cempaka.cyclone.invoker;

import org.cempaka.cyclone.measurements.Measure;
import org.cempaka.cyclone.measurements.Measurement;
import org.cempaka.cyclone.measurements.MeasurementRegistry;
import org.cempaka.cyclone.utils.Reflections;

class MeasurementRegistrar
{
    void registerAll(final Class testClass, final MeasurementRegistry measurementRegistry)
    {
        Reflections.getThunderboltMethods(testClass)
            .forEach(method -> {
                Reflections.getMeasuredAnnotations(method)
                    .forEach(measured -> {
                        final Measurement measurement = Reflections.newInstance(measured.value(), measured.name());
                        measurementRegistry.register(method, measurement);
                    });
                Reflections.getMeasureAnnotatedParameters(method)
                    .forEach(parameter -> {
                        final Measure measure = parameter.getAnnotation(Measure.class);
                        final Class<?> measurementClass = parameter.getType();
                        final Measurement measurement =
                            (Measurement) Reflections.newInstance(measurementClass, measure.value());
                        measurementRegistry.register(method, measurement);
                    });
            });
        Reflections.getMeasureAnnotatedFields(testClass)
            .forEach(field -> {
                final Measure measure = field.getAnnotation(Measure.class);
                final Class<?> measurementClass = field.getType();
                final Measurement measurement =
                    (Measurement) Reflections.newInstance(measurementClass, measure.value());
                measurementRegistry.register(measurement);
            });
    }
}
