package com.edasaki.rpg.spells.reaper;

import org.bukkit.ChatColor;
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

public class BloodRush extends SpellEffect {

    @Override
    public boolean cast(final Player p, final PlayerDataRPG pd, int level) {
        int damage = pd.getDamage(true);
        switch (level) {
            case 1:
                damage *= 3.0;
                break;
            case 2:
                damage *= 3.3;
                break;
            case 3:
                damage *= 3.6;
                break;
            case 4:
                damage *= 3.9;
                break;
            case 5:
                damage *= 4.2;
                break;
            case 6:
                damage *= 4.5;
                break;
            case 7:
                damage *= 4.8;
                break;
            case 8:
                damage *= 5.1;
                break;
            case 9:
                damage *= 5.4;
                break;
            case 10:
                damage *= 5.7;
                break;
        }
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
            p.sendMessage(ChatColor.RED + "Failed to find a Blood Rush target.");
            return false;
        }
        int selfDmg = (int) (0.1 * (pd.baseMaxHP + pd.maxHP));
        if (pd.hp <= selfDmg) {
            p.sendMessage(ChatColor.RED + "You don't have enough HP to cast Blood Rush!");
            return false;
        }
        if (selfDmg >= pd.hp)
            selfDmg = pd.hp - 1;
        start = target.getLocation().clone();
        final Location loc = start.clone().add(0, -0.3, 0);
        if (Spell.damageEntity(target, damage, p, true, false))
            pd.damageSelfTrue(selfDmg);
        for (int k = 0; k < 5; k++) {
            RScheduler.schedule(Spell.plugin, new Runnable() {
                public void run() {
                    RParticles.showWithOffset(ParticleEffect.REDSTONE, loc, 0.6, 100);
                    loc.add(0, 0.6, 0);
                }
            }, k);
        }
        Spell.notify(p, "You use your blood to cast some dangerous magic.");
        return true;
    }
}
