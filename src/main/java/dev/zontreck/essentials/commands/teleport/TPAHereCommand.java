package dev.zontreck.essentials.commands.teleport;

import com.mojang.brigadier.CommandDispatcher;

import dev.zontreck.essentials.Messages;
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
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

public class TPAHereCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("tpahere").executes(c->usage(c.getSource())).then(Commands.argument("player", EntityArgument.player()).executes(c -> tpa(c.getSource(), EntityArgument.getPlayer(c, "player")))));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int tpa(CommandSourceStack source, ServerPlayer serverPlayer) {
        // Send the request to player

        ServerPlayer play = (ServerPlayer)source.getEntity();
        if(serverPlayer == null){
            source.sendFailure(ChatHelpers.macro(Messages.PLAYER_NOT_FOUND));
            return 1;
        }
        if(play.getUUID() == serverPlayer.getUUID()){
            source.sendFailure(ChatHelpers.macro(Messages.NO_TP_TO_SELF));
            return 1;
        }
        
        TeleportContainer cont = new TeleportContainer(serverPlayer.getUUID(), play.getUUID());

        for(TeleportContainer cont2 : TeleportRegistry.get()){
            if(cont2.compareTo(cont)==0){
                ChatHelpers.broadcastTo(cont.ToPlayer, ChatHelpers.macro(Messages.NO_MORE_THAN_ONE_TPA), source.getServer());
                return 0;
            }else {
                if(cont2.ToPlayer.equals(cont.ToPlayer)){
                    ChatHelpers.broadcastTo(cont.ToPlayer, ChatHelpers.macro(Messages.NO_MORE_THAN_ONE_TPA), source.getServer());
                    return 0;
                }
            }
        }


        ClickEvent ce = Clickable.command("/tpcancel "+cont.TeleportID.toString());
        HoverEvent he = HoverTip.get(ChatColor.DARK_GREEN+"Cancel this teleport request");

        Style s = Style.EMPTY.withFont(Style.DEFAULT_FONT).withHoverEvent(he).withClickEvent(ce);

        // Send the alerts
        ChatHelpers.broadcastTo(cont.ToPlayer, new TextComponent(ChatColor.BOLD + ChatColor.DARK_GREEN +"TPA Request Sent! ").append(new TextComponent(ChatColor.BOLD+ChatColor.DARK_GRAY+"["+ChatColor.DARK_RED+"X"+ChatColor.DARK_GRAY+"]").setStyle(s)), serverPlayer.server);


        ce = Clickable.command("/tpaccept "+cont.TeleportID.toString());
        he = HoverTip.get(ChatColor.DARK_GREEN + "Accept tp request");
        ClickEvent ce2 = Clickable.command("/tpdeny "+cont.TeleportID.toString());
        HoverEvent he2 = HoverTip.get(ChatColor.DARK_RED+"Deny this request");
        s = Style.EMPTY.withFont(Style.DEFAULT_FONT).withClickEvent(ce).withHoverEvent(he);

        Style s2 = Style.EMPTY.withFont(Style.DEFAULT_FONT).withClickEvent(ce2).withHoverEvent(he2);

        Profile p;
        try {
            p = Profile.get_profile_of(cont.ToPlayer.toString());
        } catch (UserProfileNotYetExistsException e) {
            return 1;
        }
        serverPlayer.playSound(Messages.TPA_SOUND, 1, serverPlayer.getRandom().nextFloat());
        ChatHelpers.broadcastTo(cont.FromPlayer, ChatHelpers.macro(Messages.TPA_HERE, p.name_color+p.nickname).
            append(ChatHelpers.macro(Messages.TELEPORT_ACCEPT+" ").setStyle(s)).
            append(ChatHelpers.macro(Messages.TELEPORT_DENY).setStyle(s2)), serverPlayer.server);
        
        TeleportRegistry.get().add(cont);
        return 0;
    }

    private static int usage(CommandSourceStack source) {
        source.sendSuccess(new TextComponent("/tpahere USAGE\n\n      "+ChatColor.BOLD + ChatColor.DARK_GRAY+"/tpahere "+ChatColor.DARK_RED+"target_player\n"), false);
        return 0;
    }
}
