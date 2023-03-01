package dev.zontreck.essentials.warps;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dev.zontreck.essentials.commands.teleport.TeleportDestination;
import dev.zontreck.libzontreck.exceptions.InvalidDeserialization;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;

public class Warp {
    public UUID owner;
    public String WarpName;
    public boolean RTP;
    public boolean isPublic;
    public TeleportDestination destination;
    private AccessControlList ACL;

    public Warp(UUID owner, String name, boolean rtp, boolean publicWarp, TeleportDestination destination)
    {
        this.owner=owner;
        WarpName=name;
        RTP=rtp;
        isPublic=publicWarp;
        this.destination=destination;
        this.ACL = new AccessControlList();
    }

    public static Warp deserialize(CompoundTag tag) throws InvalidDeserialization
    {
        Warp warp = new Warp(tag.getUUID("owner"), tag.getString("name"), tag.getBoolean("rtp"), tag.getBoolean("public"), new TeleportDestination(tag.getCompound("destination")));

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
        giveAccess(player.getName().getContents(), player.getUUID());
    }

    /**
     * If the warp is not public, it gives an ID access to the warp
     * @param ID
     */
    protected void giveAccess(String name, UUID ID)
    {
        if(!isPublic)
        {
            ACL.addEntry(name, ID);
        }else return;
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
            ACL.removeByID(id);
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
