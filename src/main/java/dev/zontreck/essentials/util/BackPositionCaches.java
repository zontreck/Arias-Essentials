package dev.zontreck.essentials.util;


import dev.zontreck.libzontreck.vectors.WorldPosition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BackPositionCaches
{
    private static final Map<UUID, WorldPosition> backCaches = new HashMap<>();
    public static void Update(UUID ID, WorldPosition pos)
    {
        backCaches.put(ID, pos);
    }

    public static WorldPosition Pop(UUID ID) throws Exception {
        if(backCaches.containsKey(ID)) {
            WorldPosition pos = backCaches.get(ID);
            backCaches.remove(ID);
            return pos;
        }else throw new Exception("No such back cache");
    }

}
