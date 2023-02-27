package dev.zontreck.essentials.commands.teleport;

import java.util.ArrayList;
import java.util.List;

public class TeleportRegistry {
    private static final List<TeleportContainer> lst;

    static{
        lst=new ArrayList<>();
    }


    public static List<TeleportContainer> get()
    {
        return lst;
    }
}
