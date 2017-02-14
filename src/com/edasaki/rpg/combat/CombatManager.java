package com.edasaki.rpg.combat;

import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.Metadatable;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RMetadata;
import com.edasaki.core.utils.RParticles;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RSound;
import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.classes.ClassType;
import com.edasaki.rpg.general.EnvironmentManager;
import com.edasaki.rpg.items.ItemManager;
import com.edasaki.rpg.mobs.MobData;
import com.edasaki.rpg.mobs.MobManager;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellManager;

import de.slikey.effectlib.util.ParticleEffect;

public class CombatManager extends AbstractManagerRPG {

    public CombatManager(SakiRPG plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {

    }

    /*
     * Just handles respawn
     */
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        PlayerDataRPG pd = plugin.getPD(event.getPlayer());
        if (pd != null) {
            pd.dead = false;
            pd.hp = pd.baseMaxHP;
            pd.mana = PlayerDataRPG.MAX_MANA;
            pd.updateHealthManaDisplay();
            pd.setInvulnerable(5);
        }
    }

    public boolean check(ItemStack item, Player p) {
        if (item == null || p == null)
            return false;
        boolean cancel = false;
        if (!SpellManager.isCasting(p)) {
            // throw potion
            if (item != null && item.getType() == Material.DRAGONS_BREATH) {
                cancel = true;
                p.updateInventory();
                if (ItemManager.isItemWithLevel(item)) {
                    PlayerDataRPG pd = plugin.getPD(p);
                    pd.updateEquipmentStats();
                    if ((pd.classType == ClassType.ALCHEMIST || pd.classType == ClassType.VILLAGER) && System.currentTimeMillis() - pd.lastPotionThrown > pd.getAttackSpeed()) {
                        pd.lastPotionThrown = System.currentTimeMillis();
                        RSound.playSound(p, Sound.ENTITY_ARROW_SHOOT, 3, 0);
                        ThrownPotion entity = (ThrownPotion) p.launchProjectile(ThrownPotion.class);
                        entity.setVelocity(entity.getVelocity().multiply(1.3));
                        entity.setBounce(false);
                        entity.setShooter(p);
                    }
                }
            }
            // shoot wand
            if (item != null && item.getType() == Material.STICK) {
                if (ItemManager.isItemWithLevel(item)) {
                    cancel = true;
                    if (plugin.getPD(p) != null) {
                        final PlayerDataRPG pd = plugin.getPD(p);
                        if ((pd.classType == ClassType.WIZARD || pd.classType == ClassType.VILLAGER) && System.currentTimeMillis() - pd.lastWandShot > pd.getAttackSpeed()) {
                            pd.lastWandShot = System.currentTimeMillis();
                            final Vector dir = p.getLocation().getDirection();
                            final Location start = p.getLocation().add(0, p.getEyeHeight() * 0.75, 0).add(dir.multiply(0.5));
                            final ArrayList<Entity> hit = new ArrayList<Entity>();
                            ArrayList<Location> locs = RMath.calculateVectorPath(start.clone(), dir, 6, 4);
                            int count = 0;
                            for (int k = 0; k < locs.size(); k++) {
                                final Location loc = locs.get(k);
                                RScheduler.schedule(Spell.plugin, new Runnable() {
                                    public void run() {
                                        RParticles.show(ParticleEffect.CRIT, loc);
                                        int damage = pd.getDamage(false);
                                        boolean crit = false;
                                        if (Math.random() < pd.critChance) {
                                            crit = true;
                                            damage *= pd.critDamage;
                                        }
                                        hit.addAll(Spell.damageNearby(damage, p, loc, 0.6, hit, false, crit, false));
                                    }
                                }, count);
                                if (k % 3 == 0)
                                    count++;
                            }
                        }
                    }
                }
            }
            //shoot bow
            if (item != null && item.getType() == Material.BOW) {
                if (ItemManager.isItemWithLevel(item)) {
                    PlayerDataRPG pd = plugin.getPD(p);
                    pd.updateEquipmentStats();
                    if ((pd.classType == ClassType.ARCHER || pd.classType == ClassType.VILLAGER) && System.currentTimeMillis() - pd.lastArrowShot > pd.getAttackSpeed()) {
                        pd.lastArrowShot = System.currentTimeMillis();
                        pd.shootArrow();
                    }
                }
            }
        }
        return cancel;
    }

