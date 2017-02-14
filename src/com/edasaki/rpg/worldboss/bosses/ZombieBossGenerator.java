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
import com.edasaki.core.utils.entities.CustomZombie;
import com.edasaki.rpg.mobs.MobAttribute;
import com.edasaki.rpg.mobs.MobData;
import com.edasaki.rpg.mobs.MobDrop;
import com.edasaki.rpg.mobs.MobType;
import com.edasaki.rpg.mobs.spells.MobSpell;
import com.edasaki.rpg.spells.Spell;

import de.slikey.effectlib.util.ParticleEffect;

public class ZombieBossGenerator extends BossGenerator {

    public static final String[] NAMES = { "Kaliya", "Faun", "Ragnor" };
    public static final String[] ADJECTIVES = { "Hungry", "Rotten" };
    public static final String[] TYPES = { "Zombo" };

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
        mt.entityClass = CustomZombie.class;
        mt.level = level;
        mt.prefixes = new ArrayList<String>();
        mt.suffixes = new ArrayList<String>();
        mt.exp = getExp(level);
        mt.damageLow = getDamageLow(level);
        mt.damageHigh = (int) (mt.damageLow * 1.5);
        mt.maxHP = mt.damageLow * 1000;
        mt.equips = new ArrayList<ItemStack>();
        mt.attributes = new ArrayList<MobAttribute>();
        mt.spells = new ArrayList<MobSpell>();
        mt.drops = new ArrayList<MobDrop>();

        final MobType mt2 = new MobType();
        mt2.identifier = name.toLowerCase() + "_minion";
        mt2.name = name;
        mt2.entityClass = CustomZombie.class;
        mt2.level = level - 5 > 0 ? level - 5 : 1;
        mt2.prefixes = new ArrayList<String>();
        mt2.suffixes = new ArrayList<String>();
        mt2.exp = (int) (mt.exp * 0.1);
        mt2.damageLow = (int) (mt.damageLow * 0.5);
        mt2.damageHigh = (int) (mt.damageHigh * 0.5);
        mt2.maxHP = mt2.damageLow * 20;
        mt2.equips = new ArrayList<ItemStack>();
        mt2.attributes = new ArrayList<MobAttribute>();
        mt2.spells = new ArrayList<MobSpell>();
        mt2.drops = new ArrayList<MobDrop>();

        final MobType mt3 = new MobType();
        mt3.identifier = name.toLowerCase() + "_guardian";
        mt3.name = name;
        mt3.entityClass = CustomIronGolem.class;
        mt3.level = level - 2 > 0 ? level - 2 : 1;
        mt3.prefixes = new ArrayList<String>();
        mt3.suffixes = new ArrayList<String>();
        mt3.exp = (int) (mt.exp * 0.2);
        mt3.damageLow = (int) (mt.damageLow * 0.7);
        mt3.damageHigh = (int) (mt.damageHigh * 0.7);
        mt3.maxHP = mt2.damageLow * 40;
        mt3.equips = new ArrayList<ItemStack>();
        mt3.attributes = new ArrayList<MobAttribute>();
        mt3.spells = new ArrayList<MobSpell>();
        mt3.drops = new ArrayList<MobDrop>();

