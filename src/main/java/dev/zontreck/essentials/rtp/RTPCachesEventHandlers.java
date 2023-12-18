package dev.zontreck.essentials.rtp;

import dev.zontreck.essentials.events.RTPFoundEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RTPCachesEventHandlers
{
    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent event)
    {
        RTP.checkStale();
    }

    @SubscribeEvent
    public void onRTPFound(RTPFoundEvent event)
    {
        RTPCaches.Locations.add(event.rtp);
    }
}
