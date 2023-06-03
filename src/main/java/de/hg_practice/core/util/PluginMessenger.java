package de.hg_practice.core.util;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Queue;

public class PluginMessenger implements PluginMessageListener {
    public static final String BUNGEE_CHANNEL = "BungeeCord";

    private final Queue<String> responseList;
    private final Plugin plugin;

    public PluginMessenger(Plugin plugin) {
        // EvictingQueue is a FIFO queue. Meaning the First object you put In, is the First one you get Out.
        this.responseList = EvictingQueue.create(20);
        this.plugin = plugin;
        Messenger messenger = this.plugin.getServer().getMessenger();
        messenger.registerOutgoingPluginChannel(plugin, PluginMessenger.BUNGEE_CHANNEL);
        messenger.registerIncomingPluginChannel(plugin, PluginMessenger.BUNGEE_CHANNEL, this);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] messageInBytes) {
        if (!channel.equals(BUNGEE_CHANNEL)) return;
        ByteArrayDataInput in = ByteStreams.newDataInput(messageInBytes);
        String response = in.readUTF();
        responseList.add(response);
    }

    public void sendPluginMessage(byte[] bytes) {
        sendPluginMessage(getFirstPlayer(), bytes);
    }

    public void sendPluginMessage(Player player, byte[] bytes) {
        player.sendPluginMessage(plugin, BUNGEE_CHANNEL, bytes);
    }

    public void sendPluginMessage(Player player, String ... message) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        for (String s : message) {
            out.writeUTF(s);
        }
        sendPluginMessage(player, out.toByteArray());
    }
    public void sendPluginMessage(String ... message) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        for (String s : message) {
            out.writeUTF(s);
        }
        sendPluginMessage(getFirstPlayer(), out.toByteArray());
    }

    public void movePlayerToServer(Player player, String server) {
        sendPluginMessage(player, "Connect", server);
    }

    public void movePlayerToServer(String playerName, String server) {
        sendPluginMessage(getFirstPlayer(), "ConnectOther", playerName, server);
    }

    public String getLastResponse() {
        return responseList.poll();
    }

    //only used as dummy for messages where you don't care about the player
    private Player getFirstPlayer() {
        return Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
    }

    public void disable() {
        Messenger messenger = plugin.getServer().getMessenger();
        messenger.unregisterOutgoingPluginChannel(plugin, PluginMessenger.BUNGEE_CHANNEL);
        messenger.unregisterIncomingPluginChannel(plugin, PluginMessenger.BUNGEE_CHANNEL, this);
    }
}
