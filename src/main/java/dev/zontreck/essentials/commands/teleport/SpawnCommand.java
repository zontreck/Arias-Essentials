package dev.zontreck.essentials.commands.teleport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.zontreck.essentials.AriasEssentials;
import dev.zontreck.essentials.Messages;
import dev.zontreck.essentials.commands.teleport.TeleportActioner;
import dev.zontreck.essentials.commands.teleport.TeleportContainer;
import dev.zontreck.essentials.commands.teleport.TeleportDestination;
import dev.zontreck.essentials.configs.AEServerConfig;
import dev.zontreck.essentials.util.RTPContainer;
import dev.zontreck.essentials.util.RandomPositionFactory;
import dev.zontreck.essentials.warps.NoSuchWarpException;
import dev.zontreck.essentials.warps.Warp;
import dev.zontreck.essentials.warps.WarpsProvider;
import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.chat.Clickable;
import dev.zontreck.libzontreck.exceptions.InvalidSideException;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.vectors.Vector3;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class SpawnCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("spawn").executes(c-> respawn(c.getSource())));

        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
        //String arg = StringArgumentType.getString(command, "nickname");
        //return setHome(command.getSource(), arg);
        //}));
    }

    private static int respawn(CommandSourceStack source) {
        ServerPlayer p = (ServerPlayer)source.getEntity();

        ChatHelpers.broadcastTo(p.getUUID(), ChatHelpers.macro(Messages.RESPAWNING), p.server);

        BlockPos spawn = p.serverLevel().getSharedSpawnPos();

        TeleportActioner.ApplyTeleportEffect(p);
        TeleportContainer cont = new TeleportContainer(p, new Vec3(spawn.getX(), spawn.getY(), spawn.getZ()), Vec2.ZERO, p.serverLevel());
        TeleportActioner.PerformTeleport(cont);


        return 0;
    }

}
