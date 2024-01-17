package dev.zontreck.essentials.commands.teleport;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.zontreck.essentials.Messages;
import dev.zontreck.essentials.configs.server.AEServerConfig;
import dev.zontreck.essentials.events.CommandExecutionEvent;
import dev.zontreck.essentials.util.BackPositionCaches;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.common.MinecraftForge;

public class BackCommand
{
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("back").executes(c->back(c.getSource())));
    }

    public static int back(CommandSourceStack ctx)
    {
        CommandExecutionEvent exec = null;
        try {
            exec = new CommandExecutionEvent(ctx.getPlayerOrException(), "back");
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
        if(MinecraftForge.EVENT_BUS.post(exec))
        {
            return 0;
        }
        try {
            if(!AEServerConfig.getInstance().back.Enabled && !ctx.hasPermission(ctx.getServer().getOperatorUserPermissionLevel()))
            {
                ChatHelpers.broadcastTo(ctx.getPlayerOrException(), ChatHelpers.macro(Messages.TELEPORT_BACK_DISABLED), ctx.getServer());
                return 0;
            }

            WorldPosition wp = BackPositionCaches.Pop(ctx.getPlayerOrException().getUUID());

            ChatHelpers.broadcastTo(ctx.getPlayerOrException(), ChatHelpers.macro(Messages.TELEPORT_BACK), ctx.getServer());

            TeleportContainer cont = new TeleportContainer(ctx.getPlayerOrException(), wp.Position.asMinecraftVector(), ctx.getRotation(), wp.getActualDimension());

            TeleportActioner.ApplyTeleportEffect(ctx.getPlayerOrException());
            TeleportActioner.PerformTeleport(cont, true);
        } catch (Exception e) {
            ChatHelpers.broadcastTo(ctx.getEntity().getUUID(), ChatHelpers.macro(Messages.NO_BACK), ctx.getServer());
        }
        return 0;
    }
}
