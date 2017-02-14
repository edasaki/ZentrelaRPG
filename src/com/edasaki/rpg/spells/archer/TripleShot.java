package com.edasaki.rpg.spells.archer;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.metadata.FixedMetadataValue;

import com.edasaki.core.utils.RMetadata;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

public class TripleShot extends SpellEffect {

    @Override
    public boolean cast(Player p, final PlayerDataRPG pd, int level) {
        int damage = pd.getDamage(true);
        switch (level) {
            case 1:
                damage *= 0.7;
                break;
            case 2:
                damage *= 0.8;
                break;
            case 3:
                damage *= 0.9;
                break;
            case 4:
                damage *= 1.0;
                break;
            case 5:
                damage *= 1.1;
                break;
            case 6:
                damage *= 1.2;
                break;
            case 7:
                damage *= 1.3;
                break;
            case 8:
                damage *= 1.4;
                break;
            case 9:
                damage *= 1.5;
                break;
            case 10:
                damage *= 1.6;
                break;
        }
        final int fDamage = damage;
        for(int k = 0; k < 3; k++) {
            RScheduler.schedule(Spell.plugin, new Runnable() {
                public void run() {
                    Projectile arrow = pd.shootArrow();
                    arrow.setMetadata(RMetadata.META_DAMAGE, new FixedMetadataValue(Spell.plugin, fDamage));
                }
            }, k * 3);
        }
        Spell.notify(p, "You quickly shoot three arrows.");
        return true;
    }

}
