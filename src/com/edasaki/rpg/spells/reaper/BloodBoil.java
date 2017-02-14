package com.edasaki.rpg.spells.reaper;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RParticles;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RTicks;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

import de.slikey.effectlib.util.ParticleEffect;

public class BloodBoil extends SpellEffect {

    @Override
    public boolean cast(final Player p, final PlayerDataRPG pd, int level) {
        Vector dir = p.getLocation().getDirection().normalize().multiply(0.3);
        Location start = p.getLocation().add(0, p.getEyeHeight() * 0.75, 0).clone();
        Location curr = start.clone();
        Entity target = null;
        for (int k = 0; k < 30; k++) {
            for (Entity e : RMath.getNearbyEntities(curr, 1.5)) {
                if (e != p) {
                    if (Spell.canDamage(e, true)) {
                        target = e;
                        break;
                    }
                }
            }
            if (target != null)
                break;
            curr.add(dir);
            if (!RParticles.isAirlike(curr.getBlock()))
                break;
        }
        if (target == null) {
            p.sendMessage(ChatColor.RED + "Failed to find a Blood Boil target.");
            return false;
        }
        int damage = pd.getDamage(true);
        switch (level) {
            case 1:
                damage *= 1.5;
                break;
            case 2:
                damage *= 1.9;
                break;
            case 3:
                damage *= 2.3;
                break;
            case 4:
                damage *= 2.7;
                break;
            case 5:
                damage *= 3.1;
                break;
            case 6:
                damage *= 3.5;
                break;
            case 7:
                damage *= 3.9;
                break;
            case 8:
                damage *= 4.3;
                break;
            case 9:
                damage *= 4.7;
                break;
            case 10:
                damage *= 5.1;
                break;
        }

        final Entity fTarget = target;
        final Beam b = new Beam();
        b.curr = p.getLocation().add(0, p.getEyeHeight() * 0.75, 0).clone();
        for (int t = 0; t < 20; t++) {
            final int tVal = t;
            RScheduler.schedule(Spell.plugin, new Runnable() {
                public void run() {
                    if (!p.isOnline() || !fTarget.isValid() || (fTarget instanceof Player && !((Player) fTarget).isOnline()))
                        return;
                    if (p.getWorld() != fTarget.getWorld() || RMath.flatDistance(p.getLocation(), fTarget.getLocation()) > 50)
                        return;
                    Location eLoc = fTarget.getLocation().add(0, p.getEyeHeight() * 0.75, 0);
                    Vector v = eLoc.toVector().subtract(b.curr.toVector());
                    if (tVal < 10)
                        v = v.multiply(0.1);
                    else if (tVal < 15)
                        v = v.multiply(0.2);
                    else
                        v = v.multiply(0.5);
                    b.curr.add(v);
                    RParticles.showWithSpeed(ParticleEffect.REDSTONE, b.curr, 0, 2);
                }
            }, t);
        }

        final int finalDamage = damage;
        for (int k = 1; k <= 3; k++) {
            final int tick = k;
            RScheduler.schedule(Spell.plugin, new Runnable() {
                public void run() {
                    if (!p.isOnline() || !fTarget.isValid() || (fTarget instanceof Player && !((Player) fTarget).isOnline()))
                        return;
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
                    for (Vector vec : vectors) {
                        Location loc = fTarget.getLocation().clone().add(vec).add(0, 0.1, 0);
                        RParticles.showWithSpeed(ParticleEffect.REDSTONE, loc, 0, 1);
                    }
                    if (tick == 3) {
                        RScheduler.schedule(Spell.plugin, new Runnable() {
                            public void run() {
                                RParticles.show(ParticleEffect.EXPLOSION_LARGE, fTarget.getLocation(), 5);
                                Spell.damageNearby(finalDamage, p, fTarget.getLocation(), 3, new ArrayList<Entity>());
                            }
                        }, RTicks.seconds(0.5));
                    }
                }
            }, k * 20);
        }
        Spell.notify(p, "You cast a spell on the blood of your enemy.");
        return true;
    }

    private static class Beam {
        public Location curr;
    }
}
