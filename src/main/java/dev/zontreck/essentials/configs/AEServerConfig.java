package dev.zontreck.essentials.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class AEServerConfig {
    // TODO: 
    /*
     * 1. Waystone support, Issue #1
     * 2. Economy support for server owners to charge a fee to use warps and / or homes
     * 
     * 
     */


    public static final ForgeConfigSpec.Builder BUILDER= new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> COST_TO_TP_HOME;
    public static final ForgeConfigSpec.ConfigValue<Integer> COST_TO_WARP;
    public static final ForgeConfigSpec.ConfigValue<Integer> COST_TO_MAKE_HOME;
    public static final ForgeConfigSpec.ConfigValue<Integer> COST_TO_MAKE_WARP;

    public static final ForgeConfigSpec.ConfigValue<Integer> MAX_HOMES;



    static{

        BUILDER.push("prices").comment("Costs are calculated as follows: A copper coin is 1, while a iron coin is 100. Thus a gold coin is 10000");
        COST_TO_TP_HOME = BUILDER.comment("The cost to teleport home.").define("home_tp_cost", 100);
        COST_TO_WARP = BUILDER.comment("The cost to warp.").define("warp_tp_cost", 1000);
        COST_TO_MAKE_HOME = BUILDER.comment("The cost to set a new home").define("make_home_cost", 1);
        COST_TO_MAKE_WARP = BUILDER.comment("The cost to make a new warp. (This applies to all non-creative players) (Default: 1 Emerald Coin)").define("make_warp_cost", 1000000);
        BUILDER.pop();
        BUILDER.push("permissions").comment("This section defines permissions, such as total number of homes, and who can make warps");

        MAX_HOMES = BUILDER.comment("Maximum number of homes that are allowed per player (-1 disables the limit entirely)").define("max_homes", -1);
        



        BUILDER.pop();
        SPEC=BUILDER.build();
    }
    
}
