package com.edasaki.rpg.npcs;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Wolf;

import com.edasaki.core.utils.REntities;
import com.edasaki.core.utils.entities.CustomChicken;
import com.edasaki.core.utils.entities.CustomCow;
import com.edasaki.core.utils.entities.CustomIronGolem;
import com.edasaki.core.utils.entities.CustomOcelot;
import com.edasaki.core.utils.entities.CustomPig;
import com.edasaki.core.utils.entities.CustomSheep;
import com.edasaki.core.utils.entities.CustomSkeleton;
import com.edasaki.core.utils.entities.CustomVillager;
import com.edasaki.core.utils.entities.CustomWolf;

public enum NPCType {
    BABYVILLAGER(0.46),
    BABY_VILLAGER(0.46),
    VILLAGER(1.07),
    SKELETON(1.07),
    CAT(0.46),
    BLACK_CAT(0.46),
    RED_CAT(0.46),
    SIAMESE_CAT(0.46),
    BABY_CAT(0.46),
    BABY_BLACK_CAT(0.46),
    BABY_RED_CAT(0.46),
    BABY_SIAMESE_CAT(0.46),
    COW(1.07),
    BABY_COW(1.07),
    SHEEP(1.07),
    BABY_SHEEP(1.07),
    PIG(1.07),
    BABY_PIG(1.07),
    CHICKEN(1.07),
    BABY_CHICKEN(1.07),
    VILLAGER_BLACKSMITH(1.07),
    BABY_VILLAGER_BLACKSMITH(0.46),
    VILLAGER_BUTCHER(1.07),
    BABY_VILLAGER_BUTCHER(0.46),
    VILLAGER_FARMER(1.07),
    BABY_VILLAGER_FARMER(0.46),
    VILLAGER_LIBRARIAN(1.07),
    BABY_VILLAGER_LIBRARIAN(0.46),
    VILLAGER_PRIEST(1.07),
    BABY_VILLAGER_PRIEST(0.46),
    IRON_GOLEM(1.5),
    WOLF(0.5),
    ANGRY_WOLF(0.5),
    WOLF_BLACK(0.5),
    WOLF_BLUE(0.5),
    WOLF_BROWN(0.5),
    WOLF_CYAN(0.5),
    WOLF_GRAY(0.5),
    WOLF_GREEN(0.5),
    WOLF_LIGHT_BLUE(0.5),
    WOLF_LIME(0.5),
    WOLF_MAGENTA(0.5),
    WOLF_ORANGE(0.5),
    WOLF_PINK(0.5),
    WOLF_PURPLE(0.5),
    WOLF_RED(0.5),
    WOLF_SILVER(0.5),
    WOLF_WHITE(0.5),
    WOLF_YELLOW(0.5),

    ;

    public double tagHeight;

    NPCType(double height) {
        this.tagHeight = height;
    }

