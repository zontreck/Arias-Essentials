package dev.zontreck.essentials.commands.homes;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.zontreck.essentials.AriasEssentials;
import dev.zontreck.essentials.Messages;
import dev.zontreck.essentials.homes.Home;
import dev.zontreck.essentials.homes.Homes;
import dev.zontreck.libzontreck.chat.Clickable;
import dev.zontreck.libzontreck.chat.HoverTip;
import dev.zontreck.libzontreck.util.ChatHelpers;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Style;

public class HomesCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("homes").executes(HomesCommand::getHomes));
    }

    private static int getHomes(CommandContext<CommandSourceStack> ctx)
    {
        // Request homes
        try{
            ServerPlayer player = ctx.getSource().getPlayerOrException();

            Homes homes = AriasEssentials.player_homes.get(player.getUUID());

            ChatHelpers.broadcastTo(player.getUUID(), ChatHelpers.macro(Messages.HOME_COUNT, String.valueOf(homes.count())), player.server);

            
            for (Home string : homes.getList()) {
                Style st = Style.EMPTY.withFont(Style.DEFAULT_FONT).withHoverEvent(HoverTip.get(ChatHelpers.macroize(Messages.HOME_HOVER_TEXT))).withClickEvent(Clickable.command("/home "+string.homeName));

                ChatHelpers.broadcastTo(player.getUUID(), ChatHelpers.macro(Messages.HOME_FORMAT, string.homeName).setStyle(st), ctx.getSource().getServer());
                
            }
        }catch(CommandSyntaxException ex)
        {
            ex.printStackTrace();

        }

        return 0;
    }
    
}
