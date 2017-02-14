package com.edasaki.rpg.spells.paladin;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.edasaki.core.utils.RParticles;
import com.edasaki.core.utils.RTicks;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

public class WalkingSanctuary extends SpellEffect {

    public static final String BUFF_ID = "walking sanctuary";

    @Override
    public boolean cast(final Player p, PlayerDataRPG pd, int level) {
        double multiplier = 2.0;
        switch (level) {
            case 1:
                multiplier = 2.0;
                break;
            case 2:
                multiplier = 2.3;
                break;
            case 3:
                multiplier = 2.6;
                break;
            case 4:
                multiplier = 2.9;
                break;
            case 5:
                multiplier = 3.2;
                break;
        }
        ArrayList<Location> locs = new ArrayList<Location>();
        final Location startLoc = p.getLocation().clone();
        locs.add(startLoc.clone().add(1, 0, 1));
        locs.add(startLoc.clone().add(1, 0, -1));
        locs.add(startLoc.clone().add(-1, 0, 1));
        locs.add(startLoc.clone().add(-1, 0, -1));
        for (Location loc : locs) {
            RParticles.sendLightning(p, loc);
        }
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, RTicks.seconds(10), 5, true, false));
        pd.giveBuff(WalkingSanctuary.BUFF_ID, multiplier, 10000);
        Spell.notify(p, "You call upon divine powers.");
        Spell.notifyDelayed(p, "You feel Walking Sanctuary wear off.", 10);
        return true;
    }

}
