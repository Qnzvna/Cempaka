package org.cempaka.cyclone.storage.mappers;

import java.sql.Timestamp;

class TimestampConverter
{
    static long getSeconds(final Timestamp timestamp)
    {
        return timestamp.getTime() / 1_000;
    }
}
