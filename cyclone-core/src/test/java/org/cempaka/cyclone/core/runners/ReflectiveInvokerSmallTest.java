package org.cempaka.cyclone.core.runners;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.data.Offset;
import org.cempaka.cyclone.core.measurements.CounterMeasurement;
import org.cempaka.cyclone.core.measurements.Measurement;
import org.cempaka.cyclone.core.measurements.MeasurementRegistry;
import org.junit.Before;
import org.junit.Test;

public class ReflectiveInvokerSmallTest
{
    private static final Map<String, String> EMPTY_PARAMETERS = Collections.emptyMap();
    private static final MeasurementRegistry MEASUREMENT_REGISTRY = new MeasurementRegistry();

    private Invoker reflectiveInvoker;

    @Before
    public void setUp()
    {
        AbstractTestExample.reset();
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

    @Test
    public void shouldCountInvocationsUsingTicks()
    {
        //given
        final Invoker reflectiveInvoker = ReflectiveInvoker.forTestClass(MeasurementsExample.class,
            EMPTY_PARAMETERS,
            MEASUREMENT_REGISTRY);
        //when
        reflectiveInvoker.invoke();
        MeasurementsExample.setThrow(new IllegalStateException());
        reflectiveInvoker.invoke();
        //then
        assertThat(MEASUREMENT_REGISTRY.getSnapshots())
            .containsEntry("counted:success:count", 1D)
            .containsEntry("counted:failure:count", 1D);
    }

    @Test
    public void shouldRegisterMeasureOnParameter() throws NoSuchMethodException
    {
        //given
        final Method method = MeasurementsExample.class.getMethod("measureScope", CounterMeasurement.class);
        //when
        final Invoker invoker =
            ReflectiveInvoker.forTestClass(MeasurementsExample.class, EMPTY_PARAMETERS, MEASUREMENT_REGISTRY);
        final Measurement measurement = MEASUREMENT_REGISTRY.get(method, "");
        invoker.invoke();
        //then
        assertThat(measurement.getSnapshot()).containsEntry("count", 2D);
    }

    @Test
    public void shouldRegisterMeasureOnClass()
    {
        //given
        //when
        final Invoker invoker =
            ReflectiveInvoker.forTestClass(MeasurementsExample.class, EMPTY_PARAMETERS, MEASUREMENT_REGISTRY);
        final Measurement measurement = MEASUREMENT_REGISTRY.get(MeasurementsExample.GLOBAL_COUNTER_NAME);
        invoker.invoke();
        //then
        assertThat(measurement.getSnapshot()).containsEntry("count", 2D);
    }

    @Test
    public void shouldThrottleExecution()
    {
        //given
        final Invoker invoker =
            ReflectiveInvoker.forTestClass(ThrottledExample.class, EMPTY_PARAMETERS, MEASUREMENT_REGISTRY);
        //when
        final long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            invoker.invoke();
        }
        final long executionTime = System.currentTimeMillis() - startTime;
        //then
        assertThat(executionTime).isCloseTo(1_000, Offset.offset(100L));
    }

    @Test
    public void shouldNotThrottleWhenNotNeeded()
    {
        //given
        final Invoker invoker =
            ReflectiveInvoker.forTestClass(ThrottledSleepExample.class, EMPTY_PARAMETERS, MEASUREMENT_REGISTRY);
        //when
        final long startTime = System.currentTimeMillis();
        invoker.invoke();
        invoker.invoke();
        final long executionTime = System.currentTimeMillis() - startTime;
        //then
        assertThat(executionTime).isCloseTo(2_200, Offset.offset(100L));
    }
}