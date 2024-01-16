package dev.zontreck.essentials.configs.server.sections;

import net.minecraft.nbt.CompoundTag;

public class Messages
{
    public static final String TAG_NAME = "messages";
    public static final String TAG_BL_DIM_ERROR = "blacklisted_dimension_error";


    public String BlacklistedDimensionError = "!Dark_Red!You appear to be in a place where we cannot find you.";

    public static Messages deserialize(CompoundTag tag)
    {
        Messages msgs = new Messages();
        msgs.BlacklistedDimensionError = tag.getString(TAG_BL_DIM_ERROR);

        return msgs;
    }

    public CompoundTag serialize()
    {
        CompoundTag tag = new CompoundTag();
        tag.putString(TAG_BL_DIM_ERROR, BlacklistedDimensionError);

        return tag;
    }
}
