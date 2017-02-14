package com.edasaki.rpg.spells.paladin;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.edasaki.core.utils.RParticles;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

import de.slikey.effectlib.util.ParticleEffect;

public class FlameCharge extends SpellEffect {

    public static final String BUFF_ID = "flame charge";
    
    @Override
    public boolean cast(final Player p, PlayerDataRPG pd, int level) {
        int tier = 1;
        switch (level) {
            case 1:
                tier = 1;
                break;
            case 2:
                tier = 2;
                break;
            case 3:
                tier = 3;
                break;
            case 4:
                tier = 4;
                break;
            case 5:
                tier = 5;
                break;
        }
        Location start = p.getLocation().add(0, p.getEyeHeight() * 0.75, 0).clone();
        final Location loc = start.clone().add(0, -0.3, 0);
        for (int k = 0; k < 5; k++) {
            RScheduler.schedule(Spell.plugin, new Runnable() {
                public void run() {
                    RParticles.showWithOffset(ParticleEffect.FLAME, loc, 0.6, 10);
                    loc.add(0, 0.6, 0);
                }
            }, k);
        }
        pd.giveBuff(FlameCharge.BUFF_ID, tier, 5000);
        Spell.notify(p, "You charge your mace with the power of fire.");
        Spell.notifyDelayed(p, "Flame charge has worn off.", 6);
        return true;
    }

}
