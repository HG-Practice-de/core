package de.hg_practice.core.util.event;

import org.bukkit.Bukkit;
import org.bukkit.event.*;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

/**
 * This class allows you to easily register individual event handlers / event executors
 * with single method calls.
 */
/*
   Think about adding a way to add executors without listener component to make it possible
   to register classic event handler methods using method reference.
   Like an EventConsumer :)
 */
public class EventsUtil {

    protected final Plugin plugin;
    protected final Listener listener;

    public EventsUtil(Plugin plugin, Listener listener) {
        this.plugin = plugin;
        if (listener == null) {
            this.listener = new Listener() {};
            /* Empty listener to register shit to.
            I mean Listener is just a marker interface anyway.*/
        } else this.listener = listener;
    }

    public EventsUtil(Plugin plugin) {
        this(plugin, null);
    }

    public static <T extends Event> void registerGenericEventExecutor(Class<T> eventClass, Listener listener, EventPriority priority, GenericEventExecutor<T> executor, Plugin plugin, boolean ignoreCancelled) {
        Bukkit.getPluginManager().registerEvent(eventClass,
                listener,
                priority,
                executor.unwrap(eventClass),
                plugin,
                ignoreCancelled);
    }

    public static <T extends Event> void registerGenericEventExecutor(Class<T> eventClass, Listener listener, EventPriority priority, GenericEventExecutor<T> executor, Plugin plugin) {
        Bukkit.getPluginManager().registerEvent(eventClass,
                listener,
                priority,
                executor.unwrap(eventClass),
                plugin);
    }


    // ----------- canceling events -------------

    // ----------- Bulk canceling -------------

    /**
     * Registers EventExecutors with the given {@link EventPriority} that automatically cancel the given events.
     * Use carefully: Cancels the events <strong>globally</strong>.
     *
     * @param priority     The event priority: {@link EventPriority}.
     * @param cancellableClasses The event classes to register as varargs. Example: {@literal BlockPlaceEvent.class, BlockBreakEvent.class}
     */
    @SafeVarargs
    public final void cancelEvents(EventPriority priority, Class<? extends Cancellable>... cancellableClasses) {
        for (Class<? extends Cancellable> cancellableClass : cancellableClasses) {
            cancelEvent(cancellableClass, priority);
        }
    }

    /**
     * Registers EventExecutors that automatically cancel the given events.
     * Use carefully: Cancels the events <strong>globally</strong>.
     *
     * @param cancellableClass The event classes to register as varargs. Example: {@literal BlockPlaceEvent.class, BlockBreakEvent.class}
     */
    @SafeVarargs
    public final  void cancelEvents(Class<? extends Cancellable> ... cancellableClass) {
        for (Class<? extends Cancellable> eventClass : cancellableClass) {
            cancelEvent(eventClass);
        }
    }

    // ----------- Single cancelling -------------

    /**
     * Registers an EventExecutor that automatically cancels the given event.
     * Use carefully: Cancels an event <strong>globally</strong>!
     *
     * @param cancellableClass A cancellable event class to register. Example: {@literal PlayerDropItemEvent.class}
     */
    public void cancelEvent(Class<? extends Cancellable> cancellableClass) {
        cancelEvent(cancellableClass, EventPriority.NORMAL);
    }

    // ----------- with priority  -------------
    // cancellable base method

    /**
     * Registers an EventExecutor with the given {@link EventPriority} that automatically cancels the given event.
     *
     * @param cancellableClass A cancellable event class to register. Example: {@literal PlayerDropItemEvent.class}
     * @param priority   The event priority: {@link EventPriority}.
     */
    public void cancelEvent(Class<? extends Cancellable> cancellableClass, EventPriority priority) {
        Class<? extends Event> eventClass = ensureCancellableIsEventClass(cancellableClass);
        registerEventExecutor(eventClass, priority, getEventCanceller());
    }


    // -------- Event cancelling with BooleanSupplier conditions ---------

    /**
     * Registers an EventExecutor that automatically cancels the event under the given condition.
     *
     * @param eventClass A cancellable event class to register. Example: {@literal PlayerDropItemEvent.class}
     * @param <T>        A cancellable event type.
     * @param condition  The condition under which the event is cancelled.
     */
    public <T extends Event & Cancellable> void cancelEvent(Class<T> eventClass, BooleanSupplier condition) {
        cancelEvent(eventClass, condition, EventPriority.NORMAL);
    }

