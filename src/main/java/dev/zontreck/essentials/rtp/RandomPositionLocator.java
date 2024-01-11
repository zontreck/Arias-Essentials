package dev.zontreck.essentials.rtp;

import dev.zontreck.ariaslib.terminal.Task;
import dev.zontreck.ariaslib.util.DelayedExecutorService;
import dev.zontreck.essentials.AriasEssentials;
import dev.zontreck.essentials.events.RTPFoundEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class serves as the Random Position Locate system, aiming to be non-thread blocking for improved server performance.
 * To utilize, initialize an RTP from the RandomPositionFactory and execute from there.
 */
public class RandomPositionLocator extends Task {
    private static final Logger log = LogManager.getLogger("RPL-"+Thread.currentThread().getName());
    private final RTP contain;

    /**
     * Constructs a RandomPositionLocator with the specified RTP instance.
     *
     * @param rtp The RTP instance to use.
     */
    public RandomPositionLocator(RTP rtp) {
        super("RPL", true);
        contain = rtp;
    }

    @Override
    public void run() {
        if (!AriasEssentials.ALIVE) return;

        if (AriasEssentials.DEBUG) {
            log.debug("RTP Search thread");
        }

        contain.newPosition();

        if(AriasEssentials.DEBUG)
        {
            log.debug("Dispatching RTPFoundEvent - " + contain.position.Dimension);
        }

        contain.position.getActualDimension().getServer().execute(()->{
            MinecraftForge.EVENT_BUS.post(new RTPFoundEvent(contain));
        });
    }
}
