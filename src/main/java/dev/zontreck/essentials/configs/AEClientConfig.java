package dev.zontreck.essentials.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class AEClientConfig
{


    public static final ForgeConfigSpec.Builder BUILDER= new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_HEARTS_RENDER;


    static {
        BUILDER.push("overlay");
        ENABLE_HEARTS_RENDER = BUILDER.comment("Enable compressed hearts? This puts all the hearts in a single row").define("enable_hearts", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
