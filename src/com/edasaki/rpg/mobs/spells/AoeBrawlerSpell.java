package com.edasaki.rpg.mobs.spells;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.edasaki.core.utils.RMath;
import com.edasaki.rpg.mobs.MobData;
import com.edasaki.rpg.spells.Spell;

public class AoeBrawlerSpell extends MobSpell {

    private long cooldown;

    public AoeBrawlerSpell(long cooldown) {
        this.cooldown = cooldown;
    }

    public void castSpell(final LivingEntity caster, final MobData md, Player target) {
        int dmg = md.getDamage();
        for (Entity e : RMath.getNearbyEntities(caster.getLocation(), 1.5)) {
            if (e instanceof Player && Spell.canDamage(e, false)) {
                Spell.damageEntity(e, dmg, caster, true, false);
            }
        }
    }

    @Override
    public long getCastDelay() {
        return cooldown;
    }
}
