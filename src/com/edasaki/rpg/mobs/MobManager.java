package com.edasaki.rpg.mobs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.edasaki.core.utils.RHead;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RTicks;
import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.items.ItemManager;
import com.edasaki.rpg.items.RPGItem;
import com.edasaki.rpg.mobs.spells.MobSpell;
import com.edasaki.rpg.worldboss.bosses.BossGenerator;

import net.minecraft.server.v1_10_R1.Entity;

public class MobManager extends AbstractManagerRPG {

    public static HashMap<String, MobType> mobTypes;
    public static HashMap<UUID, MobData> spawnedMobs; //these are all 3 parts of each mob
    public static HashMap<UUID, MobData> spawnedMobs_onlyMain; //each mob in rpg is 3 parts. these are the visible parts

    public static ArrayList<MobSpawn> spawns;

    private static int taskCounter = 0;

    public MobManager(SakiRPG plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {
        mobTypes = new HashMap<String, MobType>();
        spawnedMobs = new HashMap<UUID, MobData>();
        spawnedMobs_onlyMain = new HashMap<UUID, MobData>();
        spawns = new ArrayList<MobSpawn>();
        reload();
    }

    public static void reload() {
        Collection<MobData> collection = new ArrayList<MobData>();
        collection.addAll(spawnedMobs.values());
        for (MobSpawn ms : spawns)
            ms.stop();
        for (MobData m : collection)
            m.die(false);

        mobTypes.clear();
        spawnedMobs.clear();
        spawnedMobs_onlyMain.clear();
        spawns.clear();
        File dir = new File(plugin.getDataFolder(), "mobs");
        if (!dir.exists())
            dir.mkdirs();
        for (File f : dir.listFiles()) {
            if (f.getName().endsWith(".txt")) {
                readMob(f);
            }
        }
        BossGenerator.plugin = plugin;
        dir = new File(plugin.getDataFolder(), "spawns");
        if (!dir.exists())
            dir.mkdirs();
        for (File f : dir.listFiles()) {
            if (f.getName().endsWith(".txt")) {
                readSpawn(f);
            }
        }
        Collections.shuffle(spawns);
        for (MobSpawn ms : spawns)
            ms.spawn(taskCounter++);
    }

    public static void respawn(MobSpawn ms) {
        if (ms == null)
            return;
        RScheduler.schedule(plugin, () -> {
            ms.spawn(taskCounter++);
        }, RTicks.seconds(ms.respawnDelay));
    }

    private static void readMob(File f) {
        Scanner scan = null;
        try {
            scan = new Scanner(f);
            // name
            String name = scan.nextLine().trim();
            //type
            @SuppressWarnings("unchecked")
            Class<? extends net.minecraft.server.v1_10_R1.Entity> entityClass = (Class<? extends Entity>) Class.forName("com.edasaki.core.utils.entities." + scan.nextLine().trim());
            // level
            int level = Integer.parseInt(scan.nextLine().replaceAll("[^0-9]", ""));
            // prefixes
            ArrayList<String> prefixes = new ArrayList<String>();
            String[] data = scan.nextLine().substring("Prefixes: ".length()).split(",");
            for (String s : data) {
                if (s.length() == 0)
                    continue;
                s = s.trim();
                if (s.equalsIgnoreCase("none"))
                    prefixes.add("");
                else
                    prefixes.add(s + " ");
            }
            // suffixes
            ArrayList<String> suffixes = new ArrayList<String>();
            data = scan.nextLine().substring("Suffixes: ".length()).split(",");
            for (String s : data) {
                s = s.trim();
                if (s.length() == 0)
                    continue;
                if (s.equalsIgnoreCase("none"))
                    suffixes.add("");
                else
                    suffixes.add(" " + s);
            }
            long exp = MobBalance.getMobEXP(level, Integer.parseInt(scan.nextLine().replaceAll("[^0-9]", "")) / 100.0);
            //health
            long health = MobBalance.getMobHP(level, Integer.parseInt(scan.nextLine().replaceAll("[^0-9]", "")) / 100.0);
            // damage
            // one is damage line and one is for range line
            int[] dmg = MobBalance.getMobDamage(level, Integer.parseInt(scan.nextLine().replaceAll("[^0-9]", "")) / 100.0, Integer.parseInt(scan.nextLine().replaceAll("[^0-9]", "")) / 100.0);
            int damageLow = dmg[0];
            int damageHigh = dmg[0];
            // equips
            boolean hasSkull = false;
            ArrayList<ItemStack> equips = new ArrayList<ItemStack>();
            data = scan.nextLine().substring("Equips: ".length()).split(",");
            String orig = null;
            ItemStack offhand = null;
            for (String s : data) {
                orig = s;
                s = s.trim().toUpperCase().replace(' ', '_');
                if (s.length() == 0)
                    continue;
                if (!s.equalsIgnoreCase("none")) {
                    if (s.startsWith("HEAD")) {
                        String[] temp = s.split(":");
                        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
                        SkullMeta meta = (SkullMeta) skull.getItemMeta();
                        meta.setOwner(temp[1]);
                        skull.setItemMeta(meta);
                        equips.add(skull);
                        hasSkull = true;
                    } else if (s.startsWith("URL")) {
                        if (orig != null)
                            orig = orig.trim().replace(' ', '_');
                        String[] temp = orig.split(":");
                        ItemStack skull = RHead.getSkull(RHead.PREFIX + temp[1].toLowerCase());
                        equips.add(skull);
                        hasSkull = true;
                    } else if (s.startsWith("OFFHAND")) {
                        String[] temp = s.split(":");
                        try {
                            ItemStack item = new ItemStack(Material.getMaterial(temp[1]));
                            offhand = item;
                        } catch (Exception e) {
                            System.err.println("Failed to find material " + s + " for mob " + f.getName());
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            ItemStack item;
                            if (s.contains(":")) {
                                String[] temp = s.split(":");
                                item = new ItemStack(Material.getMaterial(temp[0]));
                                ItemMeta im = item.getItemMeta();
                                if (temp.length > 1 && temp[1].startsWith("ENCHANT")) {
                                    im.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
                                }
                                if (temp.length > 2) {
                                    try {
                                        if (temp[2].split("#").length == 3 && im instanceof LeatherArmorMeta) {
                                            String[] colorData = temp[2].split("#");
                                            int r = Integer.parseInt(colorData[0].trim());
                                            int g = Integer.parseInt(colorData[1].trim());
                                            int b = Integer.parseInt(colorData[2].trim());
                                            ((LeatherArmorMeta) im).setColor(org.bukkit.Color.fromRGB(r, g, b));
                                        } else {
                                            ((LeatherArmorMeta) im).setColor(DyeColor.valueOf(temp[2]).getColor());
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                item.setItemMeta(im);
                            } else {
                                item = new ItemStack(Material.getMaterial(s));
                            }
                            equips.add(item);
                        } catch (Exception e) {
                            System.err.println("Failed to find material " + s + " for mob " + f.getName());
                            e.printStackTrace();
                        }
                    }
                }
            }
            // attributes
            ArrayList<MobAttribute> attributes = new ArrayList<MobAttribute>();
            data = scan.nextLine().substring("Attributes: ".length()).split(",");
            for (String s : data) {
                if (s.length() == 0)
                    continue;
                s = s.trim().toLowerCase();
                if (s.equalsIgnoreCase("none")) {
                    continue;
                } else {
                    MobAttribute ma = MobAttribute.get(s);
                    if (ma != null)
                        attributes.add(ma);
                    else
                        Log.error("Could not find MobAttribute with id " + s + ".");
                }
            }
            //spells
            ArrayList<MobSpell> spells = new ArrayList<MobSpell>();
            data = scan.nextLine().substring("Spells: ".length()).split(",");
            for (String s : data) {
                if (s.length() == 0)
                    continue;
                s = s.trim().toLowerCase();
                if (s.equalsIgnoreCase("none")) {
                    continue;
                } else {
                    MobSpell sp = MobSpellbook.getSpell(s);
                    if (sp != null)
                        spells.add(sp);
                }
            }
            //drops
            ArrayList<MobDrop> drops = new ArrayList<MobDrop>();
            scan.nextLine(); // filler line "Drops:"
            while (scan.hasNextLine()) {
                String[] dropInfo = scan.nextLine().split(" ");
                String[] amountInfo = dropInfo[0].split("-");
                int minAmount = Integer.parseInt(amountInfo[0]);
                int maxAmount = Integer.parseInt(amountInfo[1]);
                String itemIdentifier = dropInfo[1];
                double chance = Double.parseDouble(dropInfo[2].replaceAll("[^0-9]", "")) / 100.0;
                RPGItem item = ItemManager.itemIdentifierToRPGItemMap.get(itemIdentifier);
                if (item == null) {
                    Log.error("Failed to find mob drop at " + f.getName() + " for identifier " + itemIdentifier);
                    continue;
                }
                MobDrop md = new MobDrop(item, minAmount, maxAmount, chance);
                drops.add(md);
            }
            MobType mt = new MobType();
            mt.identifier = f.getName().substring(0, f.getName().indexOf(".txt"));
            mt.name = name;
            mt.entityClass = entityClass;
            mt.level = level;
            mt.prefixes = prefixes;
            mt.suffixes = suffixes;
            mt.exp = exp;
            mt.maxHP = health;
            mt.damageLow = damageLow;
            mt.damageHigh = damageHigh;
            mt.equips = equips;
            mt.attributes = attributes;
            mt.spells = spells;
            mt.drops = drops;
            mt.hasSkull = hasSkull;
            mt.offhand = offhand;
            if (mt.identifier.startsWith("gift_"))
                mt.exp = 100;
            mobTypes.put(mt.identifier, mt);
            scan.close();
        } catch (Exception e) {
            System.out.println("Error reading file " + f.getName());
            e.printStackTrace();
        } finally {
            if (scan != null)
                scan.close();
        }
    }

    public static void readSpawn(File f) {
        Scanner scan = null;
        try {
            scan = new Scanner(f);
            String[] data = scan.nextLine().split(" ");
            ArrayList<MobType> list = new ArrayList<MobType>();
            for (String s : data) {
                MobType mt = MobManager.mobTypes.get(s);
                if (mt == null) {
                    System.out.println("Invalid mob spawn: " + s + " in " + f.getPath());
                    continue;
                }
                list.add(mt);
            }
            data = scan.nextLine().split(" ");
            int spawnRange = Integer.parseInt(data[0]);
            int respawnDelay = Integer.parseInt(data[1]);
            int leash = Integer.parseInt(data[2]);
            while (scan.hasNextLine()) {
                data = scan.nextLine().trim().split(", ");
                double x = Double.parseDouble(data[0]);
                double y = Double.parseDouble(data[1]);
                double z = Double.parseDouble(data[2]);
                World w = plugin.getServer().getWorld(data[3]);
                if (w == null) {
                    System.out.println("Invalid mob spawn loc: " + Arrays.toString(data) + " in " + f.getPath());
                    continue;
                }
                Location loc = new Location(w, x, y, z);
                MobSpawn ms = new MobSpawn(list.toArray(new MobType[list.size()]), loc, spawnRange, respawnDelay, leash);
                spawns.add(ms);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (scan != null)
                scan.close();
        }
    }

    public static MobData createMob(String identifier, Location loc) {
        MobType mt = MobManager.mobTypes.get(identifier);
        if (mt == null) {
            Log.error("Attempted to spawn non-existing mob " + identifier + ".");
            return null;
        }
        return mt.spawn(loc);
    }
}
