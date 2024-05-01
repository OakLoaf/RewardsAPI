package org.lushplugins.rewardsapi.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.lushlib.manager.Manager;
import org.lushplugins.rewardsapi.RewardsAPIPlugin;
import org.lushplugins.rewardsapi.api.reward.Reward;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class ConfigManager extends Manager {
    private static final File REWARDS_FOLDER = new File(RewardsAPIPlugin.getInstance().getDataFolder(), "rewards");

    private HashMap<String, List<Reward>> rewards;
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
                YamlConfiguration rewardsConfig = YamlConfiguration.loadConfiguration(rewardsFile);

                for (String id : rewardsConfig.getKeys(false)) {
                    if (rewardsConfig.isList(id)) {
                        this.rewards.put(id, Reward.loadRewards(rewardsConfig.getMapList(id), id));
                    } else {
                        ConfigurationSection rewardConfig = rewardsConfig.getConfigurationSection(id);
                        if (rewardConfig != null) {
                            Reward reward = Reward.loadReward(rewardConfig);
                            if (reward != null) {
                                this.rewards.put(id, List.of(reward));
                            }
                        }
                    }
                }
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

    @NotNull
    public List<Reward> getRewards(String name) {
        return rewards.getOrDefault(name, new ArrayList<>());
    }

    public void addReward(String name, Reward reward) {
        addRewards(name, List.of(reward));
    }

    public void addRewards(String name, List<Reward> rewards) {
        this.rewards.put(name, rewards);
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
