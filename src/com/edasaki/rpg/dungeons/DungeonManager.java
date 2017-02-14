package com.edasaki.rpg.dungeons;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;

import com.edasaki.core.utils.ChunkWrapper;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RTicks;
import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.items.ItemManager;
import com.edasaki.rpg.items.RPGItem;
import com.edasaki.rpg.mobs.MobManager;
import com.edasaki.rpg.npcs.NPCManager;

public class DungeonManager extends AbstractManagerRPG {

    public static ArrayList<Dungeon> dungeons = new ArrayList<Dungeon>();

    public static ArrayList<DungeonVillager> dungeonVillagers;
    public static HashMap<ChunkWrapper, HashSet<DungeonBoss>> bossPerChunk = new HashMap<ChunkWrapper, HashSet<DungeonBoss>>();

    public static HashMap<UUID, DungeonBoss> spawners = new HashMap<UUID, DungeonBoss>();

    public DungeonManager(SakiRPG plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {
        reload();
    }

    public static void reload() {
        if (dungeonVillagers == null) {
            dungeonVillagers = new ArrayList<DungeonVillager>();
        } else {
            for (DungeonVillager sv : dungeonVillagers) {
                NPCManager.unregister(sv);
            }
            dungeonVillagers.clear();
        }
        dungeons.clear();
        File dir = new File(plugin.getDataFolder(), "dungeons");
        if (!dir.exists())
            dir.mkdirs();
        for (File f : dir.listFiles()) {
            if (f.getName().endsWith(".txt")) {
                readDungeon(f);
            }
        }
        Collections.shuffle(MobManager.spawns);
    }

    public static void readDungeon(File f) {
        Scanner scan = null;
        int id = 0;
        try {
            String next = "";
            scan = new Scanner(f);
            Dungeon d = new Dungeon();
            while (scan.hasNextLine()) {
                next = scan.nextLine().trim();
                if (next.length() == 0 || next.startsWith("#")) {
                    continue;
                }
                try {
                    String[] data = next.split(" ");
                    switch (data[0].toUpperCase()) {
                        case "NAME":
                            StringBuilder sb = new StringBuilder();
                            for (int k = 1; k < data.length; k++) {
                                sb.append(data[k]);
                                sb.append(' ');
                            }
                            d.dungeonName = sb.toString().trim();
                            break;
                        case "REGION":
                            sb = new StringBuilder();
                            for (int k = 1; k < data.length; k++) {
                                sb.append(data[k]);
                                sb.append(' ');
                            }
                            d.region = sb.toString().trim();
                            break;
                        case "DESC":
                            sb = new StringBuilder();
                            for (int k = 1; k < data.length; k++) {
                                sb.append(data[k]);
                                sb.append(' ');
                            }
                            d.description = sb.toString().trim();
                            break;
                        case "TIP":
                            sb = new StringBuilder();
                            for (int k = 1; k < data.length; k++) {
                                sb.append(data[k]);
                                sb.append(' ');
                            }
                            d.tips.add(sb.toString().trim());
                            break;
                        case "LEVEL":
                            d.recLevel = Integer.parseInt(data[1]);
                            break;
                        case "MASTER":
                            double x = Double.parseDouble(data[1]);
                            double y = Double.parseDouble(data[2]);
                            double z = Double.parseDouble(data[3]);
                            String world = data[4];
                            DungeonVillager dv = new DungeonVillager(++id, "Dungeon Master", x, y, z, world);
                            dv.dungeon = d;
                            dv.register();
                            d.dungeonMaster = dv;
                            dungeonVillagers.add(dv);
                            break;
                        case "SPAWN":
                            x = Double.parseDouble(data[1]);
                            y = Double.parseDouble(data[2]);
                            z = Double.parseDouble(data[3]);
                            float yaw = Float.parseFloat(data[4]);
                            float pitch = Float.parseFloat(data[5]);
                            World w = plugin.getServer().getWorld(data[6]);
                            if (w == null)
                                throw new Exception("Missing quest world " + data[6]);
                            Location loc = new Location(w, x, y, z, yaw, pitch);
                            d.dungeonSpawn = loc;
                            break;
                        //                        case "MOB":
                        //                            MobType mt = MobManager.mobTypes.get(data[1]);
                        //                            if (mt == null) {
                        //                                System.out.println("Invalid dungeon mob spawn: " + data[0]);
                        //                                continue;
                        //                            }
                        //                            x = Double.parseDouble(data[2]);
                        //                            y = Double.parseDouble(data[3]);
                        //                            z = Double.parseDouble(data[4]);
                        //                            w = plugin.getServer().getWorld(data[5]);
                        //                            if (w == null)
                        //                                return;
                        //                            loc = new Location(w, x, y, z);
                        //                            int radius = 50;
                        //                            int respawnDelay = 1;
                        //                            MobSpawn ms = new MobSpawn(mt, loc, radius, respawnDelay);
                        //                            MobManager.spawns.add(ms);
                        //                            break;
                        //                        case "BOSS":
                        //                            mt = MobManager.mobTypes.get(data[1]);
                        //                            x = Double.parseDouble(data[2]);
                        //                            y = Double.parseDouble(data[3]);
                        //                            z = Double.parseDouble(data[4]);
                        //                            w = plugin.getServer().getWorld(data[5]);
                        //                            if (w == null)
                        //                                return;
                        //                            loc = new Location(w, x, y, z);
                        //                            DungeonBoss db = new DungeonBoss();
                        //                            db.type = mt;
                        //                            db.setLoc(loc);
                        //                            db.dungeon = d;
                        //                            db.spawnSpawner();
                        //                            d.boss = db;
                        //                            DungeonManager.registerBoss(db);
                        //                            break;
                        case "EXIT":
                            x = Double.parseDouble(data[1]);
                            y = Double.parseDouble(data[2]);
                            z = Double.parseDouble(data[3]);
                            world = data[4];
                            dv = new DungeonVillager(++id, "Dungeon Master", x, y, z, world);
                            dv.dungeon = d;
                            dv.register();
                            d.dungeonExit = dv;
                            dv.isExit = true;
                            dungeonVillagers.add(dv);
                            break;
                        case "REWARD":
                            String s = data[1];
                            int min = Integer.parseInt(data[2]);
                            int max = Integer.parseInt(data[3]);
                            double chance = Double.parseDouble(data[4]);
                            RPGItem item = ItemManager.itemIdentifierToRPGItemMap.get(s);
                            if (item == null) {
                                throw new Exception("Failed to find dungeon reward with identifier " + s);
                            } else {
                                DungeonReward dr = new DungeonReward(item, min, max, chance);
                                d.rewardsList.add(dr);
                            }
                            break;
                        case "REWARDRAND":
                            int tier = Integer.parseInt(data[1]);
                            min = Integer.parseInt(data[2]);
                            max = Integer.parseInt(data[3]);
                            chance = Double.parseDouble(data[4]);
                            if (tier < 1 || tier > 5) {
                                throw new Exception("Failed to find dungeon reward with tier " + data[1]);
                            } else {
                                DungeonReward dr = new DungeonReward(tier, min, max, chance);
                                d.rewardsList.add(dr);
                            }
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    System.out.println("Error loading dungeon " + f.toString());
                    //                    e.printStackTrace();
                }
            }
            dungeons.add(d);
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

    public static void registerBoss(DungeonBoss db) {
        ChunkWrapper cw = new ChunkWrapper(db.getLoc().getChunk());
        if (!bossPerChunk.containsKey(cw)) {
            bossPerChunk.put(cw, new HashSet<DungeonBoss>());
        }
        bossPerChunk.get(cw).add(db);
    }

    public static void unregisterBoss(DungeonBoss db) {
        try {
            db.despawn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ChunkWrapper cw = new ChunkWrapper(db.getLoc().getChunk());
        if (bossPerChunk.containsKey(cw)) {
            bossPerChunk.get(cw).remove(db);
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        handleChunk(event.getChunk());
    }

    public static void handleChunk(Chunk chunk) {
        final ChunkWrapper cw = new ChunkWrapper(chunk);
        if (!bossPerChunk.containsKey(cw))
            return;
        RScheduler.schedule(plugin, new Runnable() {
            public void run() {
                HashSet<DungeonBoss> bosses = bossPerChunk.get(cw);
                for (DungeonBoss db : bosses)
                    if (!db.isStillSpawned())
                        db.spawnSpawner();
            }
        }, RTicks.seconds(2));
    }

    public static void registerSpawner(EnderCrystal spawner, DungeonBoss dungeonBoss) {
        spawners.put(spawner.getUniqueId(), dungeonBoss);
    }

    public void handleBossSpawn(Entity player, Entity e) {
        if (!(player instanceof Player))
            return;
        Player p = (Player) player;
        PlayerDataRPG pd = plugin.getPD(p);
        if (pd != null && spawners.containsKey(e.getUniqueId())) {
            try {
                DungeonBoss db = spawners.remove(e.getUniqueId());
                db.spawnBoss();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.ENDER_CRYSTAL) {
            handleBossSpawn(event.getPlayer(), event.getRightClicked());
            event.setCancelled(true);
        } else if (event.getRightClicked().getType() == EntityType.ARMOR_STAND) {
            handleRewards(event.getPlayer(), event.getRightClicked());
        }
    }

    @EventHandler
    public void onPlayerHitDungeonDisplay(EntityDamageByEntityEvent event) {
        if (event.getEntityType() == EntityType.ENDER_CRYSTAL) {
            if (event.getDamager() instanceof Player)
                handleBossSpawn(event.getDamager(), event.getEntity());
            event.setCancelled(true);
        } else if (event.getEntityType() == EntityType.ARMOR_STAND) {
            if (event.getDamager() instanceof Player)
                handleRewards(event.getDamager(), event.getEntity());
        }
    }

    public void handleRewards(Entity player, Entity e) {
        if (!(player instanceof Player))
            return;
        Player p = (Player) player;
        PlayerDataRPG pd = plugin.getPD(p);
        if (pd != null && rewardsToBosses.containsKey(e.getUniqueId())) {
            try {
                DungeonBoss db = rewardsToBosses.get(e.getUniqueId());
                db.dungeon.handleRewards(p, pd);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static HashMap<UUID, DungeonBoss> rewardsToBosses = new HashMap<UUID, DungeonBoss>();

    public static void rewardsChest(Dungeon dungeon) {
        DungeonBoss db = dungeon.boss;
        World w = db.getLoc().getWorld();
        ArmorStand as = (ArmorStand) w.spawnEntity(db.getLoc().add(0, -1.00, 0), EntityType.ARMOR_STAND);
        as.setGravity(false);
        as.setAI(false);
        as.setVisible(false);
        as.setCustomName(ChatColor.GOLD + "Dungeon Rewards");
        as.setCustomNameVisible(true);
        as.setHelmet(new ItemStack(Material.CHEST));

        ArmorStand as2 = (ArmorStand) w.spawnEntity(db.getLoc().add(0, -0.25, 0), EntityType.ARMOR_STAND);
        as2.setGravity(false);
        as2.setAI(false);
        as2.setVisible(false);
        as2.setCustomName(ChatColor.GREEN + "Disappearing in " + ChatColor.YELLOW + ChatColor.BOLD + "60" + ChatColor.GREEN + "...");
        as2.setCustomNameVisible(true);

        ArmorStand as3 = (ArmorStand) w.spawnEntity(db.getLoc().add(0, -0.50, 0), EntityType.ARMOR_STAND);
        as3.setGravity(false);
        as3.setAI(false);
        as3.setVisible(false);
        as3.setCustomName(ChatColor.GRAY + "[Click to Open]");
        as3.setCustomNameVisible(true);

        db.rewardsStage = true;
        Chunk chunk = as.getLocation().getChunk();
        Chunk chunk2 = as2.getLocation().getChunk();
        Chunk chunk3 = as3.getLocation().getChunk();

        rewardsToBosses.put(as.getUniqueId(), db);
        rewardsToBosses.put(as2.getUniqueId(), db);
        rewardsToBosses.put(as3.getUniqueId(), db);

        RScheduler.schedule(plugin, new Runnable() {
            int counter = 0;
            int sec = 60;

            public void run() {
                if (!chunk.isLoaded() || !chunk2.isLoaded() || !chunk3.isLoaded() || !as.isValid() || !as2.isValid() || !as3.isValid()) {
                    db.rewardsStage = false;
                    try {
                        as.remove();
                        as2.remove();
                        as3.remove();
                        rewardsToBosses.remove(as.getUniqueId());
                        rewardsToBosses.remove(as2.getUniqueId());
                        rewardsToBosses.remove(as3.getUniqueId());
                        dungeon.boss.spawnSpawner();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
                ((CraftArmorStand) as).getHandle().yaw += 10;
                if (counter++ < RTicks.seconds(60)) {
                    RScheduler.schedule(plugin, this, 1);
                    if (this.counter % 20 == 0)
                        as2.setCustomName(ChatColor.GREEN + "Disappearing in " + ChatColor.YELLOW + ChatColor.BOLD + (--sec) + ChatColor.GREEN + "...");
                } else {
                    db.rewardsStage = false;
                    as.remove();
                    as2.remove();
                    as3.remove();
                    rewardsToBosses.remove(as.getUniqueId());
                    rewardsToBosses.remove(as2.getUniqueId());
                    rewardsToBosses.remove(as3.getUniqueId());
                    dungeon.boss.spawnSpawner();
                }
            }
        }, 1);
    }

    public static void dispose(UUID uuid) {
        for (Dungeon d : dungeons)
            d.givenRewards.remove(uuid);
    }
}
