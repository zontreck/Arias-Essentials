package dev.zontreck.essentials.rtp;

import dev.zontreck.essentials.AriasEssentials;
import dev.zontreck.essentials.Messages;
import dev.zontreck.essentials.commands.teleport.TeleportActioner;
import dev.zontreck.essentials.events.RTPFoundEvent;
import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.util.ChatHelpers;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

public class RTPCachesEventHandlers
{
    public int lastTick;
    public boolean firstRun=true;
    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent event)
    {
        if(!AriasEssentials.ALIVE) return;
        lastTick++;
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if(lastTick>=400)
        {
            lastTick=0;

            if(firstRun)
            {
                try {

                    MinecraftForge.EVENT_BUS.unregister(this);

                    firstRun=false;
                    AriasEssentials.LOGGER.info("Aria's Essentials startup is running. Scanning for initial RTP locations");
                    for(ServerLevel level : server.getAllLevels())
                    {
                        if(AriasEssentials.DEBUG)
                        {
                            AriasEssentials.LOGGER.info("Scanning a level");
                        }
                        if(TeleportActioner.isBlacklistedDimension(level))
                        {
                            continue;
                        }

                        RandomPositionFactory.beginRTPSearch(level);
                    }

                    AriasEssentials.LOGGER.info("Startup done. RTP searching will continue in a separate thread");
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRTPFound(final RTPFoundEvent event)
    {
        RTPCaches.Locations.add(event.rtp);
        ChatHelpers.broadcast(ChatHelpers.macro(Messages.RTP_CACHED, event.rtp.position.Dimension), event.rtp.position.getActualDimension().getServer());
    }
}
