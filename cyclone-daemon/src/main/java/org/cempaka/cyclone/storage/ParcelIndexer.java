package org.cempaka.cyclone.storage;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.io.CharStreams;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.cempaka.cyclone.beans.Parcel;
import org.cempaka.cyclone.beans.exceptions.IndexingParcelException;
import org.cempaka.cyclone.tests.ImmutableTest;
import org.cempaka.cyclone.tests.ImmutableTestParameter;
import org.cempaka.cyclone.tests.Test;
import org.cempaka.cyclone.tests.TestParameter;
import org.cempaka.cyclone.utils.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ParcelIndexer
{
    private static final Logger LOG = LoggerFactory.getLogger(ParcelIndexer.class);

    private static final String PARCEL_PREFIX = "index_tmp_";
    private static final String PARCEL_SUFFIX = ".jar";
    private static final String METADATA_PRINT = "org.cempaka.cyclone.cli.MetadataPrint";

    private final String guavaPath;

    @Inject
    public ParcelIndexer(@Named("guava.path") final String guavaPath)
    {
        this.guavaPath = checkNotNull(guavaPath);
    }

    public Set<Test> index(final Parcel parcel)
    {
        checkNotNull(parcel);
        final List<String> processOutput = getProcessOutput(parcel);
        return processOutput.stream()
            .map(this::parseTestMetadata)
            .map(testBuilder -> testBuilder.parcelId(parcel.getId()))
            .map(ImmutableTest.Builder::build)
            .collect(Collectors.toSet());
    }

    // TODO extract and write tests
    private List<String> getProcessOutput(final Parcel parcel)
    {
        final File temporaryParcelFile = createTemporaryFile(parcel);
        try {
            Files.write(temporaryParcelFile.toPath(), parcel.getData());
            final String classPath = temporaryParcelFile.getPath() + ":" + guavaPath;
            final Process process = new ProcessBuilder("java",
                "-cp",
                classPath,
                METADATA_PRINT).start();
            final List<String> response =
                CharStreams.readLines(new InputStreamReader(process.getInputStream()));
            final int exitCode = process.waitFor();
            if (exitCode != 0) {
                final String error = String.join("",
                    CharStreams.readLines(new InputStreamReader(process.getErrorStream())));
                LOG.error("Indexing parcel failed with errors {}", error);
                throw new IndexingParcelException();
            }
            return response;
        } catch (InterruptedException e) {
            throw new IndexingParcelException();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            //noinspection ResultOfMethodCallIgnored
            temporaryParcelFile.delete();
        }
    }

    private File createTemporaryFile(final Parcel parcel)
    {
        try {
            return File.createTempFile(PARCEL_PREFIX + parcel.getId(), PARCEL_SUFFIX);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private ImmutableTest.Builder parseTestMetadata(final String data)
    {
        final String[] split = data.split(Metadata.SEPARATOR);
        checkArgument(split.length > 0);
        final String testName = split[0];
        final Set<TestParameter> testParameters = Stream.of(split)
            .skip(1)
            .map(this::parseParameterMetadata)
            .collect(Collectors.toSet());
        return ImmutableTest.builder()
            .name(testName)
            .addAllParameters(testParameters);
    }

    private TestParameter parseParameterMetadata(final String data)
    {
        final String[] split = data.split(Metadata.PARAMETER_SEPARATOR);
        checkArgument(split.length == 3);
        return ImmutableTestParameter.builder()
            .name(split[0])
            .type(split[1])
            .defaultValue(split[2])
            .build();
    }
}
