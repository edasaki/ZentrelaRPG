package com.edasaki.rpg.spells;

import com.edasaki.rpg.spells.reaper.BloodBoil;
import com.edasaki.rpg.spells.reaper.BloodPact;
import com.edasaki.rpg.spells.reaper.BloodRush;
import com.edasaki.rpg.spells.reaper.DarkBargain;
import com.edasaki.rpg.spells.reaper.Drain;
import com.edasaki.rpg.spells.reaper.Pledge;
import com.edasaki.rpg.spells.reaper.Siphon;

public class SpellbookReaper extends Spellbook {

    public static final Spell DRAIN = new Spell("Drain", 4, 10, 1, 0, new String[] {
            "Drain an enemy target for 80% damage over 3 seconds, healing yourself for the same amount.",
            "Drain an enemy target for 90% damage over 3 seconds, healing yourself for the same amount.",
            "Drain an enemy target for 100% damage over 3 seconds, healing yourself for the same amount.",
            "Drain an enemy target for 110% damage over 3 seconds, healing yourself for the same amount.",
            "Drain an enemy target for 120% damage over 3 seconds, healing yourself for the same amount.",
            "Drain an enemy target for 130% damage over 3 seconds, healing yourself for the same amount.",
            "Drain an enemy target for 140% damage over 3 seconds, healing yourself for the same amount.",
            "Drain an enemy target for 150% damage over 3 seconds, healing yourself for the same amount.",
            "Drain an enemy target for 160% damage over 3 seconds, healing yourself for the same amount.",
            "Drain an enemy target for 170% damage over 3 seconds, healing yourself for the same amount.",
    }, null, new Drain());

    public static final Spell SIPHON = new Spell("Siphon", 3, 15, 1, 1, new String[] {
            "Shoot a magical bolt that deals 100% damage and heals for the same amount.",
            "Shoot a magical bolt that deals 110% damage and heals for the same amount.",
            "Shoot a magical bolt that deals 120% damage and heals for the same amount.",
            "Shoot a magical bolt that deals 130% damage and heals for the same amount.",
            "Shoot a magical bolt that deals 140% damage and heals for the same amount.",
            "Shoot a magical bolt that deals 150% damage and heals for the same amount.",
            "Shoot a magical bolt that deals 160% damage and heals for the same amount.",
            "Shoot a magical bolt that deals 170% damage and heals for the same amount.",
            "Shoot a magical bolt that deals 180% damage and heals for the same amount.",
            "Shoot a magical bolt that deals 190% damage and heals for the same amount.",
            "Shoot a magical bolt that deals 200% damage and heals for the same amount.",
            "Shoot a magical bolt that deals 210% damage and heals for the same amount.",
            "Shoot a magical bolt that deals 220% damage and heals for the same amount.",
            "Shoot a magical bolt that deals 230% damage and heals for the same amount.",
            "Shoot a magical bolt that deals 240% damage and heals for the same amount.",
    }, new Object[] {
            DRAIN,
            5
    }, new Siphon());

    public static final Spell BLOOD_RUSH = new Spell("Blood Rush", 3, 10, 2, 0, new String[] {
            "Sacrifice 10% of your maximum HP to deal 300% damage to a single enemy target.",
            "Sacrifice 10% of your maximum HP to deal 330% damage to a single enemy target.",
            "Sacrifice 10% of your maximum HP to deal 360% damage to a single enemy target.",
            "Sacrifice 10% of your maximum HP to deal 390% damage to a single enemy target.",
            "Sacrifice 10% of your maximum HP to deal 420% damage to a single enemy target.",
            "Sacrifice 10% of your maximum HP to deal 450% damage to a single enemy target.",
            "Sacrifice 10% of your maximum HP to deal 480% damage to a single enemy target.",
            "Sacrifice 10% of your maximum HP to deal 510% damage to a single enemy target.",
            "Sacrifice 10% of your maximum HP to deal 540% damage to a single enemy target.",
            "Sacrifice 10% of your maximum HP to deal 570% damage to a single enemy target.",
    }, null, new BloodRush());

    public static final Spell BLOOD_PACT = new Spell("Blood Pact", 5, 5, 2, 1, new String[] {
            "Sacrifice 8% of your maximum HP and deal that amount over 2 seconds to a single enemy target.",
            "Sacrifice 11% of your maximum HP and deal that amount over 2 seconds to a single enemy target.",
            "Sacrifice 14% of your maximum HP and deal that amount over 2 seconds to a single enemy target.",
            "Sacrifice 17% of your maximum HP and deal that amount over 2 seconds to a single enemy target.",
            "Sacrifice 20% of your maximum HP and deal that amount over 2 seconds to a single enemy target.",
    }, null, new BloodPact());

