package org.lushplugins.rewardsapi.api.reward;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class WrapperReward extends Reward {
    protected final List<Reward> rewards;

    public WrapperReward(List<Reward> rewards) {
        this.rewards = rewards;
    }

    @SuppressWarnings("unchecked")
    public WrapperReward(Map<?, ?> map) {
        List<Map<?, ?>> rewardMaps;
        try {
            rewardMaps = map.containsKey("rewards") ? (List<Map<?, ?>>) map.get("rewards") : List.of(Collections.emptyMap());
        } catch(ClassCastException exc) {
            throw new IllegalArgumentException("Invalid config format at '" + map + "'");
        }

        this.rewards = rewardMaps.stream().map(rewardMap -> Reward.loadReward(rewardMap, rewardMap.toString())).toList();
    }

    /**
     * It is possible that not all rewards within the wrapper would be received by a player depending on the
     * subclass implementation so use this carefully
     *
     * @return the list of rewards in this wrapper
     */
    public List<Reward> getRewards() {
        return rewards;
    }
}
