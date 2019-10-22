package org.cempaka.cyclone.invoker;

import org.cempaka.cyclone.annotations.AfterStorm;
import org.cempaka.cyclone.annotations.BeforeStorm;
import org.cempaka.cyclone.annotations.Parameter;
import org.cempaka.cyclone.annotations.Thunderbolt;

public class TestExample extends AbstractTestExample
{
    @Parameter(name = "parameter")
    private String parameter;

    @BeforeStorm
    public void beforeStorm()
    {
        before();
    }

    @Thunderbolt
    public void thunderbolt()
    {
        thunder();
    }

    @AfterStorm
    public void afterStorm()
    {
        after();
    }

    @Override
    void setParameter()
    {
        setParameterHolder(parameter);
    }
}
