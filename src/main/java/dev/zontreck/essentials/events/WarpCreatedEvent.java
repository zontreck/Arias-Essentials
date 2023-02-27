package dev.zontreck.essentials.events;

import dev.zontreck.essentials.warps.Warp;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class WarpCreatedEvent extends Event
{
    public Warp warp;
    public WarpCreatedEvent(Warp w)
    {
        warp=w;
    }
}
