package com.edasaki.rpg.mobs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.util.Vector;

import com.edasaki.core.utils.RMath;
//RMessages;
import com.edasaki.core.utils.RParticles;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RTicks;
import com.edasaki.core.utils.entities.CustomMagmaCube;
import com.edasaki.rpg.PlayerDataRPG;

import de.slikey.effectlib.util.ParticleEffect;
import net.minecraft.server.v1_10_R1.AttributeInstance;
import net.minecraft.server.v1_10_R1.EntityArrow;
import net.minecraft.server.v1_10_R1.EntityInsentient;
import net.minecraft.server.v1_10_R1.EntityLiving;
import net.minecraft.server.v1_10_R1.EntityTippedArrow;
import net.minecraft.server.v1_10_R1.GenericAttributes;
import net.minecraft.server.v1_10_R1.MathHelper;
import net.minecraft.server.v1_10_R1.Packet;
import net.minecraft.server.v1_10_R1.PacketListener;
import net.minecraft.server.v1_10_R1.PacketPlayOutAnimation;

public class MobAI implements Runnable {

    private static int HIDDEN_ID = 1;

    private static final int SAFESPOT_THRESHOLD = 70;

    private static final double DEFAULT_WALK_SPEED = 1.12d;
    private static final double MAX_AGGRO_DISTANCE_NO_TARGET = 15d;
    private static final double MAX_DISTANCE_FROM_ORIGINAL = 120d;
    private static final double BACK_HOME_DISTANCE = 5d;
    private static final double TRACK_NEARBY_DISTANCE = 40d;
    private static final double TRACK_PLAYER_NEARBY_OR_DESPAWN = 150d;
    private static final double SNEAK_DISTANCE_BONUS = 2.5d;
    private static final double TICKS_TO_CHANGE_TARGET = RTicks.seconds(10d);
    private static final double DANGER_ZONE_PERCENTAGE = 0.35d;
    private static final double DANGER_ZONE_SPEED_BUFF = 1.1d;
    private static final double NO_TARGET_REGEN_HP_PERCENTAGE = 0.08d;
    private static final long LOW_WANDER_TIME = 10000;
    private static final long NORMAL_WANDER_TIME = 30000;

    public static HashSet<UUID> ignore = new HashSet<UUID>();

    @SuppressWarnings("unused")
    private int debugID = HIDDEN_ID++;

    public boolean aggressive = true;
    public boolean ranged = false;
    public boolean archer = false;
    public int rangeDistance = 1;
    public float rangePower = 1;
    public int rangeRate = 10;
    public double currentWalkSpeed = DEFAULT_WALK_SPEED;
    public boolean dangerZone = false;
    public long lastReturn = 0;

    private boolean hasReturnedAfterLostAggro = false;

    private double standardWalkSpeed = -1;

    public Player target = null;

    private Location dest;

    public int counter = 0; // 1 counter = 1/4 second = 5 ticks
    public int lastAttackTickCounter = 0;

    public LivingEntity entity;
    public MobData md;
    public net.minecraft.server.v1_10_R1.Entity nmsEntity;
    public final Location originalLoc;

    private ArrayList<Player> nearbyPlayers;

    public Location forcedDest = null;

    private boolean stopped = false;

    private boolean forceReturn = false;

    private boolean rapid = false;

    private long lastDirChange = 0;
    private Vector lastDir = null;

    private boolean randomMovement;

    public Player getTarget() {
        return target;
    }

    public void setTarget(Player p) {
        if (ignore.contains(p.getUniqueId()))
            return;
        target = p;
    }

    public void stop() {
        this.stopped = true;
        this.entity = null;
        if (this.md != null)
            this.md.ai = null;
        this.md = null;
        this.nmsEntity = null;
        //        this.originalLoc = null;
        this.target = null;
        if (this.nearbyPlayers != null)
            this.nearbyPlayers.clear();
        this.nearbyPlayers = null;
        this.forcedDest = null;
    }

