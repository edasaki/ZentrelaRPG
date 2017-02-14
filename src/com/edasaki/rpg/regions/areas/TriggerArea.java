package com.edasaki.rpg.regions.areas;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.regions.RegionBoundary;
import com.edasaki.rpg.regions.RegionPoint;

public class TriggerArea {

    public String name;
    public RegionBoundary boundary;
    public World world;
    public double size;
    public List<TriggerAreaAction> actions;

    private long delay = 0;
    private HashMap<UUID, Long> last = new HashMap<UUID, Long>();

    public TriggerArea(String name, World world, long delay, List<RegionPoint> points, List<TriggerAreaAction> actions) {
        this.name = name;
        this.world = world;
        this.delay = delay;
        this.boundary = RegionBoundary.create(points);
        this.size = boundary.area();
        this.actions = actions;
    }

    public boolean isWithin(Player p) {
        return isWithin(p.getLocation());
    }

    public boolean isWithin(Location loc) {
        if (!loc.getWorld().equals(this.world))
            return false;
        return boundary.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    @Override
    public String toString() {
        return name + " " + world.getName() + " " + boundary + " size:" + size + " actions: " + actions.toString();
    }

    public void runActions(Player p, PlayerDataRPG pd) {
        if (System.currentTimeMillis() - last.getOrDefault(pd.getUUID(), 0l) < delay) {
            return;
        }
        last.put(pd.getUUID(), System.currentTimeMillis());
        for (TriggerAreaAction taa : actions)
            taa.act(p, pd);
        //        pd.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "You smell some delicious " + ChatColor.GOLD + "Pumpkins" + ChatColor.GREEN + ".");
    }
}
