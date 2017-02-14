package com.edasaki.rpg.spells.paladin;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.edasaki.core.utils.RParticles;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

import de.slikey.effectlib.util.ParticleEffect;

public class ChantOfNecessarius extends SpellEffect {

    public static final String BUFF_ID = "chant of necessarius";

    @Override
    public boolean cast(final Player p, PlayerDataRPG pd, int level) {
        int duration = 3;
        switch (level) {
            case 1:
                duration = 3;
                break;
            case 2:
                duration = 4;
                break;
            case 3:
                duration = 5;
                break;
        }
        ArrayList<Location> locs = new ArrayList<Location>();
        final Location startLoc = p.getLocation().clone();
        locs.add(startLoc.clone().add(1, 0, 1));
        locs.add(startLoc.clone().add(1, 0, 0));
        locs.add(startLoc.clone().add(1, 0, -1));
        locs.add(startLoc.clone().add(0, 0, 1));
        locs.add(startLoc.clone().add(0, 0, 0));
        locs.add(startLoc.clone().add(0, 0, -1));
        locs.add(startLoc.clone().add(-1, 0, 1));
        locs.add(startLoc.clone().add(-1, 0, 0));
        locs.add(startLoc.clone().add(-1, 0, -1));
        for (Location loc : locs) {
            for (int k = 0; k < 10; k++) {
                RParticles.showWithSpeed(ParticleEffect.PORTAL, loc, 0, 10);
                loc = loc.add(0, 0.5, 0);
            }
        }
        pd.giveBuff(ChantOfNecessarius.BUFF_ID, 0, duration * 1000);
        Spell.notify(p, "You begin an ancient chant of divine protection.");
        Spell.notifyDelayed(p, "You finish your chant.", duration);
        return true;
    }

}
