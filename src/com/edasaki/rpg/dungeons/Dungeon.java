package com.edasaki.rpg.dungeons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.edasaki.core.utils.RFormatter;
import com.edasaki.core.utils.RUtils;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.regions.Region;

public class Dungeon {

    public DungeonVillager dungeonMaster;
    public DungeonVillager dungeonExit;

    public String dungeonName;
    public Location dungeonSpawn;
    public String description;
    public String region;

    public DungeonBoss boss;

    public HashSet<String> players = new HashSet<String>();

    public ArrayList<String> tips = new ArrayList<String>();
    public int recLevel = 1;

    public ArrayList<DungeonReward> rewardsList = new ArrayList<DungeonReward>();
    public HashMap<UUID, ArrayList<ItemStack>> givenRewards = new HashMap<UUID, ArrayList<ItemStack>>();

    private ArrayList<String> desc = null;

    private ArrayList<String> rewarded = new ArrayList<String>();

    public boolean checkRegion(Region r) {
        if (r.name.equalsIgnoreCase(region))
            return true;
        return false;
    }

    public ArrayList<String> getDisplay() {
        if (desc == null) {
            desc = new ArrayList<String>();
            desc.add(ChatColor.GOLD + "Recommended Level: " + ChatColor.YELLOW + recLevel);
            desc.add(ChatColor.AQUA + description);
            desc.add("");
            desc.add(ChatColor.WHITE + "Tips");
            for (String s : tips) {
                ArrayList<String> temp = RFormatter.stringToLore(s, "   ");
                if (temp.size() > 0)
                    temp.set(0, "-" + temp.get(0).substring(2));
                for (String t : temp)
                    desc.add(ChatColor.GRAY + t);
            }
        }
        return desc;
    }

    public void sendMessage(String s) {
        String msg = ChatColor.GRAY + "[" + ChatColor.YELLOW + "Dungeon" + ChatColor.GRAY + "] " + ChatColor.GREEN + s;
        HashSet<String> toRemove = new HashSet<String>();
        for (String uuid : players) {
            PlayerDataRPG pd = DungeonManager.plugin.getPD(uuid);
            if (pd != null && pd.getPlayer() != null && pd.getPlayer().isOnline() && checkRegion(pd.region))
                pd.sendMessage(msg);
            else
                toRemove.add(uuid);
        }
        players.removeAll(toRemove);
    }

    public void handleRewards(Player p, PlayerDataRPG pd) {
        ArrayList<ItemStack> list = new ArrayList<ItemStack>();
        for (DungeonReward dr : this.rewardsList) {
            list.addAll(dr.roll());
        }
        if (RUtils.hasEmptySpaces(p, list.size())) {
            if (rewarded.contains(p.getName())) {
                p.sendMessage(ChatColor.RED + "You have already claimed your rewards for this dungeon run!");
            } else {
                p.sendMessage("");
                p.sendMessage("");
                p.sendMessage(ChatColor.GRAY + "> " + ChatColor.AQUA + ChatColor.BOLD + "Dungeon rewards received!");
                for (ItemStack i : list) {
                    p.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "You received " + i.getItemMeta().getDisplayName() + ChatColor.WHITE + ".");
                    p.getInventory().addItem(i);
                }
                p.updateInventory();
                rewarded.add(p.getName());
            }
            p.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "Leave the dungeon by talking to the " + ChatColor.RED + "Dungeon Master" + ChatColor.GREEN + ".");
            if (this.dungeonSpawn != null)
                p.teleport(this.dungeonSpawn);
            else
                p.teleport(this.dungeonMaster.getTPLoc());
        } else {
            p.sendMessage("");
            p.sendMessage("");
            p.sendMessage(ChatColor.GRAY + "> " + ChatColor.RED + ChatColor.BOLD + "Not enough inventory space!");
            p.sendMessage(ChatColor.GRAY + "> " + ChatColor.RED + "You need " + ChatColor.YELLOW + list.size() + ChatColor.RED + " inventory spaces for your dungeon rewards.");
            p.sendMessage(ChatColor.GRAY + "> " + ChatColor.RED + "Please clear out some space and try again.");
            givenRewards.put(p.getUniqueId(), list);
        }
    }

    public void clearRewarded() {
        rewarded.clear();
    }

}
