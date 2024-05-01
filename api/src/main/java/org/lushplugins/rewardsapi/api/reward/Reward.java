package org.lushplugins.rewardsapi.api.reward;

import org.jetbrains.annotations.NotNull;
import org.lushplugins.rewardsapi.api.RewardsAPI;
import org.lushplugins.rewardsapi.api.util.SchedulerType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Reward implements Cloneable {

    public abstract SchedulerType getSchedulerType();

    public abstract Map<String, Object> asMap();

    public void give(Player player) {
        if (this instanceof PlayerReward playerReward) {
            playerReward.giveTo(player);
        } else if (this instanceof LocationReward locationReward) {
            locationReward.giveAt(player.getWorld(), player.getLocation());
        } else if (this instanceof GenericReward genericReward) {
            genericReward.give();
        }
    }

    @Nullable
    public static Reward loadReward(ConfigurationSection configurationSection) {
        return loadReward(configurationSection.getValues(true), configurationSection.getCurrentPath());
    }

    @Nullable
    public static Reward loadReward(Map<?, ?> rewardMap, String path) {
        RewardTypes rewardTypes = RewardsAPI.getInstance().getRewardTypes();

        String rewardType = (String) rewardMap.get("type");
        if (!rewardTypes.isRegistered(rewardType)) {
            RewardsAPI.getInstance().getLogger().severe("Invalid reward type at '" + path + "'");
            return null;
        }

        try {
            return rewardTypes.loadRewardOfType(rewardType, rewardMap);
        } catch (IllegalArgumentException e) {
            RewardsAPI.getInstance().getLogger().warning(e.getCause().getMessage());
            return null;
        }
    }

    @NotNull
    public static List<Reward> loadRewards(ConfigurationSection configurationSection, String path) {
        return loadRewards(configurationSection.getMapList(path), configurationSection.getCurrentPath());
    }

    @NotNull
    public static List<Reward> loadRewards(List<Map<?, ?>> maps, String path) {
        List<Reward> rewardList = new ArrayList<>();

        maps.forEach((map) -> {
            Reward reward = loadReward(map, path);
            if (reward != null) {
                rewardList.add(reward);
            }
        });

        return !rewardList.isEmpty() ? rewardList : new ArrayList<>();
    }

    @Override
    public Reward clone() {
        try {
            return (Reward) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
