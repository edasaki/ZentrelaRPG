package com.edasaki.rpg.worldboss.bosses;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.mobs.MobData;

public abstract class BossGenerator {
    public static SakiRPG plugin;

    public abstract MobData generate(Location loc, int level);

    public int getTier(int level) {
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

    public abstract String getMessage1();

    public abstract String getMessage2();

    public abstract String getMessage3();

    public abstract void playSound(Player p);
}
