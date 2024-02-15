package dev.zontreck.essentials.items;

import dev.zontreck.essentials.AriasEssentials;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = AriasEssentials.MODID, value = Dist.CLIENT)
public class CreativeModeTabs {
    public static DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AriasEssentials.MODID);

    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }
    public static final List<Supplier<? extends ItemLike>> AE_TAB_ITEMS = new ArrayList<>();
    public static RegistryObject<CreativeModeTab> AE_GAME_TAB = REGISTER.register("ariasessentials", ()->CreativeModeTab.builder()
            .icon(ModItems.TIME_IN_A_BOTTLE.get()::getDefaultInstance)
            .title(Component.translatable("itemGroup.tabs.ariasessentials"))
            .displayItems((display, output) -> AE_TAB_ITEMS.forEach(it->output.accept(it.get())))
            .build());

    public static <T extends Item> RegistryObject<T> addToAETab(RegistryObject<T> item)
    {
        AE_TAB_ITEMS.add(item);
        return item;
    }
}
