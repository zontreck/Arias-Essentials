package dev.zontreck.essentials.util;

import dev.zontreck.essentials.Messages;
import dev.zontreck.essentials.configs.server.AEServerConfig;
import dev.zontreck.essentials.events.CommandExecutionEvent;
import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.libzontreck.profiles.UserProfileNotYetExistsException;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.util.ServerUtilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.time.Instant;
import java.util.UUID;

public class CommandCooldowns
{
    @SubscribeEvent
    public void onCommand(CommandExecutionEvent ev)
    {
        if(isOnCooldown(ev.playerID, ev.command))
        {
            ev.setCanceled(true);
            ChatHelpers.broadcastTo(ev.playerID, ChatHelpers.macro(Messages.COOLDOWN_IN_PROGRESS, ev.command, getCooldownSeconds(ev.playerID, ev.command) + " seconds"), ServerUtilities.getPlayerByID(ev.playerID.toString()).server);
        }
    }

    public long getCooldownSeconds(UUID ID, String command)
    {
        try{
            Profile prof = Profile.get_profile_of(ID.toString());
            CompoundTag commands = prof.NBT.getCompound("commands");
            return Instant.now().getEpochSecond() - (commands.getLong(command) + getConfigCooldown(command));
        }catch(Exception e)
        {
            e.printStackTrace();
            return 0;
        }
    }

    public long getConfigCooldown(String command)
    {
        if(AEServerConfig.getInstance().cooldowns.containsKey(command))
        {
            return AEServerConfig.getInstance().cooldowns.get(command).Seconds;
        }else return 0;
    }

    public boolean isOnCooldown(UUID ID, String command)
    {
        try {
            Profile prof = Profile.get_profile_of(ID.toString());
            CompoundTag commands = prof.NBT.getCompound("commands");
            if(commands.contains(command))
            {
                long cfg = getConfigCooldown(command);
                if(Instant.now().getEpochSecond() > (commands.getLong(command) + cfg))
                {
                    commands.putLong(command, Instant.now().getEpochSecond());
                    prof.commit();
                    return false;
                }else return true;
            }else {
                commands = new CompoundTag();
                prof.NBT.put("commands", commands);
                commands.putLong(command, Instant.now().getEpochSecond());
                prof.commit();

                return false;
            }
        } catch (UserProfileNotYetExistsException e) {
            throw new RuntimeException(e);
        }

    }
}
