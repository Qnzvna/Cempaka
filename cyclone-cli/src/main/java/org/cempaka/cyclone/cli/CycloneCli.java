package org.cempaka.cyclone.cli;

import static org.cempaka.cyclone.utils.CliParametrs.CLI_PORT;
import static org.cempaka.cyclone.utils.CliParametrs.DAEMON_PORT;
import static org.cempaka.cyclone.utils.CliParametrs.LOOP_COUNT;
import static org.cempaka.cyclone.utils.CliParametrs.PARAMETERS;
import static org.cempaka.cyclone.utils.CliParametrs.TEST_CLASSES;
import static org.cempaka.cyclone.utils.CliParametrs.THREADS;
import static org.cempaka.cyclone.utils.Preconditions.checkArgument;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.cempaka.cyclone.metrics.MeasurementRegistry;
import org.cempaka.cyclone.protocol.DaemonChannel;
import org.cempaka.cyclone.protocol.LogsPayload;
import org.cempaka.cyclone.protocol.MeasurementsPayload;
import org.cempaka.cyclone.protocol.Status;
import org.cempaka.cyclone.protocol.UdpDaemonChannel;
import org.cempaka.cyclone.runner.LoopRunner;
import org.cempaka.cyclone.runner.Runner;
import org.cempaka.cyclone.runner.SimpleRunner;
import org.cempaka.cyclone.runner.ThreadRunner;
import picocli.CommandLine;
import picocli.CommandLine.Option;

public class CycloneCli
{
    @Option(names = {TEST_CLASSES,
        "--test-classes"}, description = "test names to run", required = true, split = ",")
    private String[] testNames;
    @Option(names = {LOOP_COUNT,
        "--loopCount"}, description = "number of loops to run", defaultValue = "1")
    private long loopCount;
    @Option(names = {THREADS,
        "--threads"}, description = "number of threads to run", defaultValue = "1")
    private int threads;
    @Option(names = {PARAMETERS,
        "--parameters"}, description = "passed parameters to tests", split = ",")
    private Map<String, String> parameters = new HashMap<>();
    @Option(names = {DAEMON_PORT}, description = "daemon port to send updates")
    private int daemonPort;
    @Option(names = {CLI_PORT}, description = "cli port to receive updates")
    private int cliPort;
    @Option(names = {
        "--measurement-period"}, description = "measurements period in seconds", defaultValue = "5")
    private int measurementsPeriod;

    private final MeasurementRegistry measurementRegistry;
    private final DaemonChannel daemonChannel;
    private final ScheduledExecutorService metricsExecutor;

    private CycloneCli()
    {
        this.measurementRegistry = new MeasurementRegistry();
        this.daemonChannel = new UdpDaemonChannel();
        this.metricsExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    public static void main(String[] args) throws IOException, InterruptedException
    {
        final CycloneCli cycloneCli = new CycloneCli();
        new CommandLine(cycloneCli).parse(args);

        cycloneCli.run();
        System.exit(0);
    }

    private void run() throws IOException, InterruptedException
    {
        if (isUdpEnabled()) {
            checkArgument(daemonPort > 0, "daemon port must be defined");
            daemonChannel.connect(cliPort);
            daemonChannel.write(new MeasurementsPayload(Status.STARTED), daemonPort);
            metricsExecutor.scheduleAtFixedRate(this::reportMeasurements, 1, measurementsPeriod,
                TimeUnit.SECONDS);
        }
        final List<Class> testClasses = loadTestClasses();
        final SimpleRunner simpleRunner = new SimpleRunner(testClasses,
            parameters,
            measurementRegistry);
        final ThreadRunner threadRunner = new ThreadRunner(simpleRunner, threads);
        final Runner runner = new LoopRunner(threadRunner, loopCount);
        try {
            runner.run();
        } catch (Exception e) {
            e.printStackTrace();
            sendStackTrace(e);
        }
        threadRunner.awaitTermination(Duration.ofMinutes(1));
        metricsExecutor.shutdown();
        metricsExecutor.awaitTermination(1, TimeUnit.MINUTES);
        if (isUdpEnabled()) {
            daemonChannel.write(new MeasurementsPayload(Status.ENDED), daemonPort);
        }
        daemonChannel.close();
    }

    private boolean isUdpEnabled()
    {
        return cliPort > 0;
    }

    private void reportMeasurements()
    {
        final Map<String, Double> snapshots = measurementRegistry.getSnapshots();
        daemonChannel.write(new MeasurementsPayload(Status.RUNNING, snapshots), daemonPort);
    }

    private List<Class> loadTestClasses()
    {
        return Stream.of(testNames)
            .map(className -> {
                try {
                    return Class.forName(className);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
    }

    private void sendStackTrace(final Exception exception)
    {
        final List<String> stackTrace = Stream.of(exception.getStackTrace())
            .map(StackTraceElement::toString)
            .collect(Collectors.toList());
        daemonChannel.write(new LogsPayload(stackTrace), daemonPort);
    }
}
