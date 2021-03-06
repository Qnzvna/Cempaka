package org.cempaka.cyclone.core.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;
import org.cempaka.cyclone.core.annotations.MetadataParameter;
import org.cempaka.cyclone.core.annotations.Parameter;
import org.cempaka.cyclone.core.annotations.Thunderbolt;
import org.cempaka.cyclone.core.measurements.Measure;
import org.cempaka.cyclone.core.measurements.Measured;

public final class Reflections
{
    public static boolean containsAnnotation(final Method method, final Class<? extends Annotation> annotationClass)
    {
        return Stream.of(method.getDeclaredAnnotations())
            .anyMatch(annotation -> annotation.annotationType().equals(annotationClass));
    }

    public static void invokeMethod(final Object test, final Method method, final Object[] args)
        throws InvocationTargetException
    {
        try {
            method.invoke(test, args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void invokeMethod(final Object test, final Method method) throws InvocationTargetException
    {
        try {
            method.invoke(test);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Stream<Method> getThunderboltMethods(final Class testClass)
    {
        return Stream.of(testClass.getDeclaredMethods()).filter(Reflections::isThunderboltMethod);
    }

    public static boolean isThunderboltMethod(final Method method)
    {
        return Stream.of(method.getDeclaredAnnotations()).anyMatch(Reflections::isThunderboltAnnotation);
    }

    public static boolean isThunderboltAnnotation(final Annotation annotation)
    {
        return annotation.annotationType().equals(Thunderbolt.class);
    }

    public static boolean isFieldParameter(final Field field)
    {
        return Stream.of(field.getDeclaredAnnotations())
            .anyMatch(annotation -> annotation.annotationType().equals(Parameter.class));
    }

    public static boolean isFieldMetadataParameter(final Field field)
    {
        return Stream.of(field.getDeclaredAnnotations())
            .anyMatch(annotation -> annotation.annotationType().equals(MetadataParameter.class));
    }

    public static void setFieldValue(final Field field, final Object object, final Object value)
    {
        field.setAccessible(true);
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T newInstance(final Class<T> clazz)
    {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T newInstance(final Class<T> clazz, final Object... arguments)
    {
        try {
            //noinspection unchecked
            return Arrays.stream(clazz.getConstructors())
                .filter(constructor -> constructor.getParameterCount() == arguments.length)
                .filter(constructor -> {
                    final Class<?>[] types = constructor.getParameterTypes();
                    for (int i = 0; i < types.length; i++) {
                        if (!arguments[i].getClass().equals(types[i])) {
                            return false;
                        }
                    }
                    return true;
                })
                .findFirst()
                .map(constructor -> (Constructor<T>) constructor)
                .orElseThrow(() -> new IllegalArgumentException("Matching constructor can't be found."))
                .newInstance(arguments);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getFieldValue(final Object object, final Field field)
    {
        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Stream<Measured> getMeasuredAnnotations(final Method method)
    {
        return getAnnotations(method, Measured.class);
    }

    public static <T extends Annotation> Stream<T> getAnnotations(final Method method, final Class<T> annotationClass)
    {
        return Stream.of(method.getAnnotations())
            .flatMap(annotation -> {
                final Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType.equals(annotationClass)) {
                    //noinspection unchecked
                    return Stream.of((T) annotation);
                } else {
                    return Arrays.stream(annotationType.getDeclaredAnnotationsByType(annotationClass));
                }
            });
    }

    public static Stream<java.lang.reflect.Parameter> getMeasureAnnotatedParameters(final Method method)
    {
        return Stream.of(method.getParameters()).filter(parameter -> parameter.isAnnotationPresent(Measure.class));
    }

    public static Stream<Field> getMeasureAnnotatedFields(final Class testClass)
    {
        return Stream.of(testClass.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Measure.class));
    }
}
