package com.edasaki.rpg.spells.assassin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

public class Stealth extends SpellEffect {
    
    public static final String BUFF_ID = "double stab";

    @Override
    public boolean cast(final Player p, PlayerDataRPG pd, int level) {
        if(pd.isStealthed()) {
            p.sendMessage(ChatColor.RED + "You are already stealthed!");
            return false;
        }
        int durationSeconds = 0;
        switch(level) {
            case 1:
                durationSeconds = 10;
                break;
            case 2:
                durationSeconds = 20;
                break;
            case 3:
                durationSeconds = 30;
                break;
            case 4:
                durationSeconds = 40;
                break;
            case 5:
                durationSeconds = 50;
                break;
            case 6:
                durationSeconds = 60;
                break;
        }
        pd.giveStealth(durationSeconds);
        Spell.notify(p, "You are now stealthed.");
        return true;
    }

}
