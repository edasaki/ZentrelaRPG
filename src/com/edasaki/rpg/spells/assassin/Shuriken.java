package com.edasaki.rpg.spells.assassin;

import java.util.ArrayList;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RMetadata;
import com.edasaki.core.utils.RParticles;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

import de.slikey.effectlib.util.ParticleEffect;

public class Shuriken extends SpellEffect {

    @Override
    public boolean cast(final Player p, PlayerDataRPG pd, int level) {
        int damage = pd.getDamage(true);
        switch (level) {
            case 1:
                damage *= 1.2;
                break;
            case 2:
                damage *= 1.4;
                break;
            case 3:
                damage *= 1.6;
                break;
            case 4:
                damage *= 1.8;
                break;
            case 5:
                damage *= 2.0;
                break;
            case 6:
                damage *= 2.2;
                break;
            case 7:
                damage *= 2.4;
                break;
            case 8:
                damage *= 2.6;
                break;
            case 9:
                damage *= 2.8;
                break;
            case 10:
                damage *= 3.0;
                break;
        }
        ItemStack itemstack = new ItemStack(Material.CARPET, 1, DyeColor.BLACK.getData());
        final Item item = p.getWorld().dropItem(p.getLocation().add(0, p.getEyeHeight() * 0.71, 0), itemstack);
        item.setMetadata(RMetadata.META_NO_PICKUP, new FixedMetadataValue(Spell.plugin, 0));
        Vector dir = p.getLocation().getDirection().normalize();
        dir.setY(dir.getY() + .1);
        dir.multiply(1.22);
        item.setVelocity(dir);
        final int fDamage = damage;
        RScheduler.schedule(Spell.plugin, new Runnable() {

            private Location lastLoc = null;
            private static final double EPSILON = 0.02;
            private int count = 0;

            public void run() {
                if (item == null || !item.isValid() || item.isDead()) {
                    removeItem();
                    return;
                }
                Location currLoc = item.getLocation();
                if (lastLoc != null && RMath.flatDistance(currLoc, lastLoc) < EPSILON && Math.abs(currLoc.getY() - lastLoc.getY()) < EPSILON) {
                    removeItem();
                    return;
                }
                lastLoc = currLoc;
                ArrayList<Entity> list = Spell.damageNearby(fDamage, p, currLoc, 0.7, new ArrayList<Entity>());
                if (list.size() > 0) {
                    removeItem();
                    for (Entity e : list) {
                        if (e instanceof Player) {
                            pd.removeStealth();
                            break;
                        }
                    }
                    return;
                }
                count++;
                if (count % 2 == 0)
                    RParticles.show(ParticleEffect.CRIT, currLoc);
                RScheduler.schedule(Spell.plugin, this, 1);
            }

            public void removeItem() {
                if (item != null) {
                    item.remove();
                    RParticles.show(ParticleEffect.SMOKE_LARGE, item.getLocation(), 2);
                }
            }
        });
        Spell.notify(p, "You throw a shuriken.");
        return true;
    }

}
