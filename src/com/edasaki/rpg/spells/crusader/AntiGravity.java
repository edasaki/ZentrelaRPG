package com.edasaki.rpg.spells.crusader;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RParticles;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.mobs.MobManager;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

import de.slikey.effectlib.util.ParticleEffect;

public class AntiGravity extends SpellEffect {

    @Override
    public boolean cast(final Player p, PlayerDataRPG pd, int level) {
        double force = 0;
        switch (level) {
            case 1:
                force = 1.5;
                break;
            case 2:
                force = 3;
                break;
            case 3:
                force = 5;
                break;
        }
        for (Entity e : RMath.getNearbyEntitiesCylinder(p.getLocation(), 7, 9)) {
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
                Vector pushVector = e.getLocation().toVector().subtract(p.getLocation().toVector()).normalize();
                pushVector.multiply(force);
                if (pushVector.getY() > 0.5)
                    pushVector.setY(0.5);
                e.setVelocity(pushVector);
            }
        }
        RParticles.show(ParticleEffect.EXPLOSION_LARGE, p.getLocation(), 5);
        Spell.notify(p, "You push away nearby enemies.");
        return true;
    }

}
