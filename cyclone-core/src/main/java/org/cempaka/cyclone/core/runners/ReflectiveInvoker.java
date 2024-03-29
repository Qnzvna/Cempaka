package org.cempaka.cyclone.core.runners;

import static org.cempaka.cyclone.core.utils.Preconditions.checkArgument;
import static org.cempaka.cyclone.core.utils.Preconditions.checkNotNull;
import static org.cempaka.cyclone.core.utils.Preconditions.checkState;
import static org.cempaka.cyclone.core.utils.Reflections.containsAnnotation;
import static org.cempaka.cyclone.core.utils.Reflections.getAnnotations;
import static org.cempaka.cyclone.core.utils.Reflections.getMeasureAnnotatedFields;
import static org.cempaka.cyclone.core.utils.Reflections.getMeasureAnnotatedParameters;
import static org.cempaka.cyclone.core.utils.Reflections.getMeasuredAnnotations;
import static org.cempaka.cyclone.core.utils.Reflections.getThunderboltMethods;
import static org.cempaka.cyclone.core.utils.Reflections.invokeMethod;
import static org.cempaka.cyclone.core.utils.Reflections.newInstance;
import static org.cempaka.cyclone.core.utils.Reflections.setFieldValue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.cempaka.cyclone.core.annotations.AfterStorm;
import org.cempaka.cyclone.core.annotations.BeforeStorm;
import org.cempaka.cyclone.core.annotations.MetadataParameter;
import org.cempaka.cyclone.core.annotations.Parameter;
import org.cempaka.cyclone.core.annotations.SuppressThrowables;
import org.cempaka.cyclone.core.annotations.Throttle;
import org.cempaka.cyclone.core.measurements.Measure;
import org.cempaka.cyclone.core.measurements.Measurement;
import org.cempaka.cyclone.core.measurements.MeasurementRegistry;
import org.cempaka.cyclone.core.utils.Memoizer;
import org.cempaka.cyclone.core.utils.Reflections;

class ReflectiveInvoker implements Invoker
{
    private final Class<?> testClass;
    private final MeasurementRegistry measurementRegistry;
    private final Supplier<Object> testInstance;

    private long throttleMultiplier = 1;

    private ReflectiveInvoker(final Class<?> testClass,
                              final Map<String, String> parameters,
                              final Map<String, String> metadata,
                              final MeasurementRegistry measurementRegistry)
    {
        this.testClass = checkNotNull(testClass);
        this.measurementRegistry = checkNotNull(measurementRegistry);
        this.testInstance = Memoizer.memoize(() -> createTest(testClass, parameters, metadata));
        this.measurementRegistry.registerAll(testClass);
    }

    public static Invoker forTestClass(final Class<?> testClass,
                                       final Map<String, String> parameters,
                                       final MeasurementRegistry measurementRegistry)
    {
        return forTestClass(testClass, parameters, new HashMap<>(), measurementRegistry);
    }

    public static Invoker forTestClass(final Class<?> testClass,
                                       final Map<String, String> parameters,
                                       final Map<String, String> metadata,
                                       final MeasurementRegistry measurementRegistry)
    {
        return new ReflectiveInvoker(testClass, parameters, metadata, measurementRegistry);
    }

    @Override
    public void setThrottleMultiplier(final long multiplier)
    {
        this.throttleMultiplier = multiplier;
    }

    @Override
    public void invokeBefore()
    {
        runOneAnnotatedMethod(testClass, testInstance.get(), BeforeStorm.class);
    }

    @Override
    public void invoke()
    {
        runThunderbolts(testClass, testInstance.get());
    }

    @Override
    public void invokeAfter()
    {
        runOneAnnotatedMethod(testClass, testInstance.get(), AfterStorm.class);
    }

