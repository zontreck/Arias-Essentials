package dev.zontreck.essentials;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import dev.zontreck.ariaslib.util.DelayedExecutorService;
import dev.zontreck.essentials.client.Keybindings;
import dev.zontreck.essentials.commands.teleport.TeleportActioner;
import dev.zontreck.essentials.configs.AEClientConfig;
import dev.zontreck.essentials.configs.AEServerConfig;
import dev.zontreck.essentials.events.TeleportEvent;
import dev.zontreck.essentials.gui.HeartsRenderer;
import dev.zontreck.essentials.networking.ModMessages;
import dev.zontreck.essentials.rtp.RTPCachesEventHandlers;
import dev.zontreck.essentials.util.BackPositionCaches;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import dev.zontreck.essentials.commands.CommandRegister;
import dev.zontreck.essentials.homes.Homes;
import dev.zontreck.essentials.util.EssentialsDatastore;
import dev.zontreck.essentials.util.ForgeEventsHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AriasEssentials.MODID)
public class AriasEssentials {
    public static final String MODID = "ariasessentials";
    public static final Random random = new Random(Instant.now().getEpochSecond());
    public static final Logger LOGGER = LogUtils.getLogger();
    public static boolean ALIVE;
    public static Map<UUID, Homes> player_homes = new HashMap<>();

    public AriasEssentials()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        DelayedExecutorService.setup();


        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, AEServerConfig.SPEC, "arias-essentials-server.toml");


        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, AEClientConfig.SPEC, "arias-essentials-client.toml");


        

        EssentialsDatastore.initialize();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new CommandRegister());
        MinecraftForge.EVENT_BUS.register(new ForgeEventsHandler());
        MinecraftForge.EVENT_BUS.register(new RTPCachesEventHandlers());
    }

    @SubscribeEvent
    public void onTeleport(TeleportEvent event)
    {
        if(TeleportActioner.isBlacklistedDimension(event.getContainer().Dimension))
        {
            event.setCanceled(true);
        }
    }

    public void setup(FMLCommonSetupEvent ev)
    {
        ModMessages.register();
    }


    @SubscribeEvent
    public void onServerStart(final ServerStartedEvent ev)
    {
        ALIVE=true;


    }


    @SubscribeEvent
    public void onServerStop(final ServerStoppingEvent ev)
    {
        ALIVE=false;
    }

    @SubscribeEvent (priority = EventPriority.HIGHEST)
    public void onPlayerDied(final LivingDeathEvent ev)
    {
        if(ev.getEntity() instanceof ServerPlayer sp)
        {
            // Update player back position!
            WorldPosition wp = new WorldPosition(sp);
            BackPositionCaches.Update(sp.getUUID(), wp);
        }
    }


    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = AriasEssentials.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

            LOGGER.info("Client setup");

            MinecraftForge.EVENT_BUS.register(new HeartsRenderer());
        }

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        public static void onRegisterKeyBinds(RegisterKeyMappingsEvent ev)
        {
            ev.register(Keybindings.AUTOWALK);
        }

    }

}
