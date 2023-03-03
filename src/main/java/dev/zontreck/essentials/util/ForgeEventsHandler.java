package dev.zontreck.essentials.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import dev.zontreck.essentials.AriasEssentials;
import dev.zontreck.essentials.Messages;
import dev.zontreck.essentials.commands.teleport.TeleportActioner;
import dev.zontreck.essentials.commands.teleport.TeleportContainer;
import dev.zontreck.essentials.commands.teleport.TeleportDestination;
import dev.zontreck.essentials.events.RTPNotCancelledEvent;
import dev.zontreck.essentials.homes.HomesProvider;
import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.events.ProfileLoadedEvent;
import dev.zontreck.libzontreck.events.ProfileUnloadedEvent;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import net.minecraft.network.chat.TextComponent;
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


    @SubscribeEvent
    public void onRTPNotCancelled(final RTPNotCancelledEvent ev)
    {
        final TeleportContainer contain = ev.container.container;
        
        ChatHelpers.broadcastTo(contain.PlayerInst.getUUID(), ChatHelpers.macro(Messages.WARP_RTP_FOUND), contain.PlayerInst.server);
        TeleportActioner.ApplyTeleportEffect(contain.PlayerInst);
        TeleportActioner.PerformTeleport(contain);
    }
}
