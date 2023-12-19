package dev.zontreck.essentials.rtp;

import dev.zontreck.essentials.Messages;
import dev.zontreck.essentials.commands.teleport.TeleportActioner;
import dev.zontreck.essentials.events.RTPFoundEvent;
import dev.zontreck.libzontreck.util.ChatHelpers;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RTPCachesEventHandlers
{
    public int lastTick;
    public boolean firstRun=true;
    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent event)
    {
        lastTick++;
        if(lastTick>=40)
        {
            lastTick=0;

            if(firstRun)
            {
                try {

                    firstRun=false;
                    for(ServerLevel level : event.getServer().getAllLevels())
                    {
                        if(TeleportActioner.isBlacklistedDimension(level))
                        {
                            continue;
                        }

                        RandomPositionFactory.beginRTPSearch(level); // Populate 10 RTP points
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    @SubscribeEvent
    public void onRTPFound(RTPFoundEvent event)
    {
        RTPCaches.Locations.add(event.rtp);
        ChatHelpers.broadcast(ChatHelpers.macro(Messages.RTP_CACHED, event.rtp.position.Dimension), event.rtp.position.getActualDimension().getServer());
    }
}
