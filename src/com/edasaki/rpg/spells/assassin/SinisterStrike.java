package com.edasaki.rpg.spells.assassin;

import org.bukkit.entity.Player;

import com.edasaki.core.utils.RParticles;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

import de.slikey.effectlib.util.ParticleEffect;

public class SinisterStrike extends SpellEffect {
    
    public static final String BUFF_ID = "sinister strike";
    public static final String BUFF_ID_DAMAGE = "sinister strike damage";
    public static final String DEBUFF_ID = "sinister strike cripple";

    @Override
    public boolean cast(final Player p, PlayerDataRPG pd, int level) {
        RParticles.showWithOffset(ParticleEffect.SPELL, p.getLocation().add(0, p.getEyeHeight() * 0.5, 0), 1.0, 15);
        double value = 0;
        double damageBuff = 1.0;
        switch(level) {
            case 1:
                value = 2;
                damageBuff = 1.10;
                break;
            case 2:
                value = 3;
                damageBuff = 1.20;
                break;
            case 3:
                value = 4;
                damageBuff = 1.30;
                break;
            case 4:
                value = 5;
                damageBuff = 1.40;
                break;
            case 5:
                value = 6;
                damageBuff = 1.50;
                break;
        }
        pd.giveBuff(SinisterStrike.BUFF_ID, value, Spell.LONG_DURATION);
        pd.giveBuff(SinisterStrike.BUFF_ID_DAMAGE, damageBuff, Spell.LONG_DURATION);
        Spell.notify(p, "You prepare to cripple your enemy on your next attack.");
        return true;
    }

}
