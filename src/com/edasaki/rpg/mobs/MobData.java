package com.edasaki.rpg.mobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.edasaki.core.options.OptionsManager;
import com.edasaki.core.utils.REntities;
import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RMessages;
import com.edasaki.core.utils.RParticles;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RTags;
import com.edasaki.core.utils.RTicks;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.classes.ClassType;
import com.edasaki.rpg.combat.DamageType;
import com.edasaki.rpg.drops.DropManager;
import com.edasaki.rpg.dungeons.Dungeon;
import com.edasaki.rpg.dungeons.DungeonManager;
import com.edasaki.rpg.items.EquipType;
import com.edasaki.rpg.items.ItemManager;
import com.edasaki.rpg.items.RandomItemGenerator;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellbookReaper;
import com.edasaki.rpg.spells.paladin.FlameCharge;
import com.edasaki.rpg.spells.paladin.LightningCharge;
import com.edasaki.rpg.worldboss.bosses.BossAIRunnable;

import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.ParticleEffect.BlockData;

public class MobData {
    public static SakiRPG plugin;

    private static int MOBDATA_ID = 1;

    public int id = MOBDATA_ID++;

    public boolean dead = false;

    public LivingEntity entity;
    public LivingEntity mount = null;

    public String nameWithoutLevel;
    public String fullName;

    public long hp;
    public MobType mobType;

    public HashMap<UUID, Long> lastAttackedByTimes;
    public HashMap<UUID, Long> damageDealt;
    public long lastDamaged = 0;
    public long lastKnockback;

    public ArrayList<MobAttribute> attributes;

    public MobAI ai;
    public BossAIRunnable bossAI = null;

    public MobSpellTicker spellTicker = new MobSpellTicker();

    public int poisonTicks = 0;
    public int poisonTier = 0;
    public int burnTicks = 0;
    public int burnTier = 0;

    public boolean isBoss = false;
    public boolean isWorldBoss = false;
    public boolean noKnockback = false;
    public boolean elite = false;

    public Dungeon dungeon = null;

    private ChatColor tierColor = null;

    private Runnable lastScheduledRevertHP = null;

    public boolean frozen = false; // no movement
    public boolean reflect = false; // reflect all damage

    public MobSpawn spawner;

    public boolean despawned = false;

    public long invuln = 0; // cannot attack or damage

    public int worldBossValue;

    public MobData() {
        lastAttackedByTimes = new HashMap<UUID, Long>();
        damageDealt = new HashMap<UUID, Long>();
        attributes = new ArrayList<MobAttribute>();
    }

    public void startTasks() {
        ai = new MobAI(entity, this);
        RScheduler.schedule(plugin, ai, 5);
        spellTicker.start();
    }

    public void mount(Class<? extends net.minecraft.server.v1_10_R1.Entity> mountClass) {
        if (entity == null || dead)
            return;
        mount = REntities.createLivingEntity(mountClass, entity.getLocation());
        mount.setPassenger(entity);
        MobManager.spawnedMobs.put(mount.getUniqueId(), this);
    }

    public void mount(LivingEntity le) {
        if (entity == null || dead)
            return;
        mount = le;
        mount.setPassenger(entity);
        MobManager.spawnedMobs.put(mount.getUniqueId(), this);
    }

    public void givePoison(int durationSeconds, int tier) {
        if (poisonTicks > 0 && tier <= poisonTier) {
            int value = tier * durationSeconds;
            value /= poisonTier;
            poisonTicks += value;
        } else {
            poisonTicks = durationSeconds;
            poisonTier = tier;
        }
    }

    public void removePoison() {
        poisonTicks = 0;
    }

    public void tickPoison() {
        poisonTicks--;
        Location loc = entity.getLocation().add(0, entity.getEyeHeight() * 0.6, 0);
        BlockData data = new BlockData(Material.STAINED_CLAY, (byte) DyeColor.BLUE.getWoolData());
        RParticles.showWithData(ParticleEffect.BLOCK_CRACK, loc, data, 10);
        double multiplier = 0.001;
        switch (poisonTier) {
            default:
            case 1:
                multiplier = 0.01;
                break;
            case 2:
                multiplier = 0.015;
                break;
            case 3:
                multiplier = 0.020;
                break;
            case 4:
                multiplier = 0.030;
                break;
            case 5:
                multiplier = 0.050;
                break;
        }
        int amount = (int) (multiplier * hp);
        if (amount < 1)
            amount = 1;
        damage(amount, null, DamageType.ENVIRONMENTAL_INSTANT);
    }

