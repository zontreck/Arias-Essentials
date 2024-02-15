package dev.zontreck.essentials.configs.server.sections;

import net.minecraft.nbt.CompoundTag;

public class Bottles {
    public static final String TAG_NAME = "bottles";
    public static final String TAG_DURATION = "durationEachUse";
    public static final String TAG_TICKS = "ticks";
    public static final String TAG_RANDOM_TICKS = "avgRandomTicks";
    public static final String TAG_MAX_TIME_RATE = "maxTimeRate";
    public static final String TAG_STORED_TIME = "maxTime";

    public static final String TAG_MESSAGE_STORED_TIME = "msg_storedTime";
    public static final String TAG_MESSAGE_ACCUMULATED_TIME = "msg_accumulatedTime";




    public int eachUseDuration = 30;
    public int ticks = 20;
    public int avgRandomTicks = 512;
    public int maxTimeRate = 8;
    public int maxTime = (60 * 60 * 24 * 30); // 30 days is the default
    public String storedTimeStr = "!Dark_Green!Stored Time: [0]:[1]:[2]";
    public String accumulatedTimeStr = "!Gray!Total Accumulated Time: [0]:[1]:[2]";



    public CompoundTag serialize()
    {
        CompoundTag tag = new CompoundTag();
        tag.putInt(TAG_DURATION, eachUseDuration);
        tag.putInt(TAG_TICKS, ticks);
        tag.putInt(TAG_RANDOM_TICKS, avgRandomTicks);
        tag.putInt(TAG_MAX_TIME_RATE, maxTimeRate);
        tag.putString(TAG_MESSAGE_STORED_TIME, storedTimeStr);
        tag.putString(TAG_MESSAGE_ACCUMULATED_TIME, accumulatedTimeStr);
        tag.putInt(TAG_STORED_TIME, maxTime);


        return tag;
    }

    public static Bottles deserialize(CompoundTag tag)
    {
        Bottles bottles = new Bottles();
        if(tag.contains(TAG_DURATION))
        {
            bottles.eachUseDuration = tag.getInt(TAG_DURATION);
        }

        if(tag.contains(TAG_TICKS))
            bottles.ticks = tag.getInt(TAG_TICKS);

        if(tag.contains(TAG_RANDOM_TICKS))
            bottles.avgRandomTicks = tag.getInt(TAG_RANDOM_TICKS);

        if(tag.contains(TAG_MAX_TIME_RATE))
            bottles.maxTimeRate = tag.getInt(TAG_MAX_TIME_RATE);

        if(tag.contains(TAG_MESSAGE_STORED_TIME))
            bottles.storedTimeStr = tag.getString(TAG_MESSAGE_STORED_TIME);

        if(tag.contains(TAG_MESSAGE_ACCUMULATED_TIME))
            bottles.accumulatedTimeStr = tag.getString(TAG_MESSAGE_ACCUMULATED_TIME);

        if(tag.contains(TAG_STORED_TIME))
            bottles.maxTime = tag.getInt(TAG_STORED_TIME);




        return bottles;
    }

}
