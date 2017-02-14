package com.edasaki.rpg.particles;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.edasaki.core.menus.MenuManager;
import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;

public class ParticleManager extends AbstractManagerRPG {

    protected static EffectManager em;

    public ParticleManager(SakiRPG plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {
        em = new EffectManager(plugin);
        File dir = new File(plugin.getDataFolder() + "/effects/");
        if(!dir.exists())
            dir.mkdirs();
        for(File f : dir.listFiles()) {
            f.delete();
        }
        System.out.println("Cleared cached effects.");
    }

    public static void cleanup() {
        em.dispose();
    }

    public static void showMenu(Player p, PlayerDataRPG pd) {
        Inventory i = MenuManager.createMenu(p, "Special Effects", 6, new Object[][] {
                {
                        0,
                        4,
                        Material.NETHER_STAR,
                        ChatColor.GOLD + "Special Effects",
                        new Object[] {
                                ChatColor.WHITE,
                                "Special Effects are particle effects to make your character look super fancy!",
                                null,
                                "",
                                ChatColor.AQUA,
                                "You can only have 1 Special Effect active at once. Your Special Effect will activate when you stand on the same block for 3 seconds.",
                        },
                        new Runnable() {
                            public void run() {

                            }
                        }
                },
                {
                        0,
                        8,
                        Material.CHEST,
                        ChatColor.YELLOW + "Where do I get Special Effects?",
                        new Object[] {
                                ChatColor.AQUA,
                                "Special Effects can be obtained from Costume Crates or special events.",
                                null,
                                "",
                                ChatColor.WHITE,
                                "There are four tiers of Special Effects: Common, Rare, Epic, and Legendary.",
                                null,
                                "",
                                ChatColor.GOLD,
                                "The coolest effects are also the rarest! See if you can collect them all."
                        },
                        new Runnable() {
                            public void run() {

                            }
                        }
                },
                {
                        0,
                        0,
                        Material.BARRIER,
                        ChatColor.RED + "Disable Special Effect",
                        new Object[] {
                                ChatColor.GRAY,
                                "Click here to disable your special effect.",
                        },
                        new Runnable() {
                            public void run() {
                                if (activeEffect.containsKey(pd.getUUID())) {
                                    activeEffect.remove(pd.getUUID()).cancel();
                                }
                                pd.activeEffect = null;
                                pd.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "You disabled your special effects.");
                                p.closeInventory();
                            }
                        }
                }
        });

        int commonIndex = 0;
        int rareIndex = 0;
        int epicIndex = 0;
        int legendaryIndex = 0;
        for (EffectName en : EffectName.values()) {
            int col = 0;
            int row = 2;
            switch (en.rarity) {
                default:
                case COMMON:
                    row = 2;
                    col = commonIndex++;
                    break;
                case RARE:
                    row = 3;
                    col = rareIndex++;
                    break;
                case EPIC:
                    row = 4;
                    col = epicIndex++;
                    break;
                case LEGENDARY:
                    row = 5;
                    col = legendaryIndex++;
                    break;
            }
            ItemStack item;
            if(pd.activeEffect != null && en == pd.activeEffect) {
                item = new ItemStack(Material.WOOL, 1, DyeColor.LIME.getWoolData());
            } else {
                item = new ItemStack(Material.WOOL, 1, DyeColor.LIGHT_BLUE.getWoolData());
            }
            MenuManager.modifyMenu(p, i, new Object[][] {
                    {
                            row,
                            col,
                            item,
                            ChatColor.BLUE + en.name,
                            new Object[] {
                                    ChatColor.RESET,
                                    en.rarity.display,
                                    null,
                                    "",
                                    ChatColor.GREEN,
                                    en.desc,
                                    null,
                                    "",
                                    ChatColor.GRAY,
                                    "You have unlocked this effect."
                            },
                            new Runnable() {
                                public void run() {
                                    if (pd.isValid()) {
                                        if (activeEffect.containsKey(pd.getUUID())) {
                                            activeEffect.remove(pd.getUUID()).cancel();
                                        }
                                        pd.activeEffect = en;
                                        pd.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "You set your active Special Effect to " + ChatColor.YELLOW + en.name + ChatColor.GREEN + ".");
                                        p.closeInventory();
                                    }
                                }
                            }
                    }
            });
        }
        p.openInventory(i);
    }

    private static HashMap<UUID, Location> lastLoc = new HashMap<UUID, Location>();
    private static HashMap<UUID, Integer> stillCount = new HashMap<UUID, Integer>();
    private static HashMap<UUID, Effect> activeEffect = new HashMap<UUID, Effect>();

    private static boolean isSameBlock(Location a, Location b) {
        return a.getBlockX() == b.getBlockX() && a.getBlockY() == b.getBlockY() && a.getBlockZ() == b.getBlockZ();
    }

    public static void dispose(UUID uuid) {
        if (activeEffect.containsKey(uuid)) {
            activeEffect.remove(uuid).cancel();
        }
    }

    public static void tick(Player p, PlayerDataRPG pd) {
        if (p == null || pd.activeEffect == null)
            return;
        if (lastLoc.containsKey(p.getUniqueId()) && isSameBlock(lastLoc.get(p.getUniqueId()), p.getLocation())) {
            if (stillCount.containsKey(p.getUniqueId())) {
                stillCount.put(p.getUniqueId(), stillCount.get(p.getUniqueId()) + 1);
            } else {
                stillCount.put(p.getUniqueId(), 1);
            }
            if (isStill(p)) {
                if (!activeEffect.containsKey(p.getUniqueId())) {
                    Effect temp = EffectFactory.getEffect(pd.activeEffect, p);
                    if(temp == null)
                        return;
                    temp.setEntity(p);
                    temp.start();
                    activeEffect.put(p.getUniqueId(), temp);
                }
            }
        } else {
            if (activeEffect.containsKey(p.getUniqueId())) {
                activeEffect.remove(p.getUniqueId()).cancel();
            }
            lastLoc.put(p.getUniqueId(), p.getLocation());
            stillCount.put(p.getUniqueId(), 0);
        }
    }

    public static boolean isStill(Player p) {
        if (!stillCount.containsKey(p.getUniqueId()))
            return false;
        return stillCount.get(p.getUniqueId()) >= 6; // # of half seconds
    }

}
