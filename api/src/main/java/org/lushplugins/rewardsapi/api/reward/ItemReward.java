package org.lushplugins.rewardsapi.api.reward;

import org.bukkit.Location;
import org.bukkit.World;
import org.lushplugins.lushlib.utils.SimpleItemStack;
import org.lushplugins.rewardsapi.api.util.SchedulerType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class ItemReward extends Reward implements PlayerReward, LocationReward {
    private final SimpleItemStack itemStack;

    public ItemReward(SimpleItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemReward(@NotNull Map<?, ?> map) {
        SimpleItemStack itemStack = new SimpleItemStack(map);

        if (itemStack.getType() != null) {
            this.itemStack = itemStack;
        }
        else {
            throw new IllegalArgumentException("Invalid config format at '" + map + "'");
        }
    }

    public ItemStack getItemStack() {
        return itemStack.asItemStack();
    }

    public ItemStack getItemStack(Player player) {
        return itemStack.asItemStack(player);
    }

    @Override
    public void giveTo(Player player) {
        HashMap<Integer, ItemStack> droppedItems = player.getInventory().addItem(itemStack.asItemStack(player, true));
        droppedItems.values().forEach(item -> player.getWorld().dropItem(player.getLocation(), item));
    }

    @Override
    public void giveAt(World world, Location location) {
        world.dropItem(location, itemStack.asItemStack(null, true));
    }

    @Override
    public Map<String, Object> asMap() {
        Map<String, Object> rewardMap = itemStack.asMap();

        rewardMap.put("type", "item");

        return rewardMap;
    }

    @Override
    public SchedulerType getSchedulerType() {
        return SchedulerType.PLAYER;
    }
}
