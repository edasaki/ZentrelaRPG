package com.edasaki.rpg.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.edasaki.core.utils.RFormatter;
import com.edasaki.core.utils.RParticles;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RScheduler.Halter;
import com.edasaki.core.utils.RSound;
import com.edasaki.core.utils.RTicks;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.players.HealType;
import com.edasaki.rpg.warps.WarpLocation;

import de.slikey.effectlib.util.ParticleEffect;

public class EtcItem extends RPGItem {

    public static SakiRPG plugin = null;

    @Override
    public ItemStack generate() {
        ItemStack item = new ItemStack(material);
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(name);
        ArrayList<String> lore = new ArrayList<String>();
        if (soulbound) {
            lore.add(ChatColor.RED + ChatColor.ITALIC.toString() + "Soulbound");
        }
        if (description.length() > 0)
            lore.addAll(RFormatter.stringToLore(description, ChatColor.GRAY));
        im.setLore(lore);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        item.setItemMeta(im);
        return item;
    }

    private static HashMap<UUID, Long> lastOldPlayer = new HashMap<UUID, Long>();
    private static HashMap<UUID, Long> lastVoteReward = new HashMap<UUID, Long>();
    private static HashMap<UUID, Long> lastManaPot = new HashMap<UUID, Long>();
    private static HashMap<UUID, HashMap<String, Long>> eatTimers = new HashMap<UUID, HashMap<String, Long>>();

