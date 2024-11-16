package org.lushplugins.rewardsapi.api.reward;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.lushplugins.rewardsapi.api.util.SchedulerType;

import java.util.Map;

public class EmptyReward extends Reward implements PlayerReward, LocationReward, GenericReward {

    public EmptyReward() {}

    public EmptyReward(Map<?, ?> map) {}

    @Override
    public void give() {}

    @Override
    public void giveAt(World world, Location location) {}

    @Override
    public void giveTo(Player player) {}

    @Override
    public Map<String, Object> asMap() {
        return Map.of("type", "empty");
    }

    @Override
    public SchedulerType getSchedulerType() {
        return SchedulerType.ASYNC;
    }
}
