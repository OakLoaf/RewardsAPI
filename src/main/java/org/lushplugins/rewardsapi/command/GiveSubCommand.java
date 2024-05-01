package org.lushplugins.rewardsapi.command;

import me.dave.chatcolorhandler.ChatColorHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.lushlib.command.SubCommand;
import org.lushplugins.rewardsapi.RewardsAPIPlugin;
import org.lushplugins.rewardsapi.api.reward.GenericReward;
import org.lushplugins.rewardsapi.api.reward.LocationReward;
import org.lushplugins.rewardsapi.api.reward.PlayerReward;
import org.lushplugins.rewardsapi.api.reward.Reward;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GiveSubCommand extends SubCommand {

    public GiveSubCommand() {
        super("give");
        addSubCommand(new GivePlayerCommand());
        addSubCommand(new GiveLocationCommand());
        addSubCommand(new GiveGenericCommand());
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        return true;
    }

    public static class GivePlayerCommand extends SubCommand {

        public GivePlayerCommand() {
            super("player");
            addRequiredArgs(0, () -> RewardsAPIPlugin.getInstance().getConfigManager().getRewardNames());
        }

        @Override
        public boolean execute(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
            String rewardName = args[0];

            List<Reward> rewards = RewardsAPIPlugin.getInstance().getConfigManager().getRewards(rewardName);
            if (rewards.isEmpty()) {
                ChatColorHandler.sendMessage(sender, RewardsAPIPlugin.getInstance().getConfigManager().getMessage("invalid-arg", "Invalid %arg% specified").replace("%arg%", "reward"));
                return true;
            }

            Collection<? extends Player> players;
            if (args.length >= 3) {
                if (args[2].equalsIgnoreCase("*")) {
                    players = Bukkit.getOnlinePlayers();
                } else {
                    Player player = Bukkit.getPlayer(args[2]);
                    if (player == null) {
                        ChatColorHandler.sendMessage(sender, RewardsAPIPlugin.getInstance().getConfigManager().getMessage("invalid-arg", "Invalid %arg% specified").replace("%arg%", "player"));
                        return true;
                    }

                    players = List.of(player);
                }
            } else {
                if (sender instanceof Player player) {
                    players = List.of(player);
                } else {
                    ChatColorHandler.sendMessage(sender, RewardsAPIPlugin.getInstance().getConfigManager().getMessage("invalid-arg", "Invalid %arg% specified").replace("%arg%", "player"));
                    return true;
                }
            }

            boolean given = false;
            for (Reward reward : rewards) {
                if (reward instanceof PlayerReward playerReward) {
                    players.forEach(playerReward::giveTo);
                    given = true;
                } else {
                    ChatColorHandler.sendMessage(sender, RewardsAPIPlugin.getInstance().getConfigManager().getMessage("invalid-reward-type", "Invalid reward type specified").replace("%reward-type%", "generic"));
                }
            }

            if (given) {
                ChatColorHandler.sendMessage(sender, RewardsAPIPlugin.getInstance().getConfigManager().getMessage("reward-given", "Reward '%reward%' has been given").replace("%reward%", rewardName));
            }

            return true;
        }

        @Override
        public @Nullable List<String> tabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            if (args.length == 2) {
                List<String> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
                onlinePlayers.add("*");

                return onlinePlayers;
            }

            return null;
        }
    }

    public static class GiveLocationCommand extends SubCommand {

        public GiveLocationCommand() {
            super("location");
            addRequiredArgs(0, () -> RewardsAPIPlugin.getInstance().getConfigManager().getRewardNames());
        }

        @Override
        public boolean execute(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
            String rewardName = args[0];

            List<Reward> rewards = RewardsAPIPlugin.getInstance().getConfigManager().getRewards(rewardName);
            if (rewards.isEmpty()) {
                ChatColorHandler.sendMessage(sender, RewardsAPIPlugin.getInstance().getConfigManager().getMessage("invalid-arg", "Invalid %arg% specified").replace("%arg%", "reward"));
                return true;
            }

            World world;
            Location location;
            if (args.length >= 5) {
                world = Bukkit.getWorld(args[1]);
                double x = Double.parseDouble(args[2]);
                double y = Double.parseDouble(args[3]);
                double z = Double.parseDouble(args[4]);

                location = new Location(world, x, y, z);
            } else {
                if (sender instanceof Player player) {
                    world = player.getWorld();
                    location = player.getLocation();
                } else {
                    ChatColorHandler.sendMessage(sender, RewardsAPIPlugin.getInstance().getConfigManager().getMessage("invalid-arg", "Invalid %arg% specified").replace("%arg%", "player"));
                    return true;
                }
            }

            boolean given = false;
            for (Reward reward : rewards ) {
                if (reward instanceof LocationReward locationReward) {
                    locationReward.giveAt(world, location);
                    given = true;
                } else {
                    ChatColorHandler.sendMessage(sender, RewardsAPIPlugin.getInstance().getConfigManager().getMessage("invalid-reward-type", "Invalid reward type specified").replace("%reward-type%", "location"));
                }
            }

            if (given) {
                ChatColorHandler.sendMessage(sender, RewardsAPIPlugin.getInstance().getConfigManager().getMessage("reward-given", "Reward '%reward%' has been given").replace("%reward%", rewardName));
            }

            return true;
        }

        @Override
        public @Nullable List<String> tabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            switch (args.length) {
                case 2 -> {
                    return Bukkit.getWorlds().stream().map(World::getName).toList();
                }
                case 3 -> {
                    if (sender instanceof Player player) {
                        RayTraceResult rayTrace = player.rayTraceBlocks(10);
                        if (rayTrace != null) {
                            Block hitBlock = rayTrace.getHitBlock();
                            if (hitBlock != null) {
                                return List.of(String.valueOf(hitBlock.getX()));
                            }
                        }
                    }
                }
                case 4 -> {
                    if (sender instanceof Player player) {
                        RayTraceResult rayTrace = player.rayTraceBlocks(10);
                        if (rayTrace != null) {
                            Block hitBlock = rayTrace.getHitBlock();
                            if (hitBlock != null) {
                                return List.of(String.valueOf(hitBlock.getY()));
                            }
                        }
                    }
                }
                case 5 -> {
                    if (sender instanceof Player player) {
                        RayTraceResult rayTrace = player.rayTraceBlocks(10);
                        if (rayTrace != null) {
                            Block hitBlock = rayTrace.getHitBlock();
                            if (hitBlock != null) {
                                return List.of(String.valueOf(hitBlock.getZ()));
                            }
                        }
                    }
                }
            }

            return null;
        }
    }

    public static class GiveGenericCommand extends SubCommand {

        public GiveGenericCommand() {
            super("generic");
            addRequiredArgs(0, () -> RewardsAPIPlugin.getInstance().getConfigManager().getRewardNames());
        }

        @Override
        public boolean execute(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
            String rewardName = args[0];

            List<Reward> rewards = RewardsAPIPlugin.getInstance().getConfigManager().getRewards(rewardName);
            if (rewards.isEmpty()) {
                ChatColorHandler.sendMessage(sender, RewardsAPIPlugin.getInstance().getConfigManager().getMessage("invalid-arg", "Invalid %arg% specified").replace("%arg%", "reward"));
                return true;
            }

            boolean given = false;
            for (Reward reward : rewards) {
                if (reward instanceof GenericReward genericReward) {
                    genericReward.give();
                    given = true;
                } else {
                    ChatColorHandler.sendMessage(sender, RewardsAPIPlugin.getInstance().getConfigManager().getMessage("invalid-reward-type", "Invalid reward type specified").replace("%reward-type%", "generic"));
                }
            }

            if (given) {
                ChatColorHandler.sendMessage(sender, RewardsAPIPlugin.getInstance().getConfigManager().getMessage("reward-given", "Reward '%reward%' has been given").replace("%reward%", rewardName));
            }

            return true;
        }
    }
}
