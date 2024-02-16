package dev.zontreck.essentials.items.implementation;

import dev.zontreck.ariaslib.util.TimeUtil;
import dev.zontreck.essentials.configs.NBTKeys;
import dev.zontreck.essentials.configs.server.AEServerConfig;
import dev.zontreck.essentials.items.abstraction.AbstractBottle;
import dev.zontreck.essentials.util.StylesUtil;
import dev.zontreck.libzontreck.util.ChatHelpers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class TimeBottle extends AbstractBottle
{

    public TimeBottle() {
        super();
    }
    @Override
    public boolean isFoil(ItemStack stack) {
        sanityCheck(stack);

        return stack.getTag().contains(NBTKeys.STORED_TIME);
    }

    private void sanityCheck(ItemStack stack) {

        if(!stack.hasTag() || stack.getTag()==null)
        {
            stack.setTag(new CompoundTag());
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(itemStack, world, tooltip, flag);

        int storedTime = this.getStoredEnergy(itemStack);

        int totalAccumulatedTime = this.getTotalAccumulatedTime(itemStack);

        tooltip.add(ChatHelpers.macro(AEServerConfig.getInstance().bottles.storedTimeStr, TimeUtil.secondsToTimeNotation(TimeUtil.ticksToSeconds(storedTime, AEServerConfig.getInstance().bottles.ticks)).toString()));

        tooltip.add(ChatHelpers.macro(AEServerConfig.getInstance().bottles.accumulatedTimeStr, TimeUtil.secondsToTimeNotation(TimeUtil.ticksToSeconds(totalAccumulatedTime, AEServerConfig.getInstance().bottles.ticks)).toString()));

        tooltip.add(ChatHelpers.macro(AEServerConfig.getInstance().bottles.totalUses, "" + TimeUtil.ticksToSeconds(storedTime, AEServerConfig.getInstance().bottles.ticks) / AEServerConfig.getInstance().bottles.eachUseDuration));
    }

    @Override
    public int getStoredEnergy(ItemStack stack) {
        return stack.getOrCreateTag().getInt(NBTKeys.STORED_TIME);
    }

    @Override
    public void setStoredEnergy(ItemStack stack, int energy) {
        int newStoredTime = Math.min(energy, AEServerConfig.getInstance().bottles.maxTime);
        stack.getOrCreateTag().putInt(NBTKeys.STORED_TIME, newStoredTime);
    }

    @Override
    public void applyDamage(ItemStack stack, int damage) {
        setStoredEnergy(stack, getStoredEnergy(stack) - damage);
    }

    public int getTotalAccumulatedTime(ItemStack stack) {
        return stack.getOrCreateTag().getInt(NBTKeys.TOTAL_ACCUMULATED_TIME);
    }

    public void setTotalAccumulatedTime(ItemStack stack, int value) {
        int newValue = Math.min(value, AEServerConfig.getInstance().bottles.maxTime);
        stack.getOrCreateTag().putInt(NBTKeys.TOTAL_ACCUMULATED_TIME, newValue);
    }


    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int itemSlot, boolean isSelected) {
        super.inventoryTick(itemStack, level, entity, itemSlot, isSelected);
        if (level.isClientSide) {
            return;
        }

        if (level.getGameTime() % AEServerConfig.getInstance().bottles.ticks == 0) {
            int storedTime = this.getStoredEnergy(itemStack);
            if (storedTime < AEServerConfig.getInstance().bottles.maxTime) {
                this.setStoredEnergy(itemStack, storedTime + AEServerConfig.getInstance().bottles.ticks);
            }

            int totalAccumulatedTime = this.getTotalAccumulatedTime(itemStack);
            if (totalAccumulatedTime < AEServerConfig.getInstance().bottles.maxTime) {
                this.setTotalAccumulatedTime(itemStack, totalAccumulatedTime + AEServerConfig.getInstance().bottles.ticks);
            }
        }
    }
}
