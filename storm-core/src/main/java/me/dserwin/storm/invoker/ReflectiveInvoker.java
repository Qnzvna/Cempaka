package me.dserwin.storm.invoker;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.ImmutableMap;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.dserwin.storm.annotation.AfterStorm;
import me.dserwin.storm.annotation.BeforeStorm;
import me.dserwin.storm.annotation.Parameter;
import me.dserwin.storm.annotation.Thunderbolt;

public class ReflectiveInvoker implements Invoker
{
    private final Class testClass;
    private final Supplier<Object> testInstance;

    private ReflectiveInvoker(final Class testClass, final Map<String, String> parameters)
    {
        this.testClass = checkNotNull(testClass);
        this.testInstance = () -> createTest(testClass, parameters);
    }

    public static Invoker forTestClass(final Class testClass)
    {
        return new ReflectiveInvoker(testClass, ImmutableMap.of());
    }

    public static Invoker forTestClass(final Class testClass, final Map<String, String> parameters)
    {
        return new ReflectiveInvoker(testClass, parameters);
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
            Stream.of(testClass.getDeclaredFields()).forEach(field -> setField(parameters, testObject, field));
            return testObject;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void setField(final Map<String, String> parameters, final Object testObject, final Field field)
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
        final Set<Method> beforeMethods = Stream.of(testClass.getDeclaredMethods())
            .filter(method -> containsAnnotation(method, annotationClass))
            .collect(Collectors.toSet());
        checkState(beforeMethods.size() == 1,
            String.format("Test class should have only one method annotated with '%s'.",
                annotationClass.getSimpleName()));
        beforeMethods.stream().findFirst().ifPresent(method -> invokeMethod(testInstance, method));
    }

    private void runThunderbolts(final Class testClass, final Object testInstance)
    {
        Stream.of(testClass.getDeclaredMethods())
            .filter(method -> containsAnnotation(method, Thunderbolt.class))
            .forEach(method -> invokeMethod(testInstance, method));
    }

    private void invokeMethod(final Object test, final Method method)
    {
        try {
            method.invoke(test);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean containsAnnotation(final Method method, final Class<? extends Annotation> annotationClass)
    {
        return Stream.of(method.getDeclaredAnnotations())
            .anyMatch(annotation -> annotation.annotationType() == annotationClass);
    }

}
