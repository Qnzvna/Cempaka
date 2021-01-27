package org.cempaka.cyclone.examples;

import org.cempaka.cyclone.core.annotations.Thunderbolt;
import org.cempaka.cyclone.core.measurements.Measure;

public class TooBigPayloadTest
{
    @Measure("too_big_measurement")
    TooBigMeasurement measurement;

    @Thunderbolt
    public void testLongCause()
    {
    }
}
