package org.cempaka.cyclone.core.runners;

import org.cempaka.cyclone.core.annotations.AfterStorm;
import org.cempaka.cyclone.core.annotations.BeforeStorm;
import org.cempaka.cyclone.core.annotations.Parameter;
import org.cempaka.cyclone.core.annotations.Thunderbolt;

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
