package com.edasaki.rpg.spells.wizard;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.edasaki.core.utils.RParticles;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

import de.slikey.effectlib.util.ParticleEffect;

public class FlashI extends SpellEffect {

    @Override
    public boolean cast(final Player p, final PlayerDataRPG pd, final int level) {
        final Location start = p.getLocation().add(0, p.getEyeHeight() * 0.1, 0);
        Location permStart = p.getLocation().add(0, p.getEyeHeight() * 0.1, 0).clone();
        Location loc = start;
        Vector direction = p.getLocation().getDirection().normalize();
        direction.setY(0);
        int length = 5;
        Location prev = null;
        for (int k = 0; k < length; k++) {
            Location temp = loc.clone();
            loc = loc.add(direction);
            if (loc.getBlock().getType().isSolid())
                break;
            prev = temp.clone();
        }
        if (prev != null) {
            RParticles.show(ParticleEffect.EXPLOSION_NORMAL, permStart, 10);
            RParticles.show(ParticleEffect.EXPLOSION_NORMAL, prev, 10);
            p.teleport(prev);
        } else {
            p.sendMessage(ChatColor.RED + "You can't flash through walls!");
            return false;
        }
        Spell.notify(p, "You instantly teleport a short distance.");
        return true;
    }
}
