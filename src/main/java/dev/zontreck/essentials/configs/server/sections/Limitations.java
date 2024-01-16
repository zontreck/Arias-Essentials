package dev.zontreck.essentials.configs.server.sections;

import net.minecraft.nbt.CompoundTag;

public class Limitations {
    public static final String TAG_NAME = "limits";

    public static final String TAG_MAX_HOMES = "max_homes";
    public static final String TAG_MAX_WARPS = "max_warps";


    public int MaxHomes = 27;
    public int MaxWarps = 27;


    public static Limitations deserialize(CompoundTag tag)
    {
        Limitations limits = new Limitations();
        limits.MaxHomes = tag.getInt(TAG_MAX_HOMES);
        limits.MaxWarps = tag.getInt(TAG_MAX_WARPS);

        return limits;
    }

    public CompoundTag serialize()
    {
        CompoundTag tag = new CompoundTag();
        tag.putInt(TAG_MAX_HOMES, MaxHomes);
        tag.putInt(TAG_MAX_WARPS, MaxWarps);

        return tag;
    }

}
