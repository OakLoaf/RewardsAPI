package org.lushplugins.rewardsapi.reward;

import org.lushplugins.rewardsapi.util.SchedulerType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public class PlayerCommandReward extends CommandReward {

    @SuppressWarnings("unchecked")
    public PlayerCommandReward(Map<?, ?> map) {
        super((List<String>) map.get("commands"));
    }

    @Override
    protected CommandSender getCommandSender(Player player) {
        return player;
    }

    @Override
    public Map<String, Object> asMap() {
        Map<String, Object> rewardMap = new ConcurrentHashMap<>();

        rewardMap.put("type", "player-command");
        rewardMap.put("commands", commands);

        return rewardMap;
    }

    @Override
    public SchedulerType getSchedulerType() {
        return SchedulerType.PLAYER;
    }
}
