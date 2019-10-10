package org.cempaka.cyclone.invoker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

import java.util.HashMap;
import java.util.Map;
import org.cempaka.cyclone.exceptions.TestFailedException;
import org.cempaka.cyclone.metrics.MeasurementRegistry;
import org.junit.Before;
import org.junit.Test;

public class ReflectiveInvokerSmallTest
{
    private static final HashMap<String, String> EMPTY_PARAMETERS = new HashMap<>();
    private static final MeasurementRegistry MEASUREMENT_REGISTRY = new MeasurementRegistry();

    private Invoker reflectiveInvoker;

    @Before
    public void setUp()
    {
        TestExample.reset();
        reflectiveInvoker = ReflectiveInvoker.forTestClass(TestExample.class, EMPTY_PARAMETERS, MEASUREMENT_REGISTRY);
    }

    @Test
    public void shouldRunBeforeStormMethods()
    {
        //given
        //when
        reflectiveInvoker.invokeBefore();
        //then
        assertThat(TestExample.getBeforeCounter()).isEqualTo(1);
    }

    @Test
    public void shouldRunThunderboltMethods()
    {
        //given
        //when
        reflectiveInvoker.invoke();
        //then
        assertThat(TestExample.getThunderboltCounter()).isEqualTo(1);
    }

    @Test
    public void shouldRunAfterStormMethods()
    {
        //given
        //when
        reflectiveInvoker.invokeAfter();
        //then
        assertThat(TestExample.getAfterCounter()).isEqualTo(1);
    }

    @Test
    public void shouldThrowOnFailingThunderbolt()
    {
        //given
        final IllegalStateException exception = new IllegalStateException();
        TestExample.setThrow(exception);
        //when
        final Throwable throwable = catchThrowable(reflectiveInvoker::invoke);
        //then
        assertThat(throwable).isInstanceOf(TestFailedException.class).hasCause(exception);
    }

    @Test
    public void shouldThrowOnFailingBefore()
    {
        //given
        final IllegalStateException exception = new IllegalStateException();
        TestExample.setThrow(exception);
        //when
        final Throwable throwable = catchThrowable(reflectiveInvoker::invokeBefore);
        //then
        assertThat(throwable).isInstanceOf(TestFailedException.class).hasCause(exception);
    }

    @Test
    public void shouldFailOnTwoBeforeMethods()
    {
        //given
        final Invoker reflectiveInvoker = ReflectiveInvoker.forTestClass(BadExample.class,
            EMPTY_PARAMETERS,
            MEASUREMENT_REGISTRY);
        //when
        final Throwable throwable = catchThrowable(reflectiveInvoker::invokeBefore);
        //then
        assertThat(throwable).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void shouldSetParameters()
    {
        //given
        final Map<String, String> parameters = new HashMap<>();
        final String value = "value";
        parameters.put("parameter", value);
        final Invoker reflectiveInvoker = ReflectiveInvoker.forTestClass(TestExample.class,
            parameters,
            MEASUREMENT_REGISTRY);
        //when
        reflectiveInvoker.invoke();
        //then
        assertThat(TestExample.getParameter()).isEqualTo(value);
    }
}