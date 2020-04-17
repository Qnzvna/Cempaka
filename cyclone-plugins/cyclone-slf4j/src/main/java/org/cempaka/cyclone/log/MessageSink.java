package org.cempaka.cyclone.log;

import java.io.Closeable;

public interface MessageSink extends Closeable
{
    void write(String message);
}
