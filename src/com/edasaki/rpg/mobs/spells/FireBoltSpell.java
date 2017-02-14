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

public class FireBoltSpell extends MobSpell {

    private int range;
    private long cooldown;

    public FireBoltSpell(int range, long cooldown) {
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
                            RParticles.showWithOffset(ParticleEffect.FLAME, loc, 0.2, 1);
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
        double x = v.getX();
        double y = v.getY();
        double z = v.getZ();
        double radians = Math.atan(z / x);
        if (x < 0)
            radians += Math.PI;
        vectors.add(v);

        v = new Vector();
        v.setY(y);
        v.setX(Math.cos(radians - Math.PI / 4));
        v.setZ(Math.sin(radians - Math.PI / 4));
        vectors.add(v.normalize());

        v = new Vector();
        v.setY(y);
        v.setX(Math.cos(radians - Math.PI / 8));
        v.setZ(Math.sin(radians - Math.PI / 8));
        vectors.add(v.normalize());

        v = new Vector();
        v.setY(y);
        v.setX(Math.cos(radians + Math.PI / 8));
        v.setZ(Math.sin(radians + Math.PI / 8));
        vectors.add(v.normalize());

        v = new Vector();
        v.setY(y);
        v.setX(Math.cos(radians + Math.PI / 4));
        v.setZ(Math.sin(radians + Math.PI / 4));
        vectors.add(v.normalize());
        return vectors;
    }

    @Override
    public long getCastDelay() {
        return cooldown;
    }
}
