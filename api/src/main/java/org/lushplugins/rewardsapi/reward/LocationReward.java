package org.lushplugins.rewardsapi.reward;

import org.bukkit.Location;
import org.bukkit.World;

public interface LocationReward {
    void giveAt(World world, Location location);
}
