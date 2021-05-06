package org.cempaka.cyclone.core.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class OptionalsTest
{
    @Test
    void shouldReturnTrueForExclusiveOptionals()
    {
        //given
        //when
        final boolean exclusive = Optionals.areExclusive(Optional.empty(), Optional.of("A"), Optional.empty());
        //then
        assertThat(exclusive).isTrue();
    }

    @Test
    void shouldReturnFalseForNonExclusiveOptionals()
    {
        //given
        //when
        final boolean exclusive = Optionals.areExclusive(Optional.of("A"), Optional.empty(), Optional.of("B"));
        //then
        assertThat(exclusive).isFalse();
    }
}