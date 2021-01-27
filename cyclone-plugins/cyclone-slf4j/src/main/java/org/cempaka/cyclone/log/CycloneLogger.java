package org.cempaka.cyclone.log;

import static org.cempaka.cyclone.core.utils.Preconditions.checkNotNull;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.event.Level;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

class CycloneLogger extends MarkerIgnoringBase
{
    private final LoggerConfiguration configuration;
    private final Supplier<MessageSink> messageSink;

    private final AtomicReference<MessageSink> holder = new AtomicReference<>();

    CycloneLogger(final String name,
                  final LoggerConfiguration configuration,
                  final MessageSink messageSink)
    {
        this(name, configuration, () -> messageSink);
    }

    CycloneLogger(final String name,
                  final LoggerConfiguration configuration,
                  final Supplier<MessageSink> messageSink)
    {
        this.name = checkNotNull(name);
        this.configuration = checkNotNull(configuration);
        checkNotNull(messageSink);
        this.messageSink = () -> {
            synchronized (this) {
                holder.compareAndSet(null, checkNotNull(messageSink.get()));
                return holder.get();
            }
        };
    }

    @Override
    public boolean isTraceEnabled()
    {
        return isLevelEnabled(Level.TRACE);
    }

    @Override
    public void trace(final String message)
    {
        log(Level.TRACE, message);
    }

    @Override
    public void trace(final String format, final Object arg)
    {
        log(Level.TRACE, format, arg);
    }

    @Override
    public void trace(final String format, final Object arg1, final Object arg2)
    {
        log(Level.TRACE, format, arg1, arg2);
    }

    @Override
    public void trace(final String format, final Object... arguments)
    {
        log(Level.TRACE, format, arguments);
    }

    @Override
    public void trace(final String message, final Throwable throwable)
    {
        log(Level.TRACE, message, throwable);
    }

    @Override
    public boolean isDebugEnabled()
    {
        return isLevelEnabled(Level.DEBUG);
    }

    @Override
    public void debug(final String message)
    {
        log(Level.DEBUG, message);
    }

    @Override
    public void debug(final String format, final Object arg)
    {
        log(Level.DEBUG, format, arg);
    }

    @Override
    public void debug(final String format, final Object arg1, final Object arg2)
    {
        log(Level.DEBUG, format, arg1, arg2);
    }

    @Override
    public void debug(final String format, final Object... arguments)
    {
        log(Level.DEBUG, format, arguments);
    }

    @Override
    public void debug(final String message, final Throwable throwable)
    {
        log(Level.DEBUG, message, throwable);
    }

    @Override
    public boolean isInfoEnabled()
    {
        return isLevelEnabled(Level.INFO);
    }

    @Override
    public void info(final String message)
    {
        log(Level.INFO, message);
    }

    @Override
    public void info(final String format, final Object arg)
    {
        log(Level.INFO, format, arg);
    }

    @Override
    public void info(final String format, final Object arg1, final Object arg2)
    {
        log(Level.INFO, format, arg1, arg2);
    }

    @Override
    public void info(final String format, final Object... arguments)
    {
        log(Level.INFO, format, arguments);
    }

    @Override
    public void info(final String message, final Throwable throwable)
    {
        log(Level.INFO, message, throwable);
    }

    @Override
    public boolean isWarnEnabled()
    {
        return isLevelEnabled(Level.WARN);
    }

    @Override
    public void warn(final String message)
    {
        log(Level.WARN, message);
    }

    @Override
    public void warn(final String format, final Object arg)
    {
        log(Level.WARN, format, arg);
    }

    @Override
    public void warn(final String format, final Object... arguments)
    {
        log(Level.WARN, format, arguments);
    }

    @Override
    public void warn(final String format, final Object arg1, final Object arg2)
    {
        log(Level.WARN, format, arg1, arg2);
    }

    @Override
    public void warn(final String message, final Throwable throwable)
    {
        log(Level.WARN, message, throwable);
    }

    @Override
    public boolean isErrorEnabled()
    {
        return isLevelEnabled(Level.ERROR);
    }

    @Override
    public void error(final String message)
    {
        log(Level.ERROR, message);
    }

    @Override
    public void error(final String format, final Object arg)
    {
        log(Level.ERROR, format, arg);
    }

    @Override
    public void error(final String format, final Object arg1, final Object arg2)
    {
        log(Level.ERROR, format, arg1, arg2);
    }

    @Override
    public void error(final String format, final Object... arguments)
    {
        log(Level.ERROR, format, arguments);
    }

    @Override
    public void error(final String message, final Throwable throwable)
    {
        log(Level.ERROR, message, throwable);
    }

    private boolean isLevelEnabled(final Level error)
    {
        return Level.valueOf(configuration.getLevel()) == error;
    }

    private void log(final Level level, final String message, final Throwable throwable)
    {
        if (isLevelEnabled(level)) {
            final String stackTrace = Stream.of(throwable.getStackTrace())
                .map(StackTraceElement::toString)
                .map(trace -> "! at " + trace)
                .collect(Collectors.joining("\n"));
            logLine(level, String.format("%s\n! %s: %s\n%s", message,
                throwable.getClass(),
                throwable.getMessage(),
                stackTrace));
        }
    }

    private void log(final Level level, final String format, final Object... arguments)
    {
        if (isLevelEnabled(level)) {
            logLine(level, MessageFormatter.arrayFormat(format, arguments).getMessage());
        }
    }

    private void log(final Level level, final String message)
    {
        if (isLevelEnabled(level)) {
            logLine(level, message);
        }
    }

    private void logLine(final Level level, final String message)
    {
        final FormattingTuple format = MessageFormatter.arrayFormat("{} [{}] {}: {}",
            new Object[]{level, ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), name, message});
        messageSink.get().write(format.getMessage());
    }
}
