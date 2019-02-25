package org.cempaka.cyclone.protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class PayloadEncoder
{
    ByteBuffer encode(final Payload payload)
    {
        final PayloadType type = payload.getType();
        switch (type) {
            case MEASUREMENTS:
                return encodeMeasurements((MeasurementsPayload) payload);
            case LOGS:
                return encodeLogs((LogsPayload) payload);
            default:
                throw new IllegalArgumentException();
        }
    }

    private ByteBuffer encodeLogs(final LogsPayload payload)
    {
        final int logsSize = payload.getLogs().size();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (final ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeInt(logsSize);
            payload.getLogs().forEach(logLine -> {
                try {
                    objectOutputStream.writeUTF(logLine);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
            objectOutputStream.flush();
            final byte[] data = outputStream.toByteArray();
            return ByteBuffer.wrap(data);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private ByteBuffer encodeMeasurements(final MeasurementsPayload payload)
    {
        final Map<String, Double> snapshots = payload.getSnapshots();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (final ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeInt(payload.getStatus().getCode());
            objectOutputStream.writeInt(snapshots.size());
            snapshots.forEach((name, value) -> {
                try {
                    objectOutputStream.writeUTF(name);
                    objectOutputStream.writeDouble(value);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
            objectOutputStream.flush();
            final byte[] data = outputStream.toByteArray();
            return ByteBuffer.wrap(data);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    Payload decode(final PayloadType type, final byte[] data)
    {
        switch (type) {
            case MEASUREMENTS:
                return decodeMeasurements(data);
            case LOGS:
                return decodeLogs(data);
            default:
                throw new IllegalArgumentException();
        }
    }

    private Payload decodeMeasurements(final byte[] data)
    {
        final InputStream inputStream = new ByteArrayInputStream(data);
        final Map<String, Double> snapshots = new HashMap<>();
        try (final ObjectInputStream stream = new ObjectInputStream(inputStream)) {
            final Status status = Status.fromCode(stream.readInt());
            final int size = stream.readInt();
            for (int i = 0; i < size; i++) {
                final String name = stream.readUTF();
                final double value = stream.readDouble();
                snapshots.put(name, value);
            }
            return new MeasurementsPayload(status, snapshots);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Payload decodeLogs(final byte[] data)
    {
        final InputStream inputStream = new ByteArrayInputStream(data);
        try (final ObjectInputStream stream = new ObjectInputStream(inputStream)) {
            final int size = stream.readInt();
            final List<String> logLines = new ArrayList();
            for (int i = 0; i < size; i++) {
                logLines.add(stream.readUTF());
            }
            return new LogsPayload(logLines);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
