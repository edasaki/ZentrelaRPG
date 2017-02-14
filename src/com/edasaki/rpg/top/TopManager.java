package com.edasaki.rpg.top;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.edasaki.core.menus.MenuItem;
import com.edasaki.core.menus.MenuManager;
import com.edasaki.core.sql.SQLManager;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RTicks;
import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.SakiRPG;

public class TopManager extends AbstractManagerRPG {

    private LinkedHashMap<String, Integer> levels = new LinkedHashMap<String, Integer>();
    private LinkedHashMap<String, Integer> mobKills = new LinkedHashMap<String, Integer>();
    private LinkedHashMap<String, Integer> shards = new LinkedHashMap<String, Integer>();
    private LinkedHashMap<String, Integer> playerKills = new LinkedHashMap<String, Integer>();
    private LinkedHashMap<String, Integer> playTime = new LinkedHashMap<String, Integer>();

    private static final ChatColor[] COLORS = { ChatColor.GOLD, ChatColor.GREEN, ChatColor.LIGHT_PURPLE, ChatColor.AQUA, ChatColor.BLUE };;

    public TopManager(SakiRPG pl) {
        super(pl);
    }

    @Override
    public void initialize() {
        RScheduler.schedule(plugin, new Runnable() {
            public void run() {
                update();
                RScheduler.schedule(plugin, this, RTicks.seconds(60));
            }
        });
    }

    private void update() {
        //        System.out.println("Updating leaderboards...");
        //        long start = System.currentTimeMillis();
        RScheduler.scheduleAsync(plugin, new Runnable() {
            public void run() {
                if (SakiRPG.TEST_REALM) {
                    updatedList("SELECT name,level,exp FROM main ORDER BY level DESC, exp DESC LIMIT 5;", "level", levels);
                    updatedList("SELECT name,level,mobKills FROM main ORDER BY mobKills DESC, level DESC LIMIT 5;", "mobKills", mobKills);
                    updatedList("SELECT name,level,lastShardCount FROM main ORDER BY lastShardCount DESC, level DESC LIMIT 5;", "lastShardCount", shards);
                    updatedList("SELECT name,level,playerKills FROM main ORDER BY playerKills DESC, level DESC LIMIT 5;", "playerKills", playerKills);
                    updatedList("SELECT name,level,timePlayed FROM main ORDER BY timePlayed DESC, level DESC LIMIT 5;", "timePlayed", playTime);
                } else {
                    updatedList("SELECT name,level,exp FROM main WHERE NOT rank='owner' ORDER BY level DESC, exp DESC LIMIT 5;", "level", levels);
                    updatedList("SELECT name,level,mobKills FROM main WHERE NOT rank='owner' ORDER BY mobKills DESC, level DESC LIMIT 5;", "mobKills", mobKills);
                    updatedList("SELECT name,level,lastShardCount FROM main WHERE NOT rank='owner' ORDER BY lastShardCount DESC, level DESC LIMIT 5;", "lastShardCount", shards);
                    updatedList("SELECT name,level,playerKills FROM main WHERE NOT rank='owner' ORDER BY playerKills DESC, level DESC LIMIT 5;", "playerKills", playerKills);
                    updatedList("SELECT name,level,timePlayed FROM main ORDER BY timePlayed DESC, level DESC LIMIT 5;", "timePlayed", playTime);
                }
                //                if (plugin.getServer().getOnlinePlayers().size() > 0)
                //                    System.out.println("Updated leaderboards in " + (System.currentTimeMillis() - start) + "ms.");
            }
        });
    }

