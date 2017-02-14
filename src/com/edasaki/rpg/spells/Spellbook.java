package com.edasaki.rpg.spells;

public abstract class Spellbook {

    public abstract Spell[] getSpellList();

    public Spell getSpell(String name) {
        for (Spell s : getSpellList())
            if (s.name.equalsIgnoreCase(name))
                return s;
        return null;
    }
}