    public static final Spell BLOOD_BOIL = new Spell("Blood Boil", 4, 10, 2, 2, new String[] {
            "Boil a single enemy target's blood, causing a delayed explosion at their location dealing 150% damage to nearby enemies.",
            "Boil a single enemy target's blood, causing a delayed explosion at their location dealing 190% damage to nearby enemies.",
            "Boil a single enemy target's blood, causing a delayed explosion at their location dealing 230% damage to nearby enemies.",
            "Boil a single enemy target's blood, causing a delayed explosion at their location dealing 270% damage to nearby enemies.",
            "Boil a single enemy target's blood, causing a delayed explosion at their location dealing 310% damage to nearby enemies.",
            "Boil a single enemy target's blood, causing a delayed explosion at their location dealing 350% damage to nearby enemies.",
            "Boil a single enemy target's blood, causing a delayed explosion at their location dealing 390% damage to nearby enemies.",
            "Boil a single enemy target's blood, causing a delayed explosion at their location dealing 430% damage to nearby enemies.",
            "Boil a single enemy target's blood, causing a delayed explosion at their location dealing 470% damage to nearby enemies.",
            "Boil a single enemy target's blood, causing a delayed explosion at their location dealing 510% damage to nearby enemies.",
    }, new Object[] {
            BLOOD_RUSH,
            2
    }, new BloodBoil());

    public static final Spell PLEDGE = new Spell("Pledge", 7, 5, 3, 0, new String[] {
            "Lose 20% of your maximum HP instantly and heal back 30% of your maximum HP over 10 seconds.",
            "Lose 20% of your maximum HP instantly and heal back 33% of your maximum HP over 10 seconds.",
            "Lose 20% of your maximum HP instantly and heal back 36% of your maximum HP over 10 seconds.",
            "Lose 20% of your maximum HP instantly and heal back 39% of your maximum HP over 10 seconds.",
            "Lose 20% of your maximum HP instantly and heal back 42% of your maximum HP over 10 seconds.",
    }, null, new Pledge());

    public static final Spell DARK_BARGAIN = new Spell("Dark Bargain", 6, 5, 3, 1, new String[] {
            "Sacrifice 20% of your maximum HP to deal 70% bonus damage for 10 seconds.",
            "Sacrifice 25% of your maximum HP to deal 80% bonus damage for 10 seconds.",
            "Sacrifice 30% of your maximum HP to deal 90% bonus damage for 10 seconds.",
            "Sacrifice 35% of your maximum HP to deal 100% bonus damage for 10 seconds.",
            "Sacrifice 40% of your maximum HP to deal 110% bonus damage for 10 seconds.",
    }, null, new DarkBargain());

    public static final Spell ENDLESS_FEAST = new Spell("Endless Feast", 0, 5, 1, 7, new String[] {
            "Heal for 0.10% of your maximum health whenever you kill an enemy.",
            "Heal for 0.15% of your maximum health whenever you kill an enemy.",
            "Heal for 0.20% of your maximum health whenever you kill an enemy.",
            "Heal for 0.25% of your maximum health whenever you kill an enemy.",
            "Heal for 0.30% of your maximum health whenever you kill an enemy.",
    }, null, new PassiveSpellEffect());

    public static final Spell HEAL_ENHANCE = new Spell("Heal Enhance", 0, 6, 1, 8, new String[] {
            "Gain +10% healing from all sources except HP regeneration.",
            "Gain +12% healing from all sources except HP regeneration.",
            "Gain +14% healing from all sources except HP regeneration.",
            "Gain +16% healing from all sources except HP regeneration.",
            "Gain +18% healing from all sources except HP regeneration.",
            "Gain +20% healing from all sources except HP regeneration.",
    }, null, new PassiveSpellEffect());

    public static final Spell RAPID_RECOVERY = new Spell("Rapid Recovery", 0, 5, 2, 8, new String[] {
            "Increase HP regeneration by 0.20% of your missing health.",
            "Increase HP regeneration by 0.25% of your missing health.",
            "Increase HP regeneration by 0.30% of your missing health.",
            "Increase HP regeneration by 0.35% of your missing health.",
            "Increase HP regeneration by 0.40% of your missing health.",
    }, null, new PassiveSpellEffect());

    private static final Spell[] SPELL_LIST = {
            DRAIN,
            SIPHON,
            BLOOD_RUSH,
            BLOOD_PACT,
            BLOOD_BOIL,
            PLEDGE,
            DARK_BARGAIN,
            ENDLESS_FEAST,
            HEAL_ENHANCE,
            RAPID_RECOVERY
    };
    // DON'T FORGET TO ADD TO SPELL_LIST

    public static final Spellbook INSTANCE = new SpellbookReaper();

    @Override
    public Spell[] getSpellList() {
        return SPELL_LIST;
    }

}
