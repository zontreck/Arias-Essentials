package dev.zontreck.essentials.commands;

import dev.zontreck.otemod.commands.homes.DelHomeCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommandRegister {
    @SubscribeEvent
    public void onCommandRegister(final RegisterCommandsEvent ev)
    {
        DelHomeCommand.register(ev);
    }
}
