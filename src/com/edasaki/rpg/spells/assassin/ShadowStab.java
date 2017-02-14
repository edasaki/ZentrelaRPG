package com.edasaki.rpg.spells.assassin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

public class ShadowStab extends SpellEffect {

    public static final String BUFF_ID = "shadow stab";

    @Override
    public boolean cast(final Player p, PlayerDataRPG pd, int level) {
        if (!pd.isStealthed()) {
            p.sendMessage(ChatColor.RED + "Shadow Stab can only be used while in stealth.");
            return false;
        }
        double value = 0;
        switch (level) {
            case 1:
                value = 3.0;
                break;
            case 2:
                value = 3.2;
                break;
            case 3:
                value = 3.4;
                break;
            case 4:
                value = 3.6;
                break;
            case 5:
                value = 3.8;
                break;
            case 6:
                value = 4.0;
                break;
            case 7:
                value = 4.2;
                break;
            case 8:
                value = 4.4;
                break;
            case 9:
                value = 4.6;
                break;
            case 10:
                value = 4.8;
                break;
            case 11:
                value = 5.0;
                break;
            case 12:
                value = 5.2;
                break;
            case 13:
                value = 5.4;
                break;
            case 14:
                value = 5.6;
                break;
            case 15:
                value = 5.8;
                break;
            case 16:
                value = 6.0;
                break;
            case 17:
                value = 6.2;
                break;
            case 18:
                value = 6.4;
                break;
            case 19:
                value = 6.6;
                break;
            case 20:
                value = 6.8;
                break;
        }
        pd.removeBuff(DoubleStab.BUFF_ID);
        pd.giveBuff(ShadowStab.BUFF_ID, value, Spell.LONG_DURATION);
        Spell.notify(p, "You prepare Shadow Stab.");
        return true;
    }

}
