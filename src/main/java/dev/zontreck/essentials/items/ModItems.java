package dev.zontreck.essentials.items;

import dev.zontreck.essentials.AriasEssentials;
import dev.zontreck.essentials.items.implementation.TimeBottle;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems
{
    public static DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, AriasEssentials.MODID);

    public static void register(IEventBus bus) {
        REGISTRY.register(bus);
    }

    public static RegistryObject<Item> TIME_IN_A_BOTTLE = CreativeModeTabs.addToAETab(REGISTRY.register("tiab", ()->new TimeBottle()));
}
