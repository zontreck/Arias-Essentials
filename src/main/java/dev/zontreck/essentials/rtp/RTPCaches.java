package dev.zontreck.essentials.rtp;

import dev.zontreck.essentials.events.RTPFoundEvent;
import dev.zontreck.essentials.util.EssentialsDatastore;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RTPCaches
{
    public static List<RTP> Locations = new ArrayList<>();


    public static final Path BASE = EssentialsDatastore.of("rtp.nbt", false);


    @SubscribeEvent
    public static void onRTPFound(RTPFoundEvent found)
    {

    }

    public static void deserialize(CompoundTag tag)
    {
        
    }
}
