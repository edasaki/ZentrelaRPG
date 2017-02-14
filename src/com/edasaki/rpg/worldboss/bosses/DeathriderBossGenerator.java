package com.edasaki.rpg.worldboss.bosses;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.edasaki.core.utils.REntities;
import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RMetadata;
import com.edasaki.core.utils.RParticles;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RSound;
import com.edasaki.core.utils.RTicks;
import com.edasaki.core.utils.entities.CustomHorse;
import com.edasaki.core.utils.entities.CustomSkeleton;
import com.edasaki.rpg.drops.DropManager;
import com.edasaki.rpg.mobs.MobAttribute;
import com.edasaki.rpg.mobs.MobData;
import com.edasaki.rpg.mobs.MobDrop;
import com.edasaki.rpg.mobs.MobType;
import com.edasaki.rpg.mobs.spells.MobSpell;
import com.edasaki.rpg.spells.Spell;

import de.slikey.effectlib.util.ParticleEffect;
import net.minecraft.server.v1_10_R1.AttributeInstance;
import net.minecraft.server.v1_10_R1.EntityArrow;
import net.minecraft.server.v1_10_R1.EntityInsentient;
import net.minecraft.server.v1_10_R1.EntityLiving;
import net.minecraft.server.v1_10_R1.EntityTippedArrow;
import net.minecraft.server.v1_10_R1.GenericAttributes;
import net.minecraft.server.v1_10_R1.MathHelper;

public class DeathriderBossGenerator extends BossGenerator {

    public static final String[] NAMES = { "Skeletor", "Arikor", "Obnor" };
    public static final String[] ADJECTIVES = { "" };
    public static final String[] TYPES = { "Deathrider" };

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
        if (randAdj.length() > 0) {
            sb.append(randAdj);
            sb.append(' ');
        }
        sb.append(randType);
        String name = sb.toString();

        MobType mt = new MobType();
        mt.identifier = name.toLowerCase();
        mt.name = name;
        mt.entityClass = CustomSkeleton.class;
        mt.level = level;
        mt.prefixes = new ArrayList<String>();
        mt.suffixes = new ArrayList<String>();
        mt.exp = getExp(level);
        mt.damageLow = (int) getDamageLow(level);
        mt.damageHigh = (int) (mt.damageLow * 1.5);
        mt.maxHP = mt.damageLow * 650;
        mt.equips = new ArrayList<ItemStack>();
        mt.equips.add(new ItemStack(Material.BOW));
        mt.attributes = new ArrayList<MobAttribute>();
        mt.spells = new ArrayList<MobSpell>();
        mt.drops = new ArrayList<MobDrop>();

