package org.cempaka.cyclone.channel;

import static java.net.InetAddress.getLoopbackAddress;
import static org.cempaka.cyclone.utils.Preconditions.checkArgument;
import static org.cempaka.cyclone.utils.Preconditions.checkState;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class UdpDaemonChannel implements DaemonChannel
{
    static int SIZE = 65507;

    private final MessageEncoder messageEncoder;
    private final Lock runningLock;
    private final Queue<Consumer<Payload>> writeListeners;
    private final Queue<Consumer<Exception>> failureListeners;
    private final Queue<BiConsumer<Integer, Payload>> readListeners;
    private final ExecutorService executorService;
    private final ExecutorService listenersService;

    private boolean running;
    private int port;
    private DatagramSocket socket;

    public UdpDaemonChannel()
    {
        this.messageEncoder = new MessageEncoder();
        this.runningLock = new ReentrantLock();
        this.writeListeners = new ConcurrentLinkedQueue<>();
        this.failureListeners = new ConcurrentLinkedQueue<>();
        this.readListeners = new ConcurrentLinkedQueue<>();
        this.executorService = Executors.newSingleThreadExecutor(runnable -> {
            final Thread thread = new Thread(runnable);
            thread.setName("UdpDaemonChannel");
            return thread;
        });
        this.listenersService = Executors.newSingleThreadExecutor(runnable -> {
            final Thread thread = new Thread(runnable);
            thread.setName("UdpDaemonChannel-listeners");
            return thread;
        });
    }

    @Override
    public void listen(final int port) throws SocketException
    {
        try {
            runningLock.lock();
            checkState(socket == null, "Channel already connected");
            this.port = port;
            socket = new DatagramSocket(port, getLoopbackAddress());
            running = true;
            executorService.submit(this::awaitPacket);
        } finally {
            runningLock.unlock();
        }
    }

    @Override
    public void connect(int port) throws SocketException
    {
        try {
            runningLock.lock();
            checkState(socket == null, "Channel already connected");
            this.port = port;
            socket = new DatagramSocket();
            running = true;
        } finally {
            runningLock.unlock();
        }
    }

    @Override
    public void start(final String testId)
    {
        write(new StartedPayload(testId));
    }

    @Override
    public void end(final String testId, final int exitCode)
    {
        write(new EndedPayload(testId, exitCode));
    }

    @Override
    public void running(final String testId, final Map<String, Double> measurements)
    {
        write(new RunningPayload(testId, measurements));
    }

    @Override
    public void log(final String testId, final String message)
    {
        write(new LogPayload(testId, message));
    }

    private void write(final Payload payload)
    {
        checkState(socket != null, "Socket not initialized");
        final byte[] bytes = messageEncoder.encode(payload).array();
        checkArgument(bytes.length < SIZE, String.format("Payload is too big to transfer [%s]", bytes.length));
        final DatagramPacket datagramPacket = new DatagramPacket(bytes,
            bytes.length,
            getLoopbackAddress(),
            port);
        try {
            socket.send(datagramPacket);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        writeListeners.forEach(listener -> listener.accept(payload));
    }

    private void awaitPacket()
    {
        while (running) {
            try {
                final byte[] buffer = new byte[SIZE];
                final DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                final byte[] data = packet.getData();

                final Payload payload = messageEncoder.decode(data);
                readListeners.forEach(listener ->
                    listenersService.submit(() -> listener.accept(packet.getPort(), payload)));
            } catch (Exception e) {
                failureListeners.forEach(listener -> listener.accept(e));
            }
        }

    }

    @Override
    public void addWriteListener(final Consumer<Payload> listener)
    {
        writeListeners.add(listener);
    }

    @Override
    public void addReadListener(final BiConsumer<Integer, Payload> listener)
    {
        readListeners.add(listener);
    }

    @Override
    public void addFailureListener(final Consumer<Exception> listener)
    {
        failureListeners.add(listener);
    }

    @Override
    public void close()
    {
        try {
            runningLock.lock();
            running = false;
            if (socket != null) {
                socket.close();
            }
            socket = null;
        } finally {
            runningLock.unlock();
        }
    }
}
