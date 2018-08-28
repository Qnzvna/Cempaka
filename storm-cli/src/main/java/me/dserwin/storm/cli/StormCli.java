package me.dserwin.storm.cli;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.dserwin.storm.runner.LoopRunner;
import me.dserwin.storm.runner.Runner;
import me.dserwin.storm.runner.SimpleRunner;
import me.dserwin.storm.runner.ThreadRunner;
import picocli.CommandLine;
import picocli.CommandLine.Option;

public class StormCli
{
    @Option(names = {"-c", "--test-classes"}, description = "test names to run", required = true, split = ",")
    private String[] testNames;

    @Option(names = {"-n", "--loopCount"}, description = "number of loops to run")
    private long loopCount = 1;

    @Option(names = {"-t", "--threads"}, description = "number of threads to run")
    private int threads = 1;

    @Option(names = {"-p", "--parameters"}, description = "passed parameters to tests", split = ",")
    private Map<String, String> parameters = new HashMap<>();

    public static void main(String[] args)
    {
        final StormCli stormCli = new StormCli();
        new CommandLine(stormCli).parse(args);

        stormCli.getRunner().run();
        System.exit(0);
    }

    private Runner getRunner()
    {
        final List<Class> testClasses = loadTestClasses();
        final SimpleRunner simpleRunner = new SimpleRunner(testClasses, parameters);
        final ThreadRunner threadRunner = new ThreadRunner(simpleRunner, threads);
        return new LoopRunner(threadRunner, loopCount);
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
}
