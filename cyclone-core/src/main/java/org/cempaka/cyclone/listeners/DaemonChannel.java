package org.cempaka.cyclone.listeners;

import java.io.Closeable;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.cempaka.cyclone.listeners.payloads.Payload;

public interface DaemonChannel extends Closeable
{
    void connect(int port) throws UnknownHostException, SocketException;

    void connect() throws SocketException;

    void write(Payload payload, int port);

    void addWriteListener(Consumer<Payload> listener);

    void addReadListener(BiConsumer<Integer, Payload> listener);

    void addFailureListener(Consumer<Exception> listener);
}
