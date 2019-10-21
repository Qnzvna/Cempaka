# Cyclone

[![Build Status](https://travis-ci.org/Qnzvna/Cempaka.svg?branch=master)](https://travis-ci.org/Qnzvna/Cempaka)

Cempaka Cyclone is a pure java open source application made to load test
and measure performance of systems.

## What can I do with it?

You can performance test literally everything. Cyclone use simple
process management to run the tests. Each test runs in a separate JVM
without not needed dependencies. As a developer you are fully in control
of your tests and dependencies as you can simply write them (in Java!)

Cyclone features:

* Daemon application for running and managing tests
* Simple API (dependency free) that allows you to write your Java load
  tests
* No need to use UI when creating tests, everything can be done
  programmatically
* Built-in loops threads and custom parameters for your tests

## Getting started

### Writing own performance tests

To start developing your tests, firstly create a Maven project and
cyclone-cli dependency

```
<dependency>
    <groupId>org.cempaka.cyclone</groupId>
    <artifactId>cyclone-cli</artifactId>
    <version>0.1</version>
</dependency>
```

Add your custom dependencies. It can be any http client or database
connector or maybe you have some dedicated client for your system and
you want to reuse it in your load tests. You can add any dependency you
want since the cyclone-cli artifact is completely dependency free. 

In this example I'm gonna use `apache-httpclient`

```
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.5.7</version>
</dependency>
```

Create your first test. All of your tests should be named to match
following regex pattern: `.*[Tt]est.*` Every method annotated with the
`@Thunderbolt` annotation will be executed as a test.

```
public class GoogleTest
{
    private HttpClient httpClient = HttpClients.createDefault();

    @Thunderbolt
    public void getGoogle() throws IOException
    {
        httpClient.execute(new HttpGet("http://google.pl"));
    }
}
```

The next step is to package our test with all ours dependencies in order
to upload the artifact into daemon and run the test. The simplest
solution for that will be to use maven-shade-plugin and generate uber
jar. The `org.cempaka.cyclone.cli.CycloneCli` class should be set as a
main class.

```
<build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>2.3</version>
                <configuration>
                    <createDependencyReducedPom>true</createDependencyReducedPom>
                    <filters>
                        <filter>
                            <artifact>*:*</artifact>
                            <excludes>
                                <exclude>META-INF/*.SF</exclude>
                                <exclude>META-INF/*.DSA</exclude>
                                <exclude>META-INF/*.RSA</exclude>
                            </excludes>
                        </filter>
                    </filters>
                </configuration>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <transformers>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer"/>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <mainClass>org.cempaka.cyclone.cli.CycloneCli</mainClass>
                                </transformer>
                            </transformers>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```

Please read the [javadoc](https://cempaka.org/javadoc) for full API
overview.

### Installing the daemon

To install the daemon you're gonna need the working postgresql cluster.
The quickest way to set it up locally will be to use docker compose file
located in `tools/docker.yml`

The second is the cyclone daemon process. You can either run it locally
on your machine using `java` or through docker compose file. To simply
build the fat jar run `mvn clean package`. When the build is done run
server command on the produced fat jar. 

```java -jar out/cyclone-daemon.jar server conf/local.yml```

You can access the Daemon UI in your browser by typing
`http://localhost:8080/`

#### Configuration

Daemon use YAML configuration file that is specified as an argument
after _server_ command. You can find out more about configuration by inspecting `conf/local.yml`.

### Running the tests

Tests can be run either from Damon UI or directly from local computer.
Local runs should be used mostly for debugging purposes as metrics and
test results are not gathered.

To run the test locally firstly we have to build our parcel. If you
followed earlier instructions and setup your performance test project
with the `maven-shade-plugin` one command `mvn clean package` should
produce the jar parcel file. You can run the test using following
command.

```
java -jar <parcelPath> -c <testClasses> [-n <numberOfLoops>] [-t <numberOfThreads>] [-p <testParameters>]
```

Example: 
```
java -jar cyclone-examples/target/cyclone-examples-0.1-SNAPSHOT.jar -c org.cempaka.cyclone.examples.ExampleTest -n 1 -t 1 -p sleep=1,testName=example
```

### Development

#### Requesting a feature

To request a new feature to cyclone please open a GitHub issue.

#### Code style

Please follow a code style located inside this repository in
`project/intellij.xml`

#### Running project tests

To run tests simply fire Maven verify lifecycle

```
mvn verify
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
