# Cyclone

[![Build Status](https://travis-ci.org/Qnzvna/Cempaka.svg?branch=master)](https://travis-ci.org/Qnzvna/Cempaka)
![Maven Central](https://img.shields.io/maven-central/v/org.cempaka.cyclone/cyclone)
[![codecov](https://codecov.io/gh/Qnzvna/Cempaka/branch/master/graph/badge.svg)](https://codecov.io/gh/Qnzvna/Cempaka)
![Snyk Vulnerabilities for GitHub Repo](https://img.shields.io/snyk/vulnerabilities/github/Qnzvna/Cempaka)

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

Checkout the [Wiki](https://github.com/Qnzvna/Cempaka/wiki)

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
