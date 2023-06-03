package de.hg_practice.core.util.timer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

import java.util.function.BooleanSupplier;
import java.util.function.LongConsumer;

public class BukkitCountingTimer extends AbstractTimer implements CountingTimer {

    private final Plugin plugin;
    private BukkitTask countingTask;
    protected BooleanSupplier runningCondition = () -> true;

    @Nullable
    protected Runnable onStop;
    @Nullable
    protected Runnable onStart;
    @Nullable
    protected LongConsumer beforeCount;
    private long counter;

    private final long delayTicks;
    private final long countStep;
    private final long periodTicks;

    public BukkitCountingTimer(Plugin plugin, long periodTicks, long delayTicks, long countStep) {
        this.plugin = plugin;
        this.periodTicks = periodTicks;
        this.delayTicks = delayTicks;
        this.countStep = countStep;
    }

    public BukkitCountingTimer(Plugin plugin, long periodTicks, long countStep) {
        this(plugin, periodTicks, periodTicks, countStep);
    }

    @Override
    protected void doStart() {
        if (onStart != null) onStart.run();
        this.countingTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (runningCondition.getAsBoolean()) {
                beforeCount();
                count();
            } else {
                stop();
            }
        }, this.delayTicks, this.periodTicks);
    }

    @Override
    protected void doStop() {
        if (onStop != null) onStop.run();
        countingTask.cancel();
    }

    protected void count() {
        this.counter += countStep;
    }

    /**
     * Runs the beforeCount LongConsumer with the current counter.
     */
    @Override
    public void beforeCount() {
        if (beforeCount != null) beforeCount.accept(counter);
    }

    /**
     * Sets what should happen when the timer stops.
     * @param onStop a runnable with the action to execute.
     */
    public void setOnStop(@Nullable Runnable onStop) {
        this.onStop = onStop;
    }

    /**
     * Sets what should happen when the timer starts.
     * @param onStart a runnable with the action to execute.
     */
    public void setOnStart(@Nullable Runnable onStart) {
        this.onStart = onStart;
    }

    /**
     * Runs the beforeCount runnable.
     */
    public void setBeforeCount(@Nullable LongConsumer beforeCount) {
        this.beforeCount = beforeCount;
    }

    /**
     * Sets the condition under which this timer can run. If the condition returns false, the timer will stop.
     * @param runningCondition The condition as BooleanSupplier
     */
    public void setRunningCondition(@Nullable BooleanSupplier runningCondition) {
        if (runningCondition == null) {
            this.runningCondition = () -> true;
            return;
        }
        this.runningCondition = runningCondition;
    }

    /**
     * Inversion of setRunningCondition: <p>
     * Sets the condition under which this timer will stop. If the condition returns true, the timer will stop.
     * @param stoppingCondition The condition as BooleanSupplier
     */
    public void setStoppingCondition(@Nullable BooleanSupplier stoppingCondition) {
        if (stoppingCondition == null) {
            setRunningCondition(null);
            return;
        }
        setRunningCondition(() -> !stoppingCondition.getAsBoolean());
    }

    public void setCounter(long counter) {
        this.counter = counter;
    }

    @Override
    public long getCounter() {
        return counter;
    }

    /**
     * Resets the timer to 0
     */
    @Override
    public void reset() {
        setCounter(0);
    }
}


