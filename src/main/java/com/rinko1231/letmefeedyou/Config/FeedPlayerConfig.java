package com.rinko1231.letmefeedyou.Config;


import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class FeedPlayerConfig
{
    public static final ModConfigSpec SPEC;
    public static ModConfigSpec.IntValue canBeFedBelowHungerOf;

    public static ModConfigSpec.ConfigValue<List<? extends String>> foodBlacklist;


    static
    {
        ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
        BUILDER.push("Let Me Feed You Config");

        canBeFedBelowHungerOf = BUILDER
                .comment("Normally it should be below 20, but you may have installed some mods that remove the limit.")
                .defineInRange("The Player can only be fed when the hunger is below ", 20, 1, Integer.MAX_VALUE);

        foodBlacklist = BUILDER
                .comment("Food that should not be fed to other players")
                .defineList("Food Blacklist", List.of(
                        "minecraft:spider_eye",
                        "artifacts:everlasting_beef",
                        "artifacts:eternal_steak"), () -> "", o -> (o instanceof String));
                //.define("Food Blacklist", new ArrayList<>());

               // .defineList("Food Blacklist", List.of("artifacts:everlasting_beef", "artifacts:eternal_steak"),
               //         element -> element instanceof String);

        SPEC = BUILDER.build();
    }


}