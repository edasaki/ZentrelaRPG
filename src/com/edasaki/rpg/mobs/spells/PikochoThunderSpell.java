package com.edasaki.rpg.mobs.spells;

import java.util.ArrayList;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.edasaki.core.utils.RParticles;
import com.edasaki.rpg.mobs.MobData;
import com.edasaki.rpg.spells.Spell;

public class PikochoThunderSpell extends MobSpell {

    public void castSpell(final LivingEntity caster, final MobData md, Player target) {
        RParticles.sendLightning(target, target.getLocation());
        Spell.damageNearby((int) (md.getDamage() * 1.1), caster, target.getLocation(), 1.5, new ArrayList<Entity>(), true, false, true);
    }

    @Override
    public long getCastDelay() {
        return (int) (Math.random() * 7000) + 8000;
    }
}
