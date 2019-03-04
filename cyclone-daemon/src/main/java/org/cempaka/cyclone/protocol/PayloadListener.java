package org.cempaka.cyclone.protocol;

import static org.cempaka.cyclone.utils.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import java.util.function.BiConsumer;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.protocol.payloads.Payload;
import org.cempaka.cyclone.protocol.payloads.PayloadType;

@Singleton
public class PayloadListener implements BiConsumer<Integer, Payload>
{
    private final Multimap<PayloadType, BiConsumer<String, Payload>> listeners;
    private final PortProvider portProvider;

    @Inject
    public PayloadListener(
        final Multimap<PayloadType, BiConsumer<String, Payload>> listeners,
        final PortProvider portProvider)
    {
        this.listeners = ImmutableMultimap.copyOf(listeners);
        this.portProvider = checkNotNull(portProvider);
    }

    @Override
    public void accept(final Integer integer, final Payload payload)
    {
        final String testUuid = portProvider.getTestUuid(integer);
        listeners.get(payload.getType())
            .forEach(listener -> listener.accept(testUuid, payload));
    }
}
