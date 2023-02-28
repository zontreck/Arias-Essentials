package dev.zontreck.essentials.commands.warps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.zontreck.essentials.AriasEssentials;
import dev.zontreck.essentials.Messages;
import dev.zontreck.essentials.commands.teleport.TeleportActioner;
import dev.zontreck.essentials.commands.teleport.TeleportContainer;
import dev.zontreck.essentials.commands.teleport.TeleportDestination;
import dev.zontreck.essentials.util.RTPContainer;
import dev.zontreck.essentials.util.RandomPositionFactory;
import dev.zontreck.essentials.warps.NoSuchWarpException;
import dev.zontreck.essentials.warps.Warp;
import dev.zontreck.essentials.warps.WarpsProvider;
import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.chat.Clickable;
import dev.zontreck.libzontreck.exceptions.InvalidSideException;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.vectors.Vector3;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class WarpCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("warp").executes(c-> nowarp(c.getSource())).then(Commands.argument("name", StringArgumentType.string()).executes(c->warp(c.getSource(), StringArgumentType.getString(c, "name")))));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int warp(CommandSourceStack source, String string) {
        
        final ServerPlayer p;
        try{
            p=source.getPlayerOrException();
            Warp warp = WarpsProvider.WARPS_INSTANCE.getNamedWarp(string);

            TeleportDestination dest = warp.destination;

            ServerLevel dimL=(ServerLevel) dest.getActualDimension();
            

            final int type = warp.RTP ? 1 : 0;
            final ServerLevel f_dim = dimL;

            if(type == 1)
            {
                ChatHelpers.broadcastTo(p.getUUID(), new TextComponent(Messages.ESSENTIALS_PREFIX+ ChatColor.doColors(" !Dark_Green!Attempting to locate a safe location. This may take a minute or two")), p.server);
            }else{
                
                ChatHelpers.broadcastTo(p.getUUID(), new TextComponent(Messages.ESSENTIALS_PREFIX+ ChatColor.doColors(" !Dark_Purple!Warping!")), p.server);
            }

            Thread tx = new Thread(new Runnable(){
                public void run(){

                    if(type==1){
                        try {
                            dest.Position =  Vector3.ZERO;
                            RandomPositionFactory.beginRTPSearch(p, Vec3.ZERO, Vec2.ZERO, f_dim);
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    }
    
                    TeleportActioner.ApplyTeleportEffect(p);
                    TeleportContainer tc = new TeleportContainer(p, dest.Position.asMinecraftVector(), dest.Rotation.asMinecraftVector(), f_dim);
                    TeleportActioner.PerformTeleport(tc);
                }
            });
            tx.start();
        }catch(NoSuchWarpException e)
        {
            ChatHelpers.broadcastTo(source.getEntity().getUUID(), new TextComponent(ChatColor.DARK_RED+"No such warp"), source.getServer());
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static int nowarp(CommandSourceStack source) {
        ServerPlayer p = (ServerPlayer)source.getEntity();
        ChatHelpers.broadcastTo(p.getUUID(), new TextComponent(ChatColor.DARK_RED + "You must supply the warp name. If you need to know what warps are available, please use the warps command, or click this message.").withStyle(Style.EMPTY.withFont(Style.DEFAULT_FONT).withClickEvent(Clickable.command("/warps"))), source.getServer());
        return 0;
    }

}
