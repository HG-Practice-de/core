package de.hg_practice.core.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/**
 * @author earomc
 * Created on Februar 21, 2022 | 13:51:04
 * ʕっ•ᴥ•ʔっ
 */

public class PlayerUtil {

    public static void setFlyingAndAllow(Player player, boolean b) {
        player.setAllowFlight(b);
        player.setFlying(b);
    }
    @SuppressWarnings("deprecation")
    public static boolean isHitCritical(Player player) {
        Material blockType = player.getLocation().getBlock().getType();
        return player.getFallDistance() > 0.0F && !player.isOnGround() &&
                blockType != Material.LADDER &&
                blockType != Material.VINE &&
                !player.hasPotionEffect(PotionEffectType.BLINDNESS) &&
                player.getVehicle() == null;
    }
}
