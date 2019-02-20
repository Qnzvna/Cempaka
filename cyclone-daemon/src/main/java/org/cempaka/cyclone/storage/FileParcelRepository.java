package org.cempaka.cyclone.storage;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.cempaka.cyclone.beans.Parcel;

@Singleton
public class FileParcelRepository implements ParcelRepository
{
    private final Path storagePath;

    @Inject
    public FileParcelRepository(@Named("storage.path") final String storagePath)
    {
        checkNotNull(storagePath);
        this.storagePath = Paths.get(storagePath);
        checkStoragePath();
    }

    private void checkStoragePath()
    {
        checkArgument(Files.isDirectory(storagePath), String.format("'%s' is not a directory.", storagePath));
        checkArgument(Files.isReadable(storagePath), String.format("'%s' is not readable.", storagePath));
        checkArgument(Files.isWritable(storagePath), String.format("'%s' is not writable.", storagePath));
    }

    @Override
    @Nullable
    public Parcel get(final UUID id)
    {
        final Path path = getParcelPath(id);
        if (Files.isRegularFile(path)) {
            try {
                final byte[] bytes = Files.readAllBytes(path);
                return Parcel.of(id, bytes);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        } else {
            return null;
        }
    }

    @Override
    public void put(final Parcel parcel)
    {
        final Path parcelPath = getParcelPath(parcel.getId());
        try {
            Files.write(parcelPath, parcel.getData());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void delete(final UUID id)
    {
        try {
            Files.delete(getParcelPath(id));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public Stream<UUID> list()
    {
        try {
            return Files.list(storagePath).map(Path::getFileName)
                .map(Path::toString)
                .map(UUID::fromString);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Path getParcelPath(final UUID id)
    {
        return Paths.get(storagePath.toString(), id.toString());
    }
}
