package org.cempaka.cyclone.examples;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import org.cempaka.cyclone.core.measurements.Measurement;

public class TooBigMeasurement extends Measurement
{
    public TooBigMeasurement(final String name)
    {
        super(name);
    }

    @Override
    public Map<String, Double> getSnapshot()
    {
        return LongStream.range(0, 10_000)
            .mapToObj(value -> (double) value)
            .collect(Collectors.toMap(Object::toString, Function.identity()));
    }
}
