package com.edasaki.rpg.spells.wizard;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RParticles;
import com.edasaki.core.utils.RTicks;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

import de.slikey.effectlib.util.ParticleEffect;

public class BubbleBurst extends SpellEffect {

    @Override
    public boolean cast(final Player p, final PlayerDataRPG pd, final int level) {
        ArrayList<Entity> hit = new ArrayList<Entity>();
        int damage = pd.getDamage(true);
        switch (level) {
            case 1:
                damage *= 1.0;
                break;
            case 2:
                damage *= 1.1;
                break;
            case 3:
                damage *= 1.2;
                break;
            case 4:
                damage *= 1.3;
                break;
            case 5:
                damage *= 1.4;
                break;
            case 6:
                damage *= 1.5;
                break;
            case 7:
                damage *= 1.6;
                break;
            case 8:
                damage *= 1.7;
                break;
            case 9:
                damage *= 1.8;
                break;
            case 10:
                damage *= 1.9;
                break;
        }
        final Location start = p.getLocation().add(0, p.getEyeHeight() * 0.75, 0);
        for (Vector v : getVectors(p)) {
            for (Location loc : RMath.calculateVectorPath(start.clone(), v, 3, 4)) {
                RParticles.showWithOffset(ParticleEffect.WATER_WAKE, loc, 0.2, 7);
                ArrayList<Entity> damaged = Spell.damageNearby(damage, p, loc, 1.0, hit);
                hit.addAll(damaged);
                for (Entity e : damaged) {
                    if (e instanceof LivingEntity) {
                        ((LivingEntity) e).addPotionEffect(PotionEffectType.SLOW.createEffect(RTicks.seconds(5), 2));
                    }
                }
            }
        }
        Spell.notify(p, "A circle of water bursts out from your wand.");
        return true;
    }

    public ArrayList<Vector> getVectors(Player p) {
        ArrayList<Vector> vectors = new ArrayList<Vector>();
        Vector v = p.getEyeLocation().getDirection().normalize();
        v.setY(0);
        vectors.add(v);
        double z = v.getZ();
        double x = v.getX();
        double radians = Math.atan(z / x);
        if (x < 0)
            radians += Math.PI;
        for (int k = 1; k < 24; k++) {
            Vector v2 = new Vector();
            v2.setY(v.getY());
            v2.setX(Math.cos(radians + k * Math.PI / 12));
            v2.setZ(Math.sin(radians + k * Math.PI / 12));
            vectors.add(v2.normalize());
        }
        return vectors;
    }
}
