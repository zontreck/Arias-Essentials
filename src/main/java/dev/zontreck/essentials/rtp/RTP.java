package dev.zontreck.essentials.rtp;

import dev.zontreck.ariaslib.util.Lists;
import dev.zontreck.essentials.AriasEssentials;
import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
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

public class RTP {
    private static final List<Block> BLACKLIST = Lists.of(Blocks.LAVA, Blocks.WATER, Blocks.BEDROCK);
    private final int SEARCH_DIRECTION;
    private final Heightmap.Types heightMapType;
    public WorldPosition position;
    private final ServerLevel dimension;
    private int tries;

    public RTP(ServerLevel level) {
        position = new WorldPosition(new Vector3(0, -60, 0), WorldPosition.getDim(level));
        dimension = position.getActualDimension();

        if (position.getActualDimension().dimensionType().hasCeiling()) {
            heightMapType = Heightmap.Types.MOTION_BLOCKING_NO_LEAVES;
            SEARCH_DIRECTION = -1;
        } else {
            heightMapType = Heightmap.Types.MOTION_BLOCKING_NO_LEAVES;
            SEARCH_DIRECTION = 1;
        }
    }

    public boolean isDimension(ServerLevel level) {
        String dim = WorldPosition.getDim(level);
        return dim.equals(position.Dimension);
    }

    public BlockPos findSafeLandingLocation() {
        BlockPos targetPos = position.Position.asBlockPos();

        // Search upward for a safe landing location
        while (!isSafe(targetPos) || !isSafe(targetPos.above())) {
            targetPos = targetPos.above();
        }

        return targetPos;
    }

    private boolean isSafe(BlockPos blockPos) {
        BlockState blockState = dimension.getBlockState(blockPos);
        BlockState blockStateAbove = dimension.getBlockState(blockPos.above());
        BlockState blockStateBelow = dimension.getBlockState(blockPos.below());

        if (blockState.isAir() && blockStateAbove.isAir()) {
            if (!blockStateBelow.isAir()) {
                return !BLACKLIST.contains(blockStateBelow.getBlock());
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static RTP getRTP(ServerLevel level) {
        List<RTP> slice = slicedByDimension(level);
        if (!slice.isEmpty()) {
            RTP ret = slice.get(AriasEssentials.random.nextInt(slice.size()));
            RTPCaches.Locations.remove(ret);
            RandomPositionFactory.beginRTPSearch(ret.position.getActualDimension());
            return ret;
        } else {
            return null;
        }
    }

    public void move() {
        if (SEARCH_DIRECTION == 1) {
            position.Position = position.Position.moveUp();
        } else if (SEARCH_DIRECTION == -1) {
            position.Position = position.Position.moveDown();
        }
    }

    public void moveOpposite() {
        move();
    }

    public void newPosition() {
        if (!AriasEssentials.ALIVE || tries >= 5) return;

        AriasEssentials.LOGGER.info("RTP starts looking for a new position");

        Random rng = new Random(Instant.now().getEpochSecond());

        Vector3 pos;
        BlockPos bpos;

        do {
            pos = new Vector3(rng.nextDouble(0xFFFF), -60, rng.nextDouble(0xFFFF));

            pos = spiralPositions(pos);
            position.Position = pos;
            bpos = pos.asBlockPos();
        } while (!isValidPosition(bpos));

        tries++;
        AriasEssentials.LOGGER.info("RTP returns a new position");
    }

    private boolean isValidPosition(BlockPos bpos) {
        ChunkStatus status = ChunkStatus.SPAWN;
        dimension.getChunk(bpos.getX() >> 4, bpos.getZ() >> 4, status);

        Vector3 pos = new Vector3(dimension.getHeightmapPos(heightMapType, bpos));
        return dimension.getWorldBorder().isWithinBounds(pos.asBlockPos());
    }

    private Vector3 spiralPositions(Vector3 position) {
        Vec3i posi = position.asMinecraftVec3i();
        BlockPos startBlockPos = new BlockPos(posi.getX(), dimension.getSeaLevel(), posi.getZ());

        for (BlockPos pos : BlockPos.spiralAround(startBlockPos, 16, Direction.WEST, Direction.NORTH)) {
            if (isSafe(pos)) {
                // Set the new position
                return new Vector3(pos);
            }
        }

        return position;
    }

    public static List<RTP> slicedByDimension(ServerLevel lvl) {
        List<RTP> slice = new ArrayList<>();

        Iterator<RTP> it = RTPCaches.Locations.iterator();
        while (it.hasNext()) {
            RTP nxt = it.next();
            if (nxt.isDimension(lvl)) {
                slice.add(nxt);
            }
        }

        return slice;
    }
}
