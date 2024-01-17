package dev.zontreck.essentials.configs.server;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.zontreck.ariaslib.util.Lists;
import dev.zontreck.essentials.configs.server.sections.*;
import dev.zontreck.essentials.util.EssentialsDatastore;
import dev.zontreck.essentials.util.FileHandler;
import dev.zontreck.essentials.util.Maps;
import net.minecraft.nbt.*;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AEServerConfig
{
    private static AEServerConfig inst;
    public Costs costs;
    public Limitations limits;
    public Map<String, Cooldown> cooldowns;
    public Back back;
    public Teleportation teleport;
    public Messages messages;



    public static AEServerConfig deserialize(CompoundTag tag)
    {
        AEServerConfig config = new AEServerConfig();
        config.costs = Costs.deserialize(tag.getCompound(Costs.TAG_NAME));
        config.limits = Limitations.deserialize(tag.getCompound(Limitations.TAG_NAME));
        config.cooldowns = new HashMap<>();
        ListTag cools = tag.getList(Cooldown.TAG_NAME, ListTag.TAG_COMPOUND);
        for(Tag cooldown : cools)
        {
            Cooldown cd = Cooldown.deserialize((CompoundTag) cooldown);
            config.cooldowns.put(cd.Command, cd);
        }
        config.back = Back.deserialize(tag.getCompound(Back.TAG_NAME));
        config.teleport = Teleportation.deserialize(tag.getCompound(Teleportation.TAG_NAME));
        config.messages = Messages.deserialize(tag.getCompound(Messages.TAG_NAME));


        return config;
    }

    public static void loadFromFile()
    {
        Path serverConfig = EssentialsDatastore.of("server.snbt");
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
        inst = new AEServerConfig();
        inst.reset();
    }

    private void reset()
    {
        costs = new Costs();
        limits = new Limitations();
        cooldowns = Maps.builder("", new Cooldown())
                .with("rtp", new Cooldown("rtp", 30L))
                .with("warp", new Cooldown("warp", 10L))
                .with("home", new Cooldown("home", 5L))
                .with("tpaccept", new Cooldown("tpaccept", 5L))
                .build();
        back = new Back();
        teleport = new Teleportation();
        teleport.Effects = Lists.of(
                "minecraft:blindness",
                "minecraft:levitation",
                "minecraft:slow_falling",
                "minecraft:hunger"
        );
        teleport.Blacklist = Lists.of(
                "dimdoors:dungeon_pockets",
                "dimdoors:limbo",
                "dimdoors:personal_pockets",
                "dimdoors:public_pockets",
                "witherstormmod:bowels"
        );
        messages = new Messages();



        save();
    }

    public static void save()
    {
        Path serverConfig = EssentialsDatastore.of("server.snbt", false);

        CompoundTag tag = inst.serialize();

        var snbt = NbtUtils.structureToSnbt(tag);

        FileHandler.writeFile(serverConfig.toFile().getAbsolutePath(), snbt);
    }

    public CompoundTag serialize()
    {
        CompoundTag tag = new CompoundTag();
        tag.put(Costs.TAG_NAME, costs.serialize());
        tag.put(Limitations.TAG_NAME, limits.serialize());
        ListTag cools = new ListTag();
        for(Map.Entry<String,Cooldown> entries : cooldowns.entrySet())
        {
            cools.add(entries.getValue().serialize());
        }
        tag.put(Cooldown.TAG_NAME, cools);
        tag.put(Back.TAG_NAME, back.serialize());
        tag.put(Teleportation.TAG_NAME, teleport.serialize());
        tag.put(Messages.TAG_NAME, messages.serialize());



        return tag;
    }

    public static AEServerConfig getInstance()
    {
        return inst;
    }
}
