package de.hg_practice.core.util.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * @author earomc
 * Created on Dezember 12, 2021 | 00:06:04
 * ʕっ•ᴥ•ʔっ
 */

public class Commands {

    public static <T extends CommandExecutor & TabCompleter> void registerExecutorAndCompleter(@NotNull String name, @NotNull T t, @NotNull JavaPlugin plugin) {
        PluginCommand command = ensureCommandExists(name, plugin);
        command.setExecutor(t);
        command.setTabCompleter(t);
    }

    public static void registerExecutor(@NotNull String name, @NotNull CommandExecutor executor, @NotNull JavaPlugin plugin) {
        ensureCommandExists(name, plugin).setExecutor(executor);
    }

    public static void registerTabCompleter(@NotNull String name, @NotNull TabCompleter completer, @NotNull JavaPlugin plugin) {
        ensureCommandExists(name, plugin).setTabCompleter(completer);
    }

    public static void registerCommand(@NotNull EaroCommand command, @NotNull JavaPlugin plugin) {
        registerExecutorAndCompleter(command.getName(), command, plugin);
    }

    @NotNull
    public static PluginCommand ensureCommandExists(String name, JavaPlugin plugin) {
        PluginCommand command = plugin.getCommand(name);
        if (command == null) throw new CommandNotFoundException(name, plugin);
        return command;
    }

}
