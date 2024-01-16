package dev.zontreck.essentials.warps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dev.zontreck.essentials.events.WarpCreatedEvent;
import dev.zontreck.essentials.events.WarpDeletedEvent;
import dev.zontreck.libzontreck.exceptions.InvalidDeserialization;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class Warps 
{
    private final Map<String,Warp> warps = new HashMap<>();
    
    private Warps(){

    }

    public Warp getNamedWarp(String name) throws NoSuchWarpException
    {
        if(warps.containsKey(name))
            return warps.get(name);
        else throw new NoSuchWarpException();
    }

    public void add(Warp w)
    {

        WarpCreatedEvent e = new WarpCreatedEvent(w);
        if(!MinecraftForge.EVENT_BUS.post(e))
        {

            warps.put(w.WarpName,w);
            WarpsProvider.updateFile();
        }
    }

    public Map<String, Warp> get()
    {
        return new HashMap<String, Warp>(warps);
    }

    public void delete(Warp w)
    {
        warps.remove(w.WarpName);
        WarpsProvider.updateFile();

        WarpDeletedEvent e = new WarpDeletedEvent(w);
        MinecraftForge.EVENT_BUS.post(e);
    }

    public static Warps getNew()
    {
        return new Warps();
    }

    public CompoundTag serialize()
    {
        CompoundTag tag = new CompoundTag();
        ListTag lst = new ListTag();
        for(Map.Entry<String, Warp> entry : warps.entrySet())
        {
            lst.add(entry.getValue().serialize());
        }

        tag.put("warps", lst);
        return tag;
    }

    public static Warps deserialize(CompoundTag tag)
    {
        Warps w = new Warps();
        ListTag lst = tag.getList("warps", Tag.TAG_COMPOUND);
        for (Tag tag2 : lst) {
            Warp warp;
            try {
                warp = Warp.deserialize((CompoundTag)tag2);
                w.warps.put(warp.WarpName, warp);
            } catch (InvalidDeserialization e) {
                e.printStackTrace();
            }
        }


        return w;
    }
    
}
