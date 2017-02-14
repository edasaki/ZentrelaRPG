package com.edasaki.rpg.spells;

import com.edasaki.rpg.spells.assassin.DoubleStab;
import com.edasaki.rpg.spells.assassin.ShadowAcrobat;
import com.edasaki.rpg.spells.assassin.ShadowStab;
import com.edasaki.rpg.spells.assassin.ShadowWarp;
import com.edasaki.rpg.spells.assassin.Shuriken;
import com.edasaki.rpg.spells.assassin.SinisterStrike;
import com.edasaki.rpg.spells.assassin.Stealth;

public class SpellbookAssassin extends Spellbook {

    public static final Spell DOUBLE_STAB = new Spell("Double Stab", 2, 25, 1, 0, new String[] {
            "Quickly stab twice on your next attack, dealing 60% damage each hit.",
            "Quickly stab twice on your next attack, dealing 70% damage each hit.",
            "Quickly stab twice on your next attack, dealing 80% damage each hit.",
            "Quickly stab twice on your next attack, dealing 90% damage each hit.",
            "Quickly stab twice on your next attack, dealing 100% damage each hit.",

            "Quickly stab twice on your next attack, dealing 110% damage each hit.",
            "Quickly stab twice on your next attack, dealing 120% damage each hit.",
            "Quickly stab twice on your next attack, dealing 130% damage each hit.",
            "Quickly stab twice on your next attack, dealing 140% damage each hit.",
            "Quickly stab twice on your next attack, dealing 150% damage each hit.",

            "Quickly stab twice on your next attack, dealing 160% damage each hit.",
            "Quickly stab twice on your next attack, dealing 170% damage each hit.",
            "Quickly stab twice on your next attack, dealing 180% damage each hit.",
            "Quickly stab twice on your next attack, dealing 190% damage each hit.",
            "Quickly stab twice on your next attack, dealing 200% damage each hit.",

            "Quickly stab twice on your next attack, dealing 210% damage each hit.",
            "Quickly stab twice on your next attack, dealing 220% damage each hit.",
            "Quickly stab twice on your next attack, dealing 230% damage each hit.",
            "Quickly stab twice on your next attack, dealing 240% damage each hit.",
            "Quickly stab twice on your next attack, dealing 250% damage each hit.",

            "Quickly stab twice on your next attack, dealing 260% damage each hit.",
            "Quickly stab twice on your next attack, dealing 270% damage each hit.",
            "Quickly stab twice on your next attack, dealing 280% damage each hit.",
            "Quickly stab twice on your next attack, dealing 290% damage each hit.",
            "Quickly stab twice on your next attack, dealing 300% damage each hit.",
    }, null, new DoubleStab());

    public static final Spell SINISTER_STRIKE = new Spell("Sinister Strike", 5, 5, 1, 1, new String[] {
            "Perform a cruel strike on your next attack that deals 110% damage. Enemy players are crippled for 2 seconds, receiving 75% less healing from all sources.",
            "Perform a cruel strike on your next attack that deals 120% damage. Enemy players are crippled for 3 seconds, receiving 75% less healing from all sources.",
            "Perform a cruel strike on your next attack that deals 130% damage. Enemy players are crippled for 4 seconds, receiving 75% less healing from all sources.",
            "Perform a cruel strike on your next attack that deals 140% damage. Enemy players are crippled for 5 seconds, receiving 75% less healing from all sources.",
            "Perform a cruel strike on your next attack that deals 150% damage. Enemy players are crippled for 6 seconds, receiving 75% less healing from all sources.",
    }, null, new SinisterStrike());

    public static final Spell STEALTH = new Spell("Stealth", 4, 6, 2, 0, new String[] {
            "Turn invisible for 10 seconds. Stealth is broken when you attack.",
            "Turn invisible for 20 seconds. Stealth is broken when you attack.",
            "Turn invisible for 30 seconds. Stealth is broken when you attack.",
            "Turn invisible for 40 seconds. Stealth is broken when you attack.",
            "Turn invisible for 50 seconds. Stealth is broken when you attack.",
            "Turn invisible for 60 seconds. Stealth is broken when you attack.",
    }, null, new Stealth());