    public void giveBurn(int durationSeconds, int tier) {
        if (burnTicks > 0 && tier <= burnTicks) {
            int value = tier * durationSeconds;
            value /= burnTier;
            burnTicks += value;
        } else {
            burnTicks = durationSeconds;
            burnTier = tier;
        }
    }

    public void removeBurn() {
        burnTicks = 0;
    }

    public void tickBurn() {
        burnTicks--;
        Location loc = entity.getLocation().add(0, entity.getEyeHeight() * 0.6, 0);
        BlockData data = new BlockData(Material.LAVA, (byte) 0);
        RParticles.showWithData(ParticleEffect.BLOCK_CRACK, loc, data, 10);
        double multiplier = 0.001;
        switch (burnTier) {
            default:
            case 1:
                multiplier = 0.01;
                break;
            case 2:
                multiplier = 0.015;
                break;
            case 3:
                multiplier = 0.020;
                break;
            case 4:
                multiplier = 0.030;
                break;
            case 5:
                multiplier = 0.050;
                break;
        }
        int amount = (int) (multiplier * hp);
        if (amount < 1)
            amount = 1;
        damage(amount, null, DamageType.ENVIRONMENTAL_INSTANT);
    }

    public void die() {
        die(true);
    }

    public void die(boolean drop) {
        if (dead)
            return;
        dead = true;
        ai.stop();
        ai = null;
        Location deathLoc = entity.getLocation();
        // Remove entity and update name display
        entity.setCustomName(fullName);
        RTags.makeFloatingText(ChatColor.GOLD + "+" + mobType.exp * (elite ? 25 : 1) + " EXP", entity.getLocation(), 1.3, 1.0, 1.5, 1);
        if (mount != null) {
            try {
                mount.eject();
                entity.leaveVehicle();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //            RScheduler.schedule(plugin, () -> {
            //                mount.playEffect(EntityEffect.HURT);
            //            });
            mount.damage(1000000000);
        }
        entity.damage(1000000000);
        //        RParticles.showWithOffset(ParticleEffect.REDSTONE, entity.getLocation().add(0, 1, 0), 1.3, 10);
        //        RParticles.showWithDataAndOffset(ParticleEffect.BLOCK_CRACK, entity.getLocation().add(0, 1, 0), new BlockData(Material.LAVA, (byte) 0), 10, 1.2, 0.4, 1.2);
        //        RParticles.showWithData(ParticleEffect.BLOCK_CRACK, entity.getLocation().add(0, 1, 0), new BlockData(Material.LAVA, (byte) 0), 10);
        //        RScheduler.schedule(plugin, () -> {
        //            entity.playEffect(EntityEffect.HURT);
        //        });
        RScheduler.schedule(plugin, new Runnable() {
            public void run() {
                despawn();
            }
        }, RTicks.seconds(1));
        if (dungeon != null) {
            DungeonManager.rewardsChest(dungeon);
            dungeon.boss.spawnedBoss = null; // remove reference to this mobdata
        }

        // Distribute EXP
        int totalLevel = 0;
        int pdCount = 0;
        ArrayList<PlayerDataRPG> playerdatas = new ArrayList<PlayerDataRPG>();
        double damageSum = 0;
        PlayerDataRPG playerdata = null;
        for (Entry<UUID, Long> entry : damageDealt.entrySet()) {
            if ((playerdata = plugin.getPD(entry.getKey())) != null) {
                damageSum += entry.getValue();
                totalLevel += playerdata.level;
                pdCount++;
                playerdatas.add(plugin.getPD(entry.getKey()));
            }
        }
        if (pdCount < 1) {
            totalLevel = 1;
            pdCount = 1;
        }
        double avgLv = totalLevel / (double) pdCount;
        for (PlayerDataRPG pd : playerdatas) {
            if (damageDealt.containsKey(pd.getUUID())) {
                pd.incrementMobCounter(this.mobType.identifier);
                double percentage = damageDealt.get(pd.getUUID()) / ((double) damageSum);
                if (percentage >= 0.10) {
                    pd.mobKills++;
                    if (this.isBoss || this.isWorldBoss)
                        pd.bossKills++;
                }
                long amt = (long) Math.ceil(mobType.exp * (elite ? 25 : 1) * percentage);
                if (!(this.mobType.level >= 80 && pd.level >= 80) && Math.abs(pd.level - this.mobType.level) >= 10) {
                    double penalty = 0.70;
                    penalty -= (Math.abs(pd.level - this.mobType.level) - 10) * 0.06;
                    if (penalty < 0.10)
                        penalty = 0.10;
                    pd.gainExp((long) Math.ceil(amt * penalty), true);
                } else {
                    pd.gainExp(amt);
                }
                int totalHP = pd.getBaseMaxHP() + pd.maxHP;
                if (pd.hp < totalHP) {
                    if (pd.getSpellLevel(SpellbookReaper.ENDLESS_FEAST) > 0) {
                        switch (pd.getSpellLevel(SpellbookReaper.ENDLESS_FEAST)) {
                            case 1:
                                pd.heal((int) Math.ceil(totalHP * 0.0010));
                                break;
                            case 2:
                                pd.heal((int) Math.ceil(totalHP * 0.0015));
                                break;
                            case 3:
                                pd.heal((int) Math.ceil(totalHP * 0.0020));
                                break;
                            case 4:
                                pd.heal((int) Math.ceil(totalHP * 0.0025));
                                break;
                            case 5:
                                pd.heal((int) Math.ceil(totalHP * 0.0030));
                                break;
                        }
                    }
                }
            }
        }

        // Get Priority Looter
        long mostDealt = 0;
        UUID priority = null;
        PlayerDataRPG topDamage = null;
        for (Entry<UUID, Long> entry : damageDealt.entrySet()) {
            Player p = plugin.getServer().getPlayer(entry.getKey());
            if (p == null)
                continue;
            if (RMath.flatDistance(p.getLocation(), deathLoc) < 50) {
                if (entry.getValue() >= mostDealt) {
                    if (entry.getValue() == mostDealt && Math.random() > 0.5)
                        continue;
                    if (plugin.getPD(entry.getKey()) == null)
                        continue;
                    mostDealt = entry.getValue();
                    priority = entry.getKey();
                    topDamage = plugin.getPD(priority);
                }
            }
        }
        if (drop || this.hp < this.mobType.maxHP * (elite ? 10 : 1)) {
            // Drop any preset drops
            for (MobDrop md : mobType.drops) {
                if (md.roll()) {
                    int amount = RMath.randInt(md.minAmount, md.maxAmount);
                    for (int k = 0; k < amount; k++)
                        DropManager.dropItem(md.generate(), deathLoc, priority);
                }
            }
            int rarityFinder = topDamage != null ? topDamage.rarityFinder : 0;
            // Drop random drops
            if (!attributes.contains(MobAttribute.NODROP)) {
                if (Math.random() < MobBalance.getDropRate(mobType.level)) {
                    int low = mobType.level - (elite ? 0 : 2);
                    int high = mobType.level + (elite ? 5 : 2);
                    int lv = RMath.randInt(low, high);
                    if (lv < 1)
                        lv = 1;
                    ItemStack item;
                    if (avgLv <= 20 && topDamage != null && topDamage.classType != ClassType.VILLAGER) {
                        if (Math.random() < 0.2) {
                            item = RandomItemGenerator.generateEquip(EquipType.getWeaponForClass(topDamage.classType), lv, rarityFinder);
                        } else {
                            item = RandomItemGenerator.generateEquip(EquipType.randomArmor(), lv, rarityFinder);
                        }
                    } else {
                        item = RandomItemGenerator.generateEquip(EquipType.random(), lv, rarityFinder);
                    }
                    DropManager.dropItem(item, deathLoc, priority);
                }
            }
            //event stuff
            if (this.mobType.identifier.startsWith("gift_")) { // xmas gift event
                //                RMessages.announce("killed gift");
                for (PlayerDataRPG pd : playerdatas) {
                    pd.xmasPoints++;
                    pd.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "+1 Gift Point [" + pd.xmasPoints + " total]");
                    pd.sendMessage(ChatColor.GRAY + "> (You'll be able to spend these Gift Points for cool things soon!)");
                }
            }
        }
        if (elite) {
            ItemStack elitething = ItemManager.getItemForIdentifier("power_fragment");
            if (elitething != null)
                DropManager.dropItem(elitething, deathLoc, priority);
        }
        MobManager.respawn(this.spawner);
    }

