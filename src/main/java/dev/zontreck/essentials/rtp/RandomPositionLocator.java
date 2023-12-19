package dev.zontreck.essentials.rtp;

import dev.zontreck.ariaslib.terminal.Task;
import dev.zontreck.ariaslib.util.DelayedExecutorService;
import dev.zontreck.essentials.AriasEssentials;
import dev.zontreck.essentials.events.RTPFoundEvent;
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
public class RandomPositionLocator extends Task
{
    private final RTP contain;

    public RandomPositionLocator(RTP rtp)
    {
        super("RPL",true);
        contain=rtp;
    }

    @Override
    public void run() {
        if(!AriasEssentials.ALIVE)return;
        
        //ChatHelpers.broadcastTo(contain.container.PlayerInst.getUUID(), ChatHelpers.macro(Messages.RTP_SEARCHING, String.valueOf(contain.tries), "30"), contain.container.PlayerInst.server);

        ServerLevel levl = contain.position.getActualDimension();
        ChunkAccess chunk  = levl.getChunk(contain.position.Position.asBlockPos());
        ChunkPos cpos = chunk.getPos();
        boolean needsLoading = false;
        needsLoading = !(levl.getForcedChunks().contains(cpos.toLong()));


        if(needsLoading)
            levl.setChunkForced(cpos.x, cpos.z, true);

        int curChecks=0;
        while(curChecks<3)
        {
            if(contain.isSafe(contain.position.Position.asBlockPos()))
            {
                if(needsLoading)    
                    levl.setChunkForced(cpos.x, cpos.z, false);

                MinecraftForge.EVENT_BUS.post(new RTPFoundEvent(contain));

                return;
            } else {
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
