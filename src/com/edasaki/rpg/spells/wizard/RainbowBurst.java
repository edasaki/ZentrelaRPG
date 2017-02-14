package com.edasaki.rpg.spells.wizard;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RParticles;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

import de.slikey.effectlib.util.ParticleEffect;

public class RainbowBurst extends SpellEffect {

    @Override
    public boolean cast(final Player p, final PlayerDataRPG pd, final int level) {
        ArrayList<Entity> hit = new ArrayList<Entity>();
        int damage = pd.getDamage(true);
        switch (level) {
            case 1:
                damage *= 1.4;
                break;
            case 2:
                damage *= 1.6;
                break;
            case 3:
                damage *= 1.8;
                break;
            case 4:
                damage *= 2.0;
                break;
            case 5:
                damage *= 2.2;
                break;
            case 6:
                damage *= 2.4;
                break;
            case 7:
                damage *= 2.6;
                break;
            case 8:
                damage *= 2.8;
                break;
            case 9:
                damage *= 3.0;
                break;
            case 10:
                damage *= 3.2;
                break;
        }
        final Location start = p.getLocation().add(0, p.getEyeHeight() * 0.75, 0);
        for (Vector v : getVectors(p)) {
            for (Location loc : RMath.calculateVectorPath(start.clone(), v, 3, 4)) {
                RParticles.showWithSpeed(ParticleEffect.SPELL_MOB, loc, 2, 1);
                ArrayList<Entity> damaged = Spell.damageNearby(damage, p, loc, 1.0, hit);
                hit.addAll(damaged);
                for (Entity e : damaged)
                    Spell.knockbackEntity(e, p, 10.0);
            }
        }
        Spell.notify(p, "Woohoo! Rainbow magic bursts out from your wand.");
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
