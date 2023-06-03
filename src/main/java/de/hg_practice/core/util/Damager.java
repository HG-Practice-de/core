package de.hg_practice.core.util;

import de.hg_practice.core.util.timer.BukkitCountingTimer;
import org.bukkit.entity.Damageable;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;

/**
 * A class that repeatedly deals damage in a set interval (periodTicks) given in Minecraft ticks (20 ticks = 1 second).
 * You can also set the amount of damage cycles after which the damager will stop on its own (durationCycles).
 */
public class Damager extends BukkitCountingTimer {

    private final Set<Damageable> entities = new HashSet<>();

    /**
     * Creates a new Damager instance.
     * @param plugin The plugin to run the internal BukkitTask on.
     * @param periodTicks the time interval between each cycle in Minecraft ticks
     * @param damage amount of damage to deal each cycle
     * @param durationCycles amount of damage cycles after which the damager will stop on its own. Can be set to -1 to run indefinitely (or until stopped with {@link Damager#stop()}).
     */
    public Damager(Plugin plugin, long periodTicks, double damage, long durationCycles) {
        super(plugin, periodTicks, 1);
        this.beforeCount = value -> entities.forEach(damageable -> damageable.damage(damage));
        // if durationCycles is set
        setRunningCondition(durationCycles == -1 ? () -> true : () -> getCounter() < durationCycles);
    }

    /**
     * Creates a new Damager instance of a damager that runs indefinitely (or until stopped with {@link Damager#stop()}).
     * @param plugin The plugin to run the internal BukkitTask on.
     * @param periodTicks the time interval between each cycle in Minecraft ticks.
     * @param damage amount of damage to deal each cycle.
     */
    public Damager(Plugin plugin, long periodTicks, double damage) {
        this(plugin, periodTicks, damage, -1);
    }

    /**
     * Adds an entity to be damaged every cycle.
     * @param damageable The entity to add.
     */
    public void addEntity(Damageable damageable) {
        entities.add(damageable);
    }

    /**
     * Removed an entity from being damaged every cycle.
     * @param damageable The entity to remove.
     */
    public void removeEntity(Damageable damageable) {
        entities.remove(damageable);
    }
}
