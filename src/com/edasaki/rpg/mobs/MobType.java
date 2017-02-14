package com.edasaki.rpg.mobs;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftWolf;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.edasaki.core.utils.REntities;
import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.entities.CustomChicken;
import com.edasaki.core.utils.entities.CustomHorse;
import com.edasaki.core.utils.entities.CustomWolf;
import com.edasaki.core.utils.entities.Leashable;
import com.edasaki.rpg.mobs.spells.MobSpell;
import com.edasaki.rpg.particles.EffectFactory;
import com.edasaki.rpg.particles.EffectName;

import de.slikey.effectlib.Effect;

public class MobType {
    public String identifier;
    public String name;
    public Class<? extends net.minecraft.server.v1_10_R1.Entity> entityClass;
    public int level;
    public ArrayList<String> prefixes;
    public ArrayList<String> suffixes;
    public long exp;
    public long maxHP;
    public int damageLow, damageHigh;
    public ArrayList<ItemStack> equips;
    public ArrayList<MobAttribute> attributes;
    public ArrayList<MobDrop> drops;
    public ArrayList<MobSpell> spells;
    public boolean hasSkull = false;
    public ItemStack offhand;

    public static ChatColor getTierColor(int tier) {
        switch (tier) {
            case 5:
                return ChatColor.RED;
            case 4:
                return ChatColor.LIGHT_PURPLE;
            case 3:
                return ChatColor.AQUA;
            case 2:
                return ChatColor.GREEN;
            case 1:
            default:
                return ChatColor.WHITE;
        }
    }

    public int getTier() {
        if (level >= 80)
            return 5;
        else if (level >= 60)
            return 4;
        else if (level >= 40)
            return 3;
        else if (level >= 20)
            return 2;
        return 1;
    }

    public MobData spawn(Location loc) {
        return spawn(loc, 15);
    }

    public MobData spawn(Location loc, String name) {
        return spawn(loc, 15, name);
    }

    public MobData spawn(Location loc, int leash) {
        StringBuilder fullName = new StringBuilder();
        if (attributes.contains(MobAttribute.BOSS)) {
            fullName.append(ChatColor.GOLD);
            fullName.append(ChatColor.BOLD);
        }
        if (prefixes.size() > 0) {
            String prefix = prefixes.get((int) (Math.random() * prefixes.size()));
            fullName.append(prefix);
        }
        fullName.append(name);
        if (suffixes.size() > 0) {
            String suffix = suffixes.get((int) (Math.random() * suffixes.size()));
            fullName.append(suffix);
        }
        return spawn(loc, leash, fullName.toString());
    }