    public void despawn() {
        if (despawned)
            return;
        if (this.spawner != null) {
            this.spawner.spawned = null;
            MobManager.respawn(this.spawner);
        }
        despawned = true;
        if (dungeon != null) {
            dungeon.boss.despawn();
            dungeon.boss.spawnSpawner();
        }
        if (isBoss) {
            if (dead) {
                if (dungeon != null) {
                    dungeon.sendMessage(ChatColor.RED + this.fullName + " has been defeated!");
                } else {
                    RMessages.announce(ChatColor.GRAY + ">> " + ChatColor.RED + this.fullName + " has been defeated!");
                }
            } else {
                if (dungeon != null) {
                    dungeon.sendMessage(ChatColor.RED + this.fullName + " has vanished.");
                } else {
                    RMessages.announce(ChatColor.GRAY + ">> " + ChatColor.RED + this.fullName + " has vanished.");
                }
            }
        }
        dead = true;
        if (entity != null) {
            MobManager.spawnedMobs.remove(entity.getUniqueId());
            MobManager.spawnedMobs_onlyMain.remove(entity.getUniqueId());
            entity.remove();
        }
        entity = null;
        if (mount != null) {
            MobManager.spawnedMobs.remove(mount.getUniqueId());
            MobManager.spawnedMobs_onlyMain.remove(mount.getUniqueId());
            mount.remove();
        }
        mount = null;
        if (this.ai != null)
            this.ai.stop();
        this.ai = null;
        if (this.bossAI != null)
            this.bossAI.ai = null;
        this.bossAI = null;
        if (this.damageDealt != null)
            this.damageDealt.clear();
        this.damageDealt = null;
        if (this.attributes != null)
            this.attributes.clear();
        this.attributes = null;
        if (this.lastAttackedByTimes != null)
            this.lastAttackedByTimes.clear();
        this.lastAttackedByTimes = null;
        if (this.spellTicker != null)
            this.spellTicker.cleanup();
        this.spellTicker = null;
    }

