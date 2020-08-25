package org.cempaka.cyclone.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// TODO javadocs
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface MetadataParameter
{
    String value();
}
