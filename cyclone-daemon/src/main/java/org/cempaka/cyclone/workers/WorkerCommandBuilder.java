package org.cempaka.cyclone.workers;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.cempaka.cyclone.core.utils.CliParameters;

class WorkerCommandBuilder
{
    private final List<String> suCommand = new ArrayList<>();
    private final List<String> commands = new ArrayList<>();

    public WorkerCommandBuilder(final String parcelPath, @Nullable final String jvmOptions)
    {
        checkNotNull(parcelPath);
        commands.add("java");
        if (jvmOptions != null && !jvmOptions.isEmpty()) {
            commands.add(jvmOptions);
        }
        commands.add("-jar");
        commands.add(parcelPath);
    }

    WorkerCommandBuilder setUser(@Nullable final String user)
    {
        if (user != null) {
            suCommand.add("su");
            suCommand.add(user);
            suCommand.add("-c");
        }
        return this;
    }

    WorkerCommandBuilder setTestName(final String testName)
    {
        checkNotNull(testName);
        checkArgument(!testName.isEmpty());
        commands.add(CliParameters.TEST_CLASSES);
        commands.add(testName);
        return this;
    }

    WorkerCommandBuilder setLoopCount(final long loopCount)
    {
        commands.add(CliParameters.LOOP_COUNT);
        commands.add(Long.toString(loopCount));
        return this;
    }

    WorkerCommandBuilder setThreadsNumber(final int threadsNumber)
    {
        commands.add(CliParameters.THREADS);
        commands.add(Integer.toString(threadsNumber));
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
            commands.add(paramName);
            commands.add(serializedParameters);
        }
        return this;
    }

    WorkerCommandBuilder setTestId(final String testId)
    {
        checkNotNull(testId);
        commands.add(CliParameters.TEST_ID);
        commands.add(testId);
        return this;
    }

    WorkerCommandBuilder setDaemonPort(final int daemonPort)
    {
        commands.add(CliParameters.DAEMON_PORT);
        commands.add(Integer.toString(daemonPort));
        return this;
    }

    String[] build()
    {
        if (suCommand.isEmpty()) {
            return commands.toArray(new String[0]);
        } else {
            final String command = String.join(" ", commands);
            suCommand.add(command);
            return suCommand.toArray(new String[0]);
        }
    }

}
