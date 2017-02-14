package com.edasaki.rpg.spells.wizard;

import java.util.ArrayList;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.edasaki.core.utils.RParticles;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

public class LightningBolt extends SpellEffect {

    @Override
    public boolean cast(final Player p, final PlayerDataRPG pd, final int level) {
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
                damage *= 1.9;
                break;
            case 5:
                damage *= 2.2;
                break;
            case 6:
                damage *= 2.6;
                break;
            case 7:
                damage *= 3.0;
                break;
            case 8:
                damage *= 3.5;
                break;
        }
        Block b = p.getTargetBlock((Set<Material>) null, 20);
        if (b == null) {
            p.sendMessage(ChatColor.RED + "Point at a block to cast Lightning Bolt!");
            return false;
        }
        Location loc = b.getLocation();
        RParticles.sendLightning(p, loc);
        Spell.damageNearby(damage, p, loc, 2.5, new ArrayList<Entity>());
        Spell.notify(p, "You summon a bolt of lightning to strike the ground.");
        return true;
    }
}
