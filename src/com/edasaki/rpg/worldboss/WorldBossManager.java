package com.edasaki.rpg.worldboss;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import com.edasaki.core.PlayerData;
import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RMessages;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RTicks;
import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.mobs.MobData;
import com.edasaki.rpg.warps.WarpManager;
import com.edasaki.rpg.worldboss.bosses.BossGenerator;
import com.edasaki.rpg.worldboss.bosses.DeathriderBossGenerator;
import com.edasaki.rpg.worldboss.bosses.SentinelBossGenerator;
import com.edasaki.rpg.worldboss.bosses.ZombieBossGenerator;

public class WorldBossManager extends AbstractManagerRPG {

    private static WorldBossManager instance;

    private Location loc;
    private World world;

    private long nextBossSpawnTime;

    private boolean worldBossInProgress = false;
    private MobData worldBoss;

    private Location lobbyLoc;
    private static final double[][] COORDS = {
            { -232.5, 99, -321.5, 180 },
            { -208.0, 99, -329.0, 135 },
            { -201.0, 99, -354.0, 90 },
            { -214.5, 99, -384.5, 45 },
            { -236.5, 99, -389.5, 0 },
            { -262.5, 99, -380.0, -45 },
            { -270.0, 99, -353.5, -90 },
            { -260.0, 99, -331.5, -135 },
    };
    private Location[] entryLocs;

    public static final BossGenerator[] GENERATORS = { new ZombieBossGenerator(), new SentinelBossGenerator(), new DeathriderBossGenerator() };

    public WorldBossManager(SakiRPG plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {
        world = plugin.getServer().createWorld(new WorldCreator(SakiRPG.BOSS_WORLD));
        lobbyLoc = new Location(world, -249.5f, 112.5f, -945.5f, -180 + RMath.randInt(0, 3) * 90, 0);
        nextBossSpawnTime = System.currentTimeMillis() + getRandDelay();
        task();
        entryLocs = new Location[COORDS.length];
        for (int k = 0; k < COORDS.length; k++) {
            entryLocs[k] = new Location(world, COORDS[k][0], COORDS[k][1], COORDS[k][2], (float) COORDS[k][3], 0f);
        }
        instance = this;
    }

    private long getRandDelay() {
        // 30 to 90 minutes
        return RMath.randLong(60 * 1000 * 30, 60 * 1000 * 90);
    }

    public static BossGenerator getRandomBoss() {
        return RMath.randObject(GENERATORS);
    }

    private void task() {
        RScheduler.schedule(plugin, new Runnable() {
            public void run() {
                if (System.currentTimeMillis() - nextBossSpawnTime > 0) {
                    spawn();
                    nextBossSpawnTime = System.currentTimeMillis() + getRandDelay();
                    System.out.println("Preparing to spawn a boss, and next one is set for " + (nextBossSpawnTime - System.currentTimeMillis()) / 1000.0 + " seconds.");
                }
                if (world != null)
                    world.setTime(18000L);
                RScheduler.schedule(plugin, this, 20);
            }
        }, 20);
    }

    public void spawn() {
        if (worldBossInProgress && worldBoss != null && !worldBoss.dead) {
            System.out.println("Failed to spawn: Boss currently spawned.");
            return;
        }
        worldBossInProgress = true;
        if (worldBoss != null)
            worldBoss.despawn();
        BossGenerator generator = getRandomBoss();
        RMessages.announce("");
        RMessages.announce(ChatColor.GOLD + "> " + ChatColor.DARK_RED + ChatColor.BOLD + generator.getMessage1());
        RScheduler.schedule(plugin, () -> {
            RMessages.announce("");
            RMessages.announce(ChatColor.GOLD + "> " + ChatColor.DARK_RED + ChatColor.BOLD + generator.getMessage2());
            RScheduler.schedule(plugin, () -> {
                RMessages.announce("");
                RMessages.announce(ChatColor.GOLD + "> " + ChatColor.DARK_RED + ChatColor.BOLD + generator.getMessage3());
                for (Player p : plugin.getServer().getOnlinePlayers())
                    generator.playSound(p);
                worldBossInProgress = true;
                RScheduler.schedule(plugin, () -> {
                    RMessages.announce("");
                    int level = getLevel();
                    this.worldBoss = generator.generate(loc, level);
                    this.worldBoss.isBoss = true;
                    this.worldBoss.isWorldBoss = true;
                    this.worldBoss.worldBossValue = getValue(level);
                    RMessages.announce(ChatColor.translateAlternateColorCodes('&', "&7>> &4&lWARNING: &cA world boss has appeared!"));
                    //                    RMessages.announce(ChatColor.translateAlternateColorCodes('&', "&7>>> &6" + name + "&7 - &6Level " + level));
                    RMessages.announce(ChatColor.translateAlternateColorCodes('&', "&7>>> &eJoin the fight to protect Zentrela!"));
                    for (Player p : plugin.getServer().getOnlinePlayers())
                        generator.playSound(p);
                }, RTicks.seconds(15));
            }, RTicks.minutes(2));
        }, RTicks.minutes(3));
    }

    private int getValue(int level) {
        int result = (int) Math.ceil(10 * Math.pow(1.1, level - 10));
        if (result < 10)
            result = 10;
        return result;
    }

    private int getLevel() {
        int total = 0, count = 0;
        for (PlayerData pd : plugin.getAllPlayerdatas()) {
            if (pd instanceof PlayerDataRPG) {
                count++;
                total += ((PlayerDataRPG) pd).level;
            }
        }
        if (count == 0)
            count = 1;
        double avg = total / (double) count;
        avg += avg * RMath.randDouble(-0.05, 0.10); //-5% to +10% variance
        int res = (int) Math.ceil(avg);
        if (res < 10)
            res = RMath.randInt(10, 20);
        return res;
    }

    private void warp(Player p) {
        p.teleport(getRandomWarpLoc());
    }

    private Location getRandomWarpLoc() {
        return RMath.randObject(this.entryLocs);
    }

    public void enterLobby(Player p) {
        WarpManager.warp(p, this.lobbyLoc, null);
    }

    public static void checkLocation(Player p) {
        if (p.getWorld().equals(instance.world) && p.getLocation().getBlock().getType() == Material.PORTAL) {
            instance.warp(p);
        }
    }
}
