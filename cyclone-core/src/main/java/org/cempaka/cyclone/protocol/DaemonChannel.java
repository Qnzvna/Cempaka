package org.cempaka.cyclone.protocol;

import java.io.Closeable;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface DaemonChannel extends Closeable
{
    void connect(int port) throws UnknownHostException, SocketException;

    void write(Payload payload, int port);

    void addWriteListener(Consumer<Payload> listener);

    void addReadListener(BiConsumer<Integer, Payload> listener);

    void addFailureListener(Consumer<Exception> listener);
}
