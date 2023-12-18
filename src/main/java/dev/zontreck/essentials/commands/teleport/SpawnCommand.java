package dev.zontreck.essentials.commands.teleport;

import com.mojang.brigadier.CommandDispatcher;

import dev.zontreck.essentials.Messages;
import dev.zontreck.libzontreck.util.ChatHelpers;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
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
