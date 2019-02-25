package org.cempaka.cyclone.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;
import org.cempaka.cyclone.annotation.Parameter;
import org.cempaka.cyclone.annotation.Thunderbolt;

public final class Reflections
{
    public static boolean containsAnnotation(final Method method, final Class<? extends Annotation> annotationClass)
    {
        return Stream.of(method.getDeclaredAnnotations())
            .anyMatch(annotation -> annotation.annotationType() == annotationClass);
    }

    public static void invokeMethod(final Object test, final Method method)
        throws InvocationTargetException
    {
        try {
            method.invoke(test);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isThunderboltMethod(final Method method)
    {
        return Stream.of(method.getDeclaredAnnotations())
            .anyMatch(Reflections::isThunderboltAnnotation);
    }

    public static boolean isThunderboltAnnotation(final Annotation annotation)
    {
        return annotation.annotationType() == Thunderbolt.class;
    }

    public static boolean isFieldParameter(final Field field)
    {
        return Stream.of(field.getDeclaredAnnotations())
            .anyMatch(annotation -> annotation.annotationType() == Parameter.class);
    }

    public static <T> T newInstance(final Class<T> clazz)
    {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
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
}
