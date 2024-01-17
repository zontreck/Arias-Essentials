package dev.zontreck.essentials.commands.teleport;

import java.util.Iterator;
import java.util.UUID;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.zontreck.essentials.Messages;
import dev.zontreck.essentials.events.CommandExecutionEvent;
import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.util.ChatHelpers;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;

public class TPAcceptCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("tpaccept").then(Commands.argument("TeleportUUID", StringArgumentType.string()).executes(c->doAccept(c.getSource(), StringArgumentType.getString(c, "TeleportUUID")))));
    }

    private static int doAccept(CommandSourceStack source, String TPID) {

        CommandExecutionEvent exec = null;
        try {
            exec = new CommandExecutionEvent(source.getPlayerOrException(), "tpaccept");
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
        if(MinecraftForge.EVENT_BUS.post(exec))
        {
            return 0;
        }
        UUID teleporter = UUID.fromString(TPID);
        
        ServerPlayer play = (ServerPlayer)source.getEntity();
        Iterator<TeleportContainer> it = TeleportRegistry.get().iterator();
        while(it.hasNext())
        {
            TeleportContainer cont = it.next();
            if(cont.TeleportID.equals(teleporter)){
                // Accepting!

                ServerPlayer from = source.getServer().getPlayerList().getPlayer(cont.FromPlayer);
                ServerPlayer to = source.getServer().getPlayerList().getPlayer(cont.ToPlayer);

                Component comp = ChatHelpers.macro(Messages.TELEPORT_REQUEST_ACCEPTED);

                ChatHelpers.broadcastTo(cont.FromPlayer, comp, source.getServer());
                ChatHelpers.broadcastTo(cont.ToPlayer, comp, source.getServer());

                it.remove();


                cont.PlayerInst = from;
                cont.Position = to.position();
                cont.Rotation = to.getRotationVector();
                cont.Dimension = to.getLevel();

                TeleportActioner.ApplyTeleportEffect(from);
                TeleportActioner.PerformTeleport(cont, false);
                return 0;
            }
        }

        Component comp = ChatHelpers.macro(Messages.TELEPORT_REQUEST_NOT_FOUND);

        ChatHelpers.broadcastTo(play.getUUID(), comp, source.getServer());

        return 0;
    }
}
