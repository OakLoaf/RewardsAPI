package org.lushplugins.rewardsapi.reward;

import org.lushplugins.rewardsapi.RewardsAPI;
import org.lushplugins.rewardsapi.util.SchedulerType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class PermissionReward extends WrapperReward implements PlayerReward {
    private final String permission;

    public PermissionReward(String permission, List<Reward> rewards) {
        super(rewards);
        this.permission = permission;
    }

    public PermissionReward(Map<?, ?> map) {
        super(map);
        permission = (String) map.get("permission");
    }

    @Override
    public void giveTo(Player player) {
        if (player.hasPermission(permission)) {
            rewards.forEach(reward -> {
                try {
                    reward.give(player);
                } catch (Exception e) {
                    RewardsAPI.getInstance().getLogger().severe("Error occurred when giving reward (" +reward.toString() + ") to " + player.getName());
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public Map<String, Object> asMap() {
        Map<String, Object> rewardMap = new HashMap<>();

        rewardMap.put("type", "permission");
        rewardMap.put("permission", permission);

        List<Map<String, Object>> rewardsMap = rewards.stream().map(Reward::asMap).toList();
        rewardMap.put("rewards", rewardsMap);

        return rewardMap;
    }

    @Override
    public SchedulerType getSchedulerType() {
        return SchedulerType.ASYNC;
    }
}