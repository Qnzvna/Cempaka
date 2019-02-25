package org.cempaka.cyclone.cli;

import static org.cempaka.cyclone.utils.Metadata.PARAMETER_SEPARATOR;
import static org.cempaka.cyclone.utils.Metadata.SEPARATOR;
import static org.cempaka.cyclone.utils.Metadata.TEST_PATTERN;
import static org.cempaka.cyclone.utils.Metadata.TEST_SEPARATOR;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import java.io.IOException;
import java.util.stream.Stream;
import org.cempaka.cyclone.utils.Reflections;

public class MetadataPrint
{
    public static void main(String[] args) throws IOException
    {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ClassPath.from(classLoader).getTopLevelClasses().stream()
            .filter(MetadataPrint::isTestClass)
            .map(ClassInfo::load)
            .filter(clazz -> Stream.of(clazz.getDeclaredMethods()).anyMatch(Reflections::isThunderboltMethod))
            .forEach(MetadataPrint::printTestMetadata);
    }

    private static void printTestMetadata(final Class<?> clazz)
    {
        final Object object = Reflections.newInstance(clazz);
        System.out.print(clazz.getName());
        printSeparator();
        Stream.of(clazz.getDeclaredFields())
            .filter(Reflections::isFieldParameter)
            .forEach(field -> {
                field.setAccessible(true);
                System.out.print(field.getName() +
                    PARAMETER_SEPARATOR +
                    field.getType().getSimpleName() +
                    PARAMETER_SEPARATOR +
                    Reflections.getFieldValue(object, field));
                printSeparator();
            });
        System.out.print(TEST_SEPARATOR);
    }

    private static boolean isTestClass(final ClassInfo classInfo)
    {
        return TEST_PATTERN.asPredicate().test(classInfo.getSimpleName());
    }

    private static void printSeparator()
    {
        System.out.print(SEPARATOR);
    }
}
