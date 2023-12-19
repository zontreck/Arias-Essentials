package dev.zontreck.essentials.rtp;

import dev.zontreck.ariaslib.util.Lists;
import dev.zontreck.essentials.AriasEssentials;
import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * As of v1.1.121823.x, this is now used for RTP
 * <p>
 * This ensures that RTP is only scanned once every so often. This is because RTP lags the server when it is having to check block positions.
 * <p>
 * If the RTP system scans each dimension (Not blacklisted), then it can build a list of safe locations that will be rotated out every 2 hours.
 * <p>
 * Every 10 minutes, a new RTP location is scanned. This ensures sufficiently semi-random locations. Eventually old locations will be removed from the list.
 * <p>
 * At server start, it will scan 10 RTP locations per dimension while there are no players.
 */
public class RTP
{
    public RTP(ServerLevel level)
    {
        position = new WorldPosition(new Vector3(0,500,0), WorldPosition.getDim(level));

        if(position.getActualDimension().dimensionType().hasCeiling())
        {
            heightMapType = Heightmap.Types.MOTION_BLOCKING_NO_LEAVES;
            SearchDirection=-1;
        }else {
            heightMapType = Heightmap.Types.MOTION_BLOCKING_NO_LEAVES;
            SearchDirection=1;
        }
    }
    private int SearchDirection;
    private Thread containingThread;
    private Heightmap.Types heightMapType;
    public WorldPosition position;
    private List<Block> BLACKLIST = Lists.of(Blocks.LAVA, Blocks.WATER);
    protected int tries;

    public boolean isDimension(ServerLevel level)
    {
        String dim = WorldPosition.getDim(level);
        if(dim.equals(position.Dimension))
        {
            return true;
        }else return false;
    }

    /**
     * Searches for, and finds, a valid RTP location
     * @param level The level to scan
     * @return RTP data
     */
    public static void find(ServerLevel level)
    {
        RandomPositionFactory.beginRTPSearch(level);
    }

    public static List<RTP> slicedByDimension(ServerLevel lvl)
    {
        List<RTP> slice = new ArrayList<>();

        Iterator<RTP> it = RTPCaches.Locations.iterator();
        while(it.hasNext())
        {
            RTP nxt = it.next();
            if(nxt.isDimension(lvl))
            {
                slice.add(nxt);
            }
        }

        return slice;
    }

    public static RTP getRTP(ServerLevel level)
    {
        List<RTP> slice = slicedByDimension(level);
        if(slice.size()>0)
        {
            RTP ret = slice.get(AriasEssentials.random.nextInt(0, slice.size()));
            RTPCaches.Locations.remove(ret);
            RandomPositionFactory.beginRTPSearch(ret.position.getActualDimension());
            return ret;
        } else return null;
    }


    public void moveDown() {
        position.Position = position.Position.moveDown();
    }

    public void moveUp() {
        position.Position = position.Position.moveUp();
    }

    public void move()
    {
        if(SearchDirection==1){
            moveUp();
        }else if(SearchDirection==0)
        {
            moveDown();
        }
    }
    public void moveOpposite()
    {
        if(SearchDirection==1){
            moveDown();
        }else if(SearchDirection==0)
        {
            moveUp();
        }
    }

    public void newPosition() {
        if(!AriasEssentials.ALIVE)return;
        containingThread=Thread.currentThread();
        if(tries>=30)return;
        AriasEssentials.LOGGER.info("RTPContainer starts looking for new position");
        Random rng = new Random(Instant.now().getEpochSecond());
        Vector3 pos = new Vector3(rng.nextDouble(0xFFFF), 0, rng.nextDouble(0xFFFF));
        BlockPos bpos = pos.asBlockPos();
        position.getActualDimension().getChunk(bpos.getX() >> 4, bpos.getZ() >> 4, ChunkStatus.SPAWN);
        pos = new Vector3(
                position.getActualDimension().getHeightmapPos(heightMapType, pos.asBlockPos()));
        while (!position.getActualDimension().getWorldBorder().isWithinBounds(pos.asBlockPos())) {
            pos = new Vector3(rng.nextDouble(0xffff), 0, rng.nextDouble(0xffff));
            bpos = pos.asBlockPos();
            position.getActualDimension().getChunk(bpos.getX() >> 4, bpos.getZ() >> 4, ChunkStatus.SPAWN);
            pos = new Vector3(
                    position.getActualDimension().getHeightmapPos(heightMapType, pos.asBlockPos()));
        }

        position.Position = pos;

        if (pos.y < -60) {
            newPosition();
            return;
        }

        if (pos.y >= position.getActualDimension().getLogicalHeight()) {
            spiralPositions(pos);
        }

        tries++;
        AriasEssentials.LOGGER.info("RTPContainer returns new position");
    }

    private void spiralPositions(Vector3 position)
    {
        Vec3i posi = position.asMinecraftVec3i();
        for(BlockPos pos : BlockPos.spiralAround(new BlockPos(posi.getX(), this.position.getActualDimension().getSeaLevel(), posi.getZ()), 16, Direction.WEST, Direction.NORTH)){
            if(isSafe(pos)){
                // Set the new position
                this.position.Position = new Vector3(pos);
                return;
            }
        }
    }

    private boolean safe(BlockPos blockPos)
    {
        containingThread=Thread.currentThread();
        BlockState b = position.getActualDimension().getBlockState(blockPos);
        BlockState b2 = position.getActualDimension().getBlockState(blockPos.above());
        BlockState b3 = position.getActualDimension().getBlockState(blockPos.below());

        if (b.isAir() && b2.isAir()) {
            if (!b3.isAir()) {
                if (BLACKLIST.contains(b3.getBlock())) {
                    return false;
                } else
                    return true;
            } else
                return false;
        } else
            return false;

    }
    public boolean isSafe(BlockPos blockPos) {
        boolean s = safe(blockPos);
        if(s)
        {
            AriasEssentials.LOGGER.info("/!\\ SAFE /!\\");
        }else AriasEssentials.LOGGER.info("/!\\ NOT SAFE /!\\");

        return s;
    }
}
