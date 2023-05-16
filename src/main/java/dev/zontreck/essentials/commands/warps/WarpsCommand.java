package dev.zontreck.essentials.commands.warps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mojang.brigadier.CommandDispatcher;

import dev.zontreck.essentials.Messages;
import dev.zontreck.essentials.warps.Warp;
import dev.zontreck.essentials.warps.WarpsProvider;
import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.chat.Clickable;
import dev.zontreck.libzontreck.chat.HoverTip;
import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.libzontreck.profiles.UserProfileNotYetExistsException;
import dev.zontreck.libzontreck.util.ChatHelpers;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mutable;

public class WarpsCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("warps").executes(c-> warps(c.getSource())));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int warps(CommandSourceStack source) {
        

        ServerPlayer p = (ServerPlayer)source.getEntity();
    
        Map<String, Warp> warps = WarpsProvider.WARPS_INSTANCE.get();
        
        ChatHelpers.broadcastTo(p.getUUID(), ChatHelpers.macro(Messages.COUNT, String.valueOf(warps.size()), "warp"), source.getServer());

        Iterator<Entry<String, Warp>> it = warps.entrySet().iterator();
        while(it.hasNext())
        {
            // TODO: Implement public and private. Private requires an ACL be implemented. New GUI(?)
            Warp warp = it.next().getValue();
            // Pull the owner profile
            Profile prof=null;
            try {
                prof = Profile.get_profile_of(warp.owner.toString());
            } catch (UserProfileNotYetExistsException e) {
                e.printStackTrace();
                return 1;
            }
            String warpName = warp.WarpName;
            int warpType = 0;
            if(warp.RTP) warpType=1;
            
            String appendType = (warpType == 0) ? Messages.WARP_STANDARD : Messages.WARP_RTP;
            

            HoverEvent hover = HoverTip.get(ChatHelpers.macroize(appendType, warp.destination.Dimension));
            ClickEvent click = Clickable.command("/warp "+warpName);

            MutableComponent warpMsg = ChatHelpers.macro(ChatColor.GREEN + warpName + ChatColor.resetChat());

            warpMsg = ChatHelpers.applyHoverEvent(warpMsg, hover);
            
            // Now, display the warp name, along with the warp's owner information
            HoverEvent h2 = HoverTip.get(
                ChatHelpers.macroize(Messages.WARP_HOVER_FORMAT,  
                    ChatHelpers.macroize(Messages.WARP_OWNER, prof.name_color, prof.nickname),
                    ChatHelpers.macroize(Messages.WARP_ACCESS_FORMAT, 
                        (warp.isPublic ? ChatHelpers.macroize(Messages.PUBLIC) : ChatHelpers.macroize(Messages.PRIVATE))
                    )
                ) 
                
            );

            Component ownerInfo = ChatHelpers.applyHoverEvent(ChatHelpers.macro(Messages.HOVER_WARP_INFO), h2);


            // Combine the two
            warpMsg = warpMsg.copy().append(ownerInfo);
            warpMsg = ChatHelpers.applyClickEvent(warpMsg, click);
            ChatHelpers.broadcastTo(p.getUUID(), warpMsg, source.getServer());
        }
        
        return 0;
    }

}
