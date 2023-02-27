package dev.zontreck.essentials.commands.warps;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import dev.zontreck.essentials.Messages;
import dev.zontreck.essentials.warps.NoSuchWarpException;
import dev.zontreck.essentials.warps.Warp;
import dev.zontreck.essentials.warps.WarpsProvider;
import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.util.ChatHelpers;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

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
        
        ServerPlayer p = (ServerPlayer)source.getEntity();

        Warp warp;
        try {
            warp = WarpsProvider.WARPS_INSTANCE.getNamedWarp(string);
        } catch (NoSuchWarpException e) {
            ChatHelpers.broadcastTo(p.getUUID(), new TextComponent(Messages.ESSENTIALS_PREFIX+ChatColor.doColors(" !Dark_Red!That warp does not exist")), p.server);
            return 0;
        }
        if(p.getUUID().equals(warp.owner) || p.hasPermissions(5))
        {
            try {
                WarpsProvider.WARPS_INSTANCE.delete(WarpsProvider.WARPS_INSTANCE.getNamedWarp(string));
            } catch (NoSuchWarpException e) {
                ChatHelpers.broadcastTo(p.getUUID(), new TextComponent(Messages.ESSENTIALS_PREFIX+ChatColor.doColors(" !Dark_Red!That warp does not exist")), p.server);
                return 0;
            }
        
            ChatHelpers.broadcastTo(p.getUUID(), new TextComponent(Messages.ESSENTIALS_PREFIX+ChatColor.doColors(" !Dark_Green!Warp deleted successfully")), p.server);
    
        }else {
            
            ChatHelpers.broadcastTo(p.getUUID(), new TextComponent(Messages.ESSENTIALS_PREFIX+ChatColor.doColors(" !Dark_Red!Warp could not be deleted, because you do not own the warp.")), p.server);
        }
    
        return 0;
    }
}
