package org.cempaka.cyclone.log;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.function.Consumer;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.event.Level;

@ExtendWith(MockitoExtension.class)
class CycloneLoggerSmallTest
{
    private final static String MESSAGE = "message";
    private final static String COUNTDOWN_1 = "{}";
    private final static String COUNTDOWN_2 = "{} {}";
    private final static String COUNTDOWN_3 = "{} {} {}";
    private final static Throwable THROWABLE = new IllegalStateException("invalid");

    @Mock
    private LoggerConfiguration loggerConfiguration;
    @Mock
    private MessageSink messageSink;

    private CycloneLogger cycloneLogger;

    @BeforeEach
    void setUp()
    {
        cycloneLogger = new CycloneLogger("name", loggerConfiguration, messageSink);
    }

    private static class LogArguments implements ArgumentsProvider
    {
        @Override
        public Stream<? extends Arguments> provideArguments(final ExtensionContext extensionContext) throws Exception
        {
            return Stream.of(
                // TRACE
                arguments("TRACE", (Consumer<CycloneLogger>) logger -> logger.trace(MESSAGE), MESSAGE),
                arguments("TRACE", (Consumer<CycloneLogger>) logger -> logger.trace(COUNTDOWN_1, "uno"), "uno"),
                arguments(
                    "TRACE",
                    (Consumer<CycloneLogger>) logger -> logger.trace(COUNTDOWN_2, "uno", "dos"),
                    "uno dos"),
                arguments(
                    "TRACE",
                    (Consumer<CycloneLogger>) logger -> logger.trace(COUNTDOWN_3, "uno", "dos", "tres"),
                    "uno dos tres"
                ),
                arguments(
                    "TRACE",
                    (Consumer<CycloneLogger>) logger -> logger.trace(MESSAGE, THROWABLE),
                    "IllegalStateException: invalid"
                ),
                // DEBUG
                arguments("DEBUG", (Consumer<CycloneLogger>) logger -> logger.debug(MESSAGE), MESSAGE),
                arguments("DEBUG", (Consumer<CycloneLogger>) logger -> logger.debug(COUNTDOWN_1, "uno"), "uno"),
                arguments(
                    "DEBUG",
                    (Consumer<CycloneLogger>) logger -> logger.debug(COUNTDOWN_2, "uno", "dos"),
                    "uno dos"),
                arguments(
                    "DEBUG",
                    (Consumer<CycloneLogger>) logger -> logger.debug(COUNTDOWN_3, "uno", "dos", "tres"),
                    "uno dos tres"
                ),
                arguments(
                    "DEBUG",
                    (Consumer<CycloneLogger>) logger -> logger.debug(MESSAGE, THROWABLE),
                    "IllegalStateException: invalid"
                ),
                // INFO
                arguments("INFO", (Consumer<CycloneLogger>) logger -> logger.info(MESSAGE), MESSAGE),
                arguments("INFO", (Consumer<CycloneLogger>) logger -> logger.info(COUNTDOWN_1, "uno"), "uno"),
                arguments(
                    "INFO",
                    (Consumer<CycloneLogger>) logger -> logger.info(COUNTDOWN_2, "uno", "dos"),
                    "uno dos"),
                arguments(
                    "INFO",
                    (Consumer<CycloneLogger>) logger -> logger.info(COUNTDOWN_3, "uno", "dos", "tres"),
                    "uno dos tres"
                ),
                arguments(
                    "INFO",
                    (Consumer<CycloneLogger>) logger -> logger.info(MESSAGE, THROWABLE),
                    "IllegalStateException: invalid"
                ),
                // WARN
                arguments("WARN", (Consumer<CycloneLogger>) logger -> logger.warn(MESSAGE), MESSAGE),
                arguments("WARN", (Consumer<CycloneLogger>) logger -> logger.warn(COUNTDOWN_1, "uno"), "uno"),
                arguments(
                    "WARN",
                    (Consumer<CycloneLogger>) logger -> logger.warn(COUNTDOWN_2, "uno", "dos"),
                    "uno dos"),
                arguments(
                    "WARN",
                    (Consumer<CycloneLogger>) logger -> logger.warn(COUNTDOWN_3, "uno", "dos", "tres"),
                    "uno dos tres"
                ),
                arguments(
                    "WARN",
                    (Consumer<CycloneLogger>) logger -> logger.warn(MESSAGE, THROWABLE),
                    "IllegalStateException: invalid"
                ),
                // ERROR
                arguments("ERROR", (Consumer<CycloneLogger>) logger -> logger.error(MESSAGE), MESSAGE),
                arguments("ERROR", (Consumer<CycloneLogger>) logger -> logger.error(COUNTDOWN_1, "uno"), "uno"),
                arguments(
                    "ERROR",
                    (Consumer<CycloneLogger>) logger -> logger.error(COUNTDOWN_2, "uno", "dos"),
                    "uno dos"),
                arguments(
                    "ERROR",
                    (Consumer<CycloneLogger>) logger -> logger.error(COUNTDOWN_3, "uno", "dos", "tres"),
                    "uno dos tres"
                ),
                arguments(
                    "ERROR",
                    (Consumer<CycloneLogger>) logger -> logger.error(MESSAGE, THROWABLE),
                    "IllegalStateException: invalid"
                )
            );
        }
    }

    @ParameterizedTest
    @ArgumentsSource(LogArguments.class)
    void shouldLogMessage(final String level, final Consumer<CycloneLogger> logRunnable, final String message)
    {
        //given
        given(loggerConfiguration.getLevel()).willReturn(level);
        //when
        logRunnable.accept(cycloneLogger);
        //then
        verify(messageSink, times(1)).write(contains(message));
    }
}