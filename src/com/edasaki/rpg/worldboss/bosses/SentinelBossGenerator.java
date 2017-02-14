package com.edasaki.rpg.worldboss.bosses;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RParticles;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RSound;
import com.edasaki.core.utils.RTicks;
import com.edasaki.core.utils.entities.CustomIronGolem;
import com.edasaki.rpg.mobs.MobAttribute;
import com.edasaki.rpg.mobs.MobData;
import com.edasaki.rpg.mobs.MobDrop;
import com.edasaki.rpg.mobs.MobType;
import com.edasaki.rpg.mobs.spells.MobSpell;
import com.edasaki.rpg.spells.Spell;

import de.slikey.effectlib.util.ParticleEffect;

public class SentinelBossGenerator extends BossGenerator {

    public static final String[] NAMES = { "Idunn", "Iris", "Inton" };
    public static final String[] ADJECTIVES = { "Ancient" };
    public static final String[] TYPES = { "Sentinel" };

    @Override
    public MobData generate(Location loc, int level) {
        StringBuilder sb = new StringBuilder();
        final String randName = RMath.randObject(NAMES);
        final String randAdj = RMath.randObject(ADJECTIVES);
        final String randType = RMath.randObject(TYPES);
        sb.append(ChatColor.GOLD);
        sb.append(ChatColor.BOLD);
        sb.append(randName);
        sb.append(" the ");
        sb.append(randAdj);
        sb.append(' ');
        sb.append(randType);
        String name = sb.toString();

        MobType mt = new MobType();
        mt.identifier = name.toLowerCase();
        mt.name = name;
        mt.entityClass = CustomIronGolem.class;
        mt.level = level;
        mt.prefixes = new ArrayList<String>();
        mt.suffixes = new ArrayList<String>();
        mt.exp = getExp(level);
        mt.damageLow = getDamageLow(level);
        mt.damageHigh = (int) (mt.damageLow * 1.3);
        mt.maxHP = mt.damageLow * 2250;
        mt.equips = new ArrayList<ItemStack>();
        mt.attributes = new ArrayList<MobAttribute>();
        mt.spells = new ArrayList<MobSpell>();
        mt.drops = new ArrayList<MobDrop>();

        final MobData md = mt.spawn(loc, name);
        LivingEntity le = md.entity;
        le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 1));
        md.noKnockback = true;
        md.bossAI = new BossAIRunnable() {
            @Override
            public void tick() {
                if (ai.target != null) {
                    if (ai.counter % 24 == 0 && Math.random() < 0.8) {
                        //                        md.shout(ChatColor.RED + "Burn, mortal!", 12);
                        final ArrayList<Entity> hit = new ArrayList<Entity>();
                        final Location start = md.entity.getLocation();
                        start.setY(start.getY() + 1.3);
                        for (Vector v : getVectorsFlameWave(md.entity)) {
                            ArrayList<Location> locs = RMath.calculateVectorPath(start.clone(), v, 15, 4);
                            int count = 0;
                            for (int k = 0; k < locs.size(); k++) {
                                final Location loc = locs.get(k);
                                RScheduler.schedule(Spell.plugin, new Runnable() {
                                    public void run() {
                                        RParticles.showWithOffset(ParticleEffect.FLAME, loc, 0.2, 1);
                                        int damage = md.getDamage();
                                        ArrayList<Entity> damaged = Spell.damageNearby(damage * 2, md.entity, loc, 1.0, hit, true, false, true);
                                        hit.addAll(damaged);
                                    }
                                }, 1 * count);
                                if (k % 2 == 0)
                                    count++;
                            }
                        }
                    }
                    if (ai.counter % 6 == 0) {
                        final ArrayList<Entity> hit = new ArrayList<Entity>();
                        for (int a = 1; a <= 3; a++) {
                            final Location start = md.entity.getLocation();
                            start.setY(start.getY() + 0.7 * a);
                            for (Vector v : getVectorsNormal(md.entity)) {
                                ArrayList<Location> locs = RMath.calculateVectorPath(start.clone(), v, 12, 4);
                                int count = 0;
                                for (int k = 0; k < locs.size(); k++) {
                                    final Location loc = locs.get(k);
                                    RScheduler.schedule(Spell.plugin, new Runnable() {
                                        public void run() {
                                            RParticles.showWithOffset(ParticleEffect.CRIT, loc, 0.2, 1);
                                            int damage = md.getDamage();
                                            ArrayList<Entity> damaged = Spell.damageNearby(damage, md.entity, loc, 1.0, hit, true, false, true);
                                            hit.addAll(damaged);
                                        }
                                    }, 1 * count);
                                    if (k % 2 == 0)
                                        count++;
                                }
                            }
                        }
                    }
                    if (ai.counter % 60 == 0) {
                        Location loc = md.entity.getLocation();
                        md.shout(ChatColor.YELLOW + "Behold, the power of the Spirits!", 40);
                        ArrayList<Location> locs = new ArrayList<Location>();
                        for (int dx = -15; dx <= 15; dx++) {
                            for (int dz = -15; dz <= 15; dz++) {
                                if (Math.random() < 0.03) {
                                    Location temp = loc.clone();
                                    temp.add(dx, 0, dz);
                                    locs.add(temp);
                                }
                            }
                        }
                        Collections.shuffle(locs);
                        for (int k = 0; k < locs.size(); k++) {
                            final Location temp = locs.get(k);
                            RScheduler.schedule(plugin, new Runnable() {
                                public void run() {
                                    if (md.dead)
                                        return;
                                    RParticles.showWithOffsetPositiveY(ParticleEffect.VILLAGER_HAPPY, temp, 1.5, 30);
                                    RScheduler.schedule(plugin, new Runnable() {
                                        public void run() {
                                            if (md.dead)
                                                return;
                                            RParticles.sendLightning(null, temp);
                                            Spell.damageNearby(md.getDamage() * 4, md.entity, temp, 3.5, new ArrayList<Entity>(), true, false, true);
                                        }
                                    }, 20);
                                }
                            }, k * 3 + (int) (Math.random()) * 30);
                        }
                    }
                    if (ai.counter % 36 == 0) {
                        for (Entity e : RMath.getNearbyEntitiesCylinder(md.entity.getLocation(), 20, 9)) {
                            if (e instanceof Player) {
                                Player p2 = (Player) e;
                                if (Spell.canDamage(p2, false)) {
                                    Vector pullVector = e.getLocation().toVector().subtract(md.entity.getLocation().toVector()).normalize().multiply(-1.8);
                                    pullVector.setY(pullVector.getY() + 0.35);
                                    e.setVelocity(pullVector);
                                }
                            }
                        }
                    }
                }
            }
        };
        return md;
    }

    private ArrayList<Vector> getVectorsNormal(LivingEntity e) {
        ArrayList<Vector> vectors = new ArrayList<Vector>();
        Vector v = e.getEyeLocation().getDirection().normalize();
        v.setY(0);
        vectors.add(v);
        double z = v.getZ();
        double x = v.getX();
        double radians = Math.atan(z / x);
        if (x < 0)
            radians += Math.PI;
        for (int k = 1; k < 4; k++) {
            Vector v2 = new Vector();
            v2.setY(v.getY());
            v2.setX(Math.cos(radians + k * Math.PI / 2));
            v2.setZ(Math.sin(radians + k * Math.PI / 2));
            vectors.add(v2.normalize());
        }
        return vectors;
    }

    private ArrayList<Vector> getVectorsFlameWave(LivingEntity e) {
        ArrayList<Vector> vectors = new ArrayList<Vector>();
        Vector v = e.getEyeLocation().getDirection().normalize();
        v.setY(0);
        vectors.add(v);
        double z = v.getZ();
        double x = v.getX();
        double radians = Math.atan(z / x);
        if (x < 0)
            radians += Math.PI;
        for (int k = 1; k < 24; k++) {
            Vector v2 = new Vector();
            v2.setY(v.getY());
            v2.setX(Math.cos(radians + k * Math.PI / 12));
            v2.setZ(Math.sin(radians + k * Math.PI / 12));
            vectors.add(v2.normalize());
        }
        return vectors;
    }

    public int getDamageLow(int level) {
        switch (getTier(level)) {
            case 1:
                return (int) (1.5 * level);
            case 2:
                return (int) (1.8 * level);
            case 3:
                return (int) (2.1 * level);
            case 4:
                return (int) (2.4 * level);
            case 5:
                return (int) (2.7 * level);
        }
        return (int) (3.0 * level);
    }

    public int getExp(int level) {
        return level * 100;
    }

    @Override
    public String getMessage1() {
        return "You sense a sudden shift in the mana balance of the world.";
    }

    @Override
    public String getMessage2() {
        return "You can feel electricity in the air. How strange.";
    }

    @Override
    public String getMessage3() {
        return "It feels like the center of power in the world has shifted.";
    }

    @Override
    public void playSound(Player p) {
        RScheduler.schedule(plugin, new Runnable() {
            int counter = 0;

            public void run() {
                if (p == null || !p.isOnline())
                    return;
                if (Math.random() < 0.5)
                    RSound.playSound(p, Sound.ENTITY_LIGHTNING_THUNDER);
                else
                    RSound.playSound(p, Sound.ENTITY_SKELETON_AMBIENT);
                counter++;
                if (counter < 10)
                    RScheduler.schedule(plugin, this, RTicks.seconds(0.5));
            }
        });
    }

}
