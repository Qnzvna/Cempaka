package org.cempaka.cyclone.protocol.payloads;

public enum PayloadType
{
    STARTED(1),
    RUNNING(2),
    ENDED(3);

    private final int code;

    public static PayloadType fromCode(final int code)
    {
        switch (code) {
            case 1:
                return STARTED;
            case 2:
                return RUNNING;
            case 3:
                return ENDED;
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
