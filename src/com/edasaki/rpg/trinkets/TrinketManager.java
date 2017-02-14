package com.edasaki.rpg.trinkets;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftInventoryCrafting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.edasaki.core.menus.MenuManager;
import com.edasaki.core.utils.RParticles;
import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;

import de.slikey.effectlib.util.ParticleEffect;

public class TrinketManager extends AbstractManagerRPG {

    public TrinketManager(SakiRPG plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {

    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        event.setCancelled(true);
        try {
            PlayerDataRPG pd = plugin.getPD(event.getPlayer());
            if (pd != null && pd.isValid()) {
                if (pd.trinket != null) {
                    if (System.currentTimeMillis() > pd.nextTrinketCast) {
                        pd.sendMessage(ChatColor.GRAY + ">> " + ChatColor.GREEN + "You cast your Trinket Spell, " + ChatColor.YELLOW + pd.trinket.spell.getName() + ChatColor.GREEN + ".");
                        RParticles.showWithOffsetPositiveY(ParticleEffect.VILLAGER_HAPPY, event.getPlayer().getLocation().add(0, 1, 0), 1.0, 10);
                        pd.trinket.spell.cast(event.getPlayer(), pd);
                        pd.nextTrinketCast = System.currentTimeMillis() + pd.trinket.spell.getCooldown() * 1000;
                    } else {
                        double diff = ((double) pd.nextTrinketCast - System.currentTimeMillis()) / 1000.0;
                        pd.sendMessage(ChatColor.RED + "You cannot use your Trinket Spell for another " + String.format("%.1f", diff) + " seconds!");
                    }
                } else {
                    pd.sendMessage(ChatColor.RED + "You do not have a Trinket equipped! Use " + ChatColor.YELLOW + "/trinket" + ChatColor.RED + " to equip one!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        event.setCancelled(true);
    }

    public static void showMenu(Player p, final PlayerDataRPG pd) {
        p.closeInventory();
        ArrayList<Object[]> obj = new ArrayList<Object[]>();
        obj.add(new Object[] {
                0,
                0,
                Material.BOOK,
                ChatColor.AQUA + "Trinket Guide",
                new Object[] {
                        ChatColor.RED,
                        "Please read all of this! It's important.",
                        null,
                        "",
                        ChatColor.DARK_AQUA,
                        "A trinket is a secondary item that gives you extra stats and a special Trinket Spell.",
                        null,
                        "",
                        ChatColor.YELLOW,
                        "Trinket Spells are cast by pressing your \"Swap Item\" button, which is " + ChatColor.GOLD + ChatColor.BOLD + "F" + ChatColor.YELLOW + " by default.",
                        null,
                        "",
                        ChatColor.BLUE,
                        "You can switch between trinkets by opening this Trinket Manager.",
                        null,
                        "",
                        ChatColor.LIGHT_PURPLE,
                        "The trinket you use will define the way you play the game!",
                        null,
                        "",
                        ChatColor.AQUA,
                        "The trinket you equip will gain EXP as you play. Trinkets get stronger as they level up and can grow to level 10.",

                },
                new Runnable() {
                    public void run() {

                    }
                }
        });
        obj.add(new Object[] {
                0,
                2,
                Material.BARRIER,
                ChatColor.RED + "Unequip Trinket",
                new Object[] {
                        ChatColor.GRAY,
                        "Click to unequip your trinket",
                        null,
                        "",
                        ChatColor.DARK_AQUA,
                        "You only benefit from Trinkets while one is equipped!",
                        null,
                        "",
                        ChatColor.YELLOW,
                        "You should always have a Trinket equipped in combat.",

                },
                new Runnable() {
                    public void run() {
                        if (pd != null && pd.isValid()) {
                            pd.trinket = null;
                            pd.updateEquipmentStats();
                            pd.getPlayer().closeInventory();
                            pd.sendMessage(ChatColor.GREEN + "You unequipped your trinket.");
                        }
                    }
                }
        });
        if (pd.trinket == null) {
            obj.add(new Object[] {
                    0,
                    4,
                    Material.SPECTRAL_ARROW,
                    ChatColor.RED + "No Trinket Equipped",
                    new Object[] {
                            ChatColor.GRAY,
                            "Click on any trinket below to equip it.",
                            null,
                            "",
                            ChatColor.YELLOW,
                            "Trinkets give useful stats and a special Trinket Spell.",

                    },
                    new Runnable() {
                        public void run() {

                        }
                    }
            });
        } else {
            ItemStack item = pd.trinket.getDisplayItem(pd);
            obj.add(new Object[] {
                    0,
                    4,
                    Material.SPECTRAL_ARROW,
                    ChatColor.YELLOW + "Current Trinket: " + item.getItemMeta().getDisplayName(),
                    item.getItemMeta().getLore(),
                    new Runnable() {
                        public void run() {
                        }
                    }
            });
        }
        int row = 1;
        int col = 0;
        for (final Trinket trinket : Trinket.values()) {
            ItemStack item = trinket.getDisplayItem(pd);
            obj.add(new Object[] {
                    row,
                    col,
                    item,
                    item.getItemMeta().getDisplayName(),
                    item.getItemMeta().getLore(),
                    new Runnable() {
                        public void run() {
                            if (pd != null && pd.isValid()) {
                                pd.trinket = trinket;
                                pd.updateEquipmentStats();
                                pd.getPlayer().closeInventory();
                                pd.sendMessage(ChatColor.GREEN + "You equipped the " + ChatColor.AQUA + trinket.name + " Trinket" + ChatColor.GREEN + "!");
                            }
                        }
                    }
            });
            col++;
            if (col >= 9) {
                row++;
                col = 0;
            }
        }
        Inventory menu = MenuManager.createMenu(p, "Trinket Manager", row + 1 >= 6 ? 6 : row + 1, obj.toArray(new Object[obj.size()][]));
        p.openInventory(menu);
    }

    @EventHandler
    public void onPlayerClickTrinket(InventoryClickEvent event) {
        if (event.getInventory() instanceof CraftInventoryCrafting && event.getRawSlot() == 45) { //offhand slot
            event.setCancelled(true);
            if (event.getWhoClicked().getGameMode() != GameMode.ADVENTURE && event.getWhoClicked().getGameMode() != GameMode.SURVIVAL) {
                event.getWhoClicked().getInventory().setItemInOffHand(null);
            }
            if (event.getWhoClicked() instanceof Player)
                ((Player) event.getWhoClicked()).updateInventory();
        }
    }

    @EventHandler
    public void onPlayerClickTrinket2(InventoryDragEvent event) {
        if (event.getRawSlots().contains(45)) {
            event.setCancelled(true);
            if (event.getWhoClicked().getGameMode() != GameMode.ADVENTURE && event.getWhoClicked().getGameMode() != GameMode.SURVIVAL) {
                event.getWhoClicked().getInventory().setItemInOffHand(null);
            }
            if (event.getWhoClicked() instanceof Player)
                ((Player) event.getWhoClicked()).updateInventory();
        }
    }

}
