package com.edasaki.rpg.spells.paladin;

import java.util.ArrayList;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.edasaki.core.utils.RParticles;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

import de.slikey.effectlib.util.ParticleEffect;

public class Smash extends SpellEffect {

    @Override
    public boolean cast(final Player p, PlayerDataRPG pd, int level) {
        int damage = pd.getDamage(true);
        damage *= functions[0].applyAsDouble(level) / 100.0;
        RParticles.show(ParticleEffect.EXPLOSION_LARGE, p.getLocation(), 5);
        Spell.damageNearby(damage, p, p.getLocation(), 3, new ArrayList<Entity>());
        Spell.notify(p, "You smash the ground with a powerful blow.");
        return true;
    }

}
