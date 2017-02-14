package com.edasaki.rpg.particles;

public enum EffectName {
    WATER_SPOUT("Water Spout", CosmeticRarity.COMMON, "A cute water fountain spouting from your head."),
    PETITE_ANGEL_WINGS("Petite Angel Wings", CosmeticRarity.COMMON, "A cute pair of angel wings."),
    MUSICAL("Musical", CosmeticRarity.COMMON, "The perfect effect for music lovers!"),
    PINK_HEART("Pink Heart", CosmeticRarity.COMMON, "A pretty pink heart."),
    RED_HEART("Red Heart", CosmeticRarity.COMMON, "A pretty red heart."),
    BLACK_HEART("Black Heart", CosmeticRarity.COMMON, "A pretty black heart."),
    RAINBOW_HEART("Rainbow Heart", CosmeticRarity.COMMON, "A pretty heart with random colors."),

    SWIRLY("Swirly", CosmeticRarity.RARE, "Try to not get dizzy!"),
    SPARK_RING("Spark Ring", CosmeticRarity.RARE, "Rings of sparkly stars."),
    ANGEL_WINGS("Angel Wings", CosmeticRarity.RARE, "Puffy angel wings, lighter than clouds!"),
    BLOOD_HELIX("Blood Helix", CosmeticRarity.RARE, "A bloody double helix that encircles you."),

    EMERALD_STAR("Emerald Star", CosmeticRarity.EPIC, "An emerald star hovers above you."),
    ATOMIC("Atomic", CosmeticRarity.EPIC, "Stand at the center of an atom with orbitals."),
    FAIRY_WINGS("Fairy Wings", CosmeticRarity.EPIC, "A cute set of fairy wings."),
    RAINY_DAY("Rainy Day", CosmeticRarity.EPIC, "A little rainy cloud floating over your head."),

    INFERNO_TAG("Inferno Tag", CosmeticRarity.LEGENDARY, "Your enemies will cower in fear when they behold your name written majestically in flames."),
    FLAME_BREATH("Flame Breath", CosmeticRarity.LEGENDARY, "Use the power of ancient dragons to breathe tendrils of fire."),
    FALLEN_ANGEL("Fallen Angel", CosmeticRarity.LEGENDARY, "The wings of an angel fallen from glory and consumed by darkness."),
    ZELLUMINATI("Zelluminati", CosmeticRarity.LEGENDARY, "The symbol of one of Zentrela's oldest secret societies."),

    ;
    public String name;
    public CosmeticRarity rarity;
    public String desc;

    EffectName(String displayName, CosmeticRarity rarity, String desc) {
        this.name = displayName;
        this.rarity = rarity;
        this.desc = desc;
    }
}