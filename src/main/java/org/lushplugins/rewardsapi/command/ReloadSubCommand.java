package org.lushplugins.rewardsapi.command;

import me.dave.chatcolorhandler.ChatColorHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.lushlib.command.SubCommand;
import org.lushplugins.rewardsapi.RewardsAPIPlugin;

import java.util.logging.Level;

public class ReloadSubCommand extends SubCommand {

    public ReloadSubCommand() {
        super("reload");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try {
            RewardsAPIPlugin.getInstance().getConfigManager().reload();
            ChatColorHandler.sendMessage(sender, "&#b7faa2Successfully reloaded RewardsAPI");
        } catch (Exception e) {
            RewardsAPIPlugin.getInstance().getLogger().log(Level.SEVERE, "Failed to reload config:", e);
            ChatColorHandler.sendMessage(sender, "&#ff6969Something went wrong whilst reloading the config - check the console for errors.");
        }

        return true;
    }
}