        final MobData md = mt.spawn(loc, name);
        LivingEntity le = md.entity;
        le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 1));
        md.noKnockback = true;
        Horse horseMount = (Horse) REntities.createLivingEntity(CustomHorse.class, md.entity.getLocation());
        horseMount.setVariant(Variant.SKELETON_HORSE);
        horseMount.setAdult();
        horseMount.setTamed(true);
        AttributeInstance attributes = ((EntityInsentient) ((CraftLivingEntity) horseMount).getHandle()).getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
        attributes.setValue(0.40);
        md.mount(horseMount);
        ((Skeleton) md.entity).setSkeletonType(SkeletonType.WITHER);
        md.ai.ranged = true;
        md.ai.rangeDistance = 20;
        md.bossAI = new BossAIRunnable() {
            private ArrayList<Location> locs = new ArrayList<Location>();

            @Override
            public void tick() {
                if (ai.target != null) {
                    if (ai.counter % 4 == 0) {
                        md.ai.lastAttackTickCounter = 0;
                        for (Entity target : RMath.getNearbyEntitiesCylinder(md.entity.getLocation(), md.ai.rangeDistance, 9)) {
                            if (target instanceof Player && Spell.canDamage(target, false)) {
                                if (Math.random() < 0.3)
                                    continue;
                                EntityLiving bossEntity = (EntityLiving) ((CraftEntity) md.entity).getHandle();
                                //EntityArrow entityarrow = new EntityArrow(bossEntity.world, bossEntity, ((CraftPlayer) target).getHandle(), 1.35f, 0);
                                //entityarrow.b(1.0 * 2.0F);

                                EntityArrow entityarrow = new EntityTippedArrow(bossEntity.world, bossEntity);
                                EntityLiving etarget = (EntityLiving) (((CraftPlayer) target).getHandle());
                                double d0 = etarget.locX - bossEntity.locX;
                                double d1 = etarget.getBoundingBox().b + etarget.length / 3.0F - entityarrow.locY;
                                double d2 = etarget.locZ - bossEntity.locZ;
                                double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
                                entityarrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.8F, 14 - bossEntity.world.getDifficulty().a() * 4);

                                bossEntity.world.addEntity(entityarrow, SpawnReason.CUSTOM);
                            }
                        }
                    }
                    if (ai.counter % 4 == 0) {
                        for (Entity e : RMath.getNearbyEntitiesCylinder(md.entity.getLocation(), 25, 9)) {
                            if (e instanceof Player) {
                                Player p2 = (Player) e;
                                if (!p2.isFlying() && e.isOnGround() && Spell.canDamage(p2, false)) {
                                    locs.add(p2.getLocation());
                                    if (locs.size() > 100)
                                        locs.remove(0);
                                }
                            }
                        }
                    }
                    if (ai.counter % 2 == 0 && md.ai.forcedDest != null) {
                        if (locs.size() > 0 && RMath.flatDistance(md.entity.getLocation(), md.ai.forcedDest) < 3)
                            md.ai.forcedDest = locs.remove((int) (Math.random() * locs.size()));
                    }
                    if (ai.counter % 6 == 0) {
                        if (locs.size() > 0)
                            md.ai.forcedDest = locs.remove((int) (Math.random() * locs.size()));
                    }
                    if (ai.counter % 6 == 0 && Math.random() < 0.7) {
                        final Item item = md.entity.getWorld().dropItem(md.entity.getLocation(), new ItemStack(Material.BONE));
                        item.setMetadata(RMetadata.META_NO_PICKUP, new FixedMetadataValue(Spell.plugin, 0));
                        plugin.getInstance(DropManager.class).attachLabel(item, ChatColor.BOLD + "Explosive Bone");
                        final int fDamage = (int) (md.getDamage() * 3.5);
                        RScheduler.schedule(Spell.plugin, new Runnable() {
                            int tick = 1;
                            ArrayList<Entity> hit = new ArrayList<Entity>();

                            public void run() {
                                if (item == null || !item.isValid())
                                    return;
                                hit.addAll(Spell.damageNearby(fDamage, md.entity, item.getLocation(), 2.0, hit, true, false, true));
                                if (hit.size() > 0) {
                                    Spell.damageNearby(fDamage, md.entity, item.getLocation(), 3, hit, true, false, true);
                                    DropManager.removeLabel(item);
                                    item.remove();
                                    RParticles.show(ParticleEffect.EXPLOSION_LARGE, item.getLocation(), 5);
                                }
                                if (tick == 20) {
                                    RParticles.show(ParticleEffect.CLOUD, item.getLocation().add(0, 0.1, 0));
                                    DropManager.removeLabel(item);
                                    item.remove();
                                }
                                tick++;
                                if (tick <= 20)
                                    RScheduler.schedule(Spell.plugin, this, (int) (Math.random() * 7 + 6));
                            }
                        }, 5);
                    }
                    if (ai.counter % 34 == 0 && Math.random() < 0.8) {
                        if (md.mount != null) {
                            CustomHorse ch = (CustomHorse) ((CraftEntity) md.mount).getHandle();
                            ch.rearUp();
                            final ArrayList<Entity> hit = new ArrayList<Entity>();
                            RScheduler.schedule(plugin, new Runnable() {
                                public void run() {
                                    if (md.dead)
                                        return;
                                    int rad = md.ai.rangeDistance / 3;
                                    Location loc = md.entity.getLocation();
                                    int damage = (int) (md.getDamage() * 1.35);
                                    for (int dx = -rad; dx <= rad; dx++) {
                                        for (int dz = -rad; dz <= rad; dz++) {
                                            Location temp = loc.clone().add(dx, 0, dz);
                                            RParticles.showWithOffsetPositiveY(ParticleEffect.EXPLOSION_LARGE, temp, 1.0, 3);
                                            hit.addAll(Spell.damageNearby(damage, md.entity, temp, 1.2, hit, true, false, true));
                                        }
                                    }
                                }
                            }, RTicks.seconds(1.3));
                        }
                    }
                    if (ai.counter % 40 == 0 && Math.random() < 0.8) {
                        md.shout(ChatColor.AQUA + "Behold, my rain... of PAIN!", 30);
                        final int fDamage = (int) (md.getDamage() * 0.7);
                        Block b = md.entity.getLocation().getBlock();
                        Location loc = b.getLocation();
                        for (int dx = -md.ai.rangeDistance; dx <= md.ai.rangeDistance; dx++) {
                            for (int dz = -md.ai.rangeDistance; dz <= md.ai.rangeDistance; dz++) {
                                if (Math.random() > 0.50)
                                    continue;
                                final Location currentLoc = loc.clone();
                                currentLoc.setX(loc.getX() + dx - 1);
                                currentLoc.setZ(loc.getZ() + dz);
                                currentLoc.setY(loc.getY() + 8);
                                Location loc2 = loc.clone();
                                loc2.setX(loc.getX() + dx);
                                loc2.setZ(loc.getZ() + dz);
                                final Vector v = loc2.toVector().subtract(currentLoc.toVector()).normalize();
                                int count = (int) (Math.random() * 4 + 2);
                                for (int k = 0; k < count; k++) {
                                    if (Math.random() < 0.7) {
                                        RScheduler.schedule(Spell.plugin, new Runnable() {
                                            public void run() {
                                                if (md.dead)
                                                    return;
                                                Projectile arrow = ((Projectile) (currentLoc.getWorld().spawnEntity(currentLoc, EntityType.ARROW)));
                                                arrow.setVelocity(v);
                                                arrow.setShooter(md.entity);
                                                arrow.setMetadata(RMetadata.META_DAMAGE, new FixedMetadataValue(Spell.plugin, fDamage));
                                            }
                                        }, k * RTicks.seconds(1) + (int) (Math.random() * RTicks.seconds(1)));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        };
        return md;
    }

    public int getDamageLow(int level) {
        switch (getTier(level)) {
            case 1:
                return (int) (1.5 * level);
            case 2:
                return (int) (2 * level);
            case 3:
                return (int) (3 * level);
            case 4:
                return (int) (4 * level);
            case 5:
                return (int) (5 * level);
        }
        return (int) (3 * level);
    }

    public int getExp(int level) {
        return level * 100;
    }

    @Override
    public String getMessage1() {
        return "You suddenly feel a chilling sensation...";
    }

    @Override
    public String getMessage2() {
        return "It feels like your bones are vibrating. Weird.";
    }

    @Override
    public String getMessage3() {
        return "*click*, *clack*. An ominous noise sounds in the distance.";
    }

    @Override
    public void playSound(Player p) {
        RScheduler.schedule(plugin, new Runnable() {
            int counter = 0;

            public void run() {
                if (p == null || !p.isOnline())
                    return;
                RSound.playSound(p, Sound.ENTITY_SKELETON_STEP);
                RSound.playSound(p, Sound.ENTITY_SKELETON_AMBIENT);
                counter++;
                if (counter < 10)
                    RScheduler.schedule(plugin, this, RTicks.seconds(0.5));
            }
        });
    }

}
