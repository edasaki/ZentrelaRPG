package com.edasaki.rpg.spells;

import com.edasaki.rpg.spells.villager.Firework;

public class VillagerSpellbook extends Spellbook {

    public static final Spell FIREWORK = new Spell("Firework", 5, 1, 1, 0, new String[] { "Yay, fireworks! Totally harmless, by the way!",
    }, null, new Firework());

    // DON'T FORGET TO ADD TO SPELL_LIST

    public static final Spellbook INSTANCE = new VillagerSpellbook();
    private static final Spell[] SPELL_LIST = { FIREWORK };

    @Override
    public Spell[] getSpellList() {
        return SPELL_LIST;
    }
}
