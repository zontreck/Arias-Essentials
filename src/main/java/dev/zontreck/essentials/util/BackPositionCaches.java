package dev.zontreck.essentials.util;


import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.libzontreck.profiles.UserProfileNotYetExistsException;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BackPositionCaches
{
    public static void Update(UUID ID, WorldPosition pos)
    {
        try {
            Profile prof = Profile.get_profile_of(ID.toString());
            if(prof.NBT.contains("back_positions"))
            {

                ListTag backCaches = prof.NBT.getList("back_positions", CompoundTag.TAG_COMPOUND);
                backCaches.add(pos.serialize());

                while(backCaches.size()>10)
                {
                    backCaches.remove(backCaches.size()-1);
                }

                prof.commit();
            } else {
                ListTag backCaches = new ListTag();
                backCaches.add(pos.serialize());

                prof.NBT.put("back_positions", backCaches);

                prof.commit();
            }
        } catch (UserProfileNotYetExistsException e) {
            throw new RuntimeException(e);
        }
    }

    public static WorldPosition Pop(UUID ID) throws Exception {
        Profile prof = Profile.get_profile_of(ID.toString());
        if(prof.NBT.contains("back_positions"))
        {
            ListTag lst = prof.NBT.getList("back_positions", Tag.TAG_COMPOUND);
            if(lst.size()>0)
            {
                WorldPosition pos = new WorldPosition(lst.getCompound(0), false);
                lst.remove(0);

                prof.commit();
                return pos;
            }else {
                throw new Exception("No back cache");
            }
        } else throw new Exception("No back cache");
    }

}
