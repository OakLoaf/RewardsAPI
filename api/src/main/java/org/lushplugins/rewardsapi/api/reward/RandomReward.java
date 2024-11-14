package org.lushplugins.rewardsapi.api.reward;

import org.bukkit.Location;
import org.bukkit.World;
import org.lushplugins.lushlib.utils.RandomCollection;
import org.lushplugins.rewardsapi.api.RewardsAPI;
import org.lushplugins.rewardsapi.api.util.SchedulerType;
import org.bukkit.entity.Player;

import java.util.*;

@SuppressWarnings("unused")
public class RandomReward extends Reward implements PlayerReward, LocationReward, GenericReward {
    private final RandomCollection<Reward> rewards;

    public RandomReward(RandomCollection<Reward> rewards) {
        this.rewards = rewards;
    }

    @SuppressWarnings("unchecked")
    public RandomReward(Map<?, ?> map) {
        List<Map<?, ?>> rewardMaps;
        try {
            rewardMaps = map.containsKey("rewards") ? (List<Map<?, ?>>) map.get("rewards") : List.of(Collections.emptyMap());
        } catch(ClassCastException exc) {
            throw new IllegalArgumentException("Invalid config format at '" + map + "'");
        }

        this.rewards = new RandomCollection<>();

        rewardMaps.forEach((rewardMap) -> {
            Reward reward = Reward.loadReward(rewardMap, rewardMap.toString());
            int weight = rewardMap.containsKey("weight") ? (int) rewardMap.get("weight") : 1;
            if (reward != null) {
                rewards.add(reward, weight);
            }
        });
    }

    public Reward getRandomReward() {
        if (rewards != null && !rewards.isEmpty()) {
            return rewards.next();
        } else {
            return null;
        }
    }

    @Override
    public void giveTo(Player player) {
        if (rewards != null && !rewards.isEmpty()) {
            Reward reward = rewards.next();

            try {
                reward.give(player);
            } catch (Exception e) {
                RewardsAPI.getInstance().getLogger().severe("Error occurred when giving reward (" + reward + ") to " + player.getName());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void giveAt(World world, Location location) {
        if (rewards != null && !rewards.isEmpty()) {
            Reward reward = rewards.next();

            try {
                if (reward instanceof LocationReward locationReward) {
                    locationReward.giveAt(world, location);
                } else if (reward instanceof GenericReward genericReward) {
                    genericReward.give();
                }
            } catch (Exception e) {
                RewardsAPI.getInstance().getLogger().severe("Error occurred when giving reward (" + reward + ") at " + world.getName() + ": " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void give() {
        if (rewards != null && !rewards.isEmpty()) {
            Reward reward = rewards.next();

            try {
                if (reward instanceof GenericReward genericReward) {
                    genericReward.give();
                }
            } catch (Exception e) {
                RewardsAPI.getInstance().getLogger().severe("Error occurred when giving reward (" + reward + ")");
                e.printStackTrace();
            }
        }
    }

    @Override
    public Map<String, Object> asMap() {
        Map<String, Object> rewardMap = new HashMap<>();
        List<Map<String, Object>> rewardsMap = new ArrayList<>();
        rewards.getMap().forEach((weight, reward) -> {
            Map<String, Object> weightRewardMap = reward.asMap();
            weightRewardMap.put("weight", weight);

            rewardsMap.add(weightRewardMap);
        });

        rewardMap.put("type", "random");
        rewardMap.put("rewards", rewardsMap);

        return rewardMap;
    }

    @Override
    public SchedulerType getSchedulerType() {
        return SchedulerType.ASYNC;
    }
}
