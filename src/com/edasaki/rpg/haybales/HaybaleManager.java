package com.edasaki.rpg.haybales;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RSound;
import com.edasaki.core.utils.RTicks;
import com.edasaki.core.utils.RUtils;
import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.items.ItemManager;
import com.edasaki.rpg.items.RPGItem;

public class HaybaleManager extends AbstractManagerRPG {

    private static HashMap<String, Long> lastClick = new HashMap<String, Long>();
    private static HashMap<String, Long> lastSearch = new HashMap<String, Long>();
    private static HashMap<String, Location> lastSearched = new HashMap<String, Location>();

    private static HashMap<Location, RPGItem> items = new HashMap<Location, RPGItem>();

    public HaybaleManager(SakiRPG plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {
        reload();
    }

    public static void reload() {
        lastClick.clear();
        lastSearch.clear();
        lastSearched.clear();
        items.clear();
        File dir = new File(plugin.getDataFolder(), "haybales");
        if (!dir.exists())
            dir.mkdirs();
        for (File f : dir.listFiles()) {
            if (f.getName().endsWith(".txt")) {
                readLocs(f);
            }
        }
    }

    private static void readLocs(File f) {
        Scanner scan = null;
        try {
            scan = new Scanner(f);
            String s;
            while (scan.hasNextLine()) {
                s = scan.nextLine().trim();
                if (s.length() == 0 || s.startsWith("//") || s.startsWith("#"))
                    continue;
                String[] data = s.split(" ");
                int x = Integer.parseInt(data[0]);
                int y = Integer.parseInt(data[1]);
                int z = Integer.parseInt(data[2]);
                String world = data[3];
                World w = plugin.getServer().getWorld(world);
                if (w == null) {
                    Log.error("null world for file " + f + " line " + s);
                    continue;
                }
                Location loc = new Location(w, x, y, z);
                String itemIdentifier = data[4];
                RPGItem item = ItemManager.itemIdentifierToRPGItemMap.get(itemIdentifier);
                if (item == null) {
                    Log.error("Failed to find haybale item at " + f.getName() + " for identifier " + itemIdentifier);
                    continue;
                }
                items.put(loc, item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (scan != null)
                scan.close();
        }
    }

    @EventHandler
    public void searchHay(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.HAY_BLOCK) {
            Player p = event.getPlayer();
            if (lastClick.containsKey(p.getName())) {
                long last = lastClick.get(p.getName());
                if (System.currentTimeMillis() - last < 300)
                    return;
            }
            if (!RUtils.hasEmptySpaces(p, 1)) {
                p.sendMessage(ChatColor.RED + " You need 1 open inventory space to search hay bales!");
                return;
            }
            lastClick.put(p.getName(), System.currentTimeMillis());
            if (lastSearch.containsKey(p.getName())) {
                long last = lastSearch.get(p.getName());
                if (System.currentTimeMillis() - last < 30000) {
                    long diff = System.currentTimeMillis() - last;
                    p.sendMessage(ChatColor.RED + " Searching through hay is tiring...");
                    p.sendMessage(ChatColor.RED + " You can only search 1 hay bale every 30s. (wait " + String.format("%.1f", 30 - (diff / 1000.0)) + "s)");
                    return;
                }
            }
            if (lastSearched.containsKey(p.getName()) && lastSearched.get(p.getName()).equals(event.getClickedBlock().getLocation())) {
                p.sendMessage(ChatColor.RED + " You just searched this hay bale! Try a different one.");
                return;
            }
            lastSearched.put(p.getName(), event.getClickedBlock().getLocation());
            lastSearch.put(p.getName(), System.currentTimeMillis());
            RSound.playSound(p, Sound.ENTITY_FIREWORK_TWINKLE_FAR);
            p.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "You search through the hay bale, hoping for some goodies...");
            final Location loc = event.getClickedBlock().getLocation();
            RScheduler.schedule(plugin, new Runnable() {
                private int count = 0;

                public void run() {
                    if (p == null || !p.isValid() || !p.isOnline())
                        return;
                    if (count < 3) {
                        RSound.playSound(p, Sound.ENTITY_FIREWORK_TWINKLE_FAR);
                        count++;
                        p.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + ".....".substring(0, (int) (Math.random() * 3) + 3));
                        RScheduler.schedule(plugin, this, RTicks.seconds(1));
                    } else {
                        giveReward(p, loc);
                    }
                }
            });
        }
    }

    private static void found(Player p, ItemStack item) {
        String disp = "";
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName())
            disp = item.getItemMeta().getDisplayName();
        else
            disp = item.getType().toString();
        if (p != null)
            p.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "You find a " + ChatColor.YELLOW + disp + ChatColor.GREEN + "!");
    }

    private static Object[][] rands = {
            { "needle", 0.005 },
            { "warp_ellinia", 0.02 },
            { "warp_erlen", 0.02 },
            { "warp_kobaza", 0.02 },
            { "warp_korwyn", 0.02 },
            { "warp_lemia", 0.02 },
            { "warp_liptus", 0.02 },
            { "warp_maru_island", 0.02 },
            { "warp_perion", 0.01 },
            { "hp_potion_1", 0.01 },
            { "hp_potion_2", 0.01 },
            { "hp_potion_3", 0.01 },
            { "hp_potion_4", 0.01 },
            { "hp_potion_5", 0.01 },
            { "juicy_apple", 0.60 },
            { "rotten_apple", 0.40 },
            { "maru_milk", 0.20 },
            { "useless_straw", 0.07 },
    };

    private static void giveReward(Player p, Location loc) {
        if (items.containsKey(loc)) {
            ItemStack item = items.get(loc).generate();
            p.getInventory().addItem(item);
            found(p, item);
        } else {
            boolean rewarded = false;
            for (int k = 0; k < rands.length; k++) {
                int slot = (int) (Math.random() * rands.length);
                RPGItem item = ItemManager.itemIdentifierToRPGItemMap.get(rands[slot][0]);
                if (item == null) {
                    Log.error("Failed to find haybale item at for built-in item " + rands[slot][0]);
                    continue;
                }
                if (Math.random() < (double) rands[slot][1]) {
                    ItemStack i = item.generate();
                    p.getInventory().addItem(i);
                    found(p, i);
                    rewarded = true;
                    break;
                }
            }
            if (!rewarded) {
                p.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "It looks like this hay bale is empty. Better luck next time!");
            }
        }
    }

}