    private Object createTest(final Class<?> testClass,
                              final Map<String, String> parameters,
                              final Map<String, String> metadata)
    {
        final Constructor<?>[] constructors = testClass.getConstructors();
        checkArgument(constructors.length == 1,
            "Test classes should have only one constructor.");
        final Constructor<?> constructor = constructors[0];
        checkArgument(constructor.getParameterCount() == 0,
            "Test classes should have no-params constructor only.");
        try {
            final Object testObject = constructor.newInstance();
            if (!parameters.isEmpty()) {
                Stream.of(testClass.getDeclaredFields())
                    .filter(Reflections::isFieldParameter)
                    .forEach(field -> setParameterField(parameters, testObject, field));
            }
            Stream.of(testClass.getDeclaredFields())
                .filter(Reflections::isFieldMetadataParameter)
                .forEach(field -> setMetadataParameter(field, testObject, metadata));
            getMeasureAnnotatedFields(testClass)
                .forEach(field -> setFieldValue(field,
                    testObject,
                    measurementRegistry.get(field.getAnnotation(Measure.class).value())));
            return testObject;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void setParameterField(final Map<String, String> parameters,
                                   final Object testObject,
                                   final Field field)
    {
        final String name = field.getAnnotation(Parameter.class).name();
        final String value = parameters.get(name);
        if (value != null) {
            try {
                field.setAccessible(true);
                final Class<?> fieldType = field.getType();
                if (fieldType.equals(int.class) || fieldType.equals(Integer.class)) {
                    field.set(testObject, Integer.valueOf(value));
                } else if (fieldType.equals(long.class) || fieldType.equals(Long.class)) {
                    field.set(testObject, Long.valueOf(value));
                } else if (fieldType.equals(double.class) || fieldType.equals(Double.class)) {
                    field.set(testObject, Double.valueOf(value));
                } else if (fieldType.equals(float.class) || fieldType.equals(Float.class)) {
                    field.set(testObject, Double.valueOf(value));
                } else if (fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)) {
                    field.set(testObject, Boolean.valueOf(value));
                } else if (fieldType.equals(String.class)) {
                    field.set(testObject, value);
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void setMetadataParameter(final Field field,
                                      final Object testObject,
                                      final Map<String, String> metadata)
    {
        final String id = field.getAnnotation(MetadataParameter.class).value();
        try {
            field.setAccessible(true);
            final Class<?> fieldType = field.getType();
            final byte[] value = Base64.getDecoder().decode(metadata.getOrDefault(id, ""));
            if (fieldType.equals(byte[].class)) {
                field.set(testObject, value);
            } else {
                throw new IllegalArgumentException(
                    "MetadataParameter annotation should be of byte[] type.");
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void runOneAnnotatedMethod(final Class testClass,
                                       final Object testInstance,
                                       final Class<? extends Annotation> annotationClass)
    {
        final Set<Method> methods = Stream.of(testClass.getDeclaredMethods())
            .filter(method -> containsAnnotation(method, annotationClass))
            .collect(Collectors.toSet());
        System.out.println(methods);
        checkState(methods.size() <= 1,
            String.format("Test class should have only one method annotated with '%s'.",
                annotationClass.getSimpleName()));
        methods.stream().findFirst()
            .ifPresent(method -> {
                try {
                    invokeMethod(testInstance, method);
                } catch (InvocationTargetException e) {
                    throw new TestFailedException(e.getCause());
                }
            });
    }

    private void runThunderbolts(final Class testClass, final Object testInstance)
    {
        getThunderboltMethods(testClass)
            .forEach(method -> {
                final ExecutionContext executionContext = new ExecutionContext(testClass, method);
                try {
                    invokeMethod(testInstance, method, getMeasurements(method));
                    executionContext.close();
                } catch (InvocationTargetException e) {
                    final Throwable cause = e.getCause();
                    if (!isThrowableSuppressed(method, cause)) {
                        throw new TestFailedException(e.getCause());
                    } else {
                        executionContext.close(cause);
                    }
                }
                throttle(method, executionContext);
                getMeasuredAnnotations(method)
                    .forEach(measure -> {
                        final Measurement measurement = measurementRegistry.get(method, measure.name());
                        newInstance(measure.ticker()).tick(measurement, executionContext);
                    });
            });
    }

    private Measurement[] getMeasurements(final Method method)
    {
        return getMeasureAnnotatedParameters(method)
            .map(parameter ->
                measurementRegistry.get(method, parameter.getAnnotation(Measure.class).value()))
            .collect(Collectors.toList())
            .toArray(new Measurement[]{});
    }

    private boolean isThrowableSuppressed(final Method method, final Throwable throwable)
    {
        return getAnnotations(method, SuppressThrowables.class)
            .anyMatch(suppressThrowables -> suppressThrowables.value().length == 0 ||
                Stream.of(suppressThrowables.value()).anyMatch(clazz -> clazz.isAssignableFrom(throwable.getClass())));
    }

    private void throttle(final Method method, final ExecutionContext executionContext)
    {
        final long throttle = getAnnotations(method, Throttle.class).findFirst()
            .map(Throttle::value)
            .orElse(0L);
        if (throttle > 0) {
            try {
                final long sleepTime =
                    throttleMultiplier * (1_000 / throttle - executionContext.getMillisExecutionTime());
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            } catch (InterruptedException ignored) {
            }
        }
    }
}