    public static final Spell SHADOW_STAB = new Spell("Shadow Stab", 3, 20, 2, 1, new String[] {
            "Attack from the shadows on your next attack, dealing 300% damage. Spell can only be used while stealthed. Does not stack with Double Stab.",
            "Attack from the shadows on your next attack, dealing 320% damage. Spell can only be used while stealthed. Does not stack with Double Stab.",
            "Attack from the shadows on your next attack, dealing 340% damage. Spell can only be used while stealthed. Does not stack with Double Stab.",
            "Attack from the shadows on your next attack, dealing 360% damage. Spell can only be used while stealthed. Does not stack with Double Stab.",
            "Attack from the shadows on your next attack, dealing 380% damage. Spell can only be used while stealthed. Does not stack with Double Stab.",
            "Attack from the shadows on your next attack, dealing 400% damage. Spell can only be used while stealthed. Does not stack with Double Stab.",
            "Attack from the shadows on your next attack, dealing 420% damage. Spell can only be used while stealthed. Does not stack with Double Stab.",
            "Attack from the shadows on your next attack, dealing 440% damage. Spell can only be used while stealthed. Does not stack with Double Stab.",
            "Attack from the shadows on your next attack, dealing 460% damage. Spell can only be used while stealthed. Does not stack with Double Stab.",
            "Attack from the shadows on your next attack, dealing 480% damage. Spell can only be used while stealthed. Does not stack with Double Stab.",
            "Attack from the shadows on your next attack, dealing 500% damage. Spell can only be used while stealthed. Does not stack with Double Stab.",
            "Attack from the shadows on your next attack, dealing 520% damage. Spell can only be used while stealthed. Does not stack with Double Stab.",
            "Attack from the shadows on your next attack, dealing 540% damage. Spell can only be used while stealthed. Does not stack with Double Stab.",
            "Attack from the shadows on your next attack, dealing 560% damage. Spell can only be used while stealthed. Does not stack with Double Stab.",
            "Attack from the shadows on your next attack, dealing 580% damage. Spell can only be used while stealthed. Does not stack with Double Stab.",
            "Attack from the shadows on your next attack, dealing 600% damage. Spell can only be used while stealthed. Does not stack with Double Stab.",
            "Attack from the shadows on your next attack, dealing 620% damage. Spell can only be used while stealthed. Does not stack with Double Stab.",
            "Attack from the shadows on your next attack, dealing 640% damage. Spell can only be used while stealthed. Does not stack with Double Stab.",
            "Attack from the shadows on your next attack, dealing 660% damage. Spell can only be used while stealthed. Does not stack with Double Stab.",
            "Attack from the shadows on your next attack, dealing 680% damage. Spell can only be used while stealthed. Does not stack with Double Stab.",
    }, new Object[] {
            DOUBLE_STAB,
            1,
            STEALTH,
            1
    }, new ShadowStab());

    public static final Spell SHADOW_ACROBAT = new Spell("Shadow Acrobat", 3, 5, 2, 2, new String[] {
            "Gain extreme speed and jump capabilities for 5 seconds. Spell can only be used while stealthed.",
            "Gain extreme speed and jump capabilities for 7 seconds. Spell can only be used while stealthed.",
            "Gain extreme speed and jump capabilities for 9 seconds. Spell can only be used while stealthed.",
            "Gain extreme speed and jump capabilities for 11 seconds. Spell can only be used while stealthed.",
            "Gain extreme speed and jump capabilities for 13 seconds. Spell can only be used while stealthed.",
    }, new Object[] {
            STEALTH,
            1
    }, new ShadowAcrobat());

    public static final Spell SHADOW_WARP = new Spell("Shadow Warp", 2, 1, 2, 3, new String[] {
            "Teleport behind a nearby enemy target. Spell can only be used while stealthed.",
    }, new Object[] {
            STEALTH,
            1
    }, new ShadowWarp());

    public static final Spell SHURIKEN = new Spell("Shuriken", 1, 10, 3, 0, new String[] {
            "Throw a shuriken that deals 120% damage.",
            "Throw a shuriken that deals 140% damage.",
            "Throw a shuriken that deals 160% damage.",
            "Throw a shuriken that deals 180% damage.",
            "Throw a shuriken that deals 200% damage.",
            "Throw a shuriken that deals 220% damage.",
            "Throw a shuriken that deals 240% damage.",
            "Throw a shuriken that deals 260% damage.",
            "Throw a shuriken that deals 280% damage.",
            "Throw a shuriken that deals 300% damage.",
    }, null, new Shuriken());

    public static final Spell DARK_HARMONY = new Spell("Dark Harmony", 0, 1, 1, 8, new String[] {
            "Increase Mana Regen rate by 50% while in Stealth.",
    }, null, new PassiveSpellEffect());

    public static final Spell DAGGER_MASTERY = new Spell("Dagger Mastery", 0, 5, 2, 8, new String[] {
            "Deal 2% increased base damage.",
            "Deal 4% increased base damage.",
            "Deal 6% increased base damage.",
            "Deal 8% increased base damage.",
            "Deal 10% increased base damage.",
    }, null, new PassiveSpellEffect());

    private static final Spell[] SPELL_LIST = {
            DOUBLE_STAB,
            SINISTER_STRIKE,
            STEALTH,
            SHADOW_STAB,
            SHADOW_ACROBAT,
            SHADOW_WARP,
            SHURIKEN,
            DARK_HARMONY,
            DAGGER_MASTERY
    };
    // DON'T FORGET TO ADD TO SPELL_LIST

    public static final Spellbook INSTANCE = new SpellbookAssassin();

    @Override
    public Spell[] getSpellList() {
        return SPELL_LIST;
    }

}
