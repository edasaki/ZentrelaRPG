package com.edasaki.rpg.spells.archer;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.metadata.FixedMetadataValue;

import com.edasaki.core.utils.RMetadata;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

public class Hurricane extends SpellEffect {

    @Override
    public boolean cast(final Player p, final PlayerDataRPG pd, int level) {
        int damage = pd.getDamage(true);
        int numberOfArrows = 0;
        switch (level) {
            case 1:
                damage *= 0.26;
                numberOfArrows = 16;
                break;
            case 2:
                damage *= 0.27;
                numberOfArrows = 17;
                break;
            case 3:
                damage *= 0.28;
                numberOfArrows = 18;
                break;
            case 4:
                damage *= 0.29;
                numberOfArrows = 19;
                break;
            case 5:
                damage *= 0.30;
                numberOfArrows = 20;
                break;
            case 6:
                damage *= 0.31;
                numberOfArrows = 21;
                break;
            case 7:
                damage *= 0.32;
                numberOfArrows = 22;
                break;
            case 8:
                damage *= 0.33;
                numberOfArrows = 23;
                break;
            case 9:
                damage *= 0.34;
                numberOfArrows = 24;
                break;
            case 10:
                damage *= 0.35;
                numberOfArrows = 25;
                break;
            case 11:
                damage *= 0.36;
                numberOfArrows = 26;
                break;
            case 12:
                damage *= 0.37;
                numberOfArrows = 27;
                break;
            case 13:
                damage *= 0.38;
                numberOfArrows = 28;
                break;
            case 14:
                damage *= 0.39;
                numberOfArrows = 29;
                break;
            case 15:
                damage *= 0.40;
                numberOfArrows = 30;
                break;
        }
        final int fDamage = damage;
        for (int k = 0; k < numberOfArrows; k++) {
            RScheduler.schedule(Spell.plugin, new Runnable() {
                public void run() {
                    Projectile p = pd.shootArrow();
                    p.setMetadata(RMetadata.META_DAMAGE, new FixedMetadataValue(Spell.plugin, fDamage));
                    p.setMetadata(RMetadata.META_KNOCKBACK, new FixedMetadataValue(Spell.plugin, 0));
                }
            }, k * 3);
        }
        Spell.notify(p, "A hurricane of arrows begins flying out from your bow.");
        return true;
    }

}
