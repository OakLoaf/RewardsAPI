package org.lushplugins.rewardsapi.reward;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.lushplugins.lushlib.libraries.chatcolor.ChatColorHandler;
import org.lushplugins.rewardsapi.RewardsAPIPlugin;
import org.lushplugins.rewardsapi.api.reward.GenericReward;
import org.lushplugins.rewardsapi.api.reward.LocationReward;
import org.lushplugins.rewardsapi.api.reward.PlayerReward;
import org.lushplugins.rewardsapi.api.reward.Reward;
import org.lushplugins.rewardsapi.api.util.SchedulerType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReferenceReward extends Reward implements PlayerReward, LocationReward, GenericReward {
    private final String reference;

    public ReferenceReward(String reference) {
        this.reference = reference;
    }

    public ReferenceReward(Map<?, ?> map) {
        reference = (String) map.get("reference");
    }

    @Override
    public void giveTo(Player player) {
        List<Reward> rewards = RewardsAPIPlugin.getInstance().getConfigManager().getRewards(reference);
        if (rewards.isEmpty()) {
            RewardsAPIPlugin.getInstance().getLogger().severe(ChatColorHandler.translate(RewardsAPIPlugin.getInstance().getConfigManager().getMessage("invalid-arg", "Invalid %arg% specified").replace("%arg%", "reference reward '" + reference + "'")));
            return;
        }

        rewards.forEach(reward -> {
            if (reward instanceof PlayerReward playerReward) {
                playerReward.giveTo(player);
            } else if (reward instanceof LocationReward locationReward) {
                locationReward.giveAt(player.getWorld(), player.getLocation());
            } else if (reward instanceof GenericReward genericReward) {
                genericReward.give();
            } else {
                RewardsAPIPlugin.getInstance().getLogger().severe(ChatColorHandler.translate(RewardsAPIPlugin.getInstance().getConfigManager().getMessage("invalid-arg", "Invalid %arg% specified").replace("%arg%", "reference reward type '" + reference + "'")));
            }
        });
    }

    @Override
    public void giveAt(World world, Location location) {
        List<Reward> rewards = RewardsAPIPlugin.getInstance().getConfigManager().getRewards(reference);
        if (rewards.isEmpty()) {
            RewardsAPIPlugin.getInstance().getLogger().severe(ChatColorHandler.translate(RewardsAPIPlugin.getInstance().getConfigManager().getMessage("invalid-arg", "Invalid %arg% specified").replace("%arg%", "reference reward '" + reference + "'")));
            return;
        }

        rewards.forEach(reward -> {
            if (reward instanceof LocationReward locationReward) {
                locationReward.giveAt(world, location);
            } else if (reward instanceof GenericReward genericReward) {
                genericReward.give();
            } else {
                RewardsAPIPlugin.getInstance().getLogger().severe(ChatColorHandler.translate(RewardsAPIPlugin.getInstance().getConfigManager().getMessage("invalid-arg", "Invalid %arg% specified").replace("%arg%", "reference reward type '" + reference + "'")));
            }
        });
    }

    @Override
    public void give() {
        List<Reward> rewards = RewardsAPIPlugin.getInstance().getConfigManager().getRewards(reference);
        if (rewards.isEmpty()) {
            RewardsAPIPlugin.getInstance().getLogger().severe(ChatColorHandler.translate(RewardsAPIPlugin.getInstance().getConfigManager().getMessage("invalid-arg", "Invalid %arg% specified").replace("%arg%", "reference reward '" + reference + "'")));
            return;
        }

        rewards.forEach(reward -> {
            if (reward instanceof GenericReward genericReward) {
                genericReward.give();
            } else {
                RewardsAPIPlugin.getInstance().getLogger().severe(ChatColorHandler.translate(RewardsAPIPlugin.getInstance().getConfigManager().getMessage("invalid-arg", "Invalid %arg% specified").replace("%arg%", "reference reward type '" + reference + "'")));
            }
        });
    }

    @Override
    public Map<String, Object> asMap() {
        Map<String, Object> rewardMap = new HashMap<>();
        rewardMap.put("reference", reference);

        return rewardMap;
    }

    @Override
    public SchedulerType getSchedulerType() {
        return SchedulerType.ASYNC;
    }
}
