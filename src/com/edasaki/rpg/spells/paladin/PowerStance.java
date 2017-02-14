package com.edasaki.rpg.spells.paladin;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.edasaki.core.utils.RParticles;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

import de.slikey.effectlib.util.ParticleEffect;

public class PowerStance extends SpellEffect {

    public static final String BUFF_ID = "power stance";
    
    @Override
    public boolean cast(final Player p, PlayerDataRPG pd, int level) {
        int duration = 5;
        switch (level) {
            case 1:
                duration = 5;
                break;
            case 2:
                duration = 6;
                break;
            case 3:
                duration = 7;
                break;
            case 4:
                duration = 8;
                break;
            case 5:
                duration = 9;
                break;
        }
        ArrayList<Location> locs = new ArrayList<Location>();
        final Location startLoc = p.getLocation().clone();
        locs.add(startLoc.clone().add(1, 0, 1));
        locs.add(startLoc.clone().add(1, 0, -1));
        locs.add(startLoc.clone().add(-1, 0, 1));
        locs.add(startLoc.clone().add(-1, 0, -1));
        for(Location loc : locs) {
            for(int k = 0 ; k < 5; k++) {
                RParticles.showWithSpeed(ParticleEffect.SPELL_WITCH, loc, 0, 20);
                loc = loc.add(0, 1, 0);
            }
        }
        pd.giveBuff(PowerStance.BUFF_ID, 0, duration * 1000);
        Spell.notify(p, "You assume a stance of great stability.");
        Spell.notifyDelayed(p, "You stop using your power stance.", duration);
        return true;
    }

}
