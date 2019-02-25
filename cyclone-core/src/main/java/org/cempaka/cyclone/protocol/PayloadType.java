package org.cempaka.cyclone.protocol;

public enum PayloadType
{
    MEASUREMENTS(1),
    LOGS(2);

    private final int code;

    public static PayloadType fromCode(final int code)
    {
        switch (code) {
            case 1:
                return MEASUREMENTS;
            case 2:
                return LOGS;
            default:
                throw new IllegalArgumentException();
        }
    }

    PayloadType(final int code)
    {
        this.code = code;
    }

    public int getCode()
    {
        return code;
    }
}