    /**
     * Registers an EventExecutor that automatically cancels the event under the given condition.
     *
     * @param eventClass A cancellable event class to register. Example: {@literal PlayerDropItemEvent.class}
     * @param <T>        A cancellable event type.
     * @param condition  The condition under which the event is cancelled.
     * @param priority   The event priority: {@link EventPriority}.
     */
    public <T extends Event & Cancellable> void cancelEvent(Class<T> eventClass, BooleanSupplier condition, EventPriority priority) {
        registerEventExecutor(eventClass, priority, getEventCanceller(condition));
    }

    // -------- Event cancelling with Predicate conditions --------

    /**
     * Registers an EventExecutor that automatically cancels the event under the given condition.
     *
     * @param eventClass A cancellable event class to register. Example: {@literal PlayerDropItemEvent.class}
     * @param <T>        A cancellable event type.
     * @param condition  The condition under which the event is cancelled.
     */
    public <T extends Event & Cancellable> void cancelEvent(Class<T> eventClass, Predicate<T> condition) {
        cancelEvent(eventClass, condition, EventPriority.NORMAL);
    }

    /**
     * Registers an EventExecutor that automatically cancels the event under the given condition.
     *
     * @param eventClass The event class to register. Example: {@literal PlayerDropItemEvent.class}
     * @param <T>        A cancellable event type.
     * @param condition  The condition under which the event is cancelled.
     * @param priority   The event priority: {@link EventPriority}.
     */
    public <T extends Event & Cancellable> void cancelEvent(Class<T> eventClass, Predicate<T> condition, EventPriority priority) {
        registerEventExecutor(eventClass, priority, getEventCanceller(condition));
    }

    //------- checker methods ------

    protected static Class<? extends Event> ensureCancellableIsEventClass(Class<?extends Cancellable> cancellableClass) {
        if (!Event.class.isAssignableFrom(cancellableClass)) // is Event a super class of cancellable class? if not throw exception
            throw new IllegalArgumentException(cancellableClass.getName() + " has to extend org.bukkit.event.Event");
        //noinspection unchecked
        return (Class<? extends Event>) cancellableClass;
    }

    //--------- Event cancellers --------

    public EventExecutor getEventCanceller() {
        return (listener1, event) -> {
            if (event instanceof Cancellable) {
                ((Cancellable)event).setCancelled(true);
            }
        };
    }

    public EventExecutor getEventCanceller(BooleanSupplier condition) {
        return (listener, event) -> {
            if (event instanceof Cancellable) {
                Cancellable cancellable = (Cancellable) event;
                if (condition.getAsBoolean()) {
                    cancellable.setCancelled(true);
                }
            }
        };
    }

    public <T extends Event & Cancellable> EventExecutor getEventCanceller(Predicate<T> condition) {
        return (listener, event) -> {
            if (event instanceof Cancellable) {
                @SuppressWarnings("unchecked")
                T cancellableEvent = (T) event;
                if (condition.test(cancellableEvent)) {
                    cancellableEvent.setCancelled(true);
                }
            }
        };
    }

    // ------ Registering event executors ------

    // --- methods using default Bukkit EventExecutor ---

    // - with priority arg - Base method.
    // every method in this class that registers an event executor is going back to this method.

    /**
     * The base method of all event registering used by this class. This is the end point of every method that is registering anything within this class.
     *
     * @param eventClass The event class to register. Example: {@literal  PlayerDropItemEvent.class}
     * @param executor   An EventExecutor to execute the given event.
     * @param priority   The event priority: {@link EventPriority}.
     */
    protected void registerEventExecutor(Class<? extends Event> eventClass, EventPriority priority, EventExecutor executor) {
        Bukkit.getPluginManager().registerEvent(eventClass, listener, priority, executor, plugin);
    }

    // --- Methods using GenericEventExecutor ---

    // - with priority arg -

