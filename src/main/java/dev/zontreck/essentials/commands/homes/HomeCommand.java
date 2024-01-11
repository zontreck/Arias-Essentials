package dev.zontreck.essentials.commands.homes;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.zontreck.essentials.AriasEssentials;
import dev.zontreck.essentials.Messages;
import dev.zontreck.essentials.commands.teleport.TeleportActioner;
import dev.zontreck.essentials.commands.teleport.TeleportContainer;
import dev.zontreck.essentials.commands.teleport.TeleportDestination;
import dev.zontreck.essentials.homes.Home;
import dev.zontreck.essentials.exceptions.NoSuchHomeException;
import dev.zontreck.libzontreck.util.ChatHelpers;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class HomeCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("home").executes(c-> home(c.getSource(), "default")).then(Commands.argument("nickname", StringArgumentType.string()).executes(c -> home(c.getSource(), StringArgumentType.getString(c, "nickname")))));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int home(CommandSourceStack ctx, String homeName)
    {
        // Request homes
//        String homeName = "";
//        CommandSourceStack ctx = ctx2.getSource();
//        homeName = StringArgumentType.getString(ctx2, "nickname");
//        if(homeName==null)return 0;
        try{
            ServerPlayer p = ctx.getPlayerOrException();
            Home home = AriasEssentials.player_homes.get(p.getUUID()).get(homeName);

            //Transaction tx = new Transaction(Bank.getAccount(p.getUUID()), Bank.SYSTEM, AEServerConfig.COST_TO_TP_HOME.get(), Instant.now().getEpochSecond());

            //ChatHelpers.broadcastTo(p.getUUID(), ChatHelpers.macro(Messages.PAYMENT_ATTEMPTING,
            //        "$" + String.valueOf(AEServerConfig.COST_TO_TP_HOME.get()), "SYSTEM"), p.server);
            /*if(true)
            {
                ChatHelpers.broadcastTo(p.getUUID(), ChatHelpers.macro(Messages.PAYMENT_SUCCESS), p.server);
            }else {
                ChatHelpers.broadcastTo(p.getUUID(), ChatHelpers.macro(Messages.PAYMENT_FAILED), p.server);
                return 0;
            }*/

            TeleportDestination dest = home.destination;
            TeleportActioner.ApplyTeleportEffect(p);
            TeleportContainer cont = new TeleportContainer(p, dest.Position.asMinecraftVector(), dest.Rotation.asMinecraftVector(), dest.getActualDimension());
            TeleportActioner.PerformTeleport(cont);

            ChatHelpers.broadcastTo(p.getUUID(), ChatHelpers.macro(Messages.TELEPORTING_HOME), ctx.getServer());
        }catch(CommandSyntaxException e)
        {
            e.printStackTrace();
            return 1;
        }catch(NoSuchHomeException e)
        {
            
            ChatHelpers.broadcastTo(ctx.getEntity().getUUID(), ChatHelpers.macro(Messages.TELEPORT_HOME_FAIL), ctx.getServer());
            return 0;
        }


        return 0;
    }
}
