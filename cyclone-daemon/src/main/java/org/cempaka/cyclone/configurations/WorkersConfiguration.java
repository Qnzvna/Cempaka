package org.cempaka.cyclone.configurations;

import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.NotEmpty;

import javax.annotation.Nullable;

public class WorkersConfiguration
{
    @Min(1)
    private int workersNumber;
    @NotEmpty
    private String guavaPath;
    private String user;

    public int getWorkersNumber()
    {
        return workersNumber;
    }

    public String getGuavaPath()
    {
        return guavaPath;
    }

    @Nullable
    public String getUser()
    {
        return user;
    }
}
