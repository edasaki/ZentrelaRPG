package com.edasaki.rpg.spells.crusader;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RParticles;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RTicks;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

import de.slikey.effectlib.util.ParticleEffect;

public class HyperBody extends SpellEffect {

    @Override
    public boolean cast(final Player p, PlayerDataRPG pd, int level) {
        long duration = 0;
        int speed = 0;
        int jump = 0;
        switch (level) {
            case 1:
                duration = 3000;
                speed = 0;
                jump = 0;
                break;
            case 2:
                duration = 3000;
                speed = 1;
                jump = 1;
                break;
            case 3:
                duration = 4000;
                speed = 2;
                jump = 1;
                break;
            case 4:
                duration = 4000;
                speed = 3;
                jump = 2;
                break;
            case 5:
                duration = 5000;
                speed = 4;
                jump = 3;
                break;
        }

        for(final Vector v : getVectors(p)) {
            for(int k = 0; k < RTicks.seconds(duration / 1000.0); k += 3) {
                RScheduler.schedule(Spell.plugin, new Runnable() {
                    public void run() {
                        ArrayList<Location> locs = RMath.calculateVectorPath(p.getLocation().add(0, p.getEyeHeight() * 0.5, 0), v, 1, 2);
                        for(Location loc : locs)
                            RParticles.showWithSpeed(ParticleEffect.CRIT_MAGIC, loc, 0, 1);
                    }
                }, k);
            }
        }
        Spell.notify(p, "You feel a surge of athleticism!");
        p.addPotionEffect(PotionEffectType.SPEED.createEffect(RTicks.seconds(duration/1000.0), speed));
        p.addPotionEffect(PotionEffectType.JUMP.createEffect(RTicks.seconds(duration/1000.0), jump));
        Spell.notifyDelayed(p, "You feel yourself slowing down...", duration / 1000.0);
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
