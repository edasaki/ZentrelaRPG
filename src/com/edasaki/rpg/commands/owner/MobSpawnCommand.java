package com.edasaki.rpg.commands.owner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.general.SchematicManager;

public class MobSpawnCommand extends RPGAbstractCommand {

    private static int radius = 30, delay = 10, leash = 25;
    private static ArrayList<String> types = new ArrayList<String>();
    private static ArrayList<Location> locs = new ArrayList<Location>();

    public MobSpawnCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    public static String name = (int) (Math.random() * 10000) + ".txt";

    public static void setLoc(Player p, Location loc) {
        locs.add(loc);
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        String arg0 = args[0].trim().toLowerCase();
        if (arg0.equals("new")) {
            name = args[1] + ".txt";
            p.sendMessage("Set save destination to " + name);
        } else if (arg0.equals("item")) {
            SchematicManager.giveMobSpawnItem(p);
        } else if (arg0.equals("undo")) {
            if (locs.size() > 0)
                locs.remove(locs.size() - 1);
            p.sendMessage("Removed last. Current List: " + locs.toString());
        } else if (arg0.equals("save")) {
            File f = new File(plugin.getDataFolder().getPath() + File.separatorChar + "spawns" + File.separatorChar + name);
            try {
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f)));
                for (String t : types) {
                    out.print(t);
                    out.print(' ');
                }
                out.println();
                out.print(radius);
                out.print(' ');
                out.print(delay);
                out.print(' ');
                out.print(leash);
                out.println();
                for (Location loc : locs) {
                    out.println(serialize(loc));
                }
                out.close();
                p.sendMessage("Saved spawns to " + f.toString());
            } catch (IOException e) {
                p.sendMessage("Error saving.");
                e.printStackTrace();
            }
        } else if (arg0.equals("list")) {
            p.sendMessage("Save file: " + name);
            for (Location loc : locs)
                p.sendMessage(serialize(loc));
        } else if (arg0.equals("clear")) {
            locs.clear();
            p.sendMessage("Cleared locs.");
        } else if (arg0.equals("addrand")) {
            String identifier = args[1];
            for (String val : identifier.split(",")) {
                types.add(val);
                p.sendMessage("Added new entry to rand: " + val);
            }
        } else if (arg0.equals("radius")) {
            radius = Integer.parseInt(args[1]);
        } else if (arg0.equals("delay")) {
            delay = Integer.parseInt(args[1]);
        } else if (arg0.equals("leash")) {
            leash = Integer.parseInt(args[1]);
        } else if (arg0.equals("cleartypes")) {
            types.clear();
            p.sendMessage("Cleared types.");
        } else if (arg0.equals("typelist")) {
            p.sendMessage("Types:");

        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

    private String serialize(Location loc) {
        return loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ", " + loc.getWorld().getName();
    }

}