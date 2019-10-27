package org.cempaka.cyclone.configurations;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;

public class WorkersConfiguration
{
    @Min(1)
    private int workersNumber;
    @NotEmpty
    private String guavaPath;
    @NotEmpty
    private String logsPath;

    public int getWorkersNumber()
    {
        return workersNumber;
    }

    public String getGuavaPath()
    {
        return guavaPath;
    }

    public String getLogsPath()
    {
        return logsPath;
    }
}
