package de.hg_practice.core.util.commands;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class CommandNotFoundException extends RuntimeException {
    public CommandNotFoundException() {
    }

    public CommandNotFoundException(String commandName, Plugin plugin) {
        super(getMessage(commandName, plugin));
    }

    public CommandNotFoundException(String commandName, Plugin plugin, Throwable cause) {
        super(getMessage(commandName, plugin), cause);
    }

    public CommandNotFoundException(Throwable cause) {
        super(cause);
    }

    @NotNull
    private static String getMessage(String commandName, Plugin plugin) {
        return "Could not find command " + commandName + " in plugin description file of plugin " + plugin.getName();
    }
}
