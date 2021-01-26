package org.cempaka.cyclone.measurements;

import static org.cempaka.cyclone.utils.Preconditions.checkNotNull;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.cempaka.cyclone.utils.Reflections;

public class MeasurementRegistry
{
    private static final String DELIMITER = ":";

    private final Map<MeasurementKey, Measurement> measurements;

    public MeasurementRegistry()
    {
        measurements = new ConcurrentHashMap<>();
    }

    public void register(final Measurement measurement)
    {
        measurements.put(MeasurementKey.of(measurement.getName()), measurement);
    }

    public void register(final Method method, final Measurement measurement)
    {
        checkNotNull(method);
        measurements.put(MeasurementKey.of(method, measurement.getName()), measurement);
    }

    public void registerAll(final Class<?> testClass)
    {
        Reflections.getThunderboltMethods(testClass)
            .forEach(method -> {
                Reflections.getMeasuredAnnotations(method)
                    .forEach(measured -> {
                        final Measurement measurement = Reflections.newInstance(measured.value(), measured.name());
                        register(method, measurement);
                    });
                Reflections.getMeasureAnnotatedParameters(method)
                    .forEach(parameter -> {
                        final Measure measure = parameter.getAnnotation(Measure.class);
                        final Class<?> measurementClass = parameter.getType();
                        final Measurement measurement =
                            (Measurement) Reflections.newInstance(measurementClass, measure.value());
                        register(method, measurement);
                    });
            });
        Reflections.getMeasureAnnotatedFields(testClass)
            .forEach(field -> {
                final Measure measure = field.getAnnotation(Measure.class);
                final Class<?> measurementClass = field.getType();
                final Measurement measurement =
                    (Measurement) Reflections.newInstance(measurementClass, measure.value());
                register(measurement);
            });
    }

    public Measurement get(final String name)
    {
        return measurements.getOrDefault(MeasurementKey.of(name), NoopMeasurement.INSTANCE);
    }

    public Measurement get(final Method method, final String name)
    {
        checkNotNull(method);
        return measurements.getOrDefault(MeasurementKey.of(method, name), NoopMeasurement.INSTANCE);
    }

    public Map<String, Double> getSnapshots()
    {
        return measurements.entrySet()
            .stream()
            .flatMap(entry -> {
                final MeasurementKey key = entry.getKey();
                final Measurement measurement = entry.getValue();
                return measurement.getSnapshot().entrySet().stream()
                    .map(snapshotEntry -> Snapshot.of(key, snapshotEntry.getKey(), snapshotEntry.getValue()));
            })
            .collect(Collectors.toMap(Snapshot::getName, Snapshot::getValue));
    }

    private static final class MeasurementKey
    {
        private final Method method;
        private final String name;

        private static MeasurementKey of(final Method method, final String name)
        {
            return new MeasurementKey(method, name);
        }

        private static MeasurementKey of(final String name)
        {
            return new MeasurementKey(null, name);
        }

        private MeasurementKey(final Method method, final String name)
        {
            this.method = method;
            this.name = checkNotNull(name);
        }

        public Method getMethod()
        {
            return method;
        }

        public String getName()
        {
            return name;
        }

        @Override
        public boolean equals(final Object o)
        {
            if (this == o) { return true; }
            if (o == null || getClass() != o.getClass()) { return false; }
            final MeasurementKey that = (MeasurementKey) o;
            return Objects.equals(method, that.method) &&
                Objects.equals(name, that.name);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(method, name);
        }

        @Override
        public String toString()
        {
            return method == null ?
                DELIMITER + name :
                method.getName() + DELIMITER + name;
        }
    }

    private static final class Snapshot
    {
        private final String name;
        private final double value;

        private static Snapshot of(final MeasurementKey key, final String name, final double value)
        {
            return new Snapshot(key.toString() + DELIMITER + name, value);
        }

        private Snapshot(final String name, final double value)
        {
            this.name = checkNotNull(name);
            this.value = value;
        }

        public String getName()
        {
            return name;
        }

        public double getValue()
        {
            return value;
        }
    }
}
