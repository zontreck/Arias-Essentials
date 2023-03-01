package dev.zontreck.essentials.events;

import dev.zontreck.essentials.warps.Warp;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class WarpCreatedEvent extends Event
{
    public Warp warp;

    /**
     * Edit this value to specify a reason for cancelling the event to the end user
     */
    public String denyReason = "an unknown error";
    public WarpCreatedEvent(Warp w)
    {
        warp=w;
    }
}
