package dev.zontreck.essentials.commands.teleport;

import java.util.UUID;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import dev.zontreck.essentials.Messages;
import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.util.ChatHelpers;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

public class TPDenyCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("tpdeny").then(Commands.argument("TeleportUUID", StringArgumentType.string()).executes(c->doCancel(c.getSource(), StringArgumentType.getString(c, "TeleportUUID")))));
        
        //executes(c -> doCancel(c.getSource())));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int doCancel(CommandSourceStack source, String TPID) {
        UUID teleporter = UUID.fromString(TPID);
        ServerPlayer play = (ServerPlayer)source.getEntity();
        
        for(TeleportContainer cont : TeleportRegistry.get()){
            if(cont.TeleportID.equals(teleporter)){
                // Canceling!
                Component comp = new TextComponent(Messages.ESSENTIALS_PREFIX + ChatColor.DARK_PURPLE+"Teleport request was denied");

                ChatHelpers.broadcastTo(cont.FromPlayer, comp, source.getServer());
                ChatHelpers.broadcastTo(cont.ToPlayer, comp, source.getServer());

                TeleportRegistry.get().remove(cont);
                return 0;
            }
        }

        Component comp = new TextComponent(ChatColor.DARK_RED+"The teleport was not found, perhaps the request expired or was already cancelled/denied");

        ChatHelpers.broadcastTo(play.getUUID(), comp, source.getServer());

        return 0;
    }
}
