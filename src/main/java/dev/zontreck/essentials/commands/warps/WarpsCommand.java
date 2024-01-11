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
import dev.zontreck.essentials.commands.teleport.TeleportActioner;
import dev.zontreck.essentials.commands.teleport.TeleportContainer;
import dev.zontreck.essentials.commands.teleport.TeleportDestination;
import dev.zontreck.essentials.rtp.RandomPositionFactory;
import dev.zontreck.essentials.warps.Warp;
import dev.zontreck.essentials.warps.WarpsProvider;
import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.chat.Clickable;
import dev.zontreck.libzontreck.chat.HoverTip;
import dev.zontreck.libzontreck.chestgui.ChestGUI;
import dev.zontreck.libzontreck.chestgui.ChestGUIButton;
import dev.zontreck.libzontreck.chestgui.ChestGUIIdentifier;
import dev.zontreck.libzontreck.lore.LoreEntry;
import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.libzontreck.profiles.UserProfileNotYetExistsException;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.util.heads.HeadCache;
import dev.zontreck.libzontreck.util.heads.HeadUtilities;
import dev.zontreck.libzontreck.vectors.Vector2i;
import dev.zontreck.libzontreck.vectors.Vector3;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.ExecuteCommand;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mutable;

public class WarpsCommand {
    private static final ChestGUIIdentifier WARPS_GUI_ID = new ChestGUIIdentifier("ess_warps");
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

        ChestGUI gui = ChestGUI.builder().withTitle("Warps").withPlayer(p.getUUID()).withGUIId(WARPS_GUI_ID);

        final ChestGUI chestGui = gui;

        int iconX=0;
        int iconY=0;

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
            int warpType;
            if(warp.RTP) warpType=1;
            else {
                warpType = 0;
            }

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


            ChestGUIButton button = new ChestGUIButton(HeadUtilities.get(prof.username, warpName), ()->{
                TeleportDestination dest = warp.destination;
                if(warpType == 1)
                {
                    dest.Position = Vector3.ZERO;
                    RandomPositionFactory.beginRTP(p, warp.destination.getActualDimension());
                    chestGui.close();
                    return;
                }
                try {
                    TeleportActioner.ApplyTeleportEffect(p);
                    TeleportContainer tc = new TeleportContainer(p, dest.Position.asMinecraftVector(), dest.Rotation.asMinecraftVector(), dest.getActualDimension());
                    TeleportActioner.PerformTeleport(tc);

                }catch(Exception e){

                }

                chestGui.close();
            }, new Vector2i(iconX, iconY))
                    .withInfo(new LoreEntry.Builder().text(ChatColor.doColors("!Dark_Purple!Owner: " + prof.name_color + prof.nickname))

                            .build()
                    )
                    .withInfo(new LoreEntry.Builder().text(ChatHelpers.macro(appendType, warp.destination.Dimension).getString()).build());

            if(warps.size() > 27)
            {
                // Say to person
                ChatHelpers.broadcastTo(p, warpMsg, p.server);
            }else
                chestGui.withButton(button);

            iconY++;
            if(iconY>=9){
                iconY=0;
                iconX++;
            }

        }

        if(warps.size() < 27)
            chestGui.open();
        
        return 0;
    }

}
