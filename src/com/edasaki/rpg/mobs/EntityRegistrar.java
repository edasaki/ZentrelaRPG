package com.edasaki.rpg.mobs;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.EntityType;

import com.edasaki.core.utils.entities.CustomBlaze;
import com.edasaki.core.utils.entities.CustomCaveSpider;
import com.edasaki.core.utils.entities.CustomChicken;
import com.edasaki.core.utils.entities.CustomCow;
import com.edasaki.core.utils.entities.CustomHorse;
import com.edasaki.core.utils.entities.CustomIronGolem;
import com.edasaki.core.utils.entities.CustomMagmaCube;
import com.edasaki.core.utils.entities.CustomMushroomCow;
import com.edasaki.core.utils.entities.CustomOcelot;
import com.edasaki.core.utils.entities.CustomPig;
import com.edasaki.core.utils.entities.CustomPigZombie;
import com.edasaki.core.utils.entities.CustomPolarBear;
import com.edasaki.core.utils.entities.CustomRabbit;
import com.edasaki.core.utils.entities.CustomSheep;
import com.edasaki.core.utils.entities.CustomSilverfish;
import com.edasaki.core.utils.entities.CustomSkeleton;
import com.edasaki.core.utils.entities.CustomSlime;
import com.edasaki.core.utils.entities.CustomSpider;
import com.edasaki.core.utils.entities.CustomVillager;
import com.edasaki.core.utils.entities.CustomWitch;
import com.edasaki.core.utils.entities.CustomWolf;
import com.edasaki.core.utils.entities.CustomZombie;

import net.minecraft.server.v1_10_R1.BiomeBase;
import net.minecraft.server.v1_10_R1.BiomeBase.BiomeMeta;
import net.minecraft.server.v1_10_R1.EntityBlaze;
import net.minecraft.server.v1_10_R1.EntityCaveSpider;
import net.minecraft.server.v1_10_R1.EntityChicken;
import net.minecraft.server.v1_10_R1.EntityCow;
import net.minecraft.server.v1_10_R1.EntityHorse;
import net.minecraft.server.v1_10_R1.EntityInsentient;
import net.minecraft.server.v1_10_R1.EntityIronGolem;
import net.minecraft.server.v1_10_R1.EntityMagmaCube;
import net.minecraft.server.v1_10_R1.EntityMushroomCow;
import net.minecraft.server.v1_10_R1.EntityOcelot;
import net.minecraft.server.v1_10_R1.EntityPig;
import net.minecraft.server.v1_10_R1.EntityPigZombie;
import net.minecraft.server.v1_10_R1.EntityPolarBear;
import net.minecraft.server.v1_10_R1.EntityRabbit;
import net.minecraft.server.v1_10_R1.EntitySheep;
import net.minecraft.server.v1_10_R1.EntitySilverfish;
import net.minecraft.server.v1_10_R1.EntitySkeleton;
import net.minecraft.server.v1_10_R1.EntitySlime;
import net.minecraft.server.v1_10_R1.EntitySpider;
import net.minecraft.server.v1_10_R1.EntityTypes;
import net.minecraft.server.v1_10_R1.EntityVillager;
import net.minecraft.server.v1_10_R1.EntityWitch;
import net.minecraft.server.v1_10_R1.EntityWolf;
import net.minecraft.server.v1_10_R1.EntityZombie;

public enum EntityRegistrar {

    Villager("Villager", 120, EntityType.VILLAGER, EntityVillager.class, CustomVillager.class),
    Skeleton("Skeleton", 51, EntityType.SKELETON, EntitySkeleton.class, CustomSkeleton.class),
    Zombie("Zombie", 54, EntityType.ZOMBIE, EntityZombie.class, CustomZombie.class),
    Slime("Slime", 55, EntityType.SLIME, EntitySlime.class, CustomSlime.class),
    Chicken("Chicken", 93, EntityType.CHICKEN, EntityChicken.class, CustomChicken.class),
    Cow("Cow", 92, EntityType.COW, EntityCow.class, CustomCow.class),
    Spider("Spider", 52, EntityType.SPIDER, EntitySpider.class, CustomSpider.class),
    Blaze("Blaze", 61, EntityType.BLAZE, EntityBlaze.class, CustomBlaze.class),
    Iron_Golem("VillagerGolem", 99, EntityType.IRON_GOLEM, EntityIronGolem.class, CustomIronGolem.class),
    Wolf("Wolf", 95, EntityType.WOLF, EntityWolf.class, CustomWolf.class),
    Silverfish("Silverfish", 60, EntityType.SILVERFISH, EntitySilverfish.class, CustomSilverfish.class),
    Pig_Zombie("PigZombie", 57, EntityType.PIG_ZOMBIE, EntityPigZombie.class, CustomPigZombie.class),
    Magma_Cube("LavaSlime", 62, EntityType.MAGMA_CUBE, EntityMagmaCube.class, CustomMagmaCube.class),
    Cave_Spider("CaveSpider", 59, EntityType.CAVE_SPIDER, EntityCaveSpider.class, CustomCaveSpider.class),
    Pig("Pig", 90, EntityType.PIG, EntityPig.class, CustomPig.class),
    Sheep("Sheep", 91, EntityType.SHEEP, EntitySheep.class, CustomSheep.class),
    MushroomCow("MushroomCow", 96, EntityType.MUSHROOM_COW, EntityMushroomCow.class, CustomMushroomCow.class),
    Rabbit("Rabbit", 101, EntityType.RABBIT, EntityRabbit.class, CustomRabbit.class),
    Witch("Witch", 66, EntityType.WITCH, EntityWitch.class, CustomWitch.class),
    Horse("EntityHorse", 100, EntityType.HORSE, EntityHorse.class, CustomHorse.class),
    PolarBear("PolarBear", 102, EntityType.POLAR_BEAR, EntityPolarBear.class, CustomPolarBear.class),
    Ozelot("Ozelot", 98, EntityType.OCELOT, EntityOcelot.class, CustomOcelot.class)
    
