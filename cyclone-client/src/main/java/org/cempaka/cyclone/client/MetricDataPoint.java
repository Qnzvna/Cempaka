package org.cempaka.cyclone.client;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value.Immutable;

@Immutable
@ImmutableStyle
@JsonDeserialize(as = ImmutableMetricDataPoint.class)
public interface MetricDataPoint
{
    long getTimestamp();

    String getName();

    double getValue();
}
