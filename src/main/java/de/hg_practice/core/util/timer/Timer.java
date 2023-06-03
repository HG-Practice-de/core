package de.hg_practice.core.util.timer;

import org.bukkit.plugin.Plugin;

public interface Timer {

    /**
     * Tries to start the timer.
     *
     * @return If it hasn't been running if will start it and return true. Else false.
     */
    boolean start();

    /**
     * Tries to stop the timer.
     *
     * @return If it has been running if will stop it and return true. Else false.
     */
    boolean stop();

    /**
     *
     * @return True if running, false if not.
     */
    boolean isRunning();

    /**
     * Logical NOT equivalent of {@link Timer#isRunning()}.
     * @return False if running, true if not.
     */
    default boolean isStopped() {
        return !isRunning();
    }

    /**
     * Resets the timer.
     */
    default void reset() {}

    static BukkitCountingTimer newCountdownTimer(Plugin plugin, long periodTicks, long delayTicks) {
        return new BukkitCountingTimer(plugin, periodTicks, delayTicks, -1);
    }

    static BukkitCountingTimer newCountdownTimer(Plugin plugin, long periodTicks) {
        return new BukkitCountingTimer(plugin, periodTicks, periodTicks, -1);
    }

    static BukkitCountingTimer newCountUpTimer(Plugin plugin, long periodTicks, long delayTicks) {
        return new BukkitCountingTimer(plugin, periodTicks, delayTicks, +1);
    }

    static BukkitCountingTimer newCountUpTimer(Plugin plugin, long periodTicks) {
        return new BukkitCountingTimer(plugin, periodTicks, periodTicks, +1);
    }

}