package com.edasaki.rpg.mobs.spells;

import java.util.ArrayList;

import org.bukkit.ChatColor;
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

public class SlimeKingBurstSpell extends MobSpell {

    private static final String[] PHRASES = {
            ChatColor.GREEN + "Gloop gloop.",
            ChatColor.GREEN + "Blop plop.",
            ChatColor.GREEN + "Grbl mrbl.",
            ChatColor.GREEN + "Floop bloop.",
    };

    public void castSpell(final LivingEntity caster, final MobData md, Player target) {
        md.shout((String) RMath.randObject(PHRASES), 20);
        final ArrayList<Entity> hit = new ArrayList<Entity>();
        final Location start = md.entity.getLocation();
        start.setY(start.getY() + 1.3);
        for (Vector v : getVectors(md.entity)) {
            ArrayList<Location> locs = RMath.calculateVectorPath(start.clone(), v, 15, 4);
            int count = 0;
            for (int k = 0; k < locs.size(); k++) {
                final Location loc = locs.get(k);
                RScheduler.schedule(Spell.plugin, new Runnable() {
                    public void run() {
                        RParticles.showWithOffset(ParticleEffect.SLIME, loc, 0.2, 1);
                        int damage = md.getDamage() * 3;
                        ArrayList<Entity> damaged = Spell.damageNearby(damage, md.entity, loc, 1.0, hit, true, false, true);
                        hit.addAll(damaged);
                    }
                }, 1 * count);
                if (k % 2 == 0)
                    count++;
            }
        }
    }

    public ArrayList<Vector> getVectors(Entity e) {
        ArrayList<Vector> vectors = new ArrayList<Vector>();
        Vector v = e.getLocation().getDirection().normalize();
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

    @Override
    public long getCastDelay() {
        return (int) (Math.random() * 3000) + 6000;
    }
}
