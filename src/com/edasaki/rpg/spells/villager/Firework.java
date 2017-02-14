package com.edasaki.rpg.spells.villager;

import org.bukkit.entity.Player;

import com.edasaki.core.utils.RParticles;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

public class Firework extends SpellEffect {

    @Override
    public boolean cast(Player p, PlayerDataRPG pd, int level) {
        RParticles.spawnRandomFirework(p.getLocation());
        Spell.notify(p, "Pew pew! A beautiful firework shoots upwards.");
        pd.castedFirework = true;
        return true;
    }
}
