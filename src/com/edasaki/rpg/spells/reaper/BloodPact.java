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

public class BloodPact extends SpellEffect {

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
            p.sendMessage(ChatColor.RED + "Failed to find a Blood Pact target.");
            return false;
        }
        double mult = 0.08;
        switch (level) {
            case 1:
                mult = 0.08;
                break;
            case 2:
                mult = 0.11;
                break;
            case 3:
                mult = 0.14;
                break;
            case 4:
                mult = 0.17;
                break;
            case 5:
                mult = 0.20;
                break;
        }
        int selfDmg = (int) (mult * (pd.baseMaxHP + pd.maxHP));
        if (pd.hp <= selfDmg) {
            p.sendMessage(ChatColor.RED + "You don't have enough HP to cast Blood Pact!");
            return false;
        }
        if (selfDmg >= pd.hp)
            selfDmg = pd.hp - 1;
        start = target.getLocation().clone();
        pd.damageSelfTrue(selfDmg);
        RParticles.showWithOffset(ParticleEffect.REDSTONE, p.getLocation().add(0, p.getEyeHeight() * 0.7, 0), 0.6, 50);
        final int tickDmg = selfDmg / 2 < 1 ? 1 : selfDmg / 2;
        final Entity fTarget = target;
        for (int k = 1; k <= 2; k++) {
            RScheduler.schedule(Spell.plugin, new Runnable() {
                public void run() {
                    if(!p.isOnline() || !fTarget.isValid() || (fTarget instanceof Player && !((Player) fTarget).isOnline()))
                        return;
                    if (Spell.damageEntity(fTarget, tickDmg, p, true, false)) {
                        Location loc = fTarget.getLocation().clone().add(0, p.getEyeHeight() * 0.7, 0);
                        RParticles.showWithOffset(ParticleEffect.REDSTONE, loc, 0.6, 50);
                    }

                }
            }, k * 20);
        }
        Spell.notify(p, "You form a Blood Pact with your enemy.");
        return true;
    }
}
