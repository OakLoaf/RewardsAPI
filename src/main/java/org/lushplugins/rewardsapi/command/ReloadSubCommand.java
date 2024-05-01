package org.lushplugins.rewardsapi.command;

import me.dave.chatcolorhandler.ChatColorHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.lushlib.command.SubCommand;
import org.lushplugins.rewardsapi.RewardsAPIPlugin;

public class ReloadSubCommand extends SubCommand {

    public ReloadSubCommand() {
        super("reload");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try {
            RewardsAPIPlugin.getInstance().getConfigManager().reload();
            ChatColorHandler.sendMessage(sender, "&#66b04fSuccessfully reloaded RewardsAPI");
        } catch (Exception e) {
            ChatColorHandler.sendMessage(sender, "&#ff6969Something went wrong whilst reloading the config - check the console for errors.");
        }

        return true;
    }
}
