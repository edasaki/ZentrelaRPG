package com.edasaki.rpg.dungeons;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RParticles;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RTags;
import com.edasaki.core.utils.RTicks;
import com.edasaki.rpg.mobs.MobData;
import com.edasaki.rpg.mobs.MobType;

import de.slikey.effectlib.util.ParticleEffect;

public class DungeonBoss {

    public MobType type;
    private Location loc;

    public MobData spawnedBoss;

    public EnderCrystal spawner;
    public ArmorStand as;

    public Dungeon dungeon;

    public boolean rewardsStage;

    public Location getLoc() {
        return loc.clone();
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public boolean spawnSpawner() {
        if (loc.getChunk().isLoaded() && !rewardsStage) {
            despawn();
            if (spawner != null) {
                try {
                    spawner.remove();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (as != null) {
                try {
                    as.remove();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ArrayList<Entity> ls = RMath.getNearbyEntities(loc, 3);
            for (Entity e : ls) {
                if (e instanceof EnderCrystal) {
                    DungeonManager.spawners.remove(e.getUniqueId());
                    e.remove();
                } else if (e instanceof ArmorStand) {
                    ArmorStand temp = (ArmorStand) e;
                    if (temp.getCustomName() != null && temp.getCustomName().contains("to spawn")) {
                        DungeonManager.spawners.remove(temp.getUniqueId());
                        temp.remove();
                    }
                }
            }
            this.dungeon.clearRewarded();
            spawner = (EnderCrystal) loc.getWorld().spawnEntity(getLoc().add(0, 0.2, 0), EntityType.ENDER_CRYSTAL);
            as = RTags.makeStand(ChatColor.GRAY + "[Hit to spawn dungeon boss]", getLoc().add(0, 1.75, 0), true);
            DungeonManager.registerSpawner(spawner, this);
            return true;
        }
        return false;
    }

    public void despawn() {
        this.dungeon.clearRewarded();
        if (spawner != null) {
            spawner.remove();
            spawner = null;
        }
        if (as != null) {
            as.remove();
            as = null;
        }
        if (spawnedBoss != null) {
            spawnedBoss.despawn();
            spawnedBoss = null;
        }
    }

    public void spawnBoss() {
        despawn();
        RScheduler.schedule(DungeonManager.plugin, new Runnable() {

            int count = 0;

            @Override
            public void run() {
                if (count++ >= 3) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(ChatColor.GOLD);
                    sb.append(ChatColor.BOLD);
                    sb.append(type.name);
                    String name = sb.toString();
                    spawnedBoss = type.spawn(loc, name);
                    spawnedBoss.isBoss = true;
                    spawnedBoss.dungeon = dungeon;
                    RParticles.show(ParticleEffect.EXPLOSION_HUGE, getLoc().add(0, 1, 0));
                } else {
                    RParticles.showWithOffsetPositiveY(ParticleEffect.SMOKE_LARGE, loc, 2.0, 20);
                    RParticles.showWithOffsetPositiveY(ParticleEffect.SPELL_MOB, loc, 2.0, 20);
                    RScheduler.schedule(DungeonManager.plugin, this, RTicks.seconds(1));
                }
            }
        });
    }

    @Override
    public String toString() {
        return this.type.name + "-BOSS";
    }

    /**
     * OK to spawn the spawner now? true if not (something is still spawned), false if ok to spawn
     */
    public boolean isStillSpawned() {
        if (rewardsStage)
            return true;
        if (spawnedBoss != null) {
            if (spawnedBoss.dead || spawnedBoss.despawned)
                return false;
            if (!spawnedBoss.dead)
                return true;
        }
        return false;
    }

}
