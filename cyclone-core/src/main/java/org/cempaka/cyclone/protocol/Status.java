package org.cempaka.cyclone.protocol;

public enum Status
{
    STARTED(0),
    RUNNING(1),
    ENDED(2);

    private final int code;

    public static Status fromCode(final int code)
    {
        switch (code) {
            case 0:
                return STARTED;
            case 1:
                return RUNNING;
            case 2:
                return ENDED;
            default:
                throw new IllegalArgumentException();
        }
    }

    Status(final int code)
    {
        this.code = code;
    }

    public int getCode()
    {
        return code;
    }
}
