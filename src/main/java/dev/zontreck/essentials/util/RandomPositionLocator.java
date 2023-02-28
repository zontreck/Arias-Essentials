package dev.zontreck.essentials.util;

import dev.zontreck.essentials.AriasEssentials;
import dev.zontreck.essentials.Messages;
import dev.zontreck.essentials.events.RTPEvent;
import dev.zontreck.essentials.events.RTPNotCancelledEvent;
import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.util.DelayedExecutorService;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.common.MinecraftForge;

/**
 * This class aims to serve as the Random Position Locate system
 * It aims to be as non-thread blocking as possible to avoid server lag
 * 
 * To utilize, initialize a RTPContainer from the RandomPositionFactory and execute from there.
 */
public class RandomPositionLocator implements Runnable
{
    private final RTPContainer contain;

    public RandomPositionLocator(RTPContainer rtp)
    {
        contain=rtp;
    }

    @Override
    public void run() {
        if(!AriasEssentials.ALIVE)return;
        ChatHelpers.broadcastTo(contain.container.PlayerInst.getUUID(), new TextComponent(Messages.ESSENTIALS_PREFIX + ChatColor.doColors(" !Dark_Purple!Searching... Attempt "+String.valueOf(contain.tries)+"/30")), contain.container.PlayerInst.server);

        ServerLevel levl = contain.container.Dimension;
        ChunkAccess chunk  = levl.getChunk(contain.container.world_pos.Position.asBlockPos());
        ChunkPos cpos = chunk.getPos();
        boolean needsLoading = false;
        needsLoading = !(levl.getForcedChunks().contains(cpos.toLong()));


        if(needsLoading)
            levl.setChunkForced(cpos.x, cpos.z, true);

        int curChecks=0;
        while(curChecks<10)
        {
            if(contain.isSafe(contain.container.world_pos.Position.asBlockPos()))
            {
                contain.complete=true;
                if(needsLoading)    
                    levl.setChunkForced(cpos.x, cpos.z, false);
                
                if(MinecraftForge.EVENT_BUS.post(new RTPEvent(contain.container.PlayerInst, contain.container.world_pos)))
                {
                    contain.complete=false;
                    contain.container.Position = contain.container.world_pos.Position.asMinecraftVector();
                    ChatHelpers.broadcastTo(contain.container.PlayerInst.getUUID(), new TextComponent(Messages.ESSENTIALS_PREFIX + ChatColor.doColors(" !Dark_Red!Last position checked was probably claimed. Another mod has asked us not to send you to that location, continuing the search")), contain.container.PlayerInst.server);

                    break;
                }else {
                    AriasEssentials.LOGGER.info("RTP Not cancelled. Actioning");
                    new RTPNotCancelledEvent(contain).send();
                }
                return;
            }else {
                curChecks++;
                contain.move();
                //AriasEssentials.LOGGER.info("[DEBUG] "+ChatColor.doColors("!Dark_Red!Checking position: "+contain.container.world_pos.Position.toString()+"; "+contain.container.Dimension.getBlockState(contain.container.world_pos.Position.asBlockPos()).getBlock().toString()+"; "+contain.container.Dimension.getBlockState(contain.container.world_pos.Position.asBlockPos().below()).getBlock().toString()));
            }
        }
        if(needsLoading)
            levl.setChunkForced(cpos.x, cpos.z, false);
            
        contain.newPosition();

        if(contain.tries > 30)
        {
            // Abort
            ChatHelpers.broadcastTo(contain.container.PlayerInst.getUUID(), new TextComponent(Messages.ESSENTIALS_PREFIX + ChatColor.doColors(" !Dark_Red!Could not find a suitable location in 30 attempts")), contain.container.PlayerInst.server);
            contain.aborted=true;
            return;
        }else {
            // Schedule the task to execute
            //run();
            RandomPositionLocator next = new RandomPositionLocator(contain);
            DelayedExecutorService.getInstance().schedule(next, 1);
            AriasEssentials.LOGGER.info("Giving up current RTP search. Scheduling another search in 1 second");
        }
    }
    
}
