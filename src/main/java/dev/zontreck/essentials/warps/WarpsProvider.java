package dev.zontreck.essentials.warps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import dev.zontreck.essentials.util.EssentialsDatastore;
import net.minecraft.nbt.NbtIo;

public class WarpsProvider extends EssentialsDatastore
{
    public static final Path BASE = of("warps.nbt", false);

    public static final Warps WARPS_INSTANCE;
    static{
        WARPS_INSTANCE = getOrCreate();
    }

    /**
     * Creates a new warps instance, or returns a fully deserialized instance
     * @return
     */
    private static Warps getOrCreate()
    {
        Warps instance = null;
        if(BASE.toFile().exists())
        {
            try{
                instance= Warps.deserialize(NbtIo.read(BASE.toFile()));

            }catch(Exception e){
                instance=Warps.getNew();
            }
        }else {
            instance=Warps.getNew();
        }

        return instance;
    }

    public static void updateFile()
    {
        try {
            NbtIo.write(WARPS_INSTANCE.serialize(), BASE.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
