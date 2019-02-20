package org.cempaka.cyclone.worker;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.cempaka.cyclone.utils.CliParametrs.CLI_PORT;
import static org.cempaka.cyclone.utils.CliParametrs.DAEMON_PORT;
import static org.cempaka.cyclone.utils.CliParametrs.LOOP_COUNT;
import static org.cempaka.cyclone.utils.CliParametrs.TEST_CLASSES;
import static org.cempaka.cyclone.utils.CliParametrs.THREADS;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.io.CharStreams;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import org.cempaka.cyclone.beans.Parcel;
import org.cempaka.cyclone.beans.exceptions.ProcessFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Worker
{
    private static final Logger LOG = LoggerFactory.getLogger(Worker.class);
    private static final String PARCEL_PREFIX = "tmp_";
    private static final String PARCEL_SUFFIX = ".jar";

    private File temporaryParcelFile;
    private Process runningProcess;

    synchronized void start(final Parcel parcel,
                            final List<String> testNames,
                            final long loopCount,
                            final int threadsNumber,
                            final int daemonPort,
                            final int cliPort,
                            final Map<String, String> parameters)
    {
        checkNotNull(parcel);
        try {
            temporaryParcelFile = File.createTempFile(PARCEL_PREFIX + parcel.getId(), PARCEL_SUFFIX);
            Files.write(temporaryParcelFile.toPath(), parcel.getData());

            final String[] command = getCommand(testNames,
                loopCount,
                threadsNumber,
                daemonPort,
                cliPort,
                parameters);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Running command {}", ImmutableList.copyOf(command));
            }
            runningProcess = new ProcessBuilder(command).start();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private String[] getCommand(final List<String> testNames,
                                final long loopCount,
                                final int threadsNumber,
                                final int daemonPort,
                                final int cliPort,
                                final Map<String, String> parameters)
    {
        final String joinedTestNames = Joiner.on(',').join(testNames);
        final ImmutableList.Builder<String> commandBuilder = ImmutableList.builder();
        commandBuilder.add("java", "-jar", temporaryParcelFile.getPath(),
            TEST_CLASSES, joinedTestNames,
            LOOP_COUNT, Long.toString(loopCount),
            DAEMON_PORT, Integer.toString(daemonPort),
            CLI_PORT, Integer.toString(cliPort),
            THREADS, Integer.toString(threadsNumber));
        if (!parameters.isEmpty()) {
            final String joinedParameters = parameters.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(","));
            commandBuilder.add("-p", joinedParameters);
        }
        return commandBuilder.build().toArray(new String[]{});
    }

    synchronized void abort()
    {
        runningProcess.destroy();
        temporaryParcelFile.delete();
        LOG.debug("Worker aborted.");
    }

    synchronized CompletableFuture<?> onDone()
    {
        return CompletableFuture.supplyAsync(() -> {
            try {
                runningProcess.waitFor();
                temporaryParcelFile.delete();
                final int exitValue = runningProcess.exitValue();
                if (exitValue != 0) {
                    throwProcessFailureException(runningProcess, exitValue);
                }
            } catch (InterruptedException ignore) {
            }
            return runningProcess;
        });
    }

    private void throwProcessFailureException(final Process process, final int exitValue)
    {
        try {
            final String message = CharStreams.toString(new InputStreamReader(process.getErrorStream()));
            LOG.warn("Worker failed with: {}", message);
            throw new ProcessFailureException(exitValue, message);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}