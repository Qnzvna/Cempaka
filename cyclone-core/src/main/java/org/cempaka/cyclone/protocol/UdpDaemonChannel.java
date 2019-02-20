package org.cempaka.cyclone.protocol;

import static org.cempaka.cyclone.utils.Preconditions.checkState;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.List;
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
    private final MessageEncoder messageEncoder;
    private final Lock runningLock;
    private final Queue<Consumer<Payload>> writeListeners;
    private final Queue<Consumer<Exception>> failureListeners;
    private final Queue<BiConsumer<Integer, Payload>> readListeners;
    private final ExecutorService executorService;

    private boolean running;
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
    }

    @Override
    public void connect(final int port) throws SocketException
    {
        try {
            runningLock.lock();
            checkState(socket == null, "Channel already connected");
            this.socket = new DatagramSocket(port, InetAddress.getLoopbackAddress());
            running = true;
            executorService.submit(this::awaitPacket);
        } finally {
            runningLock.unlock();
        }
    }

    private void awaitPacket()
    {
        while (running) {
            try {
                final byte[] buffer = new byte[MessageEncoder.SIZE];
                final DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
                socket.receive(datagram);
                final byte[] data = datagram.getData();

                final Payload payload = messageEncoder.decode(data);
                readListeners.forEach(listener -> listener.accept(datagram.getPort(), payload)); // TODO async
            } catch (Exception e) {
                failureListeners.forEach(listener -> listener.accept(e));
            }
        }

    }

    @Override
    public void write(final Payload payload, final int port)
    {
        final List<ByteBuffer> buffers = messageEncoder.encode(payload);
        for (final ByteBuffer data : buffers) {
            final byte[] bytes = data.array();
            final DatagramPacket datagramPacket = new DatagramPacket(bytes,
                bytes.length,
                InetAddress.getLoopbackAddress(),
                port);
            try {
                socket.send(datagramPacket);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        writeListeners.forEach(listener -> listener.accept(payload));
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
