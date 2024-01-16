package dev.zontreck.essentials.events;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import java.util.UUID;

@Cancelable
public class CommandExecutionEvent extends Event
{
    public UUID playerID;
    public String command;

    public CommandExecutionEvent(Player player, String cmd)
    {
        playerID=player.getUUID();
        command=cmd;
    }
}