        final MobData md = mt.spawn(loc, name);
        md.noKnockback = true;
        md.bossAI = new BossAIRunnable() {
            @Override
            public void tick() {
                if (ai.target != null) {
                    if (ai.counter % 18 == 0 && Math.random() < 0.7) {
                        for (Entity e : md.entity.getNearbyEntities(20, 10, 20)) {
                            if (e instanceof Player) {
                                Player p = (Player) e;
                                if (!Spell.canDamage(p, false))
                                    continue;
                                int num = (int) (Math.random() * 2);
                                for (int k = 0; k < num; k++) {
                                    double rand = Math.random();
                                    if (rand < 0.15)
                                        mt3.spawn(p.getLocation().add(0, 0.3, 0), ChatColor.GOLD + randName + "'s Guardian");
                                    else
                                        mt2.spawn(p.getLocation().add(0, 0.3, 0), ChatColor.GOLD + randName + "'s Minion");
                                }
                            }
                        }
                    }
                    if (ai.counter % 48 == 0 && Math.random() < 0.7) {
                        Location loc = md.entity.getLocation();
                        md.shout(ChatColor.YELLOW + "Careful! I don't want a burnt dinner.", 40);
                        ArrayList<Location> locs = new ArrayList<Location>();
                        for (int dx = -15; dx <= 15; dx++) {
                            for (int dz = -15; dz <= 15; dz++) {
                                if (Math.random() < 0.07) {
                                    Location temp = loc.clone();
                                    temp.add(dx, 0, dz);
                                    locs.add(temp);
                                }
                            }
                        }
                        Collections.shuffle(locs);
                        for (final Location temp : locs) {
                            RScheduler.schedule(plugin, new Runnable() {
                                private int counter = 0;

                                public void run() {
                                    if (md.dead)
                                        return;
                                    if (counter++ < 3) {
                                        RParticles.showWithOffsetPositiveY(ParticleEffect.FLAME, temp, 0.5, 30);
                                        RScheduler.schedule(plugin, this, 20);
                                    } else {
                                        RParticles.showWithOffsetPositiveY(ParticleEffect.FLAME, temp, 3.0, 50);
                                        for (Entity e : RMath.getNearbyEntitiesCylinder(temp, 3, 4)) {
                                            if (e instanceof Player && Spell.canDamage(e, false)) {
                                                e.setVelocity(new Vector(0, 1.5, 0));
                                            }
                                        }
                                    }
                                }
                            }, 20);
                        }
                    }
                    if (ai.counter % 15 == 0 && Math.random() < 0.8) {
                        Spell.damageNearby(md.getDamage(), md.entity, md.entity.getLocation(), 3.0, new ArrayList<Entity>(), true, false, true);
                        RParticles.showWithOffset(ParticleEffect.EXPLOSION_LARGE, md.entity.getLocation(), 3.0, 10);
                    }
                    if (ai.counter % 12 == 0 && Math.random() < 0.8) {
                        md.entity.setVelocity(ai.target.getLocation().subtract(md.entity.getLocation()).toVector().normalize().setY(0.5));
                        RScheduler.schedule(Spell.plugin, new Runnable() {
                            public void run() {
                                Spell.damageNearby(md.getDamage(), md.entity, md.entity.getLocation(), 3.0, new ArrayList<Entity>(), true, false, true);
                                RParticles.showWithOffset(ParticleEffect.EXPLOSION_LARGE, md.entity.getLocation(), 3.0, 30);
                            }
                        }, RTicks.seconds(0.7));
                    }
                    if (ai.counter % 40 == 0 && Math.random() < 0.5) {
                        for (Entity e : md.entity.getNearbyEntities(10, 10, 10)) {
                            if (e instanceof Player) {
                                Player p = (Player) e;
                                if (!Spell.canDamage(p, false))
                                    continue;
                                if (Math.random() < 0.7) {
                                    if (Math.random() < 0.5)
                                        md.say(p, ChatColor.RED + "Come closer, delicious human.");
                                    else
                                        md.say(p, ChatColor.RED + "Allow me to taste your juicy flesh, my friend.");
                                    p.teleport(md.entity.getLocation());
                                }
                            }
                        }
                    }
                    if (ai.counter % 36 == 0 && Math.random() < 0.8) {
                        for (Entity e : md.entity.getNearbyEntities(10, 10, 10)) {
                            if (e instanceof Player) {
                                Player p = (Player) e;
                                if (!Spell.canDamage(p, false))
                                    continue;
                                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, RTicks.seconds(5), 1), true);
                                p.sendMessage(ChatColor.GRAY + ">> " + ChatColor.RESET + md.nameWithoutLevel + ": " + ChatColor.AQUA + "Slow down!");
                            }
                        }
                    }
                }
            }
        };
        LivingEntity le = md.entity;
        le.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        return md;
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
                return (int) (2.8 * level);
            case 5:
                return (int) (4.0 * level);
        }
        return (int) (5.0 * level);
    }

    public int getExp(int level) {
        return level * 100;
    }

    @Override
    public String getMessage1() {
        return "You hear a low growling sound. Strange.";
    }

    @Override
    public String getMessage2() {
        return "You smell rotten fresh. Ew.";
    }

    @Override
    public String getMessage3() {
        return "You hear a low rumbling from the ground. That can't be good.";
    }

    @Override
    public void playSound(Player p) {
        RScheduler.schedule(plugin, new Runnable() {
            int counter = 0;

            public void run() {
                if (p == null || !p.isOnline())
                    return;
                if (Math.random() < 0.5)
                    RSound.playSound(p, Sound.ENTITY_ZOMBIE_AMBIENT);
                else
                    RSound.playSound(p, Sound.ENTITY_ZOMBIE_STEP);
                counter++;
                if (counter < 10)
                    RScheduler.schedule(plugin, this, RTicks.seconds(0.5));
            }
        });
    }

}
