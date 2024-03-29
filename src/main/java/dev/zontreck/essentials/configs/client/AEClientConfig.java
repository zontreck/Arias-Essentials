package dev.zontreck.essentials.configs.client;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.zontreck.essentials.util.EssentialsDatastore;
import dev.zontreck.essentials.util.FileHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;

import java.nio.file.Path;

public class AEClientConfig
{
    public static final String TAG_HEARTS = "enable_hearts";

    private static AEClientConfig inst;
    public boolean EnableHearts = true;



    public static AEClientConfig deserialize(CompoundTag tag)
    {
        AEClientConfig config = new AEClientConfig();
        config.EnableHearts = tag.getBoolean(TAG_HEARTS);

        return config;
    }

    public static void loadFromFile()
    {
        Path serverConfig = EssentialsDatastore.of("client.snbt");
        if(serverConfig.toFile().exists())
        {

            try {
                String snbt = FileHandler.readFile(serverConfig.toFile().getAbsolutePath());

                inst = deserialize(NbtUtils.snbtToStructure(snbt));


            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }else {
            initNewConfig();
        }


    }

    private static void initNewConfig()
    {
        inst = new AEClientConfig();
        inst.reset();
    }

    private void reset()
    {
        EnableHearts = true;



        save();
    }

    public static void save()
    {
        Path serverConfig = EssentialsDatastore.of("client.snbt", false);

        CompoundTag tag = inst.serialize();

        var snbt = NbtUtils.structureToSnbt(tag);

        FileHandler.writeFile(serverConfig.toFile().getAbsolutePath(), snbt);
    }

    public CompoundTag serialize()
    {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(TAG_HEARTS, EnableHearts);



        return tag;
    }

    public static AEClientConfig getInstance()
    {
        return inst;
    }
}