    public int getDamage() {
        return RMath.randInt(mobType.damageLow, mobType.damageHigh);
    }

    public boolean attack(PlayerDataRPG pd) {
        return attack(pd, 0.5, -1, false);
    }

    public boolean attack(PlayerDataRPG pd, int rpgDamage) {
        return attack(pd, 0.5, rpgDamage, false);
    }

    public boolean attack(PlayerDataRPG pd, int rpgDamage, boolean projectile) {
        return attack(pd, 0.5, rpgDamage, projectile);
    }

    public boolean attack(PlayerDataRPG pd, double knockback, int rpgDamage, boolean projectile) {
        if (System.currentTimeMillis() < invuln)
            return false;
        boolean success = false;
        if (attributes.contains(MobAttribute.RAPID)) {
            // use armorstand as damager b/c of custom name!!
            success = pd.damage(rpgDamage > -1 ? rpgDamage : getDamage(), entity, DamageType.NORMAL_SPELL);
        } else {
            success = pd.damage(rpgDamage > -1 ? rpgDamage : getDamage(), entity, DamageType.NORMAL);
        }
        if (success && !projectile)
            pd.knockback(entity, knockback);
        return success;
    }

    public boolean damage(int damageAmount, Entity damager, DamageType damageType) {
        return damage(damageAmount, damager, damageType, false);
    }

