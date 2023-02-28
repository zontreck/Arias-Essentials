package dev.zontreck.essentials.homes;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

import dev.zontreck.essentials.util.EssentialsDatastore;
import net.minecraft.nbt.NbtIo;

public class HomesProvider {
    /**
     * DO NOT USE. Internal use only.
     * @see Profile#player_homes
     * @param player
     * @return
     */
    public static Homes getHomesForPlayer(String player)
    {
        Path homesFile = EssentialsDatastore.of(player,true).resolve("homes.nbt");

        Homes homes = new Homes(player);
        if(homesFile.toFile().exists())
        {
            try {
                homes=Homes.deserialize(NbtIo.read(homesFile.toFile()));
                homes.playerID=player;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return homes;
    }

    public static void commitHomes(Homes playerHomes)
    {

        Path homesFile = EssentialsDatastore.of(playerHomes.playerID,true).resolve("homes.nbt");

        try {
            NbtIo.write(playerHomes.serialize(), homesFile.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
