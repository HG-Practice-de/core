package de.hg_practice.core.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * @author earomc
 * Created on Februar 17, 2022 | 00:54:45
 * ʕっ•ᴥ•ʔっ
 */

public class PlayerStateSnapshot {

    private final ItemStack[] contents;
    private final ItemStack[] armorContents;
    private final int xpLevel;


    public PlayerStateSnapshot(Player player) {
        PlayerInventory inventory = player.getInventory();
        this.contents = inventory.getContents().clone();
        this.armorContents = inventory.getArmorContents().clone();
        this.xpLevel = player.getLevel();
    }

    public void load(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.setContents(contents);
        inventory.setArmorContents(armorContents);
        player.setLevel(xpLevel);
        player.updateInventory();
    }

}
