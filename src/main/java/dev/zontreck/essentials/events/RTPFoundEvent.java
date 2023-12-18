package dev.zontreck.essentials.events;

import dev.zontreck.essentials.rtp.RTP;
import net.minecraftforge.eventbus.api.Event;

public class RTPFoundEvent extends Event
{
    public RTP rtp;

    public RTPFoundEvent(RTP rtp)
    {
        this.rtp=rtp;
    }
}
