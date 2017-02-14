package com.edasaki.rpg.mobs;

import com.edasaki.rpg.items.StatBalance;

public class MobBalance {

    public static double getDropRate(int level) {
        double rate = 0.20 - 0.002 * level;
        if (rate < 0.01) {
            int diff = level - 100;
            if (diff < 1)
                diff = 1;
            rate = 0.01 * Math.pow(0.9, diff);
        }
        return rate;
    }

    /*
     * All of the methods below are only run during mob loading so they can
     * be kinda laggy and it won't be a big deal.
     */

    public static long getMobEXP(int level, double multiplier) {
        if (level < 1)
            level = 1;
        if (level - 1 >= MobStatics.MOBEXP.length) {
            return (long) Math.ceil(multiplier * (((level - 1 - MobStatics.MOBEXP.length) * 500) + MobStatics.MOBEXP[MobStatics.MOBEXP.length - 1][1]));
        }
        long val = (long) (Math.ceil(multiplier * MobStatics.MOBEXP[level - 1][1]));
        val *= 1.2; // global buff
        return val;
    }

    public static long getMobHP(int level, double multiplier) {
        double base = StatBalance.getDamageRangeCore(level) * 1.3;
        double count = 0.3 * level + 3;
        base *= count;
        base *= multiplier;
        return (long) Math.ceil(base);
    }

    public static int[] getMobDamage(int level, double damageMultiplier, double rangeMultiplier) {
        long playerHP = StatBalance.getHPCore(level) * 3 + level * 50 + StatBalance.getDefenseCore(level) + 100;
        double count = 12 - 0.05 * level;
        if (count < 2)
            count = 2;
        long core = (long) Math.ceil(playerHP / count * damageMultiplier);
        int range = (int) Math.ceil(0.15 * core);
        if (range < 3)
            range = 3;
        long low = core - range;
        long high = core + range;
        if (low < 1)
            low = 1;
        if (high > Integer.MAX_VALUE)
            high = Integer.MAX_VALUE;
        double globalMultiplier = 0.62;
        return new int[] { (int) Math.ceil(low * globalMultiplier), (int) Math.ceil(high * globalMultiplier) };
    }
}
