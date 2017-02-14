package com.edasaki.rpg.spells.assassin;

import org.bukkit.entity.Player;

import com.edasaki.core.utils.RParticles;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

import de.slikey.effectlib.util.ParticleEffect;

public class DoubleStab extends SpellEffect {

    public static final String BUFF_ID = "double stab";

    @Override
    public boolean cast(final Player p, PlayerDataRPG pd, int level) {
        RParticles.showWithOffset(ParticleEffect.SPELL, p.getLocation().add(0, p.getEyeHeight() * 0.5, 0), 1.0, 15);
        double value = 0;
        switch (level) {
            case 1:
                value = 0.6;
                break;
            case 2:
                value = 0.7;
                break;
            case 3:
                value = 0.8;
                break;
            case 4:
                value = 0.9;
                break;
            case 5:
                value = 1.0;
                break;
            case 6:
                value = 1.1;
                break;
            case 7:
                value = 1.2;
                break;
            case 8:
                value = 1.3;
                break;
            case 9:
                value = 1.4;
                break;
            case 10:
                value = 1.5;
                break;
            case 11:
                value = 1.6;
                break;
            case 12:
                value = 1.7;
                break;
            case 13:
                value = 1.8;
                break;
            case 14:
                value = 1.9;
                break;
            case 15:
                value = 2.0;
                break;
            case 16:
                value = 2.1;
                break;
            case 17:
                value = 2.2;
                break;
            case 18:
                value = 2.3;
                break;
            case 19:
                value = 2.4;
                break;
            case 20:
                value = 2.5;
                break;
            case 21:
                value = 2.6;
                break;
            case 22:
                value = 2.7;
                break;
            case 23:
                value = 2.8;
                break;
            case 24:
                value = 2.9;
                break;
            case 25:
                value = 3.0;
                break;
        }
        pd.removeBuff(ShadowStab.BUFF_ID);
        pd.giveBuff(DoubleStab.BUFF_ID, value, Spell.LONG_DURATION);
        Spell.notify(p, "You prepare to double stab on your next attack.");
        return true;
    }

}
