package dev.zontreck.essentials.events;

import dev.zontreck.essentials.warps.Warp;
import dev.zontreck.essentials.warps.AccessControlList.ACLEntry;
import dev.zontreck.libzontreck.profiles.Profile;
import net.minecraftforge.eventbus.api.Event;

/**
 * Dispatched when a warp's access control list gets updated.
 */
public class WarpAccessControlListUpdatedEvent extends Event
{
    public Warp warp;
    public ACLEntry entry;

    /**
     * If false, then the entry was deleted from the warp ACL
     */
    public boolean added;
    public WarpAccessControlListUpdatedEvent(Warp warp, ACLEntry entry, boolean added)
    {
        this.warp=warp;
        this.entry=entry;
        this.added=added;
    }
}
