package com.edasaki.rpg.spells.assassin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RParticles;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

public class ShadowWarp extends SpellEffect {
    
    @Override
    public boolean cast(final Player p, PlayerDataRPG pd, int level) {
        if(!pd.isStealthed()) {
            p.sendMessage(ChatColor.RED + "Shadow Warp can only be used while in stealth.");
            return false;
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
            if(target != null)
                break;
            curr.add(dir);
            if(!RParticles.isAirlike(curr.getBlock()))
                break;
        }
        if (target == null) {
            p.sendMessage(ChatColor.RED + "Failed to find a Shadow Warp target.");
            return false;
        }
        Location loc = target.getLocation();
        loc = loc.add(0, 0.3, 0);
        loc.add(target.getLocation().getDirection().normalize().multiply(-2));
        p.teleport(loc);
        Spell.notify(p, "You teleport behind your target.");
        return true;
    }

}
