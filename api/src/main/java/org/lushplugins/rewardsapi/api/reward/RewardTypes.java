package org.lushplugins.rewardsapi.api.reward;

import org.lushplugins.lushlib.manager.Manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class RewardTypes extends Manager {
    public static final String BROADCAST = "broadcast";
    public static final String CONSOLE_COMMAND = "command";
    public static final String EMPTY = "empty";
    public static final String ITEM = "item";
    public static final String MESSAGE = "message";
    public static final String PERMISSION = "permission";
    public static final String PLAYER_COMMAND = "player-command";
    public static final String RANDOM = "random";

    private ConcurrentHashMap<String, Function<Map<?, ?>, Reward>> rewardTypes;

    @Override
    public void onEnable() {
        rewardTypes = new ConcurrentHashMap<>();

        register(BROADCAST, BroadcastReward::new);
        register(CONSOLE_COMMAND, ConsoleCommandReward::new);
        register(EMPTY, EmptyReward::new);
        register(ITEM, ItemReward::new);
        register(MESSAGE, MessageReward::new);
        register(PERMISSION, PermissionReward::new);
        register(PLAYER_COMMAND, PlayerCommandReward::new);
        register(RANDOM, RandomReward::new);
    }

    @Override
    public void onDisable() {
        if (rewardTypes != null) {
            rewardTypes.clear();
            rewardTypes = null;
        }
    }

    public Reward loadRewardOfType(String type, Map<?, ?> rewardMap) {
        return rewardTypes.containsKey(type) ? rewardTypes.get(type).apply(rewardMap) : null;
    }

    public boolean isRegistered(String type) {
        return rewardTypes.containsKey(type);
    }

    public void register(String type, Function<Map<?, ?>, Reward> constructor) {
        rewardTypes.put(type, constructor);
    }

    public void unregister(String type) {
        rewardTypes.remove(type);
    }
}
