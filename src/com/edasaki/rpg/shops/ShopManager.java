package com.edasaki.rpg.shops;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.npcs.NPCManager;

public class ShopManager extends AbstractManagerRPG {

    public static ArrayList<ShopVillager> shopVillagers;

    public ShopManager(SakiRPG plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {
        reload();
    }

    public static void reload() {
        if (shopVillagers == null) {
            shopVillagers = new ArrayList<ShopVillager>();
        } else {
            for (ShopVillager sv : shopVillagers) {
                NPCManager.unregister(sv);
            }
            shopVillagers.clear();
        }
        File dir = new File(plugin.getDataFolder(), "shops");
        if (!dir.exists())
            dir.mkdirs();
        for (File f : dir.listFiles()) {
            if (f.getName().endsWith(".txt")) {
                readShop(f);
            }
        }
    }

    public static void readShop(File f) {
        Scanner scan = null;
        int id = 0;
        try {
            String next = "";
            scan = new Scanner(f);
            next = scan.nextLine().trim();
            String name = next;
            next = scan.nextLine().trim();
            String shopName = next;
            next = scan.nextLine().trim();
            String dialogue = next;
            next = scan.nextLine().trim();
            String[] data = next.split(" ");
            double x = Double.parseDouble(data[0]);
            double y = Double.parseDouble(data[1]);
            double z = Double.parseDouble(data[2]);
            String world = data[3];
            ShopVillager sv = new ShopVillager(++id, name, shopName, dialogue, x, y, z, world);
            while (scan.hasNextLine()) {
                next = scan.nextLine().trim();
                data = next.split(" ");
                ShopItem si = new ShopItem(data[0], Integer.parseInt(data[1]));
                sv.addItem(si);
                if (next.startsWith("//") || next.length() == 0)
                    continue;
            }
            sv.postProcess();
            sv.register();
            shopVillagers.add(sv);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error reading quest in " + f.getName() + ".");
            e.printStackTrace();
        } finally {
            if (scan != null)
                scan.close();
        }
    }

}
