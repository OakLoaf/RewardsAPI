package org.lushplugins.rewardsapi.command;

import me.dave.chatcolorhandler.ChatColorHandler;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.lushlib.command.Command;
import org.lushplugins.rewardsapi.RewardsAPIPlugin;

public class RewardsAPICommand extends Command {

    public RewardsAPICommand() {
        super("rewardsapi");
        addSubCommand(new GiveSubCommand());
        addSubCommand(new ReloadSubCommand());
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        ChatColorHandler.sendMessage(sender, "&#a8e1ffYou are currently running &#58b1e0RewardsAPI &#a8e1ffversion &#58b1e0" + RewardsAPIPlugin.getInstance().getDescription().getVersion() + "&#a8e1ff.");
        return true;
    }
}
