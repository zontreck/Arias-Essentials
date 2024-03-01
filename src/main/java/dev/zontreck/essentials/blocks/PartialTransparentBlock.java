package dev.zontreck.essentials.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.state.BlockState;

public class PartialTransparentBlock extends AbstractGlassBlock
{
    protected PartialTransparentBlock(Properties p_48729_) {
        super(p_48729_);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_48740_, BlockGetter p_48741_, BlockPos p_48742_) {
        return true;
    }
}
