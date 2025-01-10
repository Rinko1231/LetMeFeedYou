package com.rinko1231.letmefeedyou;

import com.rinko1231.letmefeedyou.Config.FeedPlayerConfig;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.UUID;

public class FeedEvents {

    private static final long FEED_COOLDOWN = 1000; //冷却
    private static final HashMap<UUID, Long> playerFeedCooldowns = new HashMap<>();

    @SubscribeEvent
    public void onPlayerRightClickEntity(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof Player targetPlayer && !event.getLevel().isClientSide) {
            Player feeder = event.getEntity();
            final ItemStack foodItem = feeder.getMainHandItem();
            if (foodItem.getFoodProperties(feeder) == null) return;
            String itemId = BuiltInRegistries.ITEM.getKey(foodItem.getItem()).toString();
            if (FeedPlayerConfig.foodBlacklist.get().contains(itemId)) return;
            if (!(targetPlayer.getFoodData().getFoodLevel() < FeedPlayerConfig.canBeFedBelowHungerOf.get())) return;

            UUID targetUUID = targetPlayer.getUUID();
            long currentTime = System.currentTimeMillis();
            playerFeedCooldowns.entrySet().removeIf(entry -> currentTime - entry.getValue() > FEED_COOLDOWN);

            if (playerFeedCooldowns.containsKey(targetUUID)) {
                long lastFedTime = playerFeedCooldowns.get(targetUUID);
                if (currentTime - lastFedTime < FEED_COOLDOWN) {
                    return;
                }
            }

            Component foodName;
            if (foodItem.has(DataComponents.CUSTOM_NAME)) {
                foodName = foodItem.getHoverName();
            } else
                foodName = foodItem.getDisplayName();
            String feederName = feeder.getDisplayName().getString();

            if (!feeder.isCreative()) {
                final ItemStack consumed = foodItem.finishUsingItem(targetPlayer.level(), targetPlayer);
                targetPlayer.displayClientMessage(Component.translatable("info.letmefeedyou.success", feederName, foodName), true);

                if (!feeder.getAbilities().instabuild) feeder.setItemInHand(InteractionHand.MAIN_HAND, consumed);

                playerFeedCooldowns.put(targetUUID, currentTime);
            }
            else {
                targetPlayer.eat(targetPlayer.level(), foodItem.copy());
                targetPlayer.displayClientMessage(Component.translatable("info.letmefeedyou.success", feederName, foodName), true);
                playerFeedCooldowns.put(targetUUID, currentTime);
            }
        }
    }


}
