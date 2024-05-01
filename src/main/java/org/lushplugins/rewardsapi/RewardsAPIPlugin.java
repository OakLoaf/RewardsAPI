package org.lushplugins.rewardsapi;

import org.lushplugins.lushlib.plugin.SpigotPlugin;
import org.lushplugins.rewardsapi.api.RewardsAPI;
import org.lushplugins.rewardsapi.command.RewardsAPICommand;
import org.lushplugins.rewardsapi.config.ConfigManager;
import org.lushplugins.rewardsapi.reward.ReferenceReward;

public final class RewardsAPIPlugin extends SpigotPlugin {
    private static RewardsAPIPlugin plugin;

    private ConfigManager configManager;

    @Override
    public void onLoad() {
        plugin = this;
        RewardsAPI.getInstance().registerRewardType("reference", ReferenceReward::new);
    }

    @Override
    public void onEnable() {
        try {
            configManager = new ConfigManager();
            configManager.reload();
        } catch (Exception e) {
            e.printStackTrace();
        }

        registerCommand(new RewardsAPICommand());
    }

    @Override
    public void onDisable() {
        if (configManager != null) {
            configManager.disable();
            configManager = null;
        }
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public static RewardsAPIPlugin getInstance() {
        return plugin;
    }
}
