package com.edasaki.rpg.spells.reaper;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.edasaki.core.utils.RParticles;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

import de.slikey.effectlib.util.ParticleEffect;

public class Pledge extends SpellEffect {

    @Override
    public boolean cast(final Player p, final PlayerDataRPG pd, int level) {
        double mult = 0.3;
        switch (level) {
            case 1:
                mult = 0.3;
                break;
            case 2:
                mult = 0.33;
                break;
            case 3:
                mult = 0.36;
                break;
            case 4:
                mult = 0.39;
                break;
            case 5:
                mult = 0.42;
                break;
        }

        final int healAmount = (int) (mult * (pd.baseMaxHP + pd.maxHP)) / 10;
        int selfDmg = (int) (0.2 * (pd.baseMaxHP + pd.maxHP));
        if (pd.hp <= selfDmg) {
            p.sendMessage(ChatColor.RED + "You don't have enough HP to cast Pledge!");
            return false;
        }
        if (selfDmg >= pd.hp)
            selfDmg = pd.hp - 1;
        pd.damageSelfTrue(selfDmg);
        ArrayList<Vector> vectors = new ArrayList<Vector>();
        Vector v = p.getEyeLocation().getDirection().normalize();
        v.setY(0);
        vectors.add(v);
        double z = v.getZ();
        double x = v.getX();
        double radians = Math.atan(z / x);
        if (x < 0)
            radians += Math.PI;
        for (int k = 1; k < 24; k++) {
            Vector v2 = new Vector();
            v2.setY(v.getY());
            v2.setX(Math.cos(radians + k * Math.PI / 12));
            v2.setZ(Math.sin(radians + k * Math.PI / 12));
            vectors.add(v2.normalize());
        }
        Location start = p.getLocation().add(0, 0.5 * p.getEyeHeight(), 0);
        for (Vector vec : vectors)
            RParticles.showWithSpeed(ParticleEffect.REDSTONE, start.clone().add(vec), 0, 1);
        for (int k = 1; k <= 10; k++) {
            RScheduler.schedule(Spell.plugin, new Runnable() {
                public void run() {
                    if (pd != null && pd.isValid())
                        pd.heal(healAmount);
                }
            }, k * 20);
        }

        Spell.notify(p, "You pledge your HP in exchange for powerful regeneration.");
        return true;
    }

}
