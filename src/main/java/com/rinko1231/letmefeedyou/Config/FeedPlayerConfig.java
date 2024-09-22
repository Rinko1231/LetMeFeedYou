package com.rinko1231.letmefeedyou.Config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;

public class FeedPlayerConfig
{
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec.IntValue canBeFedBelowHungerOf;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> foodBlacklist;


    static
    {
        BUILDER.push("Let Me Feed You Config");

        canBeFedBelowHungerOf = BUILDER
                .comment("Normally it should be below 20, but you may have installed some mods that remove the limit.")
                .defineInRange("The Player can only be fed when the hunger is below……", 20, 1, Integer.MAX_VALUE);

        foodBlacklist = BUILDER
                .comment("Food that should not be fed to other players")
                .defineList("Food Blacklist", List.of("artifacts:everlasting_beef", "artifacts:eternal_steak"),
                        element -> element instanceof String);

        SPEC = BUILDER.build();
    }

    public static void setup()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC, "LetMeFeedYou.toml");
    }


}