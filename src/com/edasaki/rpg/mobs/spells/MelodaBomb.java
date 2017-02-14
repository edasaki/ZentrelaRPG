package com.edasaki.rpg.mobs.spells;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RTicks;
import com.edasaki.rpg.mobs.MobData;
import com.edasaki.rpg.particles.EffectFactory;
import com.edasaki.rpg.particles.custom.spells.MelodaBombEffect;
import com.edasaki.rpg.spells.Spell;

public class MelodaBomb extends MobSpell {

    @Override
    public void castSpell(LivingEntity caster, MobData md, Player target) {
        for (int k = 0; k < RMath.randInt(20, 35); k++) {
            RScheduler.schedule(Spell.plugin, new Runnable() {
                public void run() {
                    if (caster == null || !caster.isValid() || caster.isDead() || md.dead || md.despawned)
                        return;
                    Location loc = caster.getLocation();
                    double range = 10;
                    Location l = loc.clone().add(RMath.randDouble(-range, range), RMath.randDouble(10, 15), RMath.randDouble(-range, range));
                    FallingBlock fall = l.getWorld().spawnFallingBlock(l, Material.PACKED_ICE, (byte) 0);
                    fall.setDropItem(false);
                    fall.setHurtEntities(false);
                    RScheduler.schedule(Spell.plugin, new Runnable() {
                        public void run() {
                            if (fall.isDead()) {
                                MelodaBombEffect effect = new MelodaBombEffect(EffectFactory.em(), fall.getLocation());
                                effect.run();
                                Spell.damageNearby(md.getDamage() * 3, caster, fall.getLocation(), 2, null);
                            } else {
                                RScheduler.schedule(Spell.plugin, this, 3);
                            }
                        }
                    });
                }
            }, RMath.randInt(RTicks.seconds(1), RTicks.seconds(7)));
        }
    }

    @Override
    public long getCastDelay() {
        return 10000;
    }

}
