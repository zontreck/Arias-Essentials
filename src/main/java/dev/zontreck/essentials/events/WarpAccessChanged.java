package dev.zontreck.essentials.events;

import dev.zontreck.essentials.warps.Warp;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;


/**
 * Dispatched only when a warp's public status is toggled on or off.
 * This event may be cancelled to prevent toggling. This could happen if the person is not the owner of the warp, or due to other permissions issues, such as from claims.
 */
@Cancelable
public class WarpAccessChanged extends Event
{
    /**
     * This is the warp the event is triggered for
     */
    public Warp warp;
    /**
     * This is the player initiating the change
     */
    public ServerPlayer player;
    
    public WarpAccessChanged(Warp warp, ServerPlayer initiator)
    {
        this.warp=warp;
        player=initiator;
    }
}
