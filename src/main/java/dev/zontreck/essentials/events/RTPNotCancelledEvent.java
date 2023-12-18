package dev.zontreck.essentials.events;

import dev.zontreck.essentials.rtp.RTPContainer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

/**
 * Fired if the RTP Was not cancelled to signal that a warp should occur now!
 */
public class RTPNotCancelledEvent extends Event
{
    public final RTPContainer container;
    public RTPNotCancelledEvent(RTPContainer container)
    {
        this.container=container;
    }

    public void send()
    {
        MinecraftForge.EVENT_BUS.post(this);
    }
    
}