    public LivingEntity createEntity(Location loc) {
        LivingEntity le = null;
        loc = loc.clone().add(0, 0.5, 0);
        switch (this) {
            /*
             * VILLAGERS
             */
            case BABYVILLAGER:
            case BABY_VILLAGER:
                le = REntities.createLivingEntity(CustomVillager.class, loc);
                ((Ageable) le).setBaby();
                ((Ageable) le).setAgeLock(true);
                break;
            case VILLAGER:
                return REntities.createLivingEntity(CustomVillager.class, loc);
            case VILLAGER_BLACKSMITH:
                le = REntities.createLivingEntity(CustomVillager.class, loc);
                ((Villager) le).setProfession(Profession.BLACKSMITH);
                break;
            case BABY_VILLAGER_BLACKSMITH:
                le = REntities.createLivingEntity(CustomVillager.class, loc);
                ((Ageable) le).setBaby();
                ((Ageable) le).setAgeLock(true);
                ((Villager) le).setProfession(Profession.BLACKSMITH);
                break;
            case VILLAGER_BUTCHER:
                le = REntities.createLivingEntity(CustomVillager.class, loc);
                ((Villager) le).setProfession(Profession.BUTCHER);
                break;
            case BABY_VILLAGER_BUTCHER:
                le = REntities.createLivingEntity(CustomVillager.class, loc);
                ((Ageable) le).setBaby();
                ((Ageable) le).setAgeLock(true);
                ((Villager) le).setProfession(Profession.BUTCHER);
                break;
            case VILLAGER_FARMER:
                le = REntities.createLivingEntity(CustomVillager.class, loc);
                ((Villager) le).setProfession(Profession.FARMER);
                break;
            case BABY_VILLAGER_FARMER:
                le = REntities.createLivingEntity(CustomVillager.class, loc);
                ((Ageable) le).setBaby();
                ((Ageable) le).setAgeLock(true);
                ((Villager) le).setProfession(Profession.FARMER);
                break;
            case VILLAGER_LIBRARIAN:
                le = REntities.createLivingEntity(CustomVillager.class, loc);
                ((Villager) le).setProfession(Profession.LIBRARIAN);
                break;
            case BABY_VILLAGER_LIBRARIAN:
                le = REntities.createLivingEntity(CustomVillager.class, loc);
                ((Ageable) le).setBaby();
                ((Ageable) le).setAgeLock(true);
                ((Villager) le).setProfession(Profession.LIBRARIAN);
                break;
            case VILLAGER_PRIEST:
                le = REntities.createLivingEntity(CustomVillager.class, loc);
                ((Villager) le).setProfession(Profession.PRIEST);
                break;
            case BABY_VILLAGER_PRIEST:
                le = REntities.createLivingEntity(CustomVillager.class, loc);
                ((Ageable) le).setBaby();
                ((Ageable) le).setAgeLock(true);
                ((Villager) le).setProfession(Profession.PRIEST);
                break;
            /*
             * CATS
             */
            case CAT:
                le = REntities.createLivingEntity(CustomOcelot.class, loc);
                ((Ocelot) le).setCatType(Ocelot.Type.WILD_OCELOT);
                break;
            case BLACK_CAT:
                le = REntities.createLivingEntity(CustomOcelot.class, loc);
                ((Ocelot) le).setCatType(Ocelot.Type.BLACK_CAT);
                break;
            case RED_CAT:
                le = REntities.createLivingEntity(CustomOcelot.class, loc);
                ((Ocelot) le).setCatType(Ocelot.Type.RED_CAT);
                break;
            case SIAMESE_CAT:
                le = REntities.createLivingEntity(CustomOcelot.class, loc);
                ((Ocelot) le).setCatType(Ocelot.Type.SIAMESE_CAT);
                break;
            case BABY_CAT:
                le = REntities.createLivingEntity(CustomOcelot.class, loc);
                ((Ocelot) le).setCatType(Ocelot.Type.WILD_OCELOT);
                ((Ageable) le).setBaby();
                ((Ageable) le).setAgeLock(true);
                break;
            case BABY_BLACK_CAT:
                le = REntities.createLivingEntity(CustomOcelot.class, loc);
                ((Ocelot) le).setCatType(Ocelot.Type.BLACK_CAT);
                ((Ageable) le).setBaby();
                ((Ageable) le).setAgeLock(true);
                break;
            case BABY_RED_CAT:
                le = REntities.createLivingEntity(CustomOcelot.class, loc);
                ((Ocelot) le).setCatType(Ocelot.Type.RED_CAT);
                ((Ageable) le).setBaby();
                ((Ageable) le).setAgeLock(true);
                break;
            case BABY_SIAMESE_CAT:
                le = REntities.createLivingEntity(CustomOcelot.class, loc);
                ((Ocelot) le).setCatType(Ocelot.Type.SIAMESE_CAT);
                ((Ageable) le).setBaby();
                ((Ageable) le).setAgeLock(true);
                break;
            /*
             * FARM ANIMALS
             */
            case COW:
                return REntities.createLivingEntity(CustomCow.class, loc);
            case BABY_COW:
                le = REntities.createLivingEntity(CustomCow.class, loc);
                ((Ageable) le).setBaby();
                ((Ageable) le).setAgeLock(true);
                break;
            case SHEEP:
                return REntities.createLivingEntity(CustomSheep.class, loc);
            case BABY_SHEEP:
                le = REntities.createLivingEntity(CustomSheep.class, loc);
                ((Ageable) le).setBaby();
                ((Ageable) le).setAgeLock(true);
                break;
            case PIG:
                return REntities.createLivingEntity(CustomPig.class, loc);
            case BABY_PIG:
                le = REntities.createLivingEntity(CustomPig.class, loc);
                ((Ageable) le).setBaby();
                ((Ageable) le).setAgeLock(true);
                break;
            case CHICKEN:
                return REntities.createLivingEntity(CustomChicken.class, loc);
            case BABY_CHICKEN:
                le = REntities.createLivingEntity(CustomChicken.class, loc);
                ((Ageable) le).setBaby();
                ((Ageable) le).setAgeLock(true);
                break;
            case SKELETON:
                return REntities.createLivingEntity(CustomSkeleton.class, loc);
            case IRON_GOLEM:
                return REntities.createLivingEntity(CustomIronGolem.class, loc);
            case WOLF:
                le = REntities.createLivingEntity(CustomWolf.class, loc);
                ((Wolf) le).setTamed(false);
                break;
            case ANGRY_WOLF:
                le = REntities.createLivingEntity(CustomWolf.class, loc);
                ((Wolf) le).setAngry(true);
                break;
            case WOLF_BLACK:
                le = REntities.createLivingEntity(CustomWolf.class, loc);
                ((Wolf) le).setTamed(true);
                ((Wolf) le).setCollarColor(DyeColor.BLACK);
                break;
            case WOLF_BLUE:
                le = REntities.createLivingEntity(CustomWolf.class, loc);
                ((Wolf) le).setTamed(true);
                ((Wolf) le).setCollarColor(DyeColor.BLUE);
                break;
            case WOLF_BROWN:
                le = REntities.createLivingEntity(CustomWolf.class, loc);
                ((Wolf) le).setTamed(true);
                ((Wolf) le).setCollarColor(DyeColor.BROWN);
                break;
            case WOLF_CYAN:
                le = REntities.createLivingEntity(CustomWolf.class, loc);
                ((Wolf) le).setTamed(true);
                ((Wolf) le).setCollarColor(DyeColor.CYAN);
                break;
            case WOLF_GRAY:
                le = REntities.createLivingEntity(CustomWolf.class, loc);
                ((Wolf) le).setTamed(true);
                ((Wolf) le).setCollarColor(DyeColor.GRAY);
                break;
            case WOLF_GREEN:
                le = REntities.createLivingEntity(CustomWolf.class, loc);
                ((Wolf) le).setTamed(true);
                ((Wolf) le).setCollarColor(DyeColor.GREEN);
                break;
            case WOLF_LIGHT_BLUE:
                le = REntities.createLivingEntity(CustomWolf.class, loc);
                ((Wolf) le).setTamed(true);
                ((Wolf) le).setCollarColor(DyeColor.LIGHT_BLUE);
                break;
            case WOLF_LIME:
                le = REntities.createLivingEntity(CustomWolf.class, loc);
                ((Wolf) le).setTamed(true);
                ((Wolf) le).setCollarColor(DyeColor.LIME);
                break;
            case WOLF_MAGENTA:
                le = REntities.createLivingEntity(CustomWolf.class, loc);
                ((Wolf) le).setTamed(true);
                ((Wolf) le).setCollarColor(DyeColor.MAGENTA);
                break;
            case WOLF_ORANGE:
                le = REntities.createLivingEntity(CustomWolf.class, loc);
                ((Wolf) le).setTamed(true);
                ((Wolf) le).setCollarColor(DyeColor.ORANGE);
                break;
            case WOLF_PINK:
                le = REntities.createLivingEntity(CustomWolf.class, loc);
                ((Wolf) le).setTamed(true);
                ((Wolf) le).setCollarColor(DyeColor.PINK);
                break;
            case WOLF_PURPLE:
                le = REntities.createLivingEntity(CustomWolf.class, loc);
                ((Wolf) le).setTamed(true);
                ((Wolf) le).setCollarColor(DyeColor.PURPLE);
                break;
            case WOLF_RED:
                le = REntities.createLivingEntity(CustomWolf.class, loc);
                ((Wolf) le).setTamed(true);
                ((Wolf) le).setCollarColor(DyeColor.RED);
                break;
            case WOLF_SILVER:
                le = REntities.createLivingEntity(CustomWolf.class, loc);
                ((Wolf) le).setTamed(true);
                ((Wolf) le).setCollarColor(DyeColor.SILVER);
                break;
            case WOLF_WHITE:
                le = REntities.createLivingEntity(CustomWolf.class, loc);
                ((Wolf) le).setTamed(true);
                ((Wolf) le).setCollarColor(DyeColor.WHITE);
                break;
            case WOLF_YELLOW:
                le = REntities.createLivingEntity(CustomWolf.class, loc);
                ((Wolf) le).setTamed(true);
                ((Wolf) le).setCollarColor(DyeColor.YELLOW);
                break;

        }
        return le;
    }
}
