package org.lushplugins.rewardsapi.api.reward;

import me.dave.chatcolorhandler.ChatColorHandler;
import me.dave.chatcolorhandler.parsers.custom.PlaceholderAPIParser;
import org.lushplugins.rewardsapi.api.util.SchedulerType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class ConsoleCommandReward extends CommandReward implements LocationReward, GenericReward {
    private static final ConsoleCommandSender CONSOLE = Bukkit.getConsoleSender();

    @SuppressWarnings("unchecked")
    public ConsoleCommandReward(Map<?, ?> map) {
        super((List<String>) map.get("commands"));
    }

    @Override
    protected CommandSender getCommandSender(Player player) {
        return CONSOLE;
    }

    @Override
    public void giveAt(World world, Location location) {
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        commands.forEach(commandRaw -> {
            String command = ChatColorHandler.translate(commandRaw.replaceAll("%world%", world.getName()).replaceAll("%location%", location.getX() + " " + location.getY() + " " + location.getZ()), List.of(PlaceholderAPIParser.class));
            Bukkit.dispatchCommand(console, command);
        });
    }

    @Override
    public void give() {
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        commands.forEach(commandRaw -> {
            String command = ChatColorHandler.translate(commandRaw, List.of(PlaceholderAPIParser.class));
            Bukkit.dispatchCommand(console, command);
        });
    }

    @Override
    public Map<String, Object> asMap() {
        Map<String, Object> rewardMap = new HashMap<>();

        rewardMap.put("type", "command");
        rewardMap.put("commands", commands);

        return rewardMap;
    }

    @Override
    public SchedulerType getSchedulerType() {
        return SchedulerType.GLOBAL;
    }
}
