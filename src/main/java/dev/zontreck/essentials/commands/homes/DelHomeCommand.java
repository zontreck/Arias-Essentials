package dev.zontreck.essentials.commands.homes;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import dev.zontreck.essentials.AriasEssentials;
import dev.zontreck.essentials.Messages;
import dev.zontreck.essentials.events.CommandExecutionEvent;
import dev.zontreck.essentials.homes.HomesSuggestionProvider;
import dev.zontreck.libzontreck.util.ChatHelpers;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.commands.FunctionCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;

public class DelHomeCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(
                Commands.literal("rmhome")
                        .executes(c->rmHome(c.getSource(), "default"))
                        .then(Commands.argument("nickname", StringArgumentType.string())
                                    .suggests(HomesSuggestionProvider.PROVIDER)
                                    .executes(c -> rmHome(c.getSource(), StringArgumentType.getString(c, "nickname")))
                )

        );
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int rmHome(CommandSourceStack ctx, String homeName)
    {

        var exec = new CommandExecutionEvent(ctx.getPlayer(), "delhome");
        if(MinecraftForge.EVENT_BUS.post(exec))
        {
            return 0;
        }
        // Request homes
//        String homeName = "";
//        CommandSourceStack ctx = ctx2.getSource();
//        homeName = StringArgumentType.getString(ctx2, "nickname");
//        if(homeName==null)return 0;
        try{
            ServerPlayer p = ctx.getPlayerOrException();
            
            AriasEssentials.player_homes.get(p.getUUID()).delete(homeName);


            ChatHelpers.broadcastTo(p, ChatHelpers.macro(Messages.HOME_DELETE_SUCCESS), ctx.getServer());
        }catch(Exception e)
        {
            e.printStackTrace();

            ChatHelpers.broadcastTo(ctx.getEntity().getUUID(), ChatHelpers.macro(Messages.HOME_DELETE_FAIL), ctx.getServer());
        }
        return 0;
    }
    
}
