package dev.zontreck.essentials.configs.server.sections;

import net.minecraft.nbt.CompoundTag;

public class Back
{
    public static final String TAG_NAME = "back";
    public static final String TAG_ENABLE = "enabled";
    public static final String TAG_ALLOW_BACK_FOR_TP = "back_for_tp";


    public boolean Enabled = true;
    public boolean EnabledForTp = false;

    public static Back deserialize(CompoundTag tag)
    {
        Back back = new Back();
        back.Enabled = tag.getBoolean(TAG_ENABLE);
        back.EnabledForTp = tag.getBoolean(TAG_ALLOW_BACK_FOR_TP);

        return back;
    }

    public CompoundTag serialize()
    {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(TAG_ENABLE, Enabled);
        tag.putBoolean(TAG_ALLOW_BACK_FOR_TP, EnabledForTp);

        return tag;
    }
}
