package org.cempaka.cyclone.invoker;

import static org.cempaka.cyclone.utils.Preconditions.checkArgument;
import static org.cempaka.cyclone.utils.Preconditions.checkNotNull;
import static org.cempaka.cyclone.utils.Preconditions.checkState;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.cempaka.cyclone.annotation.AfterStorm;
import org.cempaka.cyclone.annotation.BeforeStorm;
import org.cempaka.cyclone.annotation.Measurements;
import org.cempaka.cyclone.annotation.Parameter;
import org.cempaka.cyclone.annotation.Thunderbolt;
import org.cempaka.cyclone.exceptions.TestFailedException;
import org.cempaka.cyclone.metrics.Measurement;
import org.cempaka.cyclone.metrics.MeasurementRegistry;
import org.cempaka.cyclone.utils.Memoizer;
import org.cempaka.cyclone.utils.Reflections;

public class ReflectiveInvoker implements Invoker
{
    private final Class testClass;
    private final Supplier<Object> testInstance;
    private final MeasurementRegistry measurementRegistry;
    private final List<? extends Measurement> measurements;

    private ReflectiveInvoker(final Class testClass, final Map<String, String> parameters,
                              final MeasurementRegistry measurementRegistry)
    {
        this.testClass = checkNotNull(testClass);
        this.testInstance = Memoizer.memoize(() -> createTest(testClass, parameters));
        this.measurementRegistry = checkNotNull(measurementRegistry);
        this.measurements = createAllMeasurements(testClass);
    }

    public static Invoker forTestClass(final Class testClass,
                                       final Map<String, String> parameters,
                                       final MeasurementRegistry measurementRegistry)
    {
        return new ReflectiveInvoker(testClass, parameters, measurementRegistry);
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

    private List<? extends Measurement> createAllMeasurements(final Class testClass)
    {
        return getThunderboltsMethods(testClass)
            .flatMap(method -> Stream.of(method.getDeclaredAnnotations())
                .filter(annotation -> annotation.annotationType() == Measurements.class)
                .map(annotation -> (Measurements) annotation)
                .flatMap(measurements -> Stream.of(measurements.measurements()))
                .map(Reflections::newInstance))
            .peek(measurementRegistry::register)
            .collect(Collectors.toList());
    }

    private Object createTest(final Class testClass, final Map<String, String> parameters)
    {
        final Constructor[] constructors = testClass.getConstructors();
        checkArgument(constructors.length == 1,
            "Test classes should have only one constructor.");
        final Constructor constructor = constructors[0];
        checkArgument(constructor.getParameterCount() == 0,
            "Test classes should have no-params constructor only.");
        try {
            final Object testObject = constructor.newInstance();
            if (!parameters.isEmpty()) {
                Stream.of(testClass.getDeclaredFields())
                    .filter(Reflections::isFieldParameter)
                    .forEach(field -> setField(parameters, testObject, field));
            }
            return testObject;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void setField(final Map<String, String> parameters, final Object testObject,
                          final Field field)
    {
        final String name = field.getAnnotation(Parameter.class).name();
        final String value = parameters.get(name);
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

    private void runOneAnnotatedMethod(final Class testClass,
                                       final Object testInstance,
                                       final Class<? extends Annotation> annotationClass)
    {
        final Set<Method> methods = Stream.of(testClass.getDeclaredMethods())
            .filter(method -> Reflections.containsAnnotation(method, annotationClass))
            .collect(Collectors.toSet());
        System.out.println(methods);
        checkState(methods.size() <= 1,
            String.format("Test class should have only one method annotated with '%s'.",
                annotationClass.getSimpleName()));
        methods.stream().findFirst()
            .ifPresent(method -> {
                try {
                    Reflections.invokeMethod(testInstance, method);
                } catch (InvocationTargetException e) {
                    throw new TestFailedException(e.getCause());
                }
            });
    }

    private void runThunderbolts(final Class testClass, final Object testInstance)
    {
        getThunderboltsMethods(testClass)
            .forEach(method -> {
                measurements.forEach(Measurement::start);
                final List<Class<? extends Throwable>> suppressedThrowables =
                    getSuppressedThrowables(method);
                try {
                    Reflections.invokeMethod(testInstance, method);
                } catch (InvocationTargetException e) {
                    final Throwable cause = e.getCause();
                    final boolean suppressed = suppressedThrowables.stream()
                        .anyMatch(throwable -> throwable.isInstance(cause));
                    if (!suppressed) {
                        throw new TestFailedException(e.getCause());
                    }
                }
                measurements.forEach(Measurement::stop);
            });
    }

    private Stream<Method> getThunderboltsMethods(final Class testClass)
    {
        return Stream.of(testClass.getDeclaredMethods())
            .filter(Reflections::isThunderboltMethod);
    }

    private List<Class<? extends Throwable>> getSuppressedThrowables(final Method method)
    {
        return Stream.of(method.getDeclaredAnnotations())
            .filter(Reflections::isThunderboltAnnotation)
            .map(annotation -> (Thunderbolt) annotation)
            .flatMap(annotation -> Stream.of(annotation.suppressedThrowables()))
            .collect(Collectors.toList());
    }
}