    public MobData spawn(Location loc, int leash, String name) {
        boolean elite = false;
        if (!attributes.contains(MobAttribute.BOSS) && Math.random() < 0.01) {
            elite = true;
        }

        // spawn main mob
        LivingEntity le = REntities.createLivingEntity(entityClass, loc);

        if (((CraftLivingEntity) le).getHandle() instanceof Leashable) {
            ((Leashable) ((CraftLivingEntity) le).getHandle()).allowWalk(leash);
        }

        if (attributes.contains(MobAttribute.ANGRYWOLF)) {
            ((CustomWolf) ((CraftWolf) le).getHandle()).makeRedEyes(true);
        }
        if (attributes.contains(MobAttribute.TAMED)) {
            ((Tameable) le).setTamed(true);
        }

        // spawn invisible armorstand with name
        String displayName = ChatColor.GRAY + "[" + level + "] " + (elite ? getTierColor(getTier()).toString() + ChatColor.BOLD + "Elite " : getTierColor(getTier())) + name;

        le.setCustomName(displayName);
        le.setCustomNameVisible(true);

        if (attributes.contains(MobAttribute.ANGELWINGS)) {
            Effect e = EffectFactory.getEffect(EffectName.PETITE_ANGEL_WINGS, null);
            e.setEntity(le);
            e.start();
        }

        if (attributes.contains(MobAttribute.BABY)) {
            if (le instanceof Ageable) {
                ((Ageable) le).setBaby();
                ((Ageable) le).setAgeLock(true);
            } else if (le instanceof Zombie) {
                ((Zombie) le).setBaby(true);
            }
        }
        if (attributes.contains(MobAttribute.HUSK)) {
            ((Zombie) le).setVillagerProfession(Profession.HUSK);
        }
        if (attributes.contains(MobAttribute.STRAY)) {
            ((Skeleton) le).setSkeletonType(SkeletonType.STRAY);
        }
        if (attributes.contains(MobAttribute.WITHER)) {
            ((Skeleton) le).setSkeletonType(SkeletonType.WITHER);
        }
        if (attributes.contains(MobAttribute.SLIME1)) {
            ((Slime) le).setSize(1);
        }
        if (attributes.contains(MobAttribute.SLIME2)) {
            ((Slime) le).setSize(2);
        }
        if (attributes.contains(MobAttribute.SLIME3)) {
            ((Slime) le).setSize(3);
        }
        if (attributes.contains(MobAttribute.SLIME4)) {
            ((Slime) le).setSize(4);
        }
        if (attributes.contains(MobAttribute.SLIME5)) {
            ((Slime) le).setSize(5);
        }
        if (attributes.contains(MobAttribute.SLIME6)) {
            ((Slime) le).setSize(6);
        }
        if (attributes.contains(MobAttribute.SLIME7)) {
            ((Slime) le).setSize(7);
        }
        if (attributes.contains(MobAttribute.SLIME8)) {
            ((Slime) le).setSize(8);
        }

        if (attributes.contains(MobAttribute.INVISIBLE)) {
            le.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, true, false));
        }

        if (le instanceof Ocelot) {
            ((Ocelot) le).setCatType(RMath.randObject(Ocelot.Type.values()));
        }

        // generate mob data
        MobData md = new MobData();
        // store entities
        md.entity = le;
        // onlyMain only contains the visible main mob
        MobManager.spawnedMobs_onlyMain.put(md.entity.getUniqueId(), md);
        MobManager.spawnedMobs.put(md.entity.getUniqueId(), md);
        // register spells
        for (MobSpell sp : spells)
            md.spellTicker.addSpell(sp, md);
        // set attributes of mobdata
        md.nameWithoutLevel = name;
        md.fullName = le.getCustomName();
        md.hp = maxHP * (elite ? 10 : 1);
        md.elite = elite;
        md.mobType = this;
        md.attributes.addAll(attributes);
        if (attributes.contains(MobAttribute.HORSE1)) {
            Horse horseMount = (Horse) REntities.createLivingEntity(CustomHorse.class, md.entity.getLocation());
            horseMount.setVariant(Variant.HORSE);
            horseMount.setAdult();
            horseMount.setTamed(true);
            //            AttributeInstance attributes = ((EntityInsentient) ((CraftLivingEntity) horseMount).getHandle()).getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
            //            attributes.setValue(0.2);
            md.mount(horseMount);
        } else if (attributes.contains(MobAttribute.CHICKEN1)) {
            md.mount(CustomChicken.class);
        }
        // set equipment
        EntityEquipment ee = md.entity.getEquipment();
        for (ItemStack item : equips) {
            String s = item.getType().toString();
            if (elite) {
                item = item.clone();
                item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            }
            if (s.endsWith("_HELMET") || item.getType() == Material.SKULL_ITEM)
                ee.setHelmet(item);
            else if (s.endsWith("_CHESTPLATE"))
                ee.setChestplate(item);
            else if (s.endsWith("_LEGGINGS"))
                ee.setLeggings(item);
            else if (s.endsWith("_BOOTS"))
                ee.setBoots(item);
            else
                ee.setItemInMainHand(item);
        }
        if (this.offhand != null) {
            ItemStack temp = offhand;
            if (elite) {
                temp = temp.clone();
                temp.addEnchantment(Enchantment.DURABILITY, 1);
            }
            ee.setItemInOffHand(temp);
        }

        if (elite) {
            if (le instanceof Skeleton) {
                ((Skeleton) le).setSkeletonType(SkeletonType.WITHER);
            } else if (le instanceof Slime) {
                int newSize = ((Slime) le).getSize() * 2;
                ((Slime) le).setSize(newSize);
            }
        }

        md.startTasks();
        return md;
    }

    @Override
    public String toString() {
        return this.identifier;
    }
}
