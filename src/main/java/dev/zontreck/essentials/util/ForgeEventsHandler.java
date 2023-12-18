package dev.zontreck.essentials.util;

import java.util.UUID;

import dev.zontreck.essentials.AriasEssentials;
import dev.zontreck.essentials.homes.HomesProvider;
import dev.zontreck.libzontreck.events.ProfileLoadedEvent;
import dev.zontreck.libzontreck.events.ProfileUnloadedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AriasEssentials.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventsHandler {
    
    @SubscribeEvent
    public void onProfileLoaded(final ProfileLoadedEvent ev)
    {
        //Path playerStore = EssentialsDatastore.of(ev.profile.user_id,true);

        AriasEssentials.player_homes.put(UUID.fromString(ev.profile.user_id), HomesProvider.getHomesForPlayer(ev.profile.user_id));
        AriasEssentials.LOGGER.info("Homes loaded");

        

    }

    @SubscribeEvent
    public void onProfileUnloaded(final ProfileUnloadedEvent ev)
    {
        AriasEssentials.player_homes.remove(UUID.fromString(ev.user_id));
        AriasEssentials.LOGGER.info("Homes unloaded");
    }
}
