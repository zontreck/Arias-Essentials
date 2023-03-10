package dev.zontreck.essentials.commands;

import dev.zontreck.essentials.commands.homes.DelHomeCommand;
import dev.zontreck.essentials.commands.homes.HomeCommand;
import dev.zontreck.essentials.commands.homes.HomesCommand;
import dev.zontreck.essentials.commands.homes.SetHomeCommand;
import dev.zontreck.essentials.commands.teleport.RTPCommand;
import dev.zontreck.essentials.commands.teleport.TPACommand;
import dev.zontreck.essentials.commands.teleport.TPAHereCommand;
import dev.zontreck.essentials.commands.teleport.TPAcceptCommand;
import dev.zontreck.essentials.commands.teleport.TPCancelCommand;
import dev.zontreck.essentials.commands.teleport.TPDenyCommand;
import dev.zontreck.essentials.commands.warps.DelWarpCommand;
import dev.zontreck.essentials.commands.warps.RTPWarpCommand;
import dev.zontreck.essentials.commands.warps.SetWarpCommand;
import dev.zontreck.essentials.commands.warps.WarpCommand;
import dev.zontreck.essentials.commands.warps.WarpsCommand;
import dev.zontreck.essentials.warps.Warps;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommandRegister {
    @SubscribeEvent
    public void onCommandRegister(final RegisterCommandsEvent ev)
    {
        DelHomeCommand.register(ev.getDispatcher());
        HomeCommand.register(ev.getDispatcher());
        HomesCommand.register(ev.getDispatcher());
        SetHomeCommand.register(ev.getDispatcher());

        RTPCommand.register(ev.getDispatcher());
        TPAcceptCommand.register(ev.getDispatcher());
        TPAHereCommand.register(ev.getDispatcher());
        TPACommand.register(ev.getDispatcher());
        TPCancelCommand.register(ev.getDispatcher());
        TPDenyCommand.register(ev.getDispatcher());

        DelWarpCommand.register(ev.getDispatcher());
        RTPWarpCommand.register(ev.getDispatcher());
        SetWarpCommand.register(ev.getDispatcher());
        WarpCommand.register(ev.getDispatcher());
        WarpsCommand.register(ev.getDispatcher());
    }
}