    ;

    private String name;
    private int id;
    private EntityType entityType;
    private Class<? extends EntityInsentient> nmsClass;
    private Class<? extends EntityInsentient> customClass;

    private EntityRegistrar(String name, int id, EntityType entityType, Class<? extends EntityInsentient> nmsClass, Class<? extends EntityInsentient> customClass) {
        this.name = name;
        this.id = id;
        this.entityType = entityType;
        this.nmsClass = nmsClass;
        this.customClass = customClass;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return id;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public Class<? extends EntityInsentient> getNMSClass() {
        return nmsClass;
    }

    public Class<? extends EntityInsentient> getCustomClass() {
        return customClass;
    }

    /**
     * Register our entities.
     */
    public static void registerEntities() {

        for (EntityRegistrar entity : values())
            a(entity.getCustomClass(), entity.getName(), entity.getID());

        // BiomeBase#biomes became private.
        BiomeBase[] biomes;
        try {
            biomes = (BiomeBase[]) getPrivateStatic(BiomeBase.class, "biomes");
        } catch (Exception exc) {
            // Unable to fetch.
            return;
        }
        for (BiomeBase biomeBase : biomes) {
            if (biomeBase == null)
                break;

            // This changed names from J, K, L and M.
            // 1.9 - changed from at au av aw
            for (String field : new String[] { "u", "v", "w", "x" })
                try {
                    Field list = BiomeBase.class.getDeclaredField(field);
                    list.setAccessible(true);
                    @SuppressWarnings("unchecked")
                    List<BiomeMeta> mobList = (List<BiomeMeta>) list.get(biomeBase);

                    // Write in our custom class.
                    for (BiomeMeta meta : mobList)
                        for (EntityRegistrar entity : values())
                            if (entity.getNMSClass().equals(meta.b))
                                meta.b = entity.getCustomClass();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * Unregister our entities to prevent memory leaks. Call on disable.
     */
    public static void unregisterEntities() {
        for (EntityRegistrar entity : values()) {
            // Remove our class references.
            try {
                ((Map<?, ?>) getPrivateStatic(EntityTypes.class, "d")).remove(entity.getCustomClass());
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                ((Map<?, ?>) getPrivateStatic(EntityTypes.class, "f")).remove(entity.getCustomClass());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (EntityRegistrar entity : values())
            try {
                // Unregister each entity by writing the NMS back in place of the custom class.
                a(entity.getNMSClass(), entity.getName(), entity.getID());
            } catch (Exception e) {
                e.printStackTrace();
            }

        // Biomes#biomes was made private so use reflection to get it.
        BiomeBase[] biomes;
        try {
            biomes = (BiomeBase[]) getPrivateStatic(BiomeBase.class, "biomes");
        } catch (Exception exc) {
            // Unable to fetch.
            return;
        }
        for (BiomeBase biomeBase : biomes) {
            if (biomeBase == null)
                break;

            // The list fields changed names but update the meta regardless.
            for (String field : new String[] { "u", "v", "w", "x" })
                try {
                    Field list = BiomeBase.class.getDeclaredField(field);
                    list.setAccessible(true);
                    @SuppressWarnings("unchecked")
                    List<BiomeMeta> mobList = (List<BiomeMeta>) list.get(biomeBase);

                    // Make sure the NMS class is written back over our custom class.
                    for (BiomeMeta meta : mobList)
                        for (EntityRegistrar entity : values())
                            if (entity.getCustomClass().equals(meta.b))
                                meta.b = entity.getNMSClass();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * A convenience method.
     * @param clazz The class.
     * @param f The string representation of the private static field.
     * @return The object found
     * @throws Exception if unable to get the object.
     */
    private static Object getPrivateStatic(Class<?> clazz, String f) throws Exception {
        Field field = clazz.getDeclaredField(f);
        field.setAccessible(true);
        return field.get(null);
    }

    /*
     * Since 1.7.2 added a check in their entity registration, simply bypass it and write to the maps ourself.
     */
    @SuppressWarnings("unchecked")
    private static void a(Class<?> paramClass, String paramString, int paramInt) {
        try {
            ((Map<String, Class<?>>) getPrivateStatic(EntityTypes.class, "c")).put(paramString, paramClass);
            ((Map<Class<?>, String>) getPrivateStatic(EntityTypes.class, "d")).put(paramClass, paramString);
            ((Map<Integer, Class<?>>) getPrivateStatic(EntityTypes.class, "e")).put(Integer.valueOf(paramInt), paramClass);
            ((Map<Class<?>, Integer>) getPrivateStatic(EntityTypes.class, "f")).put(paramClass, Integer.valueOf(paramInt));
            ((Map<String, Integer>) getPrivateStatic(EntityTypes.class, "g")).put(paramString, Integer.valueOf(paramInt));
        } catch (Exception exc) {
            // Unable to register the new class.
        }
    }
}