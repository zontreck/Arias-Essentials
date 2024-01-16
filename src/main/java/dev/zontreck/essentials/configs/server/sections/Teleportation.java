package dev.zontreck.essentials.configs.server.sections;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.List;

public class Teleportation
{
    public static final String TAG_NAME = "teleport";
    public static final String TAG_EFFECTS = "effects";
    public static final String TAG_BLACKLIST = "blacklist";

    public List<String> Effects;
    public List<String> Blacklist;

    public static Teleportation deserialize(CompoundTag tag)
    {
        Teleportation teleport = new Teleportation();
        teleport.Effects = new ArrayList<>();
        teleport.Blacklist = new ArrayList<>();
        ListTag effects = tag.getList(TAG_EFFECTS, Tag.TAG_STRING);
        for(Tag str : effects)
        {
            teleport.Effects.add(str.getAsString());
        }

        ListTag dims = tag.getList(TAG_BLACKLIST, Tag.TAG_STRING);
        for(Tag dim : dims)
        {
            teleport.Blacklist.add(dim.getAsString());
        }

        return teleport;
    }

    public CompoundTag serialize()
    {
        CompoundTag tag = new CompoundTag();
        ListTag eff = new ListTag();
        for(String str : Effects)
        {
            eff.add(StringTag.valueOf(str));
        }

        tag.put(TAG_EFFECTS, eff);

        ListTag dims = new ListTag();
        for(String str : Blacklist)
        {
            dims.add(StringTag.valueOf(str));
        }

        tag.put(TAG_BLACKLIST, dims);

        return tag;
    }


    static class TeleportEffect
    {
        String Effect;
        int Duration;
        int Amplifier;
        boolean Ambient;

        public TeleportEffect(String name, int Duration, int Amplifier, boolean Ambient)
        {
            this.Effect = name;
            this.Duration = Duration;
            this.Amplifier = Amplifier;
            this.Ambient = Ambient;
        }
    }
}
