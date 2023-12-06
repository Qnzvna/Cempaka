package org.cempaka.cyclone.storage.repositories;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.cempaka.cyclone.beans.Parcel;

@Singleton
public class FileParcelRepository implements ParcelRepository
{
    private final Path storagePath;

    @Inject
    public FileParcelRepository(@Named("parcel.repository.parameters") final Map<String, String> parameters)
    {
        this.storagePath = Paths.get(parameters.get("storagePath"));
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
    public boolean contains(final UUID id)
    {
        final Path path = getParcelPath(id);
        return Files.isRegularFile(path);
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
    public Set<UUID> keys()
    {
        try {
            return Files.list(storagePath).map(Path::getFileName)
                .map(Path::toString)
                .map(UUID::fromString)
                .collect(toSet());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Path getParcelPath(final UUID id)
    {
        return Paths.get(storagePath.toString(), id.toString());
    }
}
