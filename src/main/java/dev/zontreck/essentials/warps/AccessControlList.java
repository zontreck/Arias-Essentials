package dev.zontreck.essentials.warps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;

public class AccessControlList {
    
    /**
     * Warp ACLs do not need privilege level. It is simply a question of yes or no to if someone has access to the warp.
     */
    public class ACLEntry
    {
        public String name;
        public UUID id;

        public ACLEntry(String name, UUID id)
        {
            this.name=name;
            this.id=id;
        }

        public CompoundTag serialize()
        {
            CompoundTag tag = new CompoundTag();
            tag.putString("name", name);
            tag.put("id", NbtUtils.createUUID(id));

            return tag;
        }

        public ACLEntry(CompoundTag tag)
        {
            name=tag.getString("name");
            id=NbtUtils.loadUUID(tag.get("id"));
        }
    }

    public List<ACLEntry> entries = new ArrayList<>();

    public AccessControlList()
    {

    }

    public List<String> getNames()
    {
        List<String> names = new ArrayList<>();
        for (ACLEntry entry : entries) {
            names.add(entry.name);
        }

        return names;
    }

    public List<UUID> getIDs()
    {
        List<UUID> ids = new ArrayList<>();
        for (ACLEntry entry : entries) {
            ids.add(entry.id);
        }

        return ids;
    }

    /**
     * Adds a ACL Entry for a name and ID
     * @param name
     * @param id
     * @return null if the entry was already in the ACL
     * @return Entry if the entry was successfully added to the ACL
     */
    public ACLEntry addEntry(String name, UUID id)
    {
        ACLEntry entry = new ACLEntry(name, id);
        if(getIDs().contains(id)) return null;
        entries.add(entry);

        return entry;
    }

    /**
     * Removes a ACLEntry by UUID
     * @param ID
     * @return null if no such entry
     * @return Entry that was removed from the list
     */
    public ACLEntry removeByID(UUID ID)
    {
        Iterator<ACLEntry> entr = entries.iterator();
        while(entr.hasNext())
        {
            ACLEntry entry = entr.next();
            if(entry.id==ID)
            {
                entr.remove();
                return entry;
            }
        }

        return null;
    }

    /**
     * Saves the current instance as a NBT Tag
     * @return
     */
    public CompoundTag serialize()
    {
        CompoundTag tag = new CompoundTag();
        ListTag lst = new ListTag();
        for (ACLEntry aclEntry : entries) {
            lst.add(aclEntry.serialize());
        }
        tag.put("entries", lst);

        return tag;
    }


    /**
     * Reads a NBT Tag back into an AccessControlList
     * @param tag
     * @return
     */
    public static AccessControlList deserialize(CompoundTag tag)
    {
        AccessControlList acl = new AccessControlList();
        ListTag lst = tag.getList("entries", Tag.TAG_COMPOUND);
        for (Tag tag2 : lst) {
            CompoundTag entry = (CompoundTag)tag2;
            ACLEntry entryItem = acl.deserializeEntry(entry);
            acl.entries.add(entryItem);
        }

        return acl;
    }

    /**
     * Deserializes an entry from the subclass
     * @see ACLEntry
     * @param tag
     * @return
     */
    private ACLEntry deserializeEntry(CompoundTag tag)
    {
        return new ACLEntry(tag);
    }
}
