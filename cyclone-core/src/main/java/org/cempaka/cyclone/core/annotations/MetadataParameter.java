package org.cempaka.cyclone.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate {@code byte[]} parameter to inject external metadata.
 * <p>
 * Cyclone will automatically inject metadata values at the start of the test so thunderbolts methods can use them.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface MetadataParameter
{
    String value();
}
