package com.edasaki.rpg.items;

import com.edasaki.core.utils.RMath;

public class StatBalance {

    public static int getRarityLevelBonus(int rarity) {
        return rarity * 4;
    }

    public static int getDamageRangeCore(int level) {
        return 4 * level + 7;
    }

    public static int[] getDamageRange(int level) {
        int core = getDamageRangeCore(level);
        int range_low = (int) (Math.random() * (5 + level / 5)) + 3;
        int range_high = (int) (Math.random() * (5 + level / 5)) + 3;
        int low = core;
        int high = core;
        low -= (int) (Math.random() * Math.ceil(core * 0.08));
        high += (int) (Math.random() * Math.ceil(core * 0.08));
        low -= range_low;
        high += range_high;
        if (high - low < 6) {
            high += (int) (Math.random() * 3) + 3;
            low -= (int) (Math.random() * 3) + 3;
        }
        if (low < 1)
            low = 1;
        if (high <= low)
            high = low + 1;
        return new int[] { low, high };
    }

    public static int getHPCore(int level) {
        return 6 * level + 8;
    }

    public static int getHP(int level) {
        int core = getHPCore(level);
        int range = (int) Math.ceil(level * 0.1);
        if (range < 6)
            range = (int) (Math.random() * 5) + 6;
        int low = core - range / 2;
        int high = core + range;
        if (low < 1)
            low = 1;
        return RMath.randInt(low, high);
    }

    public static int getHPMultiplier(int level) {
        int core = (int) Math.floor(level / 10.0);
        int range = RMath.randInt(-2, 2);
        core += range;
        if (core < 1)
            core = 1;
        return core;
    }

    public static int getDefenseCore(int level) {
        double val = level;
        if (level < 30)
            val /= 4;
        else if (level < 60)
            val /= 3;
        else
            val /= 2;
        return (int) val;
    }

    public static int getDefense(int level) {
        int core = getDefenseCore(level);
        int range = (int) Math.ceil(level / 12.0);
        if (range < 2)
            range = (int) (Math.random() * 3) + 2;
        int low = core - range;
        int high = core + range;
        if (low < 1)
            low = 1;
        return RMath.randInt(low, high);
    }

    public static int getDefenseMultiplier(int level) {
        int core = (int) Math.floor(level / 10.0);
        int range = RMath.randInt(-2, 2);
        core += range;
        if (core < 1)
            core = 1;
        return core;
    }

    public static int getCritChanceWeapon(int level) {
        int val = (int) Math.ceil(level / 7.0);
        val += (int) (Math.random() * 5);
        return val;
    }

    public static int getCritChanceArmor(int level) {
        int val = (int) Math.ceil(level / 8.0);
        val += (int) (Math.random() * 3);
        return val;
    }

    public static int getCritDamage(int level) {
        int val = (int) Math.ceil(level / 7.0);
        val += (int) (Math.random() * 5);
        return val;
    }

    public static int getRarityFinder(int level) {
        int val = (int) Math.ceil(level / 6.0);
        val += (int) (Math.random() * 5);
        return val;
    }

    public static int getManaRegenRate(int level) {
        int core = (int) Math.floor(level / 6.0);
        int range = RMath.randInt(-10, 10);
        core += range;
        if (core < 1)
            core = 1;
        return core;
    }

    public static int getSpellDamage(int level) {
        int core = (int) Math.floor(level / 7.5);
        int range = RMath.randInt(-5, 5);
        core += range;
        if (core < 1)
            core = 1;
        return core;
    }

    public static int getAttackDamage(int level) {
        int core = (int) Math.floor(level / 7.5);
        int range = RMath.randInt(-5, 5);
        core += range;
        if (core < 1)
            core = 1;
        return core;
    }

    public static int getLifesteal(int level) {
        int core = (int) Math.floor(level / 7.5);
        int range = RMath.randInt(-5, 5);
        core += range;
        if (core < 1)
            core = 1;
        return core;
    }

    public static int getHPRegen(int level) {
        int core = (int) Math.floor(level / 7.5);
        int range = RMath.randInt(-5, 5);
        core += range;
        if (core < 1)
            core = 1;
        return core;
    }

    public static int getAttackSpeed(int level) {
        int val = (int) Math.ceil(level / 5.0);
        val += (int) (Math.random() * 7);
        return val;
    }

    public static int getSpeed(int level) {
        return RMath.randInt(5, 15);
    }
}
