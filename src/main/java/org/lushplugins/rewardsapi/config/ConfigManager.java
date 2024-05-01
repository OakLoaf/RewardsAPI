package org.lushplugins.rewardsapi.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.lushlib.manager.Manager;
import org.lushplugins.rewardsapi.RewardsAPIPlugin;
import org.lushplugins.rewardsapi.api.reward.Reward;
import org.lushplugins.rewardsapi.util.YamlUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class ConfigManager extends Manager {
    private static final File REWARDS_FOLDER = new File(RewardsAPIPlugin.getInstance().getDataFolder(), "rewards");

    private HashMap<String, Reward> rewards;
    private HashMap<String, String> messages;

    public ConfigManager() {
        if (!REWARDS_FOLDER.exists()) {
            RewardsAPIPlugin.getInstance().saveDefaultResource("rewards/example.yml");
        }

        RewardsAPIPlugin.getInstance().saveDefaultResource("messages.yml");
    }

    public void reload() {
        disable();
        enable();
    }

    @Override
    public void onEnable() {
        this.rewards = new HashMap<>();
        try {
            Files.newDirectoryStream(REWARDS_FOLDER.toPath(), "*.yml").forEach(entry -> {
                File rewardsFile = entry.toFile();
                YamlConfiguration functionConfig = YamlConfiguration.loadConfiguration(rewardsFile);
                YamlUtils.getConfigurationSections(functionConfig).forEach(rewardConfig -> this.rewards.put(rewardConfig.getName(), Reward.loadReward(rewardConfig)));
            });
        } catch (IOException e) {
            RewardsAPIPlugin.getInstance().log(Level.SEVERE, "Something went wrong whilst reading rewards files");
            e.printStackTrace();
        }

        this.messages = new HashMap<>();
        YamlConfiguration messagesConfig = YamlConfiguration.loadConfiguration(new File(RewardsAPIPlugin.getInstance().getDataFolder(), "messages.yml"));
        messagesConfig.getValues(false).forEach((id, message) -> messages.put(id, (String) message));
    }

    @Override
    public void onDisable() {
        if (rewards != null) {
            rewards.clear();
            rewards = null;
        }

        if (messages != null) {
            messages.clear();
            messages = null;
        }
    }

    public List<String> getRewardNames() {
        return rewards.keySet().stream().toList();
    }

    @Nullable
    public Reward getReward(String name) {
        return rewards.get(name);
    }

    public void addReward(String name, Reward reward) {
        rewards.put(name, reward);
    }

    public void removeReward(String name) {
        rewards.remove(name);
    }

    public void removeAllRewards() {
        rewards.clear();
    }

    @Nullable
    public String getMessage(String id) {
        return messages.get(id);
    }

    @NotNull
    public String getMessage(String id, String def) {
        return messages.getOrDefault(id, def);
    }
}
