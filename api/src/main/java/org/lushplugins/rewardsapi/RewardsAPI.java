package org.lushplugins.rewardsapi;

import org.lushplugins.rewardsapi.hook.FloodgateHook;
import org.lushplugins.rewardsapi.hook.HookId;
import org.lushplugins.rewardsapi.reward.Reward;
import org.lushplugins.rewardsapi.reward.RewardTypes;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.lushplugins.lushlib.hook.Hook;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

public class RewardsAPI {
    private static RewardsAPI rewardsAPI;

    private final Logger logger;
    private final HashMap<String, Hook> hooks = new HashMap<>();
    private final RewardTypes rewardTypes = new RewardTypes();

    public RewardsAPI() {
        this.logger = Logger.getLogger("RewardsAPI");
        setup();
    }

    public RewardsAPI(JavaPlugin plugin) {
        this.logger = plugin.getLogger();
        setup();
    }

    private void setup() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        if (pluginManager.isPluginEnabled(HookId.FLOODGATE)) {
            hooks.put(HookId.FLOODGATE, new FloodgateHook());
        }

        rewardTypes.enable();
    }

    public RewardTypes getRewardTypes() {
        return rewardTypes;
    }

    public void registerRewardType(String type, Function<Map<?, ?>, Reward> rewardConstructor) {
        rewardTypes.register(type, rewardConstructor);
    }

    public void unregisterRewardType(String type) {
        rewardTypes.unregister(type);
    }

    public Hook getHook(String name) {
        return hooks.get(name);
    }

    public Logger getLogger() {
        return logger;
    }

    public static RewardsAPI getInstance() {
        if (rewardsAPI == null) {
            rewardsAPI = new RewardsAPI();
        }

        return rewardsAPI;
    }

    public static void setup(JavaPlugin plugin) {
        if (rewardsAPI != null) {
            throw new IllegalStateException("RewardsAPI has already been setup");
        }

        rewardsAPI = new RewardsAPI(plugin);
    }
}
