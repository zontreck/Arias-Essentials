package dev.zontreck.essentials.commands.warps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import dev.zontreck.essentials.Messages;
import dev.zontreck.essentials.commands.teleport.TeleportDestination;
import dev.zontreck.essentials.events.WarpCreatedEvent;
import dev.zontreck.essentials.warps.Warp;
import dev.zontreck.essentials.warps.WarpsProvider;
import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.vectors.Vector2;
import dev.zontreck.libzontreck.vectors.Vector3;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;

public class RTPWarpCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("rtpwarp").then(Commands.argument("nickname", StringArgumentType.string()).executes(c -> setWarp(c.getSource(), StringArgumentType.getString(c, "nickname")))));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int setWarp(CommandSourceStack source, String string) {
        
        ServerPlayer p = (ServerPlayer)source.getEntity();

        Vec3 position = p.position();
        Vec2 rot = p.getRotationVector();

        TeleportDestination dest = new TeleportDestination(new Vector3(position), new Vector2(rot), p.getLevel());
        Warp warp = new Warp(p.getUUID(), string, true, true, dest);
        WarpCreatedEvent event = new WarpCreatedEvent(warp);
        if(MinecraftForge.EVENT_BUS.post(event))
        {
            ChatHelpers.broadcastTo(p.getUUID(), ChatHelpers.macro(Messages.WARP_CREATE_ERROR), p.server);
            return 0;
        }
        WarpsProvider.WARPS_INSTANCE.add(warp);


        ChatHelpers.broadcastTo(p.getUUID(), ChatHelpers.macro(Messages.WARP_RTP_CREATED), p.server);

        return 0;
    }
}
