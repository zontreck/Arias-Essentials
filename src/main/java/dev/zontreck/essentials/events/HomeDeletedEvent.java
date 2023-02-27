package dev.zontreck.essentials.events;

import dev.zontreck.essentials.homes.Home;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * This event cannot be cancelled.
 */
public class HomeDeletedEvent extends Event
{

    public Home home;
    public HomeDeletedEvent(Home home)
    {
        this.home=home;
    }
    
}
