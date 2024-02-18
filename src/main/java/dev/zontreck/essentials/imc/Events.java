package dev.zontreck.essentials.imc;

import dev.zontreck.essentials.commands.teleport.TeleportActioner;
import dev.zontreck.essentials.commands.teleport.TeleportContainer;
import dev.zontreck.libzontreck.events.TeleportEvent;
import net.minecraft.world.phys.Vec2;

public class Events
{
    public static void onTeleportRequest(TeleportEvent ev)
    {
        ev.setCanceled(true);

        TeleportActioner.ApplyTeleportEffect(ev.getPlayer());

        TeleportContainer container = new TeleportContainer(ev.getPlayer(), ev.getPosition().Position.asMinecraftVector(), Vec2.ZERO, ev.getPosition().getActualDimension());

        TeleportActioner.PerformTeleport(container, false);

    }
}
