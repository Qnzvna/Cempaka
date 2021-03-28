package org.cempaka.cyclone.services;

public class NodeNotAliveException extends RuntimeException
{
    public NodeNotAliveException(final String node)
    {
        super(String.format("Node '%s' is not alive.", node));
    }
}
