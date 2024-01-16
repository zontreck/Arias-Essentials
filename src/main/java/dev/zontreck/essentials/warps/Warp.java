package dev.zontreck.essentials.warps;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dev.zontreck.essentials.commands.teleport.TeleportDestination;
import dev.zontreck.essentials.events.WarpAccessControlListUpdatedEvent;
import dev.zontreck.essentials.warps.AccessControlList.ACLEntry;
import dev.zontreck.libzontreck.exceptions.InvalidDeserialization;
import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.libzontreck.profiles.UserProfileNotYetExistsException;
import dev.zontreck.libzontreck.util.ServerUtilities;
import dev.zontreck.libzontreck.util.heads.HeadUtilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class Warp {
    public UUID owner;
    public String WarpName;
    public boolean RTP;
    public boolean isPublic;
    public TeleportDestination destination;
    private AccessControlList ACL;

    public ItemStack warpIcon;

    public Warp(UUID owner, String name, boolean rtp, boolean publicWarp, TeleportDestination destination, ItemStack warpIcon)
    {
        this.owner=owner;
        WarpName=name;
        RTP=rtp;
        isPublic=publicWarp;
        this.destination=destination;
        this.ACL = new AccessControlList();

        if(warpIcon==null)
        {
            try {
                Profile prof = Profile.get_profile_of(owner.toString());
                this.warpIcon = HeadUtilities.get(prof.username, name);

            } catch (UserProfileNotYetExistsException e) {
                throw new RuntimeException(e);
            }

        }else this.warpIcon=warpIcon;
    }

    public static Warp deserialize(CompoundTag tag) throws InvalidDeserialization
    {
        Warp warp = new Warp(tag.getUUID("owner"), tag.getString("name"), tag.getBoolean("rtp"), tag.getBoolean("public"), new TeleportDestination(tag.getCompound("destination")), ItemStack.of(tag.getCompound("icon")));

        if(!warp.isPublic)
        {
            warp.ACL = AccessControlList.deserialize(tag.getCompound("acl"));
        }
        
        return warp;
    }

    public CompoundTag serialize()
    {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("owner", owner);
        tag.putString("name", WarpName);
        tag.putBoolean("rtp", RTP);
        tag.putBoolean("public", isPublic);
        tag.put("destination", destination.serialize());
        tag.put("icon", warpIcon.serializeNBT());
        if(!isPublic)
        {
            tag.put("acl", ACL.serialize());
        }

        return tag;
    }

    /**
     * Checks if a player has access to a warp
     * @param player
     * @return
     */
    public boolean hasAccess(ServerPlayer player)
    {
        return hasAccess(player.getUUID());
    }

    /**
     * Checks if an ID has access to the warp
     * @param ID
     * @return
     */
    public boolean hasAccess(UUID ID)
    {
        if(isPublic)return true;
        return ACL.getIDs().contains(ID);
    }

    /**
     * Alias for Warp#giveAccess(UUID)
     * @param player
     */
    protected void giveAccess(ServerPlayer player)
    {
        giveAccess(player.getName().getString(), player.getUUID());
    }

    /**
     * If the warp is not public, it gives an ID access to the warp
     * @param ID
     */
    protected void giveAccess(String name, UUID ID)
    {
        if(!isPublic)
        {
            ACLEntry entry = ACL.addEntry(name, ID);
            MinecraftForge.EVENT_BUS.post(new WarpAccessControlListUpdatedEvent(this, entry, true));
        }else {
        }
    }

    /**
     * Takes away access to the warp
     * @param player
     */
    protected void removeAccess(ServerPlayer player)
    {
        removeAccess(player.getUUID());
    }
    /**
     * Takes away access to the warp
     * @param id
     */
    protected void removeAccess(UUID id)
    {
        if(ACL.getIDs().contains(id))
        {
            ACLEntry entry = ACL.removeByID(id);
            MinecraftForge.EVENT_BUS.post(new WarpAccessControlListUpdatedEvent(this, entry, false));
        }
    }

    /**
     * Returns a copy of the ACL List as it was when the request was made.
     * @return
     */
    protected List<UUID> getWarpACL()
    {
        return ACL.getIDs();
    }
}
