package dev.zontreck.essentials.configs.server.sections;

import net.minecraft.nbt.CompoundTag;

public class Costs
{
    public static final String TAG_NAME = "costs";
    public static final String TAG_COST_TP_HOME = "tp_home";
    public static final String TAG_COST_WARP = "warp";
    public static final String TAG_COST_MAKE_WARP = "setwarp";
    public static final String TAG_COST_MAKE_HOME = "sethome";


    public String CostToTPHome = "1i"; // 1 iron coin
    public String CostToWarp = "5i"; // 5 iron coins
    public String CostToMakeWarp = "5e"; // 5 emerald coin
    public String CostToSetHome = "1d"; // 1 diamond coin


    public static Costs deserialize(CompoundTag tag)
    {
        Costs costs = new Costs();
        costs.CostToTPHome = tag.getString(TAG_COST_TP_HOME);
        costs.CostToWarp = tag.getString(TAG_COST_WARP);
        costs.CostToMakeWarp = tag.getString(TAG_COST_MAKE_WARP);
        costs.CostToSetHome = tag.getString(TAG_COST_MAKE_HOME);


        return costs;
    }

    public CompoundTag serialize()
    {
        CompoundTag tag = new CompoundTag();
        tag.putString(TAG_COST_TP_HOME, CostToTPHome);
        tag.putString(TAG_COST_WARP, CostToWarp);
        tag.putString(TAG_COST_MAKE_WARP, CostToMakeWarp);
        tag.putString(TAG_COST_MAKE_HOME, CostToSetHome);

        return tag;
    }
}
