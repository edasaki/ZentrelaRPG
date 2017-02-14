package com.edasaki.rpg.spells.crusader;

import java.util.ArrayList;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RParticles;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RTicks;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.mobs.MobManager;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

import de.slikey.effectlib.util.ParticleEffect;

public class Gravity extends SpellEffect {

    @Override
    public boolean cast(final Player p, PlayerDataRPG pd, int level) {
        double radius = 0;
        int damage = pd.getDamage(true);
        switch (level) {
            case 1:
                radius = 8;
                damage *= 2.5;
                break;
            case 2:
                radius = 8;
                damage *= 2.7;
                break;
            case 3:
                radius = 9;
                damage *= 2.9;
                break;
            case 4:
                radius = 9;
                damage *= 3.1;
                break;
            case 5:
                radius = 10;
                damage *= 3.3;
                break;
            case 6:
                radius = 10;
                damage *= 3.5;
                break;
            case 7:
                radius = 11;
                damage *= 3.7;
                break;
            case 8:
                radius = 11;
                damage *= 3.9;
                break;
            case 9:
                radius = 12;
                damage *= 4.1;
                break;
            case 10:
                radius = 12;
                damage *= 4.3;
                break;
        }
        for (Entity e : RMath.getNearbyEntitiesCylinder(p.getLocation(), radius, 9)) {
            boolean pull = false;
            if (e instanceof Player && e != p) {
                Player p2 = (Player) e;
                PlayerDataRPG pd2 = Spell.plugin.getPD(p2);
                if (pd2 == null)
                    continue;
                if (pd.party != null && pd2.party != null && pd.party == pd2.party)
                    continue;
                if (pd2 != null && pd2.isPVP())
                    pull = true;
            } else if (MobManager.spawnedMobs_onlyMain.containsKey(e.getUniqueId())) {
                pull = true;
            }
            if (pull) {
                Vector pullVector = e.getLocation().toVector().subtract(p.getLocation().toVector()).normalize().multiply(-1.5);
                pullVector.setY(pullVector.getY() + 0.2);
                e.setVelocity(pullVector);
            }
        }
        final int fDamage = damage;
        RScheduler.schedule(Spell.plugin, new Runnable() {
            public void run() {
                RParticles.show(ParticleEffect.EXPLOSION_LARGE, p.getLocation(), 5);
                Spell.damageNearby(fDamage, p, p.getLocation(), 3, new ArrayList<Entity>());
            }
        }, RTicks.seconds(0.5));
        Spell.notify(p, "You pull in nearby enemies.");
        return true;
    }

}
