package dev.zontreck.essentials.configs.server.sections;

import dev.zontreck.essentials.events.CommandExecutionEvent;
import net.minecraft.nbt.CompoundTag;

public class Cooldown
{
    public static final String TAG_NAME = "cooldowns";
    public static final String TAG_COMMAND = "command";
    public static final String TAG_SECONDS = "seconds";

    public Cooldown(){

    }

    public Cooldown(String name, long sec)
    {
        Command = name;
        Seconds = sec;
    }

    public String Command;
    public long Seconds;

    public static Cooldown deserialize(CompoundTag tag)
    {
        Cooldown cd = new Cooldown();
        cd.Command = tag.getString(TAG_COMMAND);
        cd.Seconds = tag.getLong(TAG_SECONDS);

        return cd;
    }

    public CompoundTag serialize()
    {
        CompoundTag tag = new CompoundTag();
        tag.putString(TAG_COMMAND, Command);
        tag.putLong(TAG_SECONDS, Seconds);

        return tag;
    }
}
