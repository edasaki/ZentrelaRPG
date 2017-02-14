package com.edasaki.rpg.items;

import org.bukkit.ChatColor;

public class ItemBalance {

    /*
     * Sage
     */
    public static final String[] SAGE_NAMES = {
            // owner names
            "Edasaki",
            "Misaka",
            "Frenda",
            // Sages
            "Aleister",
            "Solomon",
            "Aiwass",
            "Silvia",
            "Fenrir",
            "Cendrillon",
            "Etzali",
            "Vasilisa",
            "Acqua",
            "Fiamma",
            "Belle"
    };
    protected static final double SAGE_CHANCE = 0.0005;

    /*
     * Equip drop rates
     */

    protected static final ChatColor[] ITEM_TIER_COLORS = {
            ChatColor.GRAY,
            ChatColor.DARK_AQUA,
            ChatColor.DARK_GREEN,
            ChatColor.LIGHT_PURPLE,
            ChatColor.GOLD
    };

    /*
     * Rarity
     */
    protected static final double[] RARITY_CHANCES = { //check by doing Math.rand() and seeing if less than value
            1.0,
            0.06, //6% rare or better
            0.03, //3% epic or better
            0.01, //1% unique or better
            0.003, //0.3% supreme or better
            0.0008, //0.08% legendary or better
            0.0001, //0.01% godlike (1 in 10000)
    };

    public static final String[] RARITY_NAMES = {
            "", //0
            "Rare", //1
            "Epic", //2
            "Unique", //3
            "Supreme", //4
            "Legendary", //5
            "Godlike" //6
    };

    protected static final ChatColor[] RARITY_COLORS = {
            ChatColor.GRAY, //normal
            ChatColor.BLUE, //rare
            ChatColor.RED, //epic
            ChatColor.AQUA, //unique
            ChatColor.LIGHT_PURPLE, //supreme
            ChatColor.GREEN, //legendary
            ChatColor.GOLD //godlike
    };

    public static String getRarityName(int rarityZeroIndexed) {
        return RARITY_NAMES[rarityZeroIndexed];
    }

    public static ChatColor getRarityColor(int rarityZeroIndexed) {
        return RARITY_COLORS[rarityZeroIndexed];
    }

    /*
     * Prefixes
     */
    protected static final double PREFIX_CHANCE = 0.8;

    public static final String[] SET_PREFIXES = {
            "Fancy",
            "Shiny",
            "Ancient",
            "Traditional",
            "Angelic",
            "Demonic",
            "Sakura",
            "Gremlin",
            "Revered",
            "Battle",
            "Dawn",
            "Dusk",
            "Barbaric",
            "Elven",
            "Scorched",
            "Viridian"
    };

    private static final String[] TIER_PREFIXES_ARMOR = {
            "Leather",
            "Chainmail",
            "Iron",
            "Golden",
            "Diamond"
    };

    public static final String getTierPrefixArmor(int level) {
        return TIER_PREFIXES_ARMOR[getTierZeroIndex(level)];
    }

    private static final String[] TIER_PREFIXES_WEAPON = {
            "Recruit",
            "Novice",
            "Apprentice",
            "Veteran",
            "Master"
    };

    public static final String getTierPrefixWeapon(int level) {
        return TIER_PREFIXES_WEAPON[getTierZeroIndex(level)];
    }

    public static int getTierZeroIndex(int level) {
        if (level < 20)
            return 0;
        else if (level < 40)
            return 1;
        else if (level < 60)
            return 2;
        else if (level < 80)
            return 3;
        else
            return 4;
    }

}
