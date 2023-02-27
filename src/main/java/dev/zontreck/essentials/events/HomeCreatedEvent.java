package dev.zontreck.essentials.events;

import dev.zontreck.essentials.homes.Home;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * This event may be cancelled if the home is in a invalid location, like in the middle of a claim they have no rights to
 */
@Cancelable
public class HomeCreatedEvent extends Event
{
    public Home home;
    public HomeCreatedEvent(Home home)
    {
        this.home=home;
    }
}