    /**
     * Use carefully: Registers an EventExecutor <strong>globally</strong>!
     *
     * @param eventClass The event class to register. Example: {@literal  PlayerDropItemEvent.class}
     * @param executor   An EventExecutor to execute the given event.
     * @param priority   The event priority: {@link EventPriority}.
     */
    public <T extends Event> void registerEventExecutor(Class<T> eventClass, EventPriority priority, GenericEventExecutor<T> executor) {
        registerEventExecutor(eventClass, priority, executor.unwrap(eventClass));
    }

    // - without priority arg -

    /**
     * Use carefully: Registers an EventExecutor <strong>globally</strong> with {@link EventPriority#NORMAL}!
     *
     * @param eventClass The event class to register. Example: {@literal  PlayerDropItemEvent.class}
     * @param executor   An EventExecutor to execute the given event.
     */
    public <T extends Event> void registerEventExecutor(Class<T> eventClass, GenericEventExecutor<T> executor) {
        registerEventExecutor(eventClass, EventPriority.NORMAL, executor.unwrap(eventClass));
    }

    // --- Methods using GenericEventExecutor and a Predicate condition ---

    // - with priority arg -

    /**
     * Use carefully: Registers an EventExecutor <strong>globally</strong>!
     *
     * @param eventClass The event class to register. Example: {@literal  PlayerDropItemEvent.class}
     * @param priority   The event priority: {@link EventPriority}.
     * @param executor   An EventExecutor to execute the given event.
     * @param condition  The condition under which the EventExecutor is run.
     * @param <T>        The event type that registered.
     */
    public <T extends Event> void registerEventExecutor(Class<T> eventClass, EventPriority priority, GenericEventExecutor<T> executor, Predicate<T> condition) {
        registerEventExecutor(eventClass, priority, ((GenericEventExecutor<T>) (listener, event) -> {
            if (condition.test(event)) {
                executor.execute(listener, event);
            }
        }).unwrap(eventClass));
    }

    // - without priority arg -

    /**
     * Use carefully: Registers an EventExecutor <strong>globally</strong>!
     *
     * @param eventClass The event class to register. Example: {@literal  PlayerDropItemEvent.class}
     * @param executor   An EventExecutor to execute the given event.
     * @param condition  The condition under which the EventExecutor is run.
     * @param <T>        The event type that registered.
     */
    public <T extends Event> void registerEventExecutor(Class<T> eventClass, GenericEventExecutor<T> executor, Predicate<T> condition) {
        registerEventExecutor(eventClass, EventPriority.NORMAL, executor, condition);
    }

    // --- Methods using GenericEventExecutor and a BooleanConsumer condition ---

    // - with priority arg -

    /**
     * Use carefully: Registers an EventExecutor <strong>globally</strong>!
     *
     * @param eventClass The event class to register. Example: {@literal  PlayerDropItemEvent.class}
     * @param priority   The event priority: {@link EventPriority}.
     * @param executor   An EventExecutor to execute the given event.
     * @param condition  The condition under which the EventExecutor is run.
     * @param <T>        The event type that registered.
     */
    public <T extends Event> void registerEventExecutor(Class<T> eventClass, EventPriority priority, GenericEventExecutor<T> executor, BooleanSupplier condition) {
        registerEventExecutor(eventClass, priority, ((GenericEventExecutor<T>) (listener, event) -> {
            if (condition.getAsBoolean()) {
                executor.execute(listener, event);
            }
        }).unwrap(eventClass));
    }

    // - without priority arg -

    /**
     * Use carefully: Registers an EventExecutor <strong>globally</strong>!
     *
     * @param eventClass The event class to register. Example: {@literal  PlayerDropItemEvent.class}
     * @param executor   An EventExecutor to execute the given event.
     * @param condition  The condition under which the EventExecutor is run.
     * @param <T>        The event type that registered.
     */
    public <T extends Event> void registerEventExecutor(Class<T> eventClass, GenericEventExecutor<T> executor, BooleanSupplier condition) {
        registerEventExecutor(eventClass, EventPriority.NORMAL, executor, condition);
    }

    // ------ END OF Registering event executors ------

    /**
     * Unregisters all events that are registered to the internal listener (The one specified in the constructor)
     */
    public void unregisterAll() {
        HandlerList.unregisterAll(listener);
    }

    // --- Getters ---

    /**
     * @return The plugin used to register all the event executors to.
     */
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * @return The listener used to register all the event executors to.
     */
    public Listener getListener() {
        return listener;
    }
}
