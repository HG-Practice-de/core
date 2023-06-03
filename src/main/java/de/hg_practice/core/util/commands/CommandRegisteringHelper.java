package de.hg_practice.core.util.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class CommandRegisteringHelper {

    @NotNull
    private final JavaPlugin plugin;

    public CommandRegisteringHelper(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void registerExecutor(@NotNull String name, @NotNull CommandExecutor executor) {
        Commands.registerExecutor(name, executor, plugin);
    }

    public void registerTabCompleter(@NotNull String name, @NotNull TabCompleter completer) {
        Commands.registerTabCompleter(name, completer, plugin);
    }

    public <T extends CommandExecutor & TabCompleter> void registerExecutorAndCompleter(@NotNull String name, @NotNull T executorAndCompleter) {
        Commands.registerExecutorAndCompleter(name, executorAndCompleter, plugin);
    }

    public void registerCommand(EaroCommand command) {
        Commands.registerCommand(command, plugin);
    }

    public void registerCommands(EaroCommand ... commands) {
        for (EaroCommand command : commands) {
            registerCommand(command);
        }
    }

}
