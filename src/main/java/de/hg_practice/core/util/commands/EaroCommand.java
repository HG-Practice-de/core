package de.hg_practice.core.util.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface EaroCommand extends CommandExecutor, TabCompleter {

    @Nullable
    @Override
    default List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    /**
     * If you use the EaroCommand interface to register a command with any of the {@link Commands} util methods or
     * the {@link CommandRegisteringHelper}, this will be used as the name to register it.
     * @return The command's name that you use to execute it.
     */
    @NotNull
    String getName();
}
