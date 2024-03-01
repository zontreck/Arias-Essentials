package dev.zontreck.essentials.items.abstraction;

import dev.zontreck.essentials.configs.server.AEServerConfig;
import dev.zontreck.essentials.entities.TimeBoostEntity;
import dev.zontreck.essentials.util.SoundUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.Optional;

public abstract class AbstractBottle extends Item {
    private static final String[] NOTES = {"C", "D", "E", "F", "G2", "A2", "B2", "C2", "D2", "E2", "F2"};

    public AbstractBottle() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        if (level.isClientSide) {
            return InteractionResult.PASS;
        }

        BlockPos pos = context.getClickedPos();
        BlockState blockState = level.getBlockState(pos);
        BlockEntity targetTE = level.getBlockEntity(pos);
        ItemStack stack = context.getItemInHand();
        Player player = context.getPlayer();

        if (targetTE == null && !blockState.isRandomlyTicking()) {
            return InteractionResult.FAIL;
        }

        int nextRate = 1;
        int energyRequired = getEnergyCost(nextRate);
        boolean isCreativeMode = player != null && player.isCreative();

        Optional<TimeBoostEntity> o = level.getEntitiesOfClass(TimeBoostEntity.class, new AABB(pos)).stream().findFirst();

        if (o.isPresent()) {
            TimeBoostEntity entityTA = o.get();
            int currentRate = entityTA.getTimeRate();

            if (currentRate >= Math.pow(2, AEServerConfig.getInstance().bottles.maxTimeRate - 1)) {
                return InteractionResult.SUCCESS;
            }

            nextRate = currentRate * 2;
            energyRequired = getEnergyCost(nextRate);

            if (!canUse(stack, isCreativeMode, energyRequired)) {
                return InteractionResult.SUCCESS;
            }

            entityTA.setTimeRate(nextRate);
            entityTA.setRemainingTime(entityTA.getRemainingTime() + AEServerConfig.getInstance().bottles.eachUseDuration);
        } else {
            // First use
            if (!canUse(stack, isCreativeMode, energyRequired)) {
                return InteractionResult.SUCCESS;
            }

            TimeBoostEntity entityTA = new TimeBoostEntity(level, pos, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            entityTA.setRemainingTime(getEachUseDuration());
            level.addFreshEntity(entityTA);
        }

        if (!isCreativeMode) {
            this.applyDamage(stack, energyRequired);
        }
        playSound(level, pos, nextRate);

        return InteractionResult.SUCCESS;
    }

    protected int getEachUseDuration() {
        return AEServerConfig.getInstance().bottles.ticks * AEServerConfig.getInstance().bottles.eachUseDuration;
    }

    public int getEnergyCost(int timeRate) {
        if (timeRate <= 1) return getEachUseDuration();
        return timeRate / 2 * getEachUseDuration();
    }

    public boolean canUse(ItemStack stack, boolean isCreativeMode, int energyRequired) {
        return getStoredEnergy(stack) >= energyRequired || isCreativeMode;
    }

    protected abstract int getStoredEnergy(ItemStack stack);

    protected abstract void setStoredEnergy(ItemStack stack, int energy);

    protected abstract void applyDamage(ItemStack stack, int damage);

    public void playSound(Level level, BlockPos pos, int nextRate) {
        switch (nextRate) {
            case 1:
                SoundUtilities.playNoteBlockHarpSound(level, pos, NOTES[0]);
                break;
            case 2:
                SoundUtilities.playNoteBlockHarpSound(level, pos, NOTES[1]);
                break;
            case 4:
                SoundUtilities.playNoteBlockHarpSound(level, pos, NOTES[2]);
                break;
            case 8:
                SoundUtilities.playNoteBlockHarpSound(level, pos, NOTES[3]);
                break;
            case 16:
                SoundUtilities.playNoteBlockHarpSound(level, pos, NOTES[4]);
                break;
            case 32:
                SoundUtilities.playNoteBlockHarpSound(level, pos, NOTES[5]);
                break;
            case 64:
                SoundUtilities.playNoteBlockHarpSound(level, pos, NOTES[6]);
                break;
            case 128:
                SoundUtilities.playNoteBlockHarpSound(level, pos, NOTES[7]);
                break;
            case 256:
                SoundUtilities.playNoteBlockHarpSound(level, pos, NOTES[8]);
                break;
            case 512:
                SoundUtilities.playNoteBlockHarpSound(level, pos, NOTES[9]);
                break;
            default:
                SoundUtilities.playNoteBlockHarpSound(level, pos, NOTES[10]);
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !ItemStack.isSameItem(oldStack, newStack);
    }
}