    /*
     * This is run AFTER all items are loaded. Items that require special listeners should be
     * have their listeners within this method. For example, basically every item that has a
     * right-click effect should be in here. Potions, special effects, etc.
     */
    public static void postInitialize() {
        // Mark of the Veteran - usable once every 30 seconds, shoots fireworks
        ItemManager.registerItemRunnable("old_player", new ItemRunnable() {
            @Override
            public void run(Event e, Player p, PlayerDataRPG pd) {
                if (lastOldPlayer.containsKey(p.getUniqueId())) {
                    if (System.currentTimeMillis() - lastOldPlayer.get(p.getUniqueId()) < 30000) {
                        p.sendMessage("");
                        p.sendMessage(ChatColor.RED + "The item can only be used once every 30 seconds!");
                        p.sendMessage(ChatColor.RED + "You can use it again in " + String.format("%.1f", 30 - (System.currentTimeMillis() - lastOldPlayer.get(p.getUniqueId())) / 1000.0) + " seconds.");
                        p.sendMessage(ChatColor.RED + "P.S. Thanks for sticking around!");
                        return;
                    }
                }
                lastOldPlayer.put(p.getUniqueId(), System.currentTimeMillis());
                ArrayList<Location> locs = new ArrayList<Location>();
                locs.add(p.getLocation().add(-2, 0, 2));
                locs.add(p.getLocation().add(-2, 0, 1));
                locs.add(p.getLocation().add(-2, 0, 0));
                locs.add(p.getLocation().add(-2, 0, -1));
                locs.add(p.getLocation().add(-2, 0, -2));
                locs.add(p.getLocation().add(-1, 0, 2));
                locs.add(p.getLocation().add(-1, 0, -2));
                locs.add(p.getLocation().add(0, 0, 2));
                locs.add(p.getLocation().add(0, 0, -2));
                locs.add(p.getLocation().add(1, 0, 2));
                locs.add(p.getLocation().add(1, 0, -2));
                locs.add(p.getLocation().add(2, 0, 2));
                locs.add(p.getLocation().add(2, 0, 1));
                locs.add(p.getLocation().add(2, 0, 0));
                locs.add(p.getLocation().add(2, 0, -1));
                locs.add(p.getLocation().add(2, 0, -2));
                for (Location loc : locs)
                    RParticles.spawnRandomFirework(loc);
                p.sendMessage(ChatColor.GOLD + p.getName() + " is a true Zentrela veteran!");
                for (Entity p2 : p.getNearbyEntities(10, 10, 10))
                    if (p2 instanceof Player)
                        ((Player) p2).sendMessage(ChatColor.GOLD + p.getName() + " is a true Zentrela veteran!");
            }
        });

        ItemManager.registerItemRunnable("july_2014_voter", new ItemRunnable() {
            @Override
            public void run(Event event, final Player p, PlayerDataRPG pd) {
                if (lastVoteReward.containsKey(p.getUniqueId())) {
                    if (System.currentTimeMillis() - lastVoteReward.get(p.getUniqueId()) < 60000) {
                        p.sendMessage("");
                        p.sendMessage(ChatColor.RED + "This can only be used once every 60 seconds!");
                        p.sendMessage(ChatColor.RED + "You can use it again in " + String.format("%.1f", 60 - (System.currentTimeMillis() - lastVoteReward.get(p.getUniqueId())) / 1000.0) + " seconds.");
                        return;
                    }
                }
                lastVoteReward.put(p.getUniqueId(), System.currentTimeMillis());
                p.sendMessage(ChatColor.GREEN + "ay bay bay grats");
                Location loc = p.getLocation();
                RParticles.show(ParticleEffect.CRIT, loc);
                RParticles.show(ParticleEffect.FIREWORKS_SPARK, loc);
                RParticles.show(ParticleEffect.NOTE, loc);
                RParticles.show(ParticleEffect.SNOWBALL, loc);
                RParticles.show(ParticleEffect.HEART, loc);
                final Halter halter = new Halter();
                RScheduler.scheduleRepeating(plugin, new Runnable() {
                    int count = 0;

                    public void run() {
                        if (p.isValid())
                            RParticles.spawnRandomFirework(p.getLocation());
                        count++;
                        if (!p.isValid() || count >= 10)
                            halter.halt = true;
                    }
                }, RTicks.seconds(0.5), halter);
                p.sendMessage(ChatColor.GOLD + "majd777 - winner of the July 2014 voting contest!");
                for (Entity p2 : p.getNearbyEntities(10, 10, 10))
                    if (p2 instanceof Player)
                        ((Player) p2).sendMessage(ChatColor.GOLD + "Congratulations to majd777, winner of the July 2014 voting contest!");
            }
        });

        ItemManager.registerItemRunnable("hp_potion_1", new ItemRunnable() {
            @Override
            public void run(Event event, Player p, PlayerDataRPG pd) {
                healWithPotion((int) Math.ceil(pd.getCurrentMaxHP() * 0.1), "hp_potion_1", event, p);
            }
        });

        ItemManager.registerItemRunnable("hp_potion_2", new ItemRunnable() {
            @Override
            public void run(Event event, Player p, PlayerDataRPG pd) {
                healWithPotion((int) Math.ceil(pd.getCurrentMaxHP() * 0.15), "hp_potion_2", event, p);
            }
        });

        ItemManager.registerItemRunnable("hp_potion_3", new ItemRunnable() {
            @Override
            public void run(Event event, Player p, PlayerDataRPG pd) {
                healWithPotion((int) Math.ceil(pd.getCurrentMaxHP() * 0.2), "hp_potion_3", event, p);
            }
        });

        ItemManager.registerItemRunnable("hp_potion_4", new ItemRunnable() {
            @Override
            public void run(Event event, Player p, PlayerDataRPG pd) {
                healWithPotion((int) Math.ceil(pd.getCurrentMaxHP() * 0.35), "hp_potion_4", event, p);
            }
        });

        ItemManager.registerItemRunnable("hp_potion_5", new ItemRunnable() {
            @Override
            public void run(Event event, Player p, PlayerDataRPG pd) {
                healWithPotion((int) Math.ceil(pd.getCurrentMaxHP() * 0.5), "hp_potion_5", event, p);
            }
        });
        ItemManager.registerItemRunnable("melon", new ItemRunnable() {
            @Override
            public void run(Event event, Player p, PlayerDataRPG pd) {
                if (justAte(pd, "melon"))
                    return;
                takeOneItemInstant(p);
                pd.heal(80);
                RSound.playSound(p, Sound.ENTITY_GENERIC_EAT);
            }
        });

        ItemManager.registerItemRunnable("melon_slice", new ItemRunnable() {
            @Override
            public void run(Event event, Player p, PlayerDataRPG pd) {
                if (justAte(pd, "melon_slice"))
                    return;
                takeOneItemInstant(p);
                pd.heal(20);
                RSound.playSound(p, Sound.ENTITY_GENERIC_EAT);
            }
        });

        ItemManager.registerItemRunnable("roasted_chicken", new ItemRunnable() {
            @Override
            public void run(Event event, Player p, PlayerDataRPG pd) {
                if (justAte(pd, "roasted_chicken"))
                    return;
                takeOneItemInstant(p);
                pd.heal(250);
                RSound.playSound(p, Sound.ENTITY_GENERIC_EAT);
            }
        });
        ItemManager.registerItemRunnable("maru_milk", new ItemRunnable() {
            @Override
            public void run(Event event, Player p, PlayerDataRPG pd) {
                takeOneItemInstant(p);
                pd.heal(pd.getCurrentMaxHP());
                p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, RTicks.seconds(30), 1), false);
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, RTicks.seconds(30), 2), false);
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, RTicks.seconds(30), 0), false);
            }
        });

        ItemManager.registerItemRunnable("tortuga_ale", new ItemRunnable() {
            @Override
            public void run(Event event, Player p, PlayerDataRPG pd) {
                takeOneItemInstant(p);
                pd.heal(pd.getCurrentMaxHP());
                p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, RTicks.seconds(15), 1), false);
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, RTicks.seconds(15), 2), false);
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, RTicks.seconds(15), 0), false);
                pd.sendMessage(ChatColor.GRAY + "> " + "The ale tastes terrible, and now you feel a little tipsy.");
            }
        });

        ItemManager.registerItemRunnable("juicy_apple", new ItemRunnable() {
            @Override
            public void run(Event event, Player p, PlayerDataRPG pd) {
                if (justAte(pd, "juicy_apple"))
                    return;
                takeOneItemInstant(p);
                pd.heal(300);
                RSound.playSound(p, Sound.ENTITY_GENERIC_EAT);
            }
        });

        ItemManager.registerItemRunnable("rotten_apple", new ItemRunnable() {
            @Override
            public void run(Event event, Player p, PlayerDataRPG pd) {
                if (justAte(pd, "rotten_apple"))
                    return;
                takeOneItemInstant(p);
                pd.heal(300);
                if (Math.random() < 0.5) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, RTicks.seconds(10), 1), false);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, RTicks.seconds(10), 2), false);
                }
            }
        });

        ItemManager.registerItemRunnable("mana_pot_1", new ItemRunnable() {
            @Override
            public void run(Event event, Player p, PlayerDataRPG pd) {
                if (lastManaPot.containsKey(p.getUniqueId()) && System.currentTimeMillis() - lastManaPot.get(p.getUniqueId()) < 500)
                    return;
                lastManaPot.put(p.getUniqueId(), System.currentTimeMillis());
                if (pd.mana < PlayerDataRPG.MAX_MANA) {
                    takeOneItemInstant(p);
                    pd.recoverMana(2);
                }
            }
        });

        ItemManager.registerItemRunnable("mana_pot_2", new ItemRunnable() {
            @Override
            public void run(Event event, Player p, PlayerDataRPG pd) {
                if (lastManaPot.containsKey(p.getUniqueId()) && System.currentTimeMillis() - lastManaPot.get(p.getUniqueId()) < 500)
                    return;
                lastManaPot.put(p.getUniqueId(), System.currentTimeMillis());
                if (pd.mana < PlayerDataRPG.MAX_MANA) {
                    takeOneItemInstant(p);
                    pd.recoverMana(3);
                }
            }
        });

        ItemManager.registerItemRunnable("mana_pot_3", new ItemRunnable() {
            @Override
            public void run(Event event, Player p, PlayerDataRPG pd) {
                if (lastManaPot.containsKey(p.getUniqueId()) && System.currentTimeMillis() - lastManaPot.get(p.getUniqueId()) < 500)
                    return;
                lastManaPot.put(p.getUniqueId(), System.currentTimeMillis());
                if (pd.mana < PlayerDataRPG.MAX_MANA) {
                    takeOneItemInstant(p);
                    pd.recoverMana(4);
                }
            }
        });

        ItemManager.registerItemRunnable("warp_korwyn", new WarpItemRunnable(WarpLocation.KORWYN.getLocation(), ItemManager.itemIdentifierToRPGItemMap.get("warp_korwyn").generate()));
        ItemManager.registerItemRunnable("warp_lemia", new WarpItemRunnable(WarpLocation.OLD_LEMIA.getLocation(), ItemManager.itemIdentifierToRPGItemMap.get("warp_lemia").generate()));
        ItemManager.registerItemRunnable("warp_erlen", new WarpItemRunnable(WarpLocation.OLD_ERLEN.getLocation(), ItemManager.itemIdentifierToRPGItemMap.get("warp_erlen").generate()));
        ItemManager.registerItemRunnable("warp_maru_island", new WarpItemRunnable(WarpLocation.OLD_MARU_ISLAND.getLocation(), ItemManager.itemIdentifierToRPGItemMap.get("warp_maru_island").generate()));
        ItemManager.registerItemRunnable("warp_perion", new WarpItemRunnable(WarpLocation.OLD_PERION.getLocation(), ItemManager.itemIdentifierToRPGItemMap.get("warp_perion").generate()));
        ItemManager.registerItemRunnable("warp_kobaza", new WarpItemRunnable(WarpLocation.OLD_KOBAZA.getLocation(), ItemManager.itemIdentifierToRPGItemMap.get("warp_kobaza").generate()));
        ItemManager.registerItemRunnable("warp_liptus", new WarpItemRunnable(WarpLocation.OLD_LIPTUS.getLocation(), ItemManager.itemIdentifierToRPGItemMap.get("warp_liptus").generate()));
        ItemManager.registerItemRunnable("warp_ellinia", new WarpItemRunnable(WarpLocation.OLD_ELLINIA.getLocation(), ItemManager.itemIdentifierToRPGItemMap.get("warp_ellinia").generate()));

    }

    private static boolean justAte(PlayerDataRPG pd, String id) {
        if (!eatTimers.containsKey(pd.getUUID()))
            eatTimers.put(pd.getUUID(), new HashMap<String, Long>());
        HashMap<String, Long> map = eatTimers.get(pd.getUUID());
        long diff = System.currentTimeMillis() - map.getOrDefault(id, 0l);
        if (diff < 10000) {
            pd.sendMessage(ChatColor.GRAY + "> " + ChatColor.RED + "You just ate a stackable food! Try again in " + String.format("%.1f", 10 - (diff / 1000.0)) + "s.");
            return true;
        }
        map.put(id, System.currentTimeMillis());
        return false;
    }

    private static HashMap<String, Long> lastHealItem = new HashMap<String, Long>();

    public static void healWithPotion(int amount, String name, Event event, Player p) {
        if (!(event instanceof PlayerInteractEvent))
            return;
        PlayerInteractEvent e = (PlayerInteractEvent) event;
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK))
            return;
        if (lastHealItem.containsKey(p.getName()) && System.currentTimeMillis() - lastHealItem.get(p.getName()) < 500) {
            return;
        }
        lastHealItem.put(p.getName(), System.currentTimeMillis());
        p.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
        PlayerDataRPG pd = plugin.getPD(p);
        pd.heal(amount, HealType.POTION);
        for (int k = 0; k < p.getInventory().getContents().length; k++) {
            if (ItemManager.isItem(p.getInventory().getItem(k), name)) {
                p.getEquipment().setItemInMainHand(p.getInventory().getItem(k));
                p.getInventory().setItem(k, new ItemStack(Material.AIR));
                break;
            }
        }
        RSound.playSound(p, Sound.ENTITY_GENERIC_DRINK);
    }

}