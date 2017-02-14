package com.edasaki.rpg.spells.alchemist;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;
import com.edasaki.rpg.spells.SpellbookAlchemist;

public class Transmute extends SpellEffect {

    @Override
    public boolean cast(final Player p, final PlayerDataRPG pd, int level) {
        double selfMult = functions[0].applyAsDouble(level) / 100.0;
        int mana = (int) functions[1].applyAsDouble(level);
        int selfDmg = (int) (selfMult * (pd.baseMaxHP + pd.maxHP));
        if (pd.hp <= selfDmg) {
            p.sendMessage(ChatColor.RED + "You don't have enough HP to cast Transmute!");
            return false;
        }
        if (selfDmg >= pd.hp)
            selfDmg = pd.hp - 1;
        pd.damageSelfTrue(selfDmg);
        pd.recoverMana(mana + SpellbookAlchemist.TRANSMUTE.manaCost);
        Spell.notify(p, "You convert some HP into mana.");
        return true;
    }
}
