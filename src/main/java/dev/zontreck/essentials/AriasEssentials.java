package dev.zontreck.essentials;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

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

    public AriasEssentials()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);
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
