package org.lushplugins.rewardsapi.reward;

import me.dave.chatcolorhandler.ChatColorHandler;
import org.lushplugins.rewardsapi.util.SchedulerType;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class BroadcastReward extends Reward implements PlayerReward, LocationReward, GenericReward {
    private final String message;

    public  BroadcastReward(String message) {
        this.message = message;
    }

    public BroadcastReward(Map<?, ?> map) {
        this.message = (String) map.get("message");
    }

    @Override
    public void giveTo(Player player) {
        ChatColorHandler.broadcastMessage(message.replaceAll("%player%", player.getDisplayName()));
    }

    @Override
    public void giveAt(World world, Location location) {
        ChatColorHandler.broadcastMessage(message.replaceAll("%world%", world.getName()));
    }

    @Override
    public void give() {
        ChatColorHandler.broadcastMessage(message);
    }

    @Override
    public Map<String, Object> asMap() {
        Map<String, Object> rewardMap = new HashMap<>();

        rewardMap.put("type", "broadcast");
        rewardMap.put("message", message);

        return rewardMap;
    }

    @Override
    public SchedulerType getSchedulerType() {
        return SchedulerType.ASYNC;
    }
}
