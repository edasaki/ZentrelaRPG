package com.edasaki.rpg.spells.reaper;

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

public class Siphon extends SpellEffect {

    @Override
    public boolean cast(final Player p, final PlayerDataRPG pd, int level) {
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
            case 11:
                damage *= 2.0;
                break;
            case 12:
                damage *= 2.1;
                break;
            case 13:
                damage *= 2.2;
                break;
            case 14:
                damage *= 2.3;
                break;
            case 15:
                damage *= 2.4;
                break;
        }

        final int fDamage = damage;
        final ArrayList<Entity> hit = new ArrayList<Entity>();
        final Location start = p.getLocation().add(0, p.getEyeHeight() * 0.7, 0);
        ArrayList<Location> locs = RMath.calculateVectorPath(start.clone(), p.getLocation().getDirection(), 6, 4);
        int count = 0;
        final int healAmount = fDamage;
        for (int k = 0; k < locs.size(); k++) {
            final Location loc = locs.get(k);
            RScheduler.schedule(Spell.plugin, new Runnable() {
                public void run() {
                    RParticles.showWithSpeed(ParticleEffect.SPELL_WITCH, loc, 0, 2);
                    ArrayList<Entity> damaged = Spell.damageNearby(fDamage, p, loc, 1.0, hit);
                    for (final Entity e : damaged) {
                        final Beam b = new Beam();
                        b.curr = e.getLocation().add(0, p.getEyeHeight() * 0.75, 0).clone();
                        for (int t = 0; t < 20; t++) {
                            final int tVal = t;
                            RScheduler.schedule(Spell.plugin, new Runnable() {
                                public void run() {
                                    if (!p.isOnline() || !p.isValid())
                                        return;
                                    Location pLoc = p.getLocation().add(0, p.getEyeHeight() * 0.75, 0);
                                    Vector v = pLoc.toVector().subtract(b.curr.toVector());
                                    if (tVal < 10)
                                        v = v.multiply(0.1);
                                    else if (tVal < 15)
                                        v = v.multiply(0.2);
                                    else
                                        v = v.multiply(0.5);
                                    if (tVal == 19) {
                                        pd.heal(healAmount);
                                    }
                                    b.curr.add(v);
                                    RParticles.showWithSpeed(ParticleEffect.REDSTONE, b.curr, 0, 2);
                                }
                            }, t);
                        }
                    }
                    hit.addAll(damaged);
                }
            }, 1 * count);
            if (k % 2 == 0)
                count++;
        }
        Spell.notify(p, "You shoot out a siphoning bolt.");
        return true;
    }

    private static class Beam {
        public Location curr;
    }

}
