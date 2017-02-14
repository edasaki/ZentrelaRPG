package com.edasaki.rpg.spells.wizard;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RParticles;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

import de.slikey.effectlib.util.ParticleEffect;

public class FlameWave extends SpellEffect {

    @Override
    public boolean cast(final Player p, final PlayerDataRPG pd, final int level) {
        final ArrayList<Entity> hit = new ArrayList<Entity>();
        final Location start = p.getLocation().add(0, p.getEyeHeight() * 0.75, 0);
        for (Vector v : getVectors(p, level)) {
            ArrayList<Location> locs = RMath.calculateVectorPath(start.clone(), v, 12, 3);
            int count = 0;
            for (int k = 0; k < locs.size(); k++) {
                final Location loc = locs.get(k);
                RScheduler.schedule(Spell.plugin, new Runnable() {
                    public void run() {
                        RParticles.showWithOffset(ParticleEffect.FLAME, loc, 0.2, 1);
                        int damage = pd.getDamage(true);
                        switch (level) {
                            case 1:
                                damage *= 2.0;
                                break;
                            case 2:
                                damage *= 2.2;
                                break;
                            case 3:
                                damage *= 2.4;
                                break;
                            case 4:
                                damage *= 2.6;
                                break;
                            case 5:
                                damage *= 2.8;
                                break;
                            case 6:
                                damage *= 3.0;
                                break;
                            case 7:
                                damage *= 3.2;
                                break;
                            case 8:
                                damage *= 3.4;
                                break;
                            case 9:
                                damage *= 3.6;
                                break;
                            case 10:
                                damage *= 3.8;
                                break;
                        }
                        ArrayList<Entity> damaged = Spell.damageNearby(damage, p, loc, 1.0, hit);
                        hit.addAll(damaged);
                    }
                }, 1 * count);
                if (k % 2 == 0)
                    count++;
            }
        }
        Spell.notify(p, "A wave of hot flame spreads out from your wand.");
        return true;
    }

    public ArrayList<Vector> getVectors(Player p, int level) {
        ArrayList<Vector> vectors = new ArrayList<Vector>();
        Vector v = p.getEyeLocation().getDirection().normalize();
        double x = v.getX();
        double y = v.getY();
        double z = v.getZ();
        double radians = Math.atan(z / x);
        if (x < 0)
            radians += Math.PI;
        vectors.add(v);

        if (level < 3) {
            v = new Vector();
            v.setY(y);
            v.setX(Math.cos(radians - Math.PI / 6));
            v.setZ(Math.sin(radians - Math.PI / 6));
            vectors.add(v.normalize());

            v = new Vector();
            v.setY(y);
            v.setX(Math.cos(radians + Math.PI / 6));
            v.setZ(Math.sin(radians + Math.PI / 6));
            vectors.add(v.normalize());
        } else if (level < 5) {
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
        } else {
            v = new Vector();
            v.setY(y);
            v.setX(Math.cos(radians - Math.PI / 10));
            v.setZ(Math.sin(radians - Math.PI / 10));
            vectors.add(v.normalize());

            v = new Vector();
            v.setY(y);
            v.setX(Math.cos(radians - Math.PI / 5));
            v.setZ(Math.sin(radians - Math.PI / 5));
            vectors.add(v.normalize());

            v = new Vector();
            v.setY(y);
            v.setX(Math.cos(radians - 3 * Math.PI / 10));
            v.setZ(Math.sin(radians - 3 * Math.PI / 10));
            vectors.add(v.normalize());

            v = new Vector();
            v.setY(y);
            v.setX(Math.cos(radians + Math.PI / 10));
            v.setZ(Math.sin(radians + Math.PI / 10));
            vectors.add(v.normalize());

            v = new Vector();
            v.setY(y);
            v.setX(Math.cos(radians + Math.PI / 5));
            v.setZ(Math.sin(radians + Math.PI / 5));
            vectors.add(v.normalize());

            v = new Vector();
            v.setY(y);
            v.setX(Math.cos(radians + 3 * Math.PI / 10));
            v.setZ(Math.sin(radians + 3 * Math.PI / 10));
            vectors.add(v.normalize());
        }
        return vectors;
    }
}
