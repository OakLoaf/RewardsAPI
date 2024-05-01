package org.lushplugins.rewardsapi.api.reward;

import me.dave.chatcolorhandler.ChatColorHandler;
import me.dave.chatcolorhandler.parsers.custom.PlaceholderAPIParser;
import org.lushplugins.lushlib.hook.Hook;
import org.lushplugins.rewardsapi.api.RewardsAPI;
import org.lushplugins.rewardsapi.api.hook.FloodgateHook;
import org.lushplugins.rewardsapi.api.hook.HookId;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class CommandReward extends Reward implements PlayerReward {
    protected final List<String> commands;

    public CommandReward(List<String> commands) {
        this.commands = commands;
    }

    protected abstract CommandSender getCommandSender(Player player);

    @Override
    public void giveTo(Player player) {
        commands.forEach(commandRaw -> {
            commandRaw = commandRaw.replaceAll("%player%", player.getName());
            String command = ChatColorHandler.translate(commandRaw, player, List.of(PlaceholderAPIParser.class));

            if (command.startsWith("java:")) {
                command = command.substring(5);

                Hook floodgateHook = RewardsAPI.getInstance().getHook(HookId.FLOODGATE);
                if (floodgateHook != null && ((FloodgateHook) floodgateHook).isFloodgatePlayer(player.getUniqueId())) {
                    return;
                }
            } else if (command.startsWith("bedrock:")) {
                command = command.substring(8);

                Hook floodgateHook = RewardsAPI.getInstance().getHook(HookId.FLOODGATE);
                if (floodgateHook == null || !((FloodgateHook) floodgateHook).isFloodgatePlayer(player.getUniqueId())) {
                    return;
                }
            }

            Bukkit.dispatchCommand(getCommandSender(player), command);
        });
    }
}
