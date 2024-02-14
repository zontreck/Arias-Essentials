package dev.zontreck.essentials.commands.gui;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import dev.zontreck.essentials.Messages;
import dev.zontreck.essentials.events.CommandExecutionEvent;
import dev.zontreck.essentials.networking.ModMessages;
import dev.zontreck.essentials.networking.packets.s2c.S2CUpdateHearts;
import dev.zontreck.libzontreck.util.ChatHelpers;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.common.MinecraftForge;

public class HeartsCommand
{
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("hearts").executes(x->usage(x.getSource())).then(Commands.argument("compress", BoolArgumentType.bool()).executes(x->hearts(x.getSource(), BoolArgumentType.getBool(x, "compress")))));
    }

    private static int hearts(CommandSourceStack stack, boolean compressHearts)
    {

        var exec = new CommandExecutionEvent(stack.getPlayer(), "hearts");
        if(MinecraftForge.EVENT_BUS.post(exec))
        {
            return 0;
        }
        // Send the state to the client, then update the config
        // Send feedback to the user
        ChatHelpers.broadcastTo(stack.getPlayer().getUUID(), ChatHelpers.macro(Messages.HEARTS_UPDATED), stack.getServer());

        S2CUpdateHearts update = new S2CUpdateHearts(compressHearts);
        ModMessages.sendToPlayer(update, stack.getPlayer());

        return 0;
    }

    private static int usage(CommandSourceStack stack)
    {
        ChatHelpers.broadcastTo(stack.getPlayer().getUUID(), ChatHelpers.macro(Messages.HEARTS_USAGE), stack.getServer());


        return 0;
    }
}
