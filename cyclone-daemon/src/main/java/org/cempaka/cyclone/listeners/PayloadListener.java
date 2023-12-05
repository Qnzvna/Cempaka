package org.cempaka.cyclone.listeners;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import java.util.function.BiConsumer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.cempaka.cyclone.core.channel.Payload;
import org.cempaka.cyclone.core.channel.PayloadType;

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
