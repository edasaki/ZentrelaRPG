package com.edasaki.rpg.trinkets;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;

public enum TrinketStat {

    HP_SMALL(ChatColor.WHITE + "+%d " + ChatColor.GREEN + "HP", 50) {
        @Override
        public void apply(Player p, PlayerDataRPG pd, int level) {
            pd.maxHP += getValue(level);
        }
    },

    HP_MEDIUM(ChatColor.WHITE + "+%d " + ChatColor.GREEN + "HP", 100) {
        @Override
        public void apply(Player p, PlayerDataRPG pd, int level) {
            pd.maxHP += getValue(level);
        }
    },

    DEFENSE_SMALL(ChatColor.WHITE + "+%d " + ChatColor.DARK_AQUA + "Defense", new int[] { 2, 4, 8, 14, 20, 26, 32, 38, 44, 50 }) {
        @Override
        public void apply(Player p, PlayerDataRPG pd, int level) {
            pd.defense += getValue(level);
        }
    },

    DEFENSE_LARGE(ChatColor.WHITE + "+%d " + ChatColor.DARK_AQUA + "Defense", new int[] { 5, 10, 15, 20, 25, 35, 45, 50, 65, 75 }) {
        @Override
        public void apply(Player p, PlayerDataRPG pd, int level) {
            pd.defense += getValue(level);
        }
    },

    HP_NEGATIVE(ChatColor.WHITE + "-%d%% " + ChatColor.RED + "Max HP", new int[] { 30, 29, 28, 27, 26, 24, 22, 20, 18, 16 }) {
        @Override
        public void apply(Player p, PlayerDataRPG pd, int level) {
            pd.maxHP -= (pd.maxHP + pd.getBaseMaxHP()) * (getValue(level) / 100.0);
        }
    },

    DAMAGE_MULTIPLIER_SMALL(ChatColor.WHITE + "+%d%% " + ChatColor.GREEN + "Damage", new int[] { 5, 6, 7, 8, 9, 10, 11, 12, 13, 15 }) {
        @Override
        public void apply(Player p, PlayerDataRPG pd, int level) {
            pd.damageLow *= (1 + (getValue(level) / 100.0));
            pd.damageHigh *= (1 + (getValue(level) / 100.0));
        }
    },

    DAMAGE_MULTIPLIER_LARGE(ChatColor.WHITE + "+%d%% " + ChatColor.GREEN + "Damage", new int[] { 15, 16, 17, 18, 20, 22, 24, 26, 28, 30 }) {
        @Override
        public void apply(Player p, PlayerDataRPG pd, int level) {
            pd.damageLow *= (1 + (getValue(level) / 100.0));
            pd.damageHigh *= (1 + (getValue(level) / 100.0));
        }
    },

    CRIT_CHANCE_LARGE(ChatColor.WHITE + "+%d%% " + ChatColor.GOLD + "Crit Chance", new int[] { 5, 6, 7, 8, 9, 10, 11, 12, 13, 15 }) {
        @Override
        public void apply(Player p, PlayerDataRPG pd, int level) {
            pd.critChance += getValue(level);
        }
    },

    SPEED_LARGE(ChatColor.WHITE + "+%d%% " + ChatColor.YELLOW + "Movement Speed", new int[] { 32, 34, 36, 38, 40, 42, 44, 46, 48, 50 }) {
        @Override
        public void apply(Player p, PlayerDataRPG pd, int level) {
            pd.speed += getValue(level);
        }
    },
    ;

    public String format;
    public int[] values;

    public abstract void apply(Player p, PlayerDataRPG pd, int level);

    public int getValue(int level) {
        return values[level];
    }

    public String getFormat(int level) {
        return String.format(format, values[level]);
    }

    TrinketStat(String format, int[] values) {
        this.format = format;
        this.values = new int[11];
        for (int k = 1; k <= values.length; k++)
            this.values[k] = values[k - 1];
    }

    TrinketStat(String format, int mult) {
        this.format = format;
        this.values = new int[11];
        for (int k = 0; k < values.length; k++)
            this.values[k] = k * mult;
    }

}