    @Override
    public void run() {
        if (md == null || stopped || md.dead || !entity.isValid()) {
            if (md != null)
                md.despawn();
            stop();
            return;
        }
        if (standardWalkSpeed == -1)
            standardWalkSpeed = currentWalkSpeed;
        if (counter % 4 == 0) {
            dangerZone = ((double) md.hp) / md.mobType.maxHP < DANGER_ZONE_PERCENTAGE;
            if (dangerZone)
                currentWalkSpeed = standardWalkSpeed * DANGER_ZONE_SPEED_BUFF;
            else
                currentWalkSpeed = standardWalkSpeed;
        }
        if (counter % 4 == 0) {
            if (md.poisonTicks > 0)
                md.tickPoison();
            if (md.burnTicks > 0)
                md.tickBurn();
        }
        if (md == null || stopped || md.dead || entity == null || !entity.isValid()) {
            if (md != null)
                md.despawn();
            stop();
            return;
        }
        Location currentLoc = entity.getLocation();
        double distanceAwayFromOriginal = RMath.flatDistance(currentLoc, originalLoc);
        if (forceReturn && distanceAwayFromOriginal < BACK_HOME_DISTANCE) {
            forceReturn = false;
            //            //RMessages.announce("resetting");
        }
        if (distanceAwayFromOriginal > MAX_DISTANCE_FROM_ORIGINAL) {
            forceReturn = true;
            //            //RMessages.announce("f-a");
            target = null;
        }
        if (aggressive) {
            if (!forceReturn) {
                // Find a new target if needed
                if (lastAttackTickCounter > TICKS_TO_CHANGE_TARGET) {
                    target = null;
                    lastAttackTickCounter = 0;
                }
                if (target != null && ignore.contains(target.getUniqueId())) {
                    target = null;
                    hasReturnedAfterLostAggro = false;
                    //                    //RMessages.announce("a-a");
                }
                if (target == null || MobData.plugin.getPD(target) == null || MobData.plugin.getPD(target).isStealthed() || !MobData.plugin.getPD(target).isPVE() || RMath.flatDistance(target.getLocation(), originalLoc) > MAX_DISTANCE_FROM_ORIGINAL || target.isDead() || !target.isValid() || !target.isOnline()) {
                    target = null;
                    Player closest = null;
                    double shortestDist = Double.MAX_VALUE;
                    if (counter % 4 == 1) {
                        boolean needDespawn = true;
                        nearbyPlayers.clear();
                        for (Player p : MobData.plugin.getServer().getOnlinePlayers()) {
                            double dist = RMath.flatDistance(p.getLocation(), currentLoc);
                            if (dist < TRACK_NEARBY_DISTANCE)
                                nearbyPlayers.add(p);
                            if (needDespawn && dist < TRACK_PLAYER_NEARBY_OR_DESPAWN) {
                                needDespawn = false;
                            }
                        }
                        if (needDespawn) {
                            md.despawn();
                            return;
                        }
                    }
                    for (Player p : nearbyPlayers) {
                        if (p == null || p.isDead() || !p.isValid() || !p.isOnline())
                            continue;
                        if (MobData.plugin.getPD(p) == null || MobData.plugin.getPD(p).isStealthed() || !MobData.plugin.getPD(p).isPVE())
                            continue;
                        double dist = RMath.flatDistance(p.getLocation(), currentLoc);
                        if (p.isSneaking())
                            dist *= SNEAK_DISTANCE_BONUS;
                        if (dist > MAX_AGGRO_DISTANCE_NO_TARGET)
                            continue;
                        if (ignore.contains(p.getUniqueId())) {
                            if (dist < 4 && Math.abs(p.getLocation().getY() - currentLoc.getY()) < 2) {
                                PlayerDataRPG pd = MobManager.plugin.getPD(p);
                                if (pd != null)
                                    pd.safespotCounter /= 2;
                                //                                //RMessages.announce("aggroing safespotter in probable reachable distance");
                            } else {
                                continue;
                            }
                        }
                        if (dist < shortestDist) {
                            shortestDist = dist;
                            closest = p;
                        }
                    }
                    target = closest;
                }
            }
            if (target != null && counter % 12 == 0) { // 4 ticks per second
                PlayerDataRPG pd = MobManager.plugin.getPD(target);
                if (pd != null && pd.isValid()) {
                    pd.safespotCounter++;
                    //RMessages.announce("Incremented " + pd.getName() + " counter from id" + debugID + ": " + pd.safespotCounter);
                    if (pd.safespotCounter > SAFESPOT_THRESHOLD) {
                        target = null;
                        hasReturnedAfterLostAggro = false;
                        //RMessages.announce("detected " + pd.getName() + " possible safespotting, disabling aggro for 30 seconds");
                        final UUID ignoreUUID = pd.getUUID();
                        if (!ignore.contains(ignoreUUID)) {
                            ignore.add(ignoreUUID);
                            RScheduler.schedule(MobManager.plugin, () -> {
                                if (ignore.contains(ignoreUUID)) {
                                    pd.safespotCounter = 0;
                                    //RMessages.announce("re-enabled aggro on Misaka");
                                }
                                ignore.remove(ignoreUUID);
                            }, RTicks.seconds(30));
                        }
                    }
                }
            }
            if (!hasReturnedAfterLostAggro && target == null) {
                forceReturn = true;
                //RMessages.announce("f-b");
                hasReturnedAfterLostAggro = true;
            }
            // Try to find and attack the target
            if (target != null && !forceReturn) {
                if (entity instanceof Creature) {
                    ((Creature) entity).setTarget(target);
                }
                if (entity instanceof IronGolem && md.attributes.contains(MobAttribute.HYPERGOLEM)) {
                    nmsEntity.world.broadcastEntityEffect(nmsEntity, (byte) 4); // Iron Golem attack animation
                }
                if (ranged) {
                    // ranged
                    try {
                        dest = target.getLocation();
                        boolean attemptAttack = false;
                        if (RMath.flatDistance(currentLoc, dest) < rangeDistance) {
                            dest = currentLoc;
                            attemptAttack = true;
                        }
                        navigate();
                        if (archer && attemptAttack && target != null) {
                            int tempRate = rangeRate;
                            if (rapid) {
                                tempRate /= 5;
                                if (tempRate < 1)
                                    tempRate = 1;
                            }
                            if (counter % tempRate == 0) {
                                lastAttackTickCounter = 0;
                                EntityLiving e = (EntityLiving) ((CraftEntity) entity).getHandle();
                                EntityArrow entityarrow = new EntityTippedArrow(e.world, e);
                                EntityLiving etarget = (EntityLiving) (((CraftPlayer) target).getHandle());
                                double d0 = etarget.locX - e.locX;
                                double d1 = etarget.getBoundingBox().b + etarget.length / 3.0F - entityarrow.locY;
                                double d2 = etarget.locZ - e.locZ;
                                double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
                                entityarrow.shoot(d0, d1 + d3 * 0.2D, d2, rangePower, 10);
                                e.world.addEntity(entityarrow, SpawnReason.CUSTOM);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // melee
                    try {
                        dest = target.getLocation();
                        navigate();
                        double xdiff = 1.5;
                        double ydiff = 3;
                        if (entity instanceof IronGolem) {
                            xdiff = 2.5;
                            ydiff = 4;
                        } else if (md.mount != null) {
                            xdiff = 2.0;
                            ydiff = 4;
                        }
                        Location targetLoc = target.getLocation();
                        if (target != null && Math.abs(targetLoc.getX() - currentLoc.getX()) < xdiff && Math.abs(targetLoc.getZ() - currentLoc.getZ()) < xdiff && Math.abs(targetLoc.getY() - currentLoc.getY()) < ydiff) {
                            PlayerDataRPG pd = MobData.plugin.getPD(target);
                            if (pd != null && !md.dead) {
                                if (md.attack(pd)) {
                                    if (entity instanceof IronGolem)
                                        nmsEntity.world.broadcastEntityEffect(nmsEntity, (byte) 4); // Iron Golem attack animation
                                    else
                                        sendNearby(new PacketPlayOutAnimation(nmsEntity, 0)); // generic swing arm animation
                                    lastAttackTickCounter = 0;
                                    if (md.attributes.contains(MobAttribute.POISON1) && Math.random() < 0.2) {
                                        pd.givePoison(5, 1);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                // no target
                if (entity instanceof Creature) {
                    ((Creature) entity).setTarget(null);
                }
                if (counter % 4 == 0 && md.hp < md.mobType.maxHP && lastAttackTickCounter > RTicks.seconds(5)) {
                    md.regen(md.mobType.maxHP * NO_TARGET_REGEN_HP_PERCENTAGE);
                    target = null;
                    dest = originalLoc.clone();
                    if (nmsEntity instanceof CustomMagmaCube) {
                        RParticles.showWithOffsetPositiveY(ParticleEffect.FLAME, entity.getLocation(), 1.5, 75);
                        entity.teleport(dest);
                        target = null;
                    } else if (entity instanceof Slime) {
                        RParticles.showWithOffsetPositiveY(ParticleEffect.SLIME, entity.getLocation(), 1.5, 75);
                        entity.teleport(dest);
                        target = null;
                    }
                    navigate();
                }
                if (forceReturn) {
                    dest = originalLoc.clone();
                    //RMessages.announce("Forcing return to " + dest);
                    lastReturn = System.currentTimeMillis();
                }
                long time = System.currentTimeMillis() - lastReturn;
                if (forceReturn || time > NORMAL_WANDER_TIME || (md.mobType.attributes.contains(MobAttribute.LOWWANDER) && time > LOW_WANDER_TIME)) {
                    AttributeInstance ati = ((EntityLiving) (((CraftLivingEntity) entity).getHandle())).getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
                    double old = ati.getValue();
                    double oldSpeed = currentWalkSpeed;
                    ati.setValue(MAX_DISTANCE_FROM_ORIGINAL + 10.0);
                    oldSpeed *= 1.5;
                    dest = originalLoc.clone();
                    navigate();
                    if (nmsEntity instanceof CustomMagmaCube) {
                        RParticles.showWithOffsetPositiveY(ParticleEffect.FLAME, entity.getLocation(), 1.5, 75);
                        entity.teleport(dest);
                        target = null;
                    } else if (entity instanceof Slime) {
                        RParticles.showWithOffsetPositiveY(ParticleEffect.SLIME, entity.getLocation(), 1.5, 75);
                        entity.teleport(dest);
                        target = null;
                    }
                    lastReturn = System.currentTimeMillis();
                    ati.setValue(old);
                    currentWalkSpeed = oldSpeed;
                }
            }
        } else {
            //passive mobs
            if (forceReturn) {
                dest = originalLoc.clone();
                navigate();
                if (nmsEntity instanceof CustomMagmaCube) {
                    RParticles.showWithOffsetPositiveY(ParticleEffect.FLAME, entity.getLocation(), 1.5, 75);
                    entity.teleport(dest);
                    target = null;
                } else if (entity instanceof Slime) {
                    RParticles.showWithOffsetPositiveY(ParticleEffect.SLIME, entity.getLocation(), 1.5, 75);
                    entity.teleport(dest);
                    target = null;
                }
            } else if (randomMovement) {
                dest = entity.getLocation();
                navigate();
            }
        }
        if (md.isBoss) {
            if (md.bossAI != null) {
                if (md.bossAI.ai == null)
                    md.bossAI.ai = this;
                md.bossAI.run();
            }
        }
        counter++;
        lastAttackTickCounter += 5;
        RScheduler.schedule(MobData.plugin, this, 3 + (int) (Math.random() * 5));
    }

    public void makeRanged(int distance, float power, int rate) {
        this.ranged = true;
        this.rangeDistance = distance;
        this.rangePower = power;
        this.rangeRate = rate;
    }

    public void setWalkSpeed(double d) {
        this.currentWalkSpeed = d;
    }

    public MobAI(LivingEntity e, MobData md) {
        this.entity = e;
        this.md = md;
        this.nmsEntity = ((CraftEntity) entity).getHandle();
        this.originalLoc = e.getLocation();
        for (MobAttribute ma : md.attributes) {
            switch (ma) {
                case RANGED:
                    this.ranged = true;
                    if (this.rangeDistance < 10)
                        this.rangeDistance = 10;
                    break;
                case PASSIVE:
                    this.aggressive = false;
                    break;
                case RANDOMPASSIVE:
                    this.randomMovement = true;
                    this.aggressive = false;
                    break;
                case ARROW1:
                    this.archer = true;
                    this.makeRanged(10, 1.0f, 10);
                    break;
                case ARROW2:
                    this.archer = true;
                    this.makeRanged(12, 1.5f, 9);
                    break;
                case ARROW3:
                    this.archer = true;
                    this.makeRanged(15, 1.8f, 8);
                    break;
                case ARROW4:
                    this.archer = true;
                    this.makeRanged(20, 2.3f, 7);
                    break;
                case ARROW5:
                    this.archer = true;
                    this.makeRanged(25, 2.5f, 6);
                    break;
                case RAPID:
                    this.rapid = true;
                    break;
                case HYPERGOLEM:
                    this.currentWalkSpeed = DEFAULT_WALK_SPEED * 2.3;
                    break;
                case SLOW1:
                    this.currentWalkSpeed = DEFAULT_WALK_SPEED * 0.9;
                    break;
                case SLOW2:
                    this.currentWalkSpeed = DEFAULT_WALK_SPEED * 0.8;
                    break;
                case SLOW3:
                    this.currentWalkSpeed = DEFAULT_WALK_SPEED * 0.7;
                    break;
                case FAST1:
                    this.currentWalkSpeed = DEFAULT_WALK_SPEED * 1.2;
                    break;
                case FAST2:
                    this.currentWalkSpeed = DEFAULT_WALK_SPEED * 1.4;
                    break;
                case FAST3:
                    this.currentWalkSpeed = DEFAULT_WALK_SPEED * 1.6;
                    break;
                default:
                    break;
            }
        }
        if (entity instanceof Villager) {
            currentWalkSpeed = 0.45;
        }
        this.nearbyPlayers = new ArrayList<Player>();
    }

    private void sendNearby(Packet<? extends PacketListener> packet) {
        for (Player p : nearbyPlayers) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
    }

    private void navigate() {
        if (entity instanceof Slime || nmsEntity instanceof CustomMagmaCube)
            return;
        if (forcedDest != null) {
            dest = forcedDest;
        } else if (randomMovement) {
            if (System.currentTimeMillis() - lastDirChange > RMath.randInt(5000, 10000) || lastDir == null) {
                Vector v = Vector.getRandom();
                v.setX(v.getX() - 0.5f);
                v.setZ(v.getZ() - 0.5f); // center around origin
                lastDir = v.setY(0).normalize().multiply(RMath.randInt(3, 5));
                lastDirChange = System.currentTimeMillis();
            }
            dest.add(lastDir);
        } else {
            if (System.currentTimeMillis() - lastDirChange > RMath.randInt(5000, 10000) || lastDir == null) {
                Vector v = Vector.getRandom();
                v.setX(v.getX() - 0.5f);
                v.setZ(v.getZ() - 0.5f); // center around origin
                lastDir = v.setY(0).normalize().multiply(RMath.randInt(1, 3));
                lastDirChange = System.currentTimeMillis();
            }
            dest.add(lastDir);
        }
        AttributeInstance ati = ((EntityLiving) (((CraftLivingEntity) entity).getHandle())).getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
        double old = ati.getValue();
        double diff = RMath.flatDistance(dest, entity.getLocation());
        if (old < diff) {
            ati.setValue(diff + 10);
        }
        double moveMultiplier = 1.0;
        //        if (entity.getLocation().getBlock().getType().isSolid()) {
        //            System.out.println("is solid block: " + entity.getLocation().getBlock().getType());
        //            moveMultiplier = 5;
        //        }
        if (md.mount != null) {
            ((EntityInsentient) ((CraftEntity) md.mount).getHandle()).getNavigation().a(dest.getX(), dest.getY(), dest.getZ(), currentWalkSpeed * moveMultiplier);
            ((EntityInsentient) ((CraftEntity) entity).getHandle()).getNavigation().a(dest.getX(), dest.getY(), dest.getZ(), currentWalkSpeed * moveMultiplier);
        } else {
            ((EntityInsentient) ((CraftEntity) entity).getHandle()).getNavigation().a(dest.getX(), dest.getY(), dest.getZ(), currentWalkSpeed * moveMultiplier);
        }
        ati.setValue(old);
    }
}
