package com.edasaki.rpg.spells.assassin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.edasaki.core.utils.RTicks;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

public class ShadowAcrobat extends SpellEffect {

    @Override
    public boolean cast(final Player p, PlayerDataRPG pd, int level) {
        if (!pd.isStealthed()) {
            p.sendMessage(ChatColor.RED + "Shadow Acrobat can only be used while in stealth.");
            return false;
        }
        int durationSeconds = 0;
        switch (level) {
            case 1:
                durationSeconds = 5;
                break;
            case 2:
                durationSeconds = 7;
                break;
            case 3:
                durationSeconds = 9;
                break;
            case 4:
                durationSeconds = 11;
                break;
            case 5:
                durationSeconds = 13;
                break;
        }
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, RTicks.seconds(durationSeconds), 4, true, false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, RTicks.seconds(durationSeconds), 2, true, false));
        Spell.notify(p, "You feel extremely nimble.");
        return true;
    }

}
