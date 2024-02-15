package dev.zontreck.essentials.entities;

import dev.zontreck.essentials.configs.NBTKeys;
import dev.zontreck.essentials.configs.server.AEServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;

public class TimeBoostEntity extends Entity
{
    private static final EntityDataAccessor<Integer> timeRate = SynchedEntityData.defineId(TimeBoostEntity.class, EntityDataSerializers.INT);

    private int remainingTime;
    private BlockPos position;


    public TimeBoostEntity(EntityType entityType, Level worldIn) {
        super(entityType, worldIn);
        entityData.set(timeRate, 1);
    }


    public TimeBoostEntity(Level worldIn, BlockPos pos, double posX, double posY, double posZ) {
        this(ModEntities.TIAB_ENTITY.get(), worldIn);
        this.position = pos;
        this.setPos(posX, posY, posZ);
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(timeRate, 1);
    }



    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        entityData.set(timeRate, compound.getInt(NBTKeys.ENTITY_TIME_RATE));
        setRemainingTime(compound.getInt(NBTKeys.ENTITY_REMAINING_TIME));
        this.position = NbtUtils.readBlockPos(compound.getCompound(NBTKeys.ENTITY_POS));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt(NBTKeys.ENTITY_TIME_RATE, getTimeRate());
        compound.putInt(NBTKeys.ENTITY_REMAINING_TIME, getRemainingTime());
        compound.put(NBTKeys.ENTITY_POS, NbtUtils.writeBlockPos(this.position));
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public int getTimeRate() {
        return entityData.get(timeRate);
    }

    public void setTimeRate(int rate) {
        entityData.set(timeRate, rate);
    }

    public int getRemainingTime() {
        return this.remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    @Override
    public void tick() {
        super.tick();
        Level level = level();

        if(position == null)
        {
            if(!level.isClientSide)
            {
                remove(RemovalReason.KILLED);
            }
            return;
        }

        BlockState state = level.getBlockState(position);
        ServerLevel serverWorld = level.getServer().getLevel(level.dimension());
        BlockEntity targetTE = level.getBlockEntity(position);


        for (int i = 0; i < getTimeRate(); i++) {
            if (targetTE != null) {
                // if is TileEntity (furnace, brewing stand, ...)
                BlockEntityTicker<BlockEntity> ticker = targetTE.getBlockState().getTicker(level, (BlockEntityType<BlockEntity>) targetTE.getType());
                if (ticker != null) {
                    ticker.tick(level, position, targetTE.getBlockState(), targetTE);
                }
            } else if (serverWorld != null && state.isRandomlyTicking()) {
                // if is random ticket block (grass block, sugar cane, wheat or sapling, ...)
                if (level.random.nextInt(AEServerConfig.getInstance().bottles.avgRandomTicks) == 0) {
                    state.randomTick(serverWorld, position, level.random);
                }
            } else {
                // block entity broken
                this.remove(RemovalReason.KILLED);
                break;
            }
        }

        this.remainingTime -= 1;
        if (this.remainingTime <= 0 && !level.isClientSide) {
            this.remove(RemovalReason.KILLED);
        }
    }
}
