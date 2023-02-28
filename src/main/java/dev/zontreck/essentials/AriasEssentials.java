package dev.zontreck.essentials;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import dev.zontreck.essentials.commands.CommandRegister;
import dev.zontreck.essentials.homes.Homes;
import dev.zontreck.essentials.homes.HomesProvider;
import dev.zontreck.essentials.util.EssentialsDatastore;
import dev.zontreck.libzontreck.events.ProfileLoadedEvent;
import dev.zontreck.libzontreck.events.ProfileUnloadedEvent;
import dev.zontreck.libzontreck.profiles.Profile;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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

        EssentialsDatastore.initialize();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new CommandRegister());
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
