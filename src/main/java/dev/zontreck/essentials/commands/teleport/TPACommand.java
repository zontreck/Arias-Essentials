package dev.zontreck.essentials.commands.teleport;

import com.mojang.brigadier.CommandDispatcher;

import dev.zontreck.ariaslib.terminal.Task;
import dev.zontreck.ariaslib.util.DelayedExecutorService;
import dev.zontreck.essentials.Messages;
import dev.zontreck.essentials.events.CommandExecutionEvent;
import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.chat.Clickable;
import dev.zontreck.libzontreck.chat.HoverTip;
import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.libzontreck.profiles.UserProfileNotYetExistsException;
import dev.zontreck.libzontreck.util.ChatHelpers;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkHooks;

public class TPACommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("tpa").executes(c->usage(c.getSource())).then(Commands.argument("player", EntityArgument.player()).executes(c -> tpa(c.getSource(), EntityArgument.getPlayer(c, "player")))));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int tpa(CommandSourceStack source, ServerPlayer serverPlayer) {

        var exec = new CommandExecutionEvent(source.getPlayer(), "tpa");
        if(MinecraftForge.EVENT_BUS.post(exec))
        {
            return 0;
        }
        // Send the request to player
        if(serverPlayer == null){
            source.sendFailure(ChatHelpers.macro(Messages.PLAYER_NOT_FOUND));
            return 1;
        }
        
        ServerPlayer play = (ServerPlayer)source.getEntity();

        
        if(play.getUUID() == serverPlayer.getUUID()){
            source.sendFailure(ChatHelpers.macro(Messages.NO_TP_TO_SELF));
            return 1;
        }
        
        final TeleportContainer cont = new TeleportContainer(play.getUUID(), serverPlayer.getUUID());

        for(TeleportContainer cont2 : TeleportRegistry.get()){
            if(cont2.compareTo(cont)==0){
                ChatHelpers.broadcastTo(cont.FromPlayer, ChatHelpers.macro(Messages.NO_MORE_THAN_ONE_TPA), source.getServer());
                return 0;
            }else {
                if(cont2.FromPlayer == cont.FromPlayer){
                    ChatHelpers.broadcastTo(cont.FromPlayer, ChatHelpers.macro(Messages.NO_MORE_THAN_ONE_TPA), source.getServer());
                    return 0;
                }
            }
        }


        ClickEvent ce = Clickable.command("/tpcancel "+cont.TeleportID.toString());
        HoverEvent he = HoverTip.get(ChatColor.DARK_GREEN+"Cancel this teleport request");

        Style s = Style.EMPTY.withFont(Style.DEFAULT_FONT).withHoverEvent(he).withClickEvent(ce);

        // Send the alerts
        ChatHelpers.broadcastTo(cont.FromPlayer, ChatHelpers.macro(ChatColor.BOLD + ChatColor.DARK_GREEN +"TPA Request Sent! ").append(ChatHelpers.macro(ChatColor.BOLD+ChatColor.DARK_GRAY+"["+ChatColor.DARK_RED+"X"+ChatColor.DARK_GRAY+"]").setStyle(s)), serverPlayer.server);


        ce = Clickable.command("/tpaccept "+cont.TeleportID.toString());
        he = HoverTip.get(ChatColor.DARK_GREEN + "Accept tp request");
        ClickEvent ce2 = Clickable.command("/tpdeny "+ cont.TeleportID);
        HoverEvent he2 = HoverTip.get(ChatColor.DARK_RED+"Deny this request");
        s = Style.EMPTY.withFont(Style.DEFAULT_FONT).withClickEvent(ce).withHoverEvent(he);

        Style s2 = Style.EMPTY.withFont(Style.DEFAULT_FONT).withClickEvent(ce2).withHoverEvent(he2);

        

        Profile p;
        try {
            p = Profile.get_profile_of(cont.FromPlayer.toString());
        } catch (UserProfileNotYetExistsException e) {
            return 1;
        }
        serverPlayer.playSound(Messages.TPA_SOUND, 1, serverPlayer.getRandom().nextFloat());
        ChatHelpers.broadcastTo(cont.ToPlayer, ChatHelpers.macro(Messages.TPA, p.name_color+p.nickname).
            append(ChatHelpers.macro(Messages.TELEPORT_ACCEPT+" ").setStyle(s)).
            append(ChatHelpers.macro(Messages.TELEPORT_DENY).setStyle(s2)), serverPlayer.server);
        
        TeleportRegistry.get().add(cont);
        DelayedExecutorService.getInstance().schedule(new Task("tpa_expire",true){
            @Override
            public void run()
            {
                if(!(TeleportRegistry.get().contains(cont)))return;
                TeleportRegistry.get().remove(cont);

                ChatHelpers.broadcastTo(cont.ToPlayer, ChatHelpers.macro("!Dark_Red!Teleport request has expired"), cont.Dimension.getServer());
                ChatHelpers.broadcastTo(cont.FromPlayer, ChatHelpers.macro("!Dark_Red!Teleport request has expired"), cont.Dimension.getServer());
            }
        }, 30);
        return 0;
    }

    private static int usage(CommandSourceStack source) {

        source.sendSystemMessage(ChatHelpers.macro("/tpa USAGE\n\n      "+ChatColor.BOLD + ChatColor.DARK_GRAY+"/tpa "+ChatColor.DARK_RED+"target_player\n"));
        return 0;
    }
}
