package dev.zontreck.essentials.events;

import dev.zontreck.essentials.commands.teleport.TeleportContainer;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class TeleportEvent extends Event
{
    private TeleportContainer container;
    public TeleportEvent(TeleportContainer container)
    {
        this.container=container;
    }

    public TeleportContainer getContainer() {
        return container;
    }
}