    public boolean damage(int damageAmount, Entity damager, DamageType damageType, boolean crit) {
        if (System.currentTimeMillis() < invuln)
            return false;
        if (dead)
            return false;
        if (damager != null && damager instanceof Player) {
            if (!((Player) damager).isOnline() || plugin.getPD((Player) damager) == null)
                return false;
            if (!plugin.getPD((Player) damager).isPVE() && !this.attributes.contains(MobAttribute.PASSIVE) && !this.attributes.contains(MobAttribute.RANDOMPASSIVE))
                return false;
            if (plugin.getPD((Player) damager).riding)
                return false;
            if (damageType == DamageType.NORMAL && lastAttackedByTimes.containsKey(damager.getUniqueId()) && System.currentTimeMillis() - lastAttackedByTimes.get(damager.getUniqueId()) < plugin.getPD((Player) damager).getAttackSpeed())
                return false;
        }
        if (damageAmount > hp) {
            damageAmount = (int) hp;
        }
        if (damager != null && damager instanceof Player) {
            if (damageDealt.containsKey(damager.getUniqueId())) {
                damageDealt.put(damager.getUniqueId(), damageDealt.get(damager.getUniqueId()) + damageAmount);
            } else {
                damageDealt.put(damager.getUniqueId(), (long) damageAmount);
            }
            if (ai.getTarget() == null) {
                ai.setTarget((Player) damager);
            } else {
                Player other = ai.getTarget();
                if (!(damageDealt.containsKey(other.getUniqueId()) && damageDealt.get(other.getUniqueId()) > damageDealt.get(damager.getUniqueId()))) {
                    ai.setTarget((Player) damager);
                }
            }
        }
        if (damager != null)
            lastAttackedByTimes.put(damager.getUniqueId(), System.currentTimeMillis());
        if (damageAmount < 1)
            damageAmount = 1;
        if (reflect) {
            if (damager != null && damager instanceof Player) {
                Player p = (Player) damager;
                PlayerDataRPG pd = plugin.getPD(p);
                if (pd != null) {
                    pd.damage((int) Math.ceil(damageAmount * 0.5), entity, DamageType.NORMAL_SPELL);
                }
            }
            damageAmount *= 0.25;
        }

        hp -= damageAmount;
        if (hp < 0)
            hp = 0;
        if (damager != null && damager instanceof Player) {
            Player p = (Player) damager;
            PlayerDataRPG pd = plugin.getPD(p);
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT, 0.65f, 0.75f);
            if (pd != null) {
                OptionsManager.msgDamage(p, pd, ChatColor.GRAY + ">> " + ChatColor.AQUA + ChatColor.BOLD + "-" + damageAmount + " HP" + ChatColor.WHITE + " to " + ChatColor.RED + fullName + (crit ? ChatColor.GRAY.toString() + ChatColor.ITALIC + " *Critical Hit*" : ""));
                if (pd.classType == ClassType.PALADIN) {
                    if (damageType == DamageType.NORMAL && pd.hasBuff(LightningCharge.BUFF_ID) && Math.random() < 0.3) {
                        RParticles.sendLightning(p, entity.getLocation());
                        Spell.damageNearby((int) (pd.getDamage(true) * pd.getBuffValue(LightningCharge.BUFF_ID)), p, p.getLocation(), 3.0, new ArrayList<Entity>());
                    }
                    if (damageType == DamageType.NORMAL && pd.hasBuff(FlameCharge.BUFF_ID) && Math.random() < 0.3) {
                        RParticles.showWithOffset(ParticleEffect.FLAME, entity.getEyeLocation(), 1.5, 15);
                        giveBurn(5, (int) pd.getBuffValue(FlameCharge.BUFF_ID));
                        Spell.notify(p, "You burn your enemy.");
                    }
                }
                if (pd.lifesteal > 0) {
                    pd.heal((int) Math.ceil(damageAmount * pd.lifesteal));
                }
            }
        }
        if (hp < 1) {
            die();
        } else {
            RParticles.showWithData(ParticleEffect.BLOCK_CRACK, entity.getLocation().add(0, 1.5, 0), new BlockData(Material.REDSTONE_BLOCK, (byte) 0), 10);
            //            RScheduler.schedule(plugin, () -> {
            entity.playEffect(EntityEffect.HURT);
            //            });
            if (mount != null)
                //                RScheduler.schedule(plugin, () -> {
                mount.playEffect(EntityEffect.HURT);
            //                });
            displayCurrentHP();
        }
        lastDamaged = System.currentTimeMillis();
        return true;
    }

    private void displayCurrentHP() {
        entity.setCustomName(getHPDisplay());
        RScheduler.schedule(plugin, lastScheduledRevertHP = new Runnable() {
            public void run() {
                if (!dead && entity != null && entity.isValid() && !entity.isDead()) {
                    if (lastScheduledRevertHP == this)
                        entity.setCustomName(fullName);
                    // no need to reschedule b/c there is guaranteed to be another task that will set it back
                }
            }
        }, RTicks.seconds(3));
    }

    public String getHPDisplay() {
        double percent = ((double) hp) / (mobType.maxHP * (elite ? 10 : 1));
        int green = (int) Math.floor(percent / 0.05);
        if (green > 20)
            green = 20;
        if (green < 1)
            green = 1;
        StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.GRAY);
        sb.append('[');

        int firstHalf = green - 10;
        if (firstHalf < 0)
            firstHalf = 0;

        sb.append(ChatColor.DARK_RED);
        for (int k = 0; k < 10 - firstHalf; k++) {
            sb.append('|');
        }
        sb.append(ChatColor.GREEN);
        for (int k = 0; k < firstHalf; k++) {
            sb.append('|');
        }

        if (isBoss || this.attributes.contains(MobAttribute.BOSS)) {
            sb.append(ChatColor.GOLD);
            sb.append(ChatColor.BOLD);
        } else {
            if (elite)
                sb.append(ChatColor.GOLD);
            else if (tierColor == null)
                sb.append(tierColor = MobType.getTierColor(mobType.getTier()));
            else
                sb.append(tierColor);
        }
        sb.append(' ');
        sb.append(hp);
        sb.append(' ');

        int secondHalf = green - firstHalf;

        sb.append(ChatColor.DARK_RED);
        for (int k = 0; k < 10 - secondHalf; k++) {
            sb.append('|');
        }
        sb.append(ChatColor.GREEN);
        for (int k = 0; k < secondHalf; k++) {
            sb.append('|');
        }

        sb.append(ChatColor.GRAY);
        sb.append(']');

        return sb.toString();
    }

    public void knockback(Entity attacker, double knockbackMultiplier) {
        return;
        //        if (dead || noKnockback || entity == null || frozen)
        //            return;
        //        if (attributes.contains(MobAttribute.LOWKNOCKBACK)) {
        //            knockbackMultiplier = 0.25;
        //        }
        //        if (System.currentTimeMillis() - lastKnockback > 600) {
        //            lastKnockback = System.currentTimeMillis();
        //            Vector newVelocity = entity.getLocation().toVector().subtract(attacker.getLocation().toVector()).normalize().multiply(knockbackMultiplier);
        //            // cap Y knockback
        //            if (Math.abs(newVelocity.getY()) > 0.01)
        //                newVelocity.setY(0.01 * Math.signum(newVelocity.getY()));
        //            // cap X knockback
        //            if (Math.abs(newVelocity.getX()) > 1)
        //                newVelocity.setX(1 * Math.signum(newVelocity.getX()));
        //            // cap Z knockback
        //            if (Math.abs(newVelocity.getZ()) > 1)
        //                newVelocity.setZ(1 * Math.signum(newVelocity.getZ()));
        //            if (newVelocity.getY() < 0.2)
        //                newVelocity.setY(0.2);
        //            if (entity != null && entity.isValid())
        //                entity.setVelocity(newVelocity);
        //        }
    }

    public void regen(double value) {
        if (hp >= mobType.maxHP * (elite ? 10 : 1))
            return;
        hp += Math.ceil(value);
        if (hp > mobType.maxHP * (elite ? 10 : 1))
            hp = mobType.maxHP * (elite ? 10 : 1);
        RParticles.show(ParticleEffect.HEART, entity.getLocation());
        displayCurrentHP();
    }

    public void playCrit() {
        if (entity == null || !entity.isValid())
            return;
        RParticles.showWithOffset(ParticleEffect.CRIT, entity.getEyeLocation(), 1, 10);
    }

    public void say(Player p, String s) {
        p.sendMessage((tierColor == null ? "" : tierColor) + nameWithoutLevel + ChatColor.WHITE + ": " + ChatColor.GRAY + s);
    }

    public void shout(String s, int radius) {
        for (Entity e : RMath.getNearbyEntities(entity.getLocation(), radius)) {
            if (e instanceof Player) {
                say((Player) e, s);
            }
        }
    }

}
