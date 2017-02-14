package com.edasaki.rpg.fun;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.edasaki.core.utils.RParticles;
import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.SakiRPG;

public class GrappleManager extends AbstractManagerRPG {

    public static ItemStack grapple;

    public GrappleManager(SakiRPG plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {
        ItemMeta im;
        grapple = new ItemStack(Material.FISHING_ROD);
        im = grapple.getItemMeta();
        im.addEnchant(Enchantment.DURABILITY, 5, true);
        im.setDisplayName(ChatColor.GOLD + "Grappling Hook");
        im.setLore(Arrays.asList(new String[] { ChatColor.YELLOW + "It's adventure time!", "", ChatColor.GREEN + "Right-click to shoot a hook.", ChatColor.GREEN + "Right-click again to grapple!" }));
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        grapple.setItemMeta(im);
    }

    @EventHandler
    public void onPlayerFishEvent(PlayerFishEvent event) {
        Fish hook = event.getHook();
        if (event.getState() != PlayerFishEvent.State.FISHING) {
            boolean isBlock = false;
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if (!RParticles.isAirlike(hook.getWorld().getBlockAt(hook.getLocation().getBlockX() + dx, hook.getLocation().getBlockY() + dy, hook.getLocation().getBlockZ() + dz))) {
                            isBlock = true;
                            break;
                        }
                    }
                }
            }
            try {
                event.getPlayer().getEquipment().getItemInMainHand().setDurability((short) 0);
            } catch (Exception e) {

            }
            if (isBlock) {
                pullEntityToLocation(event.getPlayer(), hook.getLocation());
            } else {
                event.getPlayer().sendMessage(ChatColor.RED + "Your grapple wasn't hooked on anything.");
            }
        }
    }

    public void pullEntityToLocation(Entity e, Location loc) {
        Location entityLoc = e.getLocation();
        entityLoc.setY(entityLoc.getY() + 0.5D);
        e.teleport(entityLoc);
        double g = -0.08D;
        if (loc.getWorld() != entityLoc.getWorld())
            return;
        double d = loc.distance(entityLoc);
        double t = d;
        double v_x = (1.0D + 0.07000000000000001D * t) * (loc.getX() - entityLoc.getX()) / t;
        double v_y = (1.0D + 0.03D * t) * (loc.getY() - entityLoc.getY()) / t - 0.5D * g * t;
        double v_z = (1.0D + 0.07000000000000001D * t) * (loc.getZ() - entityLoc.getZ()) / t;
        Vector v = e.getVelocity();
        v.setX(v_x);
        v.setY(v_y);
        v.setZ(v_z);
        e.setVelocity(v);
        e.setFallDistance(0f);
    }

    public static void supply(Player p) {
        p.getInventory().addItem(grapple);
        p.sendMessage(ChatColor.LIGHT_PURPLE + "Use your grapple to climb up mountains or walls!");
    }

}
