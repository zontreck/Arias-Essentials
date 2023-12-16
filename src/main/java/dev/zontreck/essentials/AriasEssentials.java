package dev.zontreck.essentials;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import dev.zontreck.ariaslib.util.DelayedExecutorService;
import dev.zontreck.essentials.configs.AEClientConfig;
import dev.zontreck.essentials.configs.AEServerConfig;
import dev.zontreck.essentials.gui.HeartsRenderer;
import dev.zontreck.essentials.networking.S2CUpdateHearts;
import dev.zontreck.libzontreck.events.RegisterPacketsEvent;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import dev.zontreck.essentials.commands.CommandRegister;
import dev.zontreck.essentials.homes.Homes;
import dev.zontreck.essentials.homes.HomesProvider;
import dev.zontreck.essentials.util.EssentialsDatastore;
import dev.zontreck.essentials.util.ForgeEventsHandler;
import dev.zontreck.libzontreck.events.ProfileLoadedEvent;
import dev.zontreck.libzontreck.events.ProfileUnloadedEvent;
import dev.zontreck.libzontreck.profiles.Profile;
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
        MinecraftForge.EVENT_BUS.register(new HeartsRenderer());
        MinecraftForge.EVENT_BUS.register(new CommandRegister());
        MinecraftForge.EVENT_BUS.register(new ForgeEventsHandler());
    }

    public void setup(FMLCommonSetupEvent ev)
    {

    }

    @SubscribeEvent
    public void onRegisterPackets(RegisterPacketsEvent ev){
        ev.packets.add(new S2CUpdateHearts());
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


}
