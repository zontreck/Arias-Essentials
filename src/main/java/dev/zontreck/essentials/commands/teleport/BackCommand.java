package dev.zontreck.essentials.commands.teleport;

import com.mojang.brigadier.CommandDispatcher;
import dev.zontreck.essentials.Messages;
import dev.zontreck.essentials.configs.AEServerConfig;
import dev.zontreck.essentials.util.BackPositionCaches;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class BackCommand
{
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("back").executes(c->back(c.getSource())));
    }

    public static int back(CommandSourceStack ctx)
    {
        try {

            if(!AEServerConfig.ENABLE_BACK.get() && !ctx.hasPermission(ctx.getServer().getOperatorUserPermissionLevel()))
            {
                ChatHelpers.broadcastTo(ctx.getPlayer(), ChatHelpers.macro(Messages.TELEPORT_BACK_DISABLED), ctx.getServer());
                return 0;
            }

            WorldPosition wp = BackPositionCaches.Pop(ctx.getPlayer().getUUID());

            ChatHelpers.broadcastTo(ctx.getPlayer(), ChatHelpers.macro(Messages.TELEPORT_BACK), ctx.getServer());

            TeleportContainer cont = new TeleportContainer(ctx.getPlayer(), wp.Position.asMinecraftVector(), ctx.getRotation(), wp.getActualDimension());

            TeleportActioner.ApplyTeleportEffect(ctx.getPlayer());
            TeleportActioner.PerformTeleport(cont);
        } catch (Exception e) {
            ChatHelpers.broadcastTo(ctx.getPlayer(), ChatHelpers.macro(Messages.NO_BACK), ctx.getServer());
        }
        return 0;
    }
}
