package com.rinko1231.letmefeedyou;


import com.rinko1231.letmefeedyou.Config.FeedPlayerConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.UUID;

@Mod(LetMeFeedYou.MODID)
public class LetMeFeedYou
{
    public static final String MODID = "letmefeedyou";
    private static final long FEED_COOLDOWN = 1000; //冷却
    private static final HashMap<UUID, Long> playerFeedCooldowns = new HashMap<>();

    public LetMeFeedYou()
    {
        MinecraftForge.EVENT_BUS.register(this);
        FeedPlayerConfig.setup();
    }


    @SubscribeEvent
    public void onPlayerRightClickEntity(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof Player targetPlayer && !event.getLevel().isClientSide) {
            Player feeder = event.getEntity();
            final ItemStack foodItem = feeder.getMainHandItem();
            if (!foodItem.isEdible()) return;
            String itemId = ForgeRegistries.ITEMS.getKey(foodItem.getItem()).toString();
            if (FeedPlayerConfig.foodBlacklist.get().contains(itemId)) return;
            if (!(targetPlayer.getFoodData().getFoodLevel() < FeedPlayerConfig.canBeFedBelowHungerOf.get())) return;

            UUID targetUUID = targetPlayer.getUUID();
            long currentTime = System.currentTimeMillis();
            playerFeedCooldowns.entrySet().removeIf(entry -> currentTime - entry.getValue() > FEED_COOLDOWN);

            // 检查目标玩家是否在冷却中
            if (playerFeedCooldowns.containsKey(targetUUID)) {
                long lastFedTime = playerFeedCooldowns.get(targetUUID);
                if (currentTime - lastFedTime < FEED_COOLDOWN) {
                    // 玩家仍在冷却中，不执行喂食
                    return;
                }
            }

            Component foodName;
            if (foodItem.hasCustomHoverName()) {
                foodName = foodItem.getHoverName();
            } else
               foodName = foodItem.getDisplayName();
            String feederName = feeder.getDisplayName().getString();

            final ItemStack consumed = foodItem.finishUsingItem(targetPlayer.getLevel(), targetPlayer);

            targetPlayer.displayClientMessage(Component.translatable("info.letmefeedyou.success", feederName ,foodName ), true);

            if (!feeder.getAbilities().instabuild) feeder.setItemInHand(InteractionHand.MAIN_HAND, consumed);

            playerFeedCooldowns.put(targetUUID, currentTime);
        }
    }


}
