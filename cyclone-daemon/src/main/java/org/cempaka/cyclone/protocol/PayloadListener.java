package org.cempaka.cyclone.protocol;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import org.cempaka.cyclone.protocol.payloads.Payload;
import org.cempaka.cyclone.protocol.payloads.PayloadType;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.function.BiConsumer;

@Singleton
public class PayloadListener implements BiConsumer<Integer, Payload>
{
    private final Multimap<PayloadType, BiConsumer<String, Payload>> listeners;

    @Inject
    public PayloadListener(final Multimap<PayloadType, BiConsumer<String, Payload>> listeners)
    {
        this.listeners = ImmutableMultimap.copyOf(listeners);
    }

    @Override
    public void accept(final Integer integer, final Payload payload)
    {
        listeners.get(payload.getType())
                .forEach(listener -> listener.accept(payload.getTestId(), payload));
    }
}
