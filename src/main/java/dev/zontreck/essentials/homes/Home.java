package dev.zontreck.essentials.homes;

import java.util.UUID;

import dev.zontreck.essentials.commands.teleport.TeleportDestination;
import dev.zontreck.libzontreck.exceptions.InvalidDeserialization;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class Home {
    public UUID owner;
    public String homeName;
    public TeleportDestination destination;
    public ItemStack homeIcon;


    public Home(ServerPlayer player, String name, TeleportDestination dest, ItemStack homeIcon)
    {
        owner=player.getUUID();
        homeName=name;
        destination=dest;
        this.homeIcon = homeIcon;
    }

    public Home(CompoundTag tag)
    {
        owner = tag.getUUID("owner");
        homeName = tag.getString("name");
        try {
            destination = new TeleportDestination(tag.getCompound("dest"));
        } catch (InvalidDeserialization e) {
            e.printStackTrace();
        }
        if(tag.contains("icon"))
        {
            homeIcon = ItemStack.of(tag.getCompound("icon"));
        } else homeIcon = new ItemStack(Items.BLUE_BED);
    }

    public CompoundTag serialize()
    {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("owner", owner);
        tag.putString("name", homeName);
        tag.put("dest", destination.serialize());
        tag.put("icon", homeIcon.serializeNBT());

        return tag;
    }
}
