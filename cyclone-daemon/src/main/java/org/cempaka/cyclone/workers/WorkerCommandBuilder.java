package org.cempaka.cyclone.workers;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableList;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.cempaka.cyclone.utils.CliParameters;

class WorkerCommandBuilder
{
    private final ImmutableList.Builder<String> commands = ImmutableList.builder();

    public WorkerCommandBuilder(final String parcelPath, @Nullable final String jvmOptions)
    {
        checkNotNull(parcelPath);
        commands.add("java");
        if (jvmOptions != null && !jvmOptions.isEmpty()) {
            commands.add(jvmOptions);
        }
        commands.add("-jar", parcelPath);
    }

    WorkerCommandBuilder setTestName(final String testName)
    {
        checkNotNull(testName);
        checkArgument(!testName.isEmpty());
        commands.add(CliParameters.TEST_CLASSES, testName);
        return this;
    }

    WorkerCommandBuilder setLoopCount(final long loopCount)
    {
        commands.add(CliParameters.LOOP_COUNT, Long.toString(loopCount));
        return this;
    }

    WorkerCommandBuilder setThreadsNumber(final int threadsNumber)
    {
        commands.add(CliParameters.THREADS, Integer.toString(threadsNumber));
        return this;
    }

    WorkerCommandBuilder setParameters(final Map<String, String> parameters)
    {
        return setMapParameter(parameters, CliParameters.PARAMETERS);
    }

    WorkerCommandBuilder setMetadata(final Map<String, String> metadata)
    {
        return setMapParameter(metadata, CliParameters.METADATA);
    }

    private WorkerCommandBuilder setMapParameter(final Map<String, String> map, final String paramName)
    {
        checkNotNull(map);
        if (!map.isEmpty()) {
            final String serializedParameters = map.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(","));
            commands.add(paramName, serializedParameters);
        }
        return this;
    }

    WorkerCommandBuilder setTestId(final String testId)
    {
        checkNotNull(testId);
        commands.add(CliParameters.TEST_ID, testId);
        return this;
    }

    WorkerCommandBuilder setDaemonPort(final int daemonPort)
    {
        commands.add(CliParameters.DAEMON_PORT, Integer.toString(daemonPort));
        return this;
    }

    String[] build()
    {
        return commands.build().toArray(new String[]{});
    }

}
