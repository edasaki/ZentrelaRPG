package com.edasaki.rpg.mobs.spells;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RParticles;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.rpg.mobs.MobData;
import com.edasaki.rpg.spells.Spell;

import de.slikey.effectlib.util.ParticleEffect;

public class QuadBeamSpell extends MobSpell {

    private int range;
    private long cooldown;

    public QuadBeamSpell(int range, long cooldown) {
        this.range = range;
        this.cooldown = cooldown;
    }

    public void castSpell(final LivingEntity caster, final MobData md, Player target) {
        final ArrayList<Entity> hit = new ArrayList<Entity>();
        for (int height = 1; height <= 3; height++) {
            final Location start = md.entity.getLocation();
            start.setY(start.getY() + 0.7 * height);
            for (Vector v : getVectorsNormal(md.entity)) {
                ArrayList<Location> locs = RMath.calculateVectorPath(start.clone(), v, range, 2);
                int count = 0;
                for (int k = 0; k < locs.size(); k++) {
                    final Location loc = locs.get(k);
                    RScheduler.schedule(Spell.plugin, new Runnable() {
                        public void run() {
                            RParticles.showWithOffset(ParticleEffect.CRIT, loc, 0.2, 1);
                            int damage = (int) (md.getDamage() * 1.2);
                            ArrayList<Entity> damaged = Spell.damageNearby(damage, md.entity, loc, 1.0, hit, true, false, true);
                            hit.addAll(damaged);
                        }
                    }, 1 * count);
                    if (k % 2 == 0)
                        count++;
                }
            }
        }
    }

    private ArrayList<Vector> getVectorsNormal(LivingEntity e) {
        ArrayList<Vector> vectors = new ArrayList<Vector>();
        Vector v = e.getEyeLocation().getDirection().normalize();
        v.setY(0);
        vectors.add(v);
        double z = v.getZ();
        double x = v.getX();
        double radians = Math.atan(z / x);
        if (x < 0)
            radians += Math.PI;
        for (int k = 1; k < 4; k++) {
            Vector v2 = new Vector();
            v2.setY(v.getY());
            v2.setX(Math.cos(radians + k * Math.PI / 2));
            v2.setZ(Math.sin(radians + k * Math.PI / 2));
            vectors.add(v2.normalize());
        }
        return vectors;
    }

    @Override
    public long getCastDelay() {
        return cooldown;
    }
}
