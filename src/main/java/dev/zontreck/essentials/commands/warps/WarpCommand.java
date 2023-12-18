package dev.zontreck.essentials.commands.warps;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.zontreck.essentials.Messages;
import dev.zontreck.essentials.commands.teleport.TeleportActioner;
import dev.zontreck.essentials.commands.teleport.TeleportContainer;
import dev.zontreck.essentials.commands.teleport.TeleportDestination;
import dev.zontreck.essentials.rtp.RandomPositionFactory;
import dev.zontreck.essentials.warps.NoSuchWarpException;
import dev.zontreck.essentials.warps.Warp;
import dev.zontreck.essentials.warps.WarpsProvider;
import dev.zontreck.libzontreck.chat.Clickable;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.vectors.Vector3;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
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
                ChatHelpers.broadcastTo(p.getUUID(), ChatHelpers.macro(Messages.WARP_ATTEMPTING), p.server);
            }else{
                
                ChatHelpers.broadcastTo(p.getUUID(), ChatHelpers.macro(Messages.WARPING), p.server);
            }

            Thread tx = new Thread(new Runnable(){
                public void run(){

                    if(type==1){
                        try {
                            dest.Position =  Vector3.ZERO;
                            RandomPositionFactory.beginRTP(p, f_dim);
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
            ChatHelpers.broadcastTo(source.getEntity().getUUID(), ChatHelpers.macro(Messages.WARP_NOT_EXIST), source.getServer());
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static int nowarp(CommandSourceStack source) {
        ServerPlayer p = (ServerPlayer)source.getEntity();

        ChatHelpers.broadcastTo(p.getUUID(), ChatHelpers.applyClickEvent(ChatHelpers.macro(Messages.WARP_NAME_REQUIRED), Clickable.command("/warps")), p.server);

        return 0;
    }

}
