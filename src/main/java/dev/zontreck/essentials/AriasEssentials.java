package dev.zontreck.essentials;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import dev.zontreck.ariaslib.util.DelayedExecutorService;
import dev.zontreck.essentials.client.Keybindings;
import dev.zontreck.essentials.commands.teleport.TeleportActioner;
import dev.zontreck.essentials.configs.client.AEClientConfig;
import dev.zontreck.essentials.configs.server.AEServerConfig;
import dev.zontreck.essentials.events.TeleportEvent;
import dev.zontreck.essentials.gui.HeartsRenderer;
import dev.zontreck.essentials.networking.ModMessages;
import dev.zontreck.essentials.rtp.RTPCaches;
import dev.zontreck.essentials.rtp.RTPCachesEventHandlers;
import dev.zontreck.essentials.util.BackPositionCaches;
import dev.zontreck.essentials.util.CommandCooldowns;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.util.ServerUtilities;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
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
    public static boolean DEBUG = true;



    public AriasEssentials()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        DelayedExecutorService.setup();

        AEServerConfig.loadFromFile();
        AEClientConfig.loadFromFile();


        

        EssentialsDatastore.initialize();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new CommandRegister());
        MinecraftForge.EVENT_BUS.register(new ForgeEventsHandler());
        MinecraftForge.EVENT_BUS.register(new RTPCachesEventHandlers());
        MinecraftForge.EVENT_BUS.register(new CommandCooldowns());
        MinecraftForge.EVENT_BUS.register(RTPCachesEventHandlers.class);
    }

    @SubscribeEvent
    public void onTeleport(TeleportEvent event)
    {
        if(TeleportActioner.isBlacklistedDimension(event.getContainer().Dimension))
        {
            event.setCanceled(true);
        } else {
            if(AEServerConfig.getInstance().back.Enabled && AEServerConfig.getInstance().back.EnabledForTp)
                BackPositionCaches.Update(event.getContainer().PlayerInst.getUUID(), event.getContainer().OldPosition);
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
        LOGGER.info("Tearing down Aria's Essentials functions and tasks");
        DelayedExecutorService.stop();

        DelayedExecutorService.getInstance().EXECUTORS.clear();
        RTPCaches.Locations.clear();
    }

    @SubscribeEvent (priority = EventPriority.HIGHEST)
    public void onPlayerDied(final LivingDeathEvent ev)
    {
        if(ev.getEntity() instanceof Player p)
        {
            if(ServerUtilities.isServer())
            {
                ServerPlayer sp = ServerUtilities.getPlayerByID(p.getUUID().toString());
                // Update player back position!
                WorldPosition wp = new WorldPosition(sp);
                BackPositionCaches.Update(sp.getUUID(), wp);

                ChatHelpers.broadcastTo(p, ChatHelpers.macro(Messages.USE_BACK_INTRO), sp.server);
            }
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

    }

}
