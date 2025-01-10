package com.rinko1231.letmefeedyou;


import com.rinko1231.letmefeedyou.Config.FeedPlayerConfig;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.UUID;

@Mod(LetMeFeedYou.MODID)
public class LetMeFeedYou
{
    public static final String MODID = "letmefeedyou";


    public LetMeFeedYou(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(new FeedEvents());
        modContainer.registerConfig(ModConfig.Type.COMMON, FeedPlayerConfig.SPEC);
    }


}
