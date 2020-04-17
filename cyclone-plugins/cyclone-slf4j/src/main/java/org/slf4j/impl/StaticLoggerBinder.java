package org.slf4j.impl;

import org.cempaka.cyclone.log.CycloneLoggerFactory;
import org.slf4j.ILoggerFactory;

public final class StaticLoggerBinder
{
    private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

    public static StaticLoggerBinder getSingleton()
    {
        return SINGLETON;
    }

    // to avoid constant folding by the compiler, this field must *not* be final
    public static String REQUESTED_API_VERSION = "1.6.99"; // !final

    private StaticLoggerBinder()
    {
    }

    public ILoggerFactory getLoggerFactory()
    {
        return new CycloneLoggerFactory();
    }

    public String getLoggerFactoryClassStr()
    {
        return CycloneLoggerFactory.class.getName();
    }
}
