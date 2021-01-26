package org.cempaka.cyclone.core.channel;

import java.io.Closeable;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface DaemonChannel extends Closeable
{
    void listen(int port) throws UnknownHostException, SocketException;

    void connect(int port) throws SocketException;

    void start(String testId);

    void end(String testId, int exitCode);

    void running(String testId, Map<String, Double> measurements);

    void log(String testId, String message);

    void addWriteListener(Consumer<Payload> listener);

    void addReadListener(BiConsumer<Integer, Payload> listener);

    void addFailureListener(Consumer<Exception> listener);
}