    private void updatedList(String query, String field, LinkedHashMap<String, Integer> toEdit) {
        AutoCloseable[] ac_dub = SQLManager.prepare(query);
        try {
            PreparedStatement request_punishment_status = (PreparedStatement) ac_dub[0];
            AutoCloseable[] ac_trip = SQLManager.executeQuery(request_punishment_status);
            ResultSet rs = (ResultSet) ac_trip[0];
            LinkedHashMap<String, Integer> tmp = new LinkedHashMap<String, Integer>();
            while (rs.next()) {
                tmp.put(rs.getString("name"), rs.getInt(field));
            }
            SQLManager.close(ac_dub);
            SQLManager.close(ac_trip);
            RScheduler.schedule(plugin, () -> {
                toEdit.clear();
                toEdit.putAll(tmp);
                //                System.out.println("updated: " + toEdit.toString() + "tmp");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMenu(Player p) {
        List<MenuItem> list = new ArrayList<MenuItem>();
        list.add(new MenuItem(0, 4, new ItemStack(Material.ENCHANTED_BOOK), ChatColor.GOLD + "The Zentrelan Elites", new String[] {
                ChatColor.GREEN + "These players are the best in Zentrela at something.",
                "",
                ChatColor.AQUA + "With a bit of hard work, you can be on this prestigious list too!",
                "",
                ChatColor.GRAY + "The toplist is updated once every minute."
        }, null));

        // mob kills
        process(list, mobKills, 0, ChatColor.DARK_AQUA + "Mob Kills", new ItemStack[] {
                new ItemStack(Material.DIAMOND_HELMET),
                new ItemStack(Material.GOLD_HELMET),
                new ItemStack(Material.IRON_HELMET),
                new ItemStack(Material.CHAINMAIL_HELMET),
                new ItemStack(Material.LEATHER_HELMET)
        });

        // player kills
        process(list, playerKills, 2, ChatColor.RED + "Player Kills", new ItemStack[] {
                new ItemStack(Material.DIAMOND_CHESTPLATE),
                new ItemStack(Material.GOLD_CHESTPLATE),
                new ItemStack(Material.IRON_CHESTPLATE),
                new ItemStack(Material.CHAINMAIL_CHESTPLATE),
                new ItemStack(Material.LEATHER_CHESTPLATE)
        });

        // levels
        process(list, levels, 4, ChatColor.AQUA + "Level", new ItemStack[] {
                new ItemStack(Material.DIAMOND_SWORD),
                new ItemStack(Material.GOLD_SWORD),
                new ItemStack(Material.IRON_SWORD),
                new ItemStack(Material.STONE_SWORD),
                new ItemStack(Material.WOOD_SWORD)
        });

        // levels
        process(list, playTime, 6, ChatColor.YELLOW + "Time Played", new ItemStack[] {
                new ItemStack(Material.DIAMOND_LEGGINGS),
                new ItemStack(Material.GOLD_LEGGINGS),
                new ItemStack(Material.IRON_LEGGINGS),
                new ItemStack(Material.CHAINMAIL_LEGGINGS),
                new ItemStack(Material.LEATHER_LEGGINGS)
        });

        // shards
        process(list, shards, 8, ChatColor.GREEN + "Total Shards", new ItemStack[] {
                new ItemStack(Material.DIAMOND_BOOTS),
                new ItemStack(Material.GOLD_BOOTS),
                new ItemStack(Material.IRON_BOOTS),
                new ItemStack(Material.CHAINMAIL_BOOTS),
                new ItemStack(Material.LEATHER_BOOTS)
        });

        Inventory i = MenuManager.createMenu(p, "Zentrela Top Players", 6, list);
        p.openInventory(i);
    }

    private void process(List<MenuItem> list, LinkedHashMap<String, Integer> map, int col, String label, ItemStack[] items) {
        int index = 0;
        for (Entry<String, Integer> e : map.entrySet()) {
            index++;
            if (index > 5)
                break;
            list.add(new MenuItem(index, col, items[index - 1], COLORS[index - 1] + "#" + index + ChatColor.GRAY + " - " + COLORS[index - 1] + e.getKey(), new String[] {
                    label + ChatColor.GRAY + ": " + ChatColor.WHITE + e.getValue()
            }, null));
        }
    }

}
