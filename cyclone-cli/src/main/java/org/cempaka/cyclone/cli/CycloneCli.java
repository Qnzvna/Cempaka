package org.cempaka.cyclone.cli;

import static org.cempaka.cyclone.utils.CliParameters.DAEMON_PORT;
import static org.cempaka.cyclone.utils.CliParameters.LOOP_COUNT;
import static org.cempaka.cyclone.utils.CliParameters.METADATA;
import static org.cempaka.cyclone.utils.CliParameters.PARAMETERS;
import static org.cempaka.cyclone.utils.CliParameters.TEST_CLASSES;
import static org.cempaka.cyclone.utils.CliParameters.TEST_ID;
import static org.cempaka.cyclone.utils.CliParameters.THREADS;

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
import org.cempaka.cyclone.channel.DaemonChannel;
import org.cempaka.cyclone.channel.UdpDaemonChannel;
import org.cempaka.cyclone.log.LoggerFactoryConfiguration;
import org.cempaka.cyclone.measurements.MeasurementRegistry;
import org.cempaka.cyclone.runners.Runner;
import org.cempaka.cyclone.runners.Runners;
import picocli.CommandLine;
import picocli.CommandLine.Option;

/**
 * Main load tests runner.
 */
public class CycloneCli
{
    private static final Duration RUNNER_AWAIT_TIME = Duration.ofMinutes(1);

    @Option(names = {TEST_CLASSES, "--test-classes"},
        description = "test names to run", required = true,
        split = ",")
    private String[] testNames;
    @Option(names = {LOOP_COUNT, "--loopCount"}, description = "number of loops to run", defaultValue = "1")
    private long loopCount;
    @Option(names = {THREADS, "--threads"}, description = "number of threads to run", defaultValue = "1")
    private int threads;
    @Option(names = {PARAMETERS, "--parameters"}, description = "passed parameters to tests", split = ",")
    private Map<String, String> parameters = new HashMap<>();
    @Option(names = DAEMON_PORT, description = "daemon port to send updates")
    private int daemonPort;
    @Option(names = TEST_ID, description = "test id to receive updates")
    private String testId = "unknown";
    @Option(names = "--measurement-period",
        description = "measurements period in seconds", defaultValue = "5")
    private int measurementsPeriod;
    @Option(names = "--measurements-print", description = "enables measurement printing", defaultValue = "false")
    private boolean measurementsPrint;
    @Option(names = METADATA)
    private Map<String, String> metadata = new HashMap<>();

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
        new CommandLine(cycloneCli).parseArgs(args);

        final int exitCode = cycloneCli.run();
        System.exit(exitCode);
    }

    private int run() throws IOException, InterruptedException
    {
        if (isUdpEnabled()) {
            LoggerFactoryConfiguration.ENABLED = true;
            LoggerFactoryConfiguration.TEST_ID = testId;
            LoggerFactoryConfiguration.PORT = daemonPort;
            daemonChannel.connect(daemonPort);
            daemonChannel.start(testId);
        }
        metricsExecutor.scheduleAtFixedRate(this::reportMetrics, 1, measurementsPeriod, TimeUnit.SECONDS);
        final int exitCode = runTest();
        metricsExecutor.shutdown();
        metricsExecutor.awaitTermination(1, TimeUnit.MINUTES);
        if (isUdpEnabled()) {
            daemonChannel.end(testId, exitCode);
        }
        daemonChannel.close();
        return exitCode;
    }

    private int runTest() throws InterruptedException
    {
        final List<Class<?>> testClasses = loadTestClasses();
        final Runner simpleRunner = Runners.simpleRunner(testClasses,
            parameters,
            metadata,
            measurementRegistry);
        final Runner threadRunner = Runners.threadRunner(simpleRunner, threads);
        final Runner runner = Runners.loopRunner(threadRunner, loopCount);
        try {
            runner.run();
            reportMetrics();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            threadRunner.awaitTermination(RUNNER_AWAIT_TIME);
        }
    }

    private boolean isUdpEnabled()
    {
        return daemonPort > 0;
    }

    private void reportMetrics()
    {
        final Map<String, Double> measurements = measurementRegistry.getSnapshots();
        if (isUdpEnabled()) {
            daemonChannel.running(testId, measurements);
        }
        if (measurementsPrint) {
            measurements.forEach((name, value) -> System.out.printf("%s=%s\n", name, value));
        }
    }

    private List<Class<?>> loadTestClasses()
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
}
