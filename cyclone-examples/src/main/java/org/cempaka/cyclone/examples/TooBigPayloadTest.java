package org.cempaka.cyclone.examples;

import org.cempaka.cyclone.annotations.Thunderbolt;
import org.cempaka.cyclone.measurements.Measure;

public class TooBigPayloadTest
{
    @Measure("too_big_measurement")
    TooBigMeasurement measurement;

    @Thunderbolt
    public void testLongCause()
    {
    }
}
