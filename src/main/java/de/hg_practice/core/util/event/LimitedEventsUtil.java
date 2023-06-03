package de.hg_practice.core.util.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;

import java.util.function.BooleanSupplier;

/**
 * Like EventsUtil, but has one "big" condition that limits its "scope" of execution.
 */
public class LimitedEventsUtil extends EventsUtil {

    private final BooleanSupplier condition;

    public LimitedEventsUtil(Plugin plugin, Listener listener, BooleanSupplier condition) {
        super(plugin, listener);
        this.condition = condition;
    }

    public LimitedEventsUtil(Plugin plugin, BooleanSupplier condition) {
        this(plugin, null, condition);
    }

    // only overriding this base method because every other method in EventsUtil is going back to referencing this method.

    @Override
    protected void registerEventExecutor(Class<? extends Event> eventClass, EventPriority priority, EventExecutor executor) {
        Bukkit.getPluginManager().registerEvent(eventClass, listener, priority, conditionWrap(executor), plugin);
    }

    /**
     * Wraps this the given executor in the condition that is defined in this class.
     * The wrapped EventExecutor can only be executed if the condition returns true.
     * @param executor The EventExecutor to wrap with the condition.
     * @return The wrapped EventExecutor
     */
    private EventExecutor conditionWrap(EventExecutor executor) {
        return (listener1, event) -> {
            if (condition.getAsBoolean()) executor.execute(listener1, event);
        };
    }

    public BooleanSupplier getCondition() {
        return condition;
    }
}
