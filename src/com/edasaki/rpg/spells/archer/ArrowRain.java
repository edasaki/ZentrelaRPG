package com.edasaki.rpg.spells.archer;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RMetadata;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RSound;
import com.edasaki.core.utils.RTicks;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

public class ArrowRain extends SpellEffect {

    @Override
    public boolean cast(final Player p, final PlayerDataRPG pd, int level) {
        int damage = pd.getDamage(true);
        switch (level) {
            case 1:
                damage *= 1.1;
                break;
            case 2:
                damage *= 1.2;
                break;
            case 3:
                damage *= 1.3;
                break;
            case 4:
                damage *= 1.4;
                break;
            case 5:
                damage *= 1.5;
                break;
            case 6:
                damage *= 1.6;
                break;
            case 7:
                damage *= 1.7;
                break;
            case 8:
                damage *= 1.8;
                break;
            case 9:
                damage *= 1.9;
                break;
            case 10:
                damage *= 2.0;
                break;
        }
        final int fDamage = damage;
        RSound.playSound(p, Sound.ENTITY_ARROW_SHOOT);
        Block b = p.getTargetBlock((Set<Material>) null, 15);
        Location loc = b.getLocation();
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                final Location currentLoc = loc.clone();
                currentLoc.setX(loc.getX() + x - 1);
                currentLoc.setZ(loc.getZ() + z);
                currentLoc.setY(loc.getY() + RMath.randInt(4, 6));
                Location loc2 = loc.clone();
                loc2.setX(loc.getX() + x);
                loc2.setZ(loc.getZ() + z);
                final Vector v = loc2.toVector().subtract(currentLoc.toVector()).normalize();
                int count = (int) (Math.random() * 3 + 1);
                for (int k = 0; k < count; k++) {
                    if (Math.random() < 0.7) {
                        RScheduler.schedule(Spell.plugin, new Runnable() {
                            public void run() {
                                Projectile arrow = ((Projectile) (currentLoc.getWorld().spawnEntity(currentLoc, EntityType.ARROW)));
                                arrow.setVelocity(v);
                                arrow.setShooter(p);
                                arrow.setMetadata(RMetadata.META_DAMAGE, new FixedMetadataValue(Spell.plugin, fDamage));
                            }
                        }, (int) (Math.random() * RTicks.seconds(2)));
                    }
                }
            }
        }
        Spell.notify(p, "A rain of arrows showers down from the skies.");
        return true;
    }

}
