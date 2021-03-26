package org.cempaka.cyclone.configurations;

import javax.annotation.Nullable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.cempaka.cyclone.workers.WorkerConfiguration;
import org.hibernate.validator.constraints.NotEmpty;

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
