package dev.zontreck.essentials.commands.warps;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import dev.zontreck.essentials.Messages;
import dev.zontreck.essentials.events.CommandExecutionEvent;
import dev.zontreck.essentials.warps.NoSuchWarpException;
import dev.zontreck.essentials.warps.Warp;
import dev.zontreck.essentials.warps.WarpsProvider;
import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.util.ChatHelpers;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;

public class DelWarpCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("delwarp").then(Commands.argument("nickname", StringArgumentType.string()).executes(c -> setWarp(c.getSource(), StringArgumentType.getString(c, "nickname")))));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int setWarp(CommandSourceStack source, String string) {

        var exec = new CommandExecutionEvent(source.getPlayer(), "delwarp");
        if(MinecraftForge.EVENT_BUS.post(exec))
        {
            return 0;
        }


        ServerPlayer p = (ServerPlayer)source.getEntity();

        Warp warp;
        try {
            warp = WarpsProvider.WARPS_INSTANCE.getNamedWarp(string);
        } catch (NoSuchWarpException e) {
            ChatHelpers.broadcastTo(p.getUUID(), ChatHelpers.macro(Messages.WARP_NOT_EXIST), p.server);
            return 0;
        }
        if(p.getUUID().equals(warp.owner) || p.hasPermissions(4))
        {
            try {
                WarpsProvider.WARPS_INSTANCE.delete(WarpsProvider.WARPS_INSTANCE.getNamedWarp(string));
            } catch (NoSuchWarpException e) {
                ChatHelpers.broadcastTo(p.getUUID(), ChatHelpers.macro(Messages.WARP_NOT_EXIST), p.server);
                return 0;
            }
        
            ChatHelpers.broadcastTo(p.getUUID(), ChatHelpers.macro(Messages.WARP_DELETE_SUCCESS), p.server);
    
        }else {
            
            ChatHelpers.broadcastTo(p.getUUID(), ChatHelpers.macro(Messages.WARP_DELETE_FAIL), p.server);
        }
    
        return 0;
    }
}