    @EventHandler
    public void onAnim(PlayerAnimationEvent event) {
        if (event.getAnimationType() == PlayerAnimationType.ARM_SWING) {
            check(event.getPlayer().getInventory().getItemInMainHand(), event.getPlayer());
        }
    }

    /*
     * potions, wands, and bows
     */
    @EventHandler
    public void onWeaponInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
            if (check(item, event.getPlayer()))
                event.setCancelled(true);
        if (item != null && item.getType() == Material.BOW) {
            event.setCancelled(true);
        }
        if (item != null && item.getType() == Material.POTION) {
            event.setCancelled(true);
        }
        if (event.getItem() != null && event.getItem().getType() != null && (event.getItem().getType() == Material.WOOD_HOE || event.getItem().getType() == Material.STONE_HOE || event.getItem().getType() == Material.IRON_HOE || event.getItem().getType() == Material.GOLD_HOE || event.getItem().getType() == Material.DIAMOND_HOE)) {
            PlayerDataRPG pd = plugin.getPD(event.getPlayer());
            if (!(pd != null && event.getPlayer().getGameMode() == GameMode.CREATIVE && EnvironmentManager.canBuild(pd))) {
                event.setCancelled(true);
            }
        }
        if (event.getItem() != null && event.getItem().getType() != null && (event.getItem().getType() == Material.WOOD_SPADE || event.getItem().getType() == Material.STONE_SPADE || event.getItem().getType() == Material.IRON_SPADE || event.getItem().getType() == Material.GOLD_SPADE || event.getItem().getType() == Material.DIAMOND_SPADE)) {
            PlayerDataRPG pd = plugin.getPD(event.getPlayer());
            if (!(pd != null && event.getPlayer().getGameMode() == GameMode.CREATIVE && EnvironmentManager.canBuild(pd))) {
                event.setCancelled(true);
            }
        }
        if (item != null && item.getType() == Material.SHEARS) {
            event.setCancelled(true);
        }
        event.getPlayer().updateInventory();
    }

    /*
     * Handles all forms of actual damage dealing
     */
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        event.getEntity().setFireTicks(0);
        event.setCancelled(true);
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent event2 = (EntityDamageByEntityEvent) event;
            event2.setCancelled(true);
            Entity attacker = event2.getDamager();
            Entity defender = event2.getEntity();
            if (attacker instanceof Player) {
                if (defender instanceof Player) {
                    if (!SpellManager.isCasting((Player) attacker)) {
                        PlayerDataRPG a = plugin.getPD((Player) attacker);
                        PlayerDataRPG d = plugin.getPD((Player) defender);
                        if (a != null && d != null) {
                            ItemManager.fixDurability(((Player) attacker).getEquipment().getItemInMainHand());
                            a.attackPlayer(d);
                        }
                    }
                    // player melee hits player
                } else {
                    if (!SpellManager.isCasting((Player) attacker)) {
                        PlayerDataRPG a = plugin.getPD((Player) attacker);
                        MobData d = MobManager.spawnedMobs.get(defender.getUniqueId());
                        if (a != null && d != null) {
                            ItemManager.fixDurability(((Player) attacker).getEquipment().getItemInMainHand());
                            a.attackMob(d);
                        }
                    }
                    // player melee hits NPC/mob/other
                }
            } else if (attacker instanceof Projectile) {
                boolean projectile = true;
                ProjectileSource source = ((Projectile) attacker).getShooter();
                int rpgDamage = -1;
                double rpgKnockback = -1;
                if (attacker instanceof Metadatable) {
                    Metadatable sourceMeta = (Metadatable) attacker;
                    if (sourceMeta.hasMetadata(RMetadata.META_DAMAGE))
                        rpgDamage = sourceMeta.getMetadata(RMetadata.META_DAMAGE).get(0).asInt();
                    if (sourceMeta.hasMetadata(RMetadata.META_KNOCKBACK))
                        rpgKnockback = sourceMeta.getMetadata(RMetadata.META_KNOCKBACK).get(0).asDouble();
                    if (sourceMeta.hasMetadata(RMetadata.META_EXPLODE) && source instanceof Entity) {
                        int explodeDamage = sourceMeta.getMetadata(RMetadata.META_EXPLODE).get(0).asInt();
                        RParticles.show(ParticleEffect.EXPLOSION_LARGE, attacker.getLocation(), 5);
                        Spell.damageNearby(explodeDamage, (Entity) source, attacker.getLocation(), 3, new ArrayList<Entity>());
                        attacker.remove();
                        return;
                    }
                }
                if (source == defender) {
                    attacker.remove();
                    return;
                }
                if (source instanceof Player) {
                    if (defender instanceof Player) {
                        PlayerDataRPG a = plugin.getPD((Player) source);
                        PlayerDataRPG d = plugin.getPD((Player) defender);
                        if (a != null && d != null) {
                            if (rpgKnockback >= 0)
                                a.attackPlayer(d, rpgKnockback, rpgDamage, projectile);
                            else
                                a.attackPlayer(d, rpgDamage, projectile);
                        }
                        // player range hits player
                    } else {
                        PlayerDataRPG a = plugin.getPD((Player) source);
                        MobData d = MobManager.spawnedMobs.get(defender.getUniqueId());
                        if (a != null && d != null) {
                            if (rpgKnockback >= 0)
                                a.attackMob(d, rpgKnockback, rpgDamage, projectile);
                            else
                                a.attackMob(d, rpgDamage, projectile);
                        }
                        // player range hits NPC/mob/other
                    }
                } else {
                    if (defender instanceof Player) {
                        MobData a = MobManager.spawnedMobs.get(((Entity) source).getUniqueId());
                        PlayerDataRPG d = plugin.getPD((Player) defender);
                        if (a != null && d != null) {
                            a.attack(d, rpgDamage, projectile);
                        }
                        // NPC/mob/other range hits player
                    } else {
                        // For now this only happens when mobs shoot stuff and it hits themselves
                        // NPC/mob/other range hits NPC/mob/other
                    }
                }
                attacker.remove();
            } else {
                if (defender instanceof Player) {
                    MobData a = MobManager.spawnedMobs.get(attacker.getUniqueId());
                    PlayerDataRPG d = plugin.getPD((Player) defender);
                    if (a != null && d != null) {
                        a.attack(d);
                    }
                    // NPC/mob/other melee hits player
                } else {
                    // NPC/mob/other melee hits NPC/mob/other
                }
            }
        } else {
            if (event.getCause() == DamageCause.STARVATION)
                return;
            if (event.getCause() == DamageCause.SUFFOCATION && MobManager.spawnedMobs_onlyMain.containsKey(event.getEntity().getUniqueId()))
                event.getEntity().teleport(event.getEntity().getLocation().add(0, 1.5, 0));
            if (event.getEntity() instanceof Player) {
                PlayerDataRPG pd = plugin.getPD((Player) event.getEntity());
                if (event.getCause() == DamageCause.LAVA) {
                    int lavaDamage = (int) Math.ceil(0.03 * (pd.baseMaxHP + pd.maxHP));
                    pd.damage(lavaDamage, null, DamageType.ENVIRONMENTAL_LAVA);
                } else if (event.getCause() == DamageCause.DROWNING) {
                    int drowningDamage = (int) Math.ceil(0.12 * (pd.baseMaxHP + pd.maxHP));
                    pd.damage(drowningDamage, null, DamageType.ENVIRONMENTAL_DROWNING);
                } else if (event.getCause() == DamageCause.FALL) {
                    double percentage = event.getDamage() / 45.0;
                    if (percentage < 0.01)
                        return;
                    int fallDamage = (int) Math.ceil(percentage * (pd.baseMaxHP + pd.maxHP));
                    pd.damage(fallDamage, null, DamageType.ENVIRONMENTAL_FALL);
                } else if (event.getCause() == DamageCause.FIRE) {
                    int fireDamage = (int) Math.ceil(0.02 * (pd.baseMaxHP + pd.maxHP));
                    pd.damage(fireDamage, null, DamageType.ENVIRONMENTAL_LAVA);
                }
            }
        }
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        ThrownPotion pot = event.getPotion();
        if (pot.getShooter() instanceof Player) {
            Player p = (Player) pot.getShooter();
            PlayerDataRPG a = plugin.getPD(p);
            for (LivingEntity e : event.getAffectedEntities()) {
                try {
                    if (e instanceof Player) {
                        PlayerDataRPG d = plugin.getPD((Player) e);
                        if (a != null && d != null && a != d) {
                            a.attackPlayer(d);
                        }
                    } else {
                        MobData d = MobManager.spawnedMobs.get(e.getUniqueId());
                        if (a != null && d != null) {
                            a.attackMob(d);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        event.setCancelled(true);
    }

    /*
     * Removes arrows when they land
     */
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        event.getEntity().remove();
    }

    /*
     * Update armor stats after equipping
     */

    @EventHandler
    public void onInventoryClose_equipItem(InventoryCloseEvent event) {
        PlayerDataRPG pd = plugin.getPD((Player) (event.getPlayer()));
        if (pd != null)
            pd.updateEquipmentStats();
    }

    @EventHandler
    public void onPlayerInteract_equipItem(final PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            RScheduler.schedule(plugin, new Runnable() {
                public void run() {
                    PlayerDataRPG pd = plugin.getPD((Player) (event.getPlayer()));
                    if (pd != null)
                        pd.updateEquipmentStats();
                }
            });
        }
    }

    @EventHandler
    public void onInventoryClick_equipItem(InventoryClickEvent event) {
        PlayerDataRPG pd = plugin.getPD((Player) (event.getWhoClicked()));
        if (pd != null)
            pd.updateEquipmentStats();
    }

    @EventHandler
    public void onChangeItemInHand_equipItem(final PlayerItemHeldEvent event) {
        RScheduler.schedule(plugin, new Runnable() {
            public void run() {
                PlayerDataRPG pd = plugin.getPD((Player) (event.getPlayer()));
                if (pd != null)
                    pd.updateEquipmentStats();
            }
        });
    }

    /*
     * No natural drops
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        event.setDroppedExp(0);
        event.getDrops().clear();
    }

    /*
    
    public static void handlePlayerVersusPlayer(Player attacker, Player defender, int rpgDamage) {
        final PlayerData a = plugin.getPD(attacker);
        final PlayerData d = plugin.getPD(defender);
        if (a == null || d == null || a == d)
            return;
        if (!a.isPVP() || !d.isPVP())
            return;
        a.attackPlayer(d, rpgDamage);
    }
    
    public static void handlePlayerVersusNPC(Player attacker, Entity defender, int rpgDamage) {
        final PlayerData a = plugin.getPD(attacker);
        final MobData md = MobManager.spawnedMobs.get(defender.getUniqueId());
        if (a == null || md == null || !a.isPVE())
            return;
        a.attackMob(md, rpgDamage);
    }
    
    // i think this only fires on ranged stuff
    public static void handleNPCVersusPlayer(Entity attacker, Player defender, int rpgDamage) {
        final MobData md = MobManager.spawnedMobs.get(attacker.getUniqueId());
        final PlayerData d = plugin.getPD(defender);
        if (md == null || d == null)
            return;
        if (!d.isPVE())
            return;
        md.attack(d, rpgDamage);
    }
    
    */

}
