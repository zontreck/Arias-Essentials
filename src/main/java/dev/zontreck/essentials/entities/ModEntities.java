package dev.zontreck.essentials.entities;

import dev.zontreck.essentials.AriasEssentials;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities
{
    public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, AriasEssentials.MODID);

    public static RegistryObject<EntityType<TimeBoostEntity>> TIAB_ENTITY = REGISTER.register("tiab_entity_type", ()->EntityType.Builder.<TimeBoostEntity>of(TimeBoostEntity::new, MobCategory.MISC)
            .sized(0.1f, 0.1f)
            .build(new ResourceLocation(AriasEssentials.MODID, "tiab_entity_type").toString())
    );


    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }
}
