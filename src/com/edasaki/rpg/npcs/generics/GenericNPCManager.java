package com.edasaki.rpg.npcs.generics;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Location;

import com.edasaki.core.utils.gson.LocationAdapter;
import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.npcs.NPCManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class GenericNPCManager extends AbstractManagerRPG {

    public static ArrayList<GenericNPC> genericVillagers = new ArrayList<GenericNPC>();

    public GenericNPCManager(SakiRPG plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {
        reloadGenerics();
    }

    public static void reloadGenerics() {
        for (GenericNPC gv : genericVillagers) {
            NPCManager.unregister(gv);
        }
        genericVillagers.clear();
        File dir = new File(plugin.getDataFolder(), "generics");
        if (!dir.exists())
            dir.mkdirs();
        int count = 0;
        for (File f : dir.listFiles()) {
            if (f.getName().endsWith(".json")) {
                try {
                    count += readGeneric(f);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Loaded " + count + " generic villagers.");
    }

    public static int readGeneric(File f) {
        int count = 0;
        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Location.class, new LocationAdapter()).create();
        try (FileReader reader = new FileReader(f)) {
            GenericNPC[] vils = gson.fromJson(reader, GenericNPC[].class);
            for (GenericNPC gv : vils) {
                if (gv == null)
                    continue;
                NPCManager.register(gv);
                genericVillagers.add(gv);
            }
            count += vils.length;
        } catch (JsonSyntaxException | JsonIOException | IOException e) {
            System.err.println("Error reading NPC in " + f.getName() + ".");
            e.printStackTrace();
        }
        return count;
    }

}
