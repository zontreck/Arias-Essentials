package dev.zontreck.essentials.events;

import dev.zontreck.essentials.warps.Warp;
import net.minecraftforge.eventbus.api.Event;

public class WarpDeletedEvent extends Event
{

    public Warp warp;
    public WarpDeletedEvent(Warp w) {
        warp=w;
    }
    
}
