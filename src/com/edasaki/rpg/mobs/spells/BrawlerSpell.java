package com.edasaki.rpg.mobs.spells;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.edasaki.core.utils.RMath;
import com.edasaki.rpg.mobs.MobData;
import com.edasaki.rpg.spells.Spell;

public class BrawlerSpell extends MobSpell {

    private long cooldown;

    public BrawlerSpell(long cooldown) {
        this.cooldown = cooldown;
    }

    public void castSpell(final LivingEntity caster, final MobData md, Player target) {
        if (Spell.canDamage(target, false) && RMath.flatDistance(target.getLocation(), caster.getLocation()) < 1.5) {
            Spell.damageEntity(target, md.getDamage(), caster, true, false);
        }
    }

    @Override
    public long getCastDelay() {
        return cooldown;
    }
}
