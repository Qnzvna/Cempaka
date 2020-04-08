package org.cempaka.cyclone.configurations;

import javax.validation.constraints.Min;
import org.hibernate.validator.constraints.NotEmpty;

public class WorkersConfiguration
{
    @Min(1)
    private int workersNumber;
    @NotEmpty
    private String guavaPath;

    public int getWorkersNumber()
    {
        return workersNumber;
    }

    public String getGuavaPath()
    {
        return guavaPath;
    }
}
