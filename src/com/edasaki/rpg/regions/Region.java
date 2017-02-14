package com.edasaki.rpg.regions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.edasaki.core.options.SakiOption;
import com.edasaki.rpg.PlayerDataRPG;

public class Region {

    public String name;
    public int recLevel;
    public int dangerLevel;
    //    public int x1, x2, y1, y2, z1, z2;
    public RegionBoundary boundary;
    public World world;
    public double size;

    public long startTime = 3000, endTime = 9000, timeDiff = 6000;
    public int cycleLengthSeconds = 300; // 0 for no cycle
    private long cycleStart = System.currentTimeMillis();

    private ArrayList<String> welcomeMsgs;

    private String compactMsg = "";

    public Region(String name, int recLevel, int dangerLevel, String worldName, List<RegionPoint> arr) {
        this(name, recLevel, dangerLevel, RegionManager.plugin.getServer().getWorld(worldName), arr);
    }

    public Region(String name, int recLevel, int dangerLevel, World world, List<RegionPoint> arr) {
        this.name = name;
        this.recLevel = recLevel;
        this.dangerLevel = dangerLevel;
        this.world = world;
        this.welcomeMsgs = new ArrayList<String>();
        boundary = RegionBoundary.create(arr);
        this.size = boundary.area();
        welcomeMsgs.add("");
        welcomeMsgs.add(ChatColor.translateAlternateColorCodes('&', "&7>> &eYou are now in &6&l" + name));
        welcomeMsgs.add(ChatColor.translateAlternateColorCodes('&', "&7>> &eRecommended Level &f&l" + recLevel));
        compactMsg = ChatColor.translateAlternateColorCodes('&', "&7>> &eYou entered &6&l" + name + "&e | Lv. &f&l" + recLevel + " &e| &cDanger Lv. ");
        switch (dangerLevel) {
            case 1:
                welcomeMsgs.add(ChatColor.translateAlternateColorCodes('&', "&7>> &cDanger Level &a&l1"));
                welcomeMsgs.add(ChatColor.translateAlternateColorCodes('&', "&7>> &aSafe zone. Nothing lost on death."));
                compactMsg += ChatColor.translateAlternateColorCodes('&', "&a&l1");
                break;
            case 2:
                welcomeMsgs.add(ChatColor.translateAlternateColorCodes('&', "&7>> &cDanger Level &b&l2"));
                welcomeMsgs.add(ChatColor.translateAlternateColorCodes('&', "&7>> &bMobs can attack you."));
                welcomeMsgs.add(ChatColor.translateAlternateColorCodes('&', "&7>> &bLose 15% of your current EXP on death."));
                compactMsg += ChatColor.translateAlternateColorCodes('&', "&b&l2");
                break;
            case 3:
                welcomeMsgs.add(ChatColor.translateAlternateColorCodes('&', "&7>> &cDanger Level &d&l3"));
                welcomeMsgs.add(ChatColor.translateAlternateColorCodes('&', "&7>> &dPlayers and mobs can attack you."));
                welcomeMsgs.add(ChatColor.translateAlternateColorCodes('&', "&7>> &dLose 20% of your current EXP on death."));
                compactMsg += ChatColor.translateAlternateColorCodes('&', "&d&l3");
                break;
            case 4:
                welcomeMsgs.add(ChatColor.translateAlternateColorCodes('&', "&7>> &cDanger Level &3&l4"));
                welcomeMsgs.add(ChatColor.translateAlternateColorCodes('&', "&7>> &3Players and mobs can attack you."));
                welcomeMsgs.add(ChatColor.translateAlternateColorCodes('&', "&7>> &3Lose 25% of your current EXP on death."));
                welcomeMsgs.add(ChatColor.translateAlternateColorCodes('&', "&7>> &3Drop &c&lHALF OF YOUR INVENTORY&3 on death."));
                welcomeMsgs.add(ChatColor.translateAlternateColorCodes('&', "&7>> &3Armor, weapons, and other valuables may be &c&lDROPPED&3!"));
                compactMsg += ChatColor.translateAlternateColorCodes('&', "&3&l4");
                break;
        }
        welcomeMsgs.add("");

    }

    public void sendWelcome(Player p, Region last) {
        if (this.name.equals(RegionManager.GENERAL_NAME)) { // don't send region message on general region b/c it's ugly
            if (last != null) {
                p.sendMessage(ChatColor.GRAY + ">> " + ChatColor.YELLOW + "You are now leaving " + ChatColor.GOLD + ChatColor.BOLD + last.name + ChatColor.YELLOW + ".");
            }
            return;
        }
        PlayerDataRPG pd = RegionManager.plugin.getPD(p);
        if (pd == null || !pd.loadedSQL)
            return;
        if (pd.getOption(SakiOption.COMPACT_REGION_MESSAGES)) {
            p.sendMessage(compactMsg);
        } else {
            for (String s : welcomeMsgs)
                p.sendMessage(s);
        }
    }

    public long getTime() {
        if (cycleLengthSeconds == 0 || timeDiff == 0 || startTime == endTime)
            return startTime;
        long sec = (System.currentTimeMillis() - cycleStart) / 1000;
        sec %= cycleLengthSeconds * 2;
        long finalTime = startTime;
        if (sec <= cycleLengthSeconds)
            finalTime = startTime + timeDiff * sec / cycleLengthSeconds;
        else
            finalTime = endTime - timeDiff * (sec - cycleLengthSeconds) / cycleLengthSeconds;
        return finalTime;
    }

    public boolean isWithin(Player p) {
        return isWithin(p.getLocation());
    }

    public boolean isWithin(Location loc) {
        if (!loc.getWorld().equals(this.world))
            return false;
        return boundary.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        //        double x = (int) loc.getX();
        //        double y = (int) loc.getY();
        //        double z = (int) loc.getZ();
        //        return x >= x1 && x <= x2 && y >= y1 && y <= y2 && z >= z1 && z <= z2;
    }

    @Override
    public String toString() {
        return name + " " + world.getName() + " " + boundary + " size:" + size + " danger: " + dangerLevel;
    }

}
