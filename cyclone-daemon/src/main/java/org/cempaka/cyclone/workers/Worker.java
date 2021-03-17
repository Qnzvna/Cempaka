package org.cempaka.cyclone.workers;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.io.ByteStreams;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import org.cempaka.cyclone.beans.Parcel;
import org.cempaka.cyclone.beans.exceptions.ProcessFailureException;
import org.cempaka.cyclone.tests.TestExecutionProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Worker
{
    private static final Logger LOG = LoggerFactory.getLogger(Worker.class);
    private static final String PARCEL_PREFIX = "tmp_";
    private static final String PARCEL_SUFFIX = ".jar";

    private final int daemonPort;
    private final String user;

    private File temporaryParcelFile;
    private Process runningProcess;

    Worker(final int daemonPort, @Nullable final String user)
    {
        this.daemonPort = daemonPort;
        this.user = user;
    }

    synchronized void start(final UUID testId, final Parcel parcel,
                            final TestExecutionProperties properties,
                            final Map<String, String> metadata)
    {
        checkNotNull(testId);
        checkNotNull(parcel);
        checkNotNull(properties);
        temporaryParcelFile = createTemporaryParcelFile(parcel);
        final String[] command = new WorkerCommandBuilder(temporaryParcelFile.getPath(), properties.getJvmOptions())
            .setUser(user)
            .setTestName(properties.getTestName())
            .setLoopCount(properties.getLoopCount())
            .setThreadsNumber(properties.getThreadsNumber())
            .setParameters(properties.getParameters())
            .setTestId(testId.toString())
            .setMetadata(metadata)
            .setDaemonPort(daemonPort)
            .build();
        runningProcess = startProcess(command);
    }

    private File createTemporaryParcelFile(final Parcel parcel)
    {
        try {
            final File temporaryParcelFile = File.createTempFile(PARCEL_PREFIX + parcel.getId(), PARCEL_SUFFIX);
            temporaryParcelFile.deleteOnExit();
            Files.write(temporaryParcelFile.toPath(), parcel.getData());
            return temporaryParcelFile;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Process startProcess(final String[] command)
    {
        try {
            LOG.debug("Starting process: {}", Arrays.toString(command));
            return new ProcessBuilder(command).start();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    synchronized void abort()
    {
        runningProcess.destroy();
        temporaryParcelFile.delete();
        LOG.debug("Worker aborted.");
    }

    Process waitFor()
    {
        try {
            runningProcess.waitFor();
            temporaryParcelFile.delete();
            final int exitValue = runningProcess.exitValue();
            if (exitValue != 0) {
                final String errorMessage = new String(ByteStreams.toByteArray(runningProcess.getErrorStream()));
                LOG.warn("Worker failed with error: {}", errorMessage);
                throw new ProcessFailureException(exitValue);
            }
        } catch (InterruptedException ignore) {
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return runningProcess;
    }
}