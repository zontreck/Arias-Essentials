package dev.zontreck.essentials.configs;

import dev.zontreck.ariaslib.util.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

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
    public static final ForgeConfigSpec.ConfigValue<Integer> MAX_WARPS;

    public static final ForgeConfigSpec.ConfigValue<List<String>> DIMENSION_BLACKLIST;
    public static final ForgeConfigSpec.ConfigValue<String> BLACKLISTED_DIMENSION_ERROR;

    public static final ForgeConfigSpec.ConfigValue<Boolean> BACK_ALLOWS_LAST_TP;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_BACK;

    public static final ForgeConfigSpec.ConfigValue<List<String>> TELEPORT_EFFECTS;


    static{

        BUILDER.push("prices").comment("Costs are calculated as follows: A copper coin is 1, while a iron coin is 100. Thus a gold coin is 10000");
        COST_TO_TP_HOME = BUILDER.comment("The cost to teleport home.").define("home_tp_cost", 100);
        COST_TO_WARP = BUILDER.comment("The cost to warp.").define("warp_tp_cost", 1000);
        COST_TO_MAKE_HOME = BUILDER.comment("The cost to set a new home").define("make_home_cost", 1);
        COST_TO_MAKE_WARP = BUILDER.comment("The cost to make a new warp. (This applies to all non-creative players) (Default: 1 Emerald Coin)").define("make_warp_cost", 1000000);
        BUILDER.pop();
        BUILDER.push("permissions").comment("This section defines permissions, such as total number of homes, and who can make warps");

        MAX_HOMES = BUILDER.comment("Maximum number of homes that are allowed per player (-1 disables the limit entirely). 27 is the current default as that is the max that can fit in the current /homes gui").define("max_homes", 27);
        MAX_WARPS = BUILDER.comment("Maximum number of warps that are allowed to exist. Default is 27, the max number that can fit in the /warps gui").define("max_warps", 27);

        BUILDER.pop();
        BUILDER.push("teleport");
        BUILDER.push("blacklist");

        DIMENSION_BLACKLIST = BUILDER.comment("Blacklist the use of teleportation commands from any of the listed dimensions.").define("blacklist", Lists.of("dimdoors:dungeon_pockets", "dimdoors:limbo", "dimdoors:personal_pockets", "dimdoors:public_pockets", "witherstormmod:bowels"));

        BLACKLISTED_DIMENSION_ERROR = BUILDER.comment("The error to say if the user tries to teleport out of a blacklisted dimension").define("error", "!Dark_Red!You appear to be in a place where we cannot find you.");

        BUILDER.pop();

        TELEPORT_EFFECTS = BUILDER.comment("Teleportation effects that get applied when the user attempts a teleport. Teleport effects will always have a random duration between 5 seconds and 15 seconds. The amplifier will also be random, between Lv1 and Lv4").define("effects", Lists.of(
                "minecraft:darkness",
                "minecraft:levitation",
                "minecraft:slow_falling",
                "minecraft:hunger"

        ));

        BUILDER.push("back");
        BACK_ALLOWS_LAST_TP = BUILDER.comment("Whether to allow going back to the last teleport location in addition to the last death (If back is enabled for non-op). The history for the back command goes back only to the very last teleport or death").define("allow_last_tp", true);

        ENABLE_BACK = BUILDER.comment("Enable the use of the back command for non-op?").define("enabled", true);

        BUILDER.pop();



        BUILDER.pop();
        SPEC=BUILDER.build();
    }
    static class TeleportEffect
    {
        String Effect;
        int Duration;
        int Amplifier;
        boolean Ambient;

        public TeleportEffect(String name, int Duration, int Amplifier, boolean Ambient)
        {
            this.Effect = name;
            this.Duration = Duration;
            this.Amplifier = Amplifier;
            this.Ambient = Ambient;
        }
    }
}
