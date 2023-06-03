package de.hg_practice.core.util;

import de.hg_practice.core.util.event.EaroListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;

/**
 * @author earomc
 * Created on Februar 22, 2022 | 13:54:06
 * ʕっ•ᴥ•ʔっ
 */

public class PlayerHider {

    private final HashSet<Player> hiddenPlayers;

    public PlayerHider(Plugin plugin) {
        hiddenPlayers = new HashSet<>();
        this.new EventListener().register(plugin);
    }

    public void hide(Player player) {
        hiddenPlayers.add(player);
        Bukkit.getOnlinePlayers().forEach(p -> p.hidePlayer(player));
    }

    public void show(Player player) {
        hiddenPlayers.remove(player);
        Bukkit.getOnlinePlayers().forEach(p -> p.showPlayer(player));
    }

    public boolean isHidden(Player player) {
        return hiddenPlayers.contains(player);
    }

    public class EventListener implements EaroListener {
        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {
            hiddenPlayers.forEach(event.getPlayer()::hidePlayer);
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {
            hiddenPlayers.remove(event.getPlayer());
        }
    }
}
