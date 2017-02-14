package com.edasaki.rpg.spells.wizard;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RParticles;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.ParticleEffect.BlockData;

public class Firestorm extends SpellEffect {

    @Override
    public boolean cast(final Player p, final PlayerDataRPG pd, final int level) {
        RScheduler.schedule(Spell.plugin, new Runnable() {
            private int count = 0;

            public void run() {
                int damage = pd.getDamage(true);
                int duration = 4;
                switch (level) {
                    case 1:
                        damage *= 1.25;
                        duration = 4;
                        break;
                    case 2:
                        damage *= 1.75;
                        duration = 5;
                        break;
                    case 3:
                        damage *= 2.25;
                        duration = 6;
                        break;
                }
                Location origin = p.getEyeLocation().add(0, 1, 0);
                ArrayList<Location> locs = new ArrayList<Location>();
                locs.add(origin);
                for (int dx = -1; dx <= 1; dx++)
                    for (int dz = -1; dz <= 1; dz++)
                        if (Math.random() < 0.5)
                            locs.add(origin.clone().add(dx * (Math.random() * 0.8 + 0.2), 0, dz * (Math.random() * 0.8 + 0.2)));
                for (Location loc : locs) {
                    RParticles.showWithData(ParticleEffect.BLOCK_CRACK, loc, new BlockData(Material.LAVA, (byte) 0), 1);
                }
                if (count % 10 == 0) {
                    for (Entity e : p.getNearbyEntities(7, 5, 7)) {
                        double d = RMath.flatDistance(e.getLocation(), p.getLocation());
                        if (d < 2)
                            d = 0.5;
                        else if (d < 4)
                            d = 0.3;
                        else if (d < 6)
                            d = 0.2;
                        else
                            d = 0.1;
                        if (Math.random() < d) {
                            if (Spell.damageEntity(e, damage, p, true, false)) {
                                Location loc = e.getLocation();
                                for (int k = 0; k < 15; k++) {
                                    RParticles.showWithOffset(ParticleEffect.FLAME, loc, 0.7, 10);
                                    loc.add(0, 0.2, 0);
                                }
                            }
                        }
                    }
                }
                if (++count < duration * 10) {
                    RScheduler.schedule(Spell.plugin, this, 2);
                }
            }
        });
        Spell.notify(p, "You surround yourself in a storm of fire.");
        return true;
    }
}
