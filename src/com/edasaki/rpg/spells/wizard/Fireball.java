package com.edasaki.rpg.spells.wizard;

import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.metadata.FixedMetadataValue;

import com.edasaki.core.utils.RMetadata;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

public class Fireball extends SpellEffect {

    @Override
    public boolean cast(Player p, PlayerDataRPG pd, int level) {
        SmallFireball fireball = (SmallFireball) p.launchProjectile(SmallFireball.class);
        fireball.setIsIncendiary(false);
        fireball.setShooter(p);
        int damage = pd.getDamage(true);
        damage *= functions[0].applyAsDouble(level) / 100.0;
        fireball.setMetadata(RMetadata.META_DAMAGE, new FixedMetadataValue(Spell.plugin, damage));
        Spell.notify(p, "You shoot off a fireball.");
        return true;
    }

}
