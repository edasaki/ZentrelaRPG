package com.edasaki.rpg.spells;

import com.edasaki.rpg.spells.crusader.AntiGravity;
import com.edasaki.rpg.spells.crusader.Cleave;
import com.edasaki.rpg.spells.crusader.Gravity;
import com.edasaki.rpg.spells.crusader.HyperBody;
import com.edasaki.rpg.spells.crusader.Judgement;
import com.edasaki.rpg.spells.crusader.Leap;
import com.edasaki.rpg.spells.crusader.SwordSpirit;

public class SpellbookCrusader extends Spellbook {

    public static final Spell LEAP = new Spell("Leap", 3, 10, 2, 0, new String[] {
            "Jump a short distance forward, dealing 150% damage to enemies nearby when you land.",
            "Jump a short distance forward, dealing 175% damage to enemies nearby when you land.",
            "Jump a short distance forward, dealing 200% damage to enemies nearby when you land.",
            "Jump a short distance forward, dealing 225% damage to enemies nearby when you land.",
            "Jump a short distance forward, dealing 250% damage to enemies nearby when you land.",
            "Jump a short distance forward, dealing 275% damage to enemies nearby when you land.",
            "Jump a short distance forward, dealing 300% damage to enemies nearby when you land.",
            "Jump a short distance forward, dealing 325% damage to enemies nearby when you land.",
            "Jump a short distance forward, dealing 350% damage to enemies nearby when you land.",
            "Jump a short distance forward, dealing 375% damage to enemies nearby when you land.",
    }, null, new Leap());

    public static final Spell SWORD_SPIRIT = new Spell("Sword Spirit", 4, 10, 3, 0, new String[] {
            "Call upon the spirit of the sword to deal +20% bonus damage with your attacks for 4 seconds.",
            "Call upon the spirit of the sword to deal +25% bonus damage with your attacks for 4 seconds.",
            "Call upon the spirit of the sword to deal +30% bonus damage with your attacks for 5 seconds.",
            "Call upon the spirit of the sword to deal +35% bonus damage with your attacks for 5 seconds.",
            "Call upon the spirit of the sword to deal +40% bonus damage with your attacks for 6 seconds.",
            "Call upon the spirit of the sword to deal +45% bonus damage with your attacks for 6 seconds.",
            "Call upon the spirit of the sword to deal +50% bonus damage with your attacks for 7 seconds.",
            "Call upon the spirit of the sword to deal +55% bonus damage with your attacks for 7 seconds.",
            "Call upon the spirit of the sword to deal +60% bonus damage with your attacks for 8 seconds.",
            "Call upon the spirit of the sword to deal +65% bonus damage with your attacks for 8 seconds.",
    }, null, new SwordSpirit());

    public static final Spell GRAVITY = new Spell("Gravity", 5, 10, 2, 1, new String[] {
            "Draw in enemies towards you and create a small explosion dealing 250% damage to the closest enemies.",
            "Draw in enemies towards you and create a small explosion dealing 270% damage to the closest enemies.",
            "Draw in enemies towards you and create a small explosion dealing 290% damage to the closest enemies.",
            "Draw in enemies towards you and create a small explosion dealing 310% damage to the closest enemies.",
            "Draw in enemies towards you and create a small explosion dealing 330% damage to the closest enemies.",
            "Draw in enemies towards you and create a small explosion dealing 350% damage to the closest enemies.",
            "Draw in enemies towards you and create a small explosion dealing 370% damage to the closest enemies.",
            "Draw in enemies towards you and create a small explosion dealing 390% damage to the closest enemies.",
            "Draw in enemies towards you and create a small explosion dealing 410% damage to the closest enemies.",
            "Draw in enemies towards you and create a small explosion dealing 430% damage to the closest enemies.",
    }, null, new Gravity());

    public static final Spell ANTIGRAVITY = new Spell("Antigravity", 5, 3, 2, 2, new String[] {
            "Push nearby enemies a short distance away.",
            "Push nearby enemies a medium distance away.",
            "Push nearby enemies a long distance away.",
    }, null, new AntiGravity());

    public static final Spell HYPER_BODY = new Spell("Hyper Body", 4, 5, 3, 1, new String[] {
            "Gain Tier 1 enhanced movement for 3 seconds.",
            "Gain Tier 2 enhanced movement for 3 seconds.",
            "Gain Tier 3 enhanced movement for 4 seconds.",
            "Gain Tier 4 enhanced movement for 4 seconds.",
            "Gain Tier 5 enhanced movement for 5 seconds.",
    }, null, new HyperBody());

    public static final Spell CLEAVE = new Spell("Cleave", 2, 10, 1, 0, new String[] {
            "Slice in an arc in front of you, hitting enemies for 120% damage.",
            "Slice in an arc in front of you, hitting enemies for 140% damage.",
            "Slice in an arc in front of you, hitting enemies for 160% damage.",
            "Slice in an arc in front of you, hitting enemies for 180% damage.",
            "Slice in an arc in front of you, hitting enemies for 200% damage.",
            "Slice in an arc in front of you, hitting enemies for 220% damage.",
            "Slice in an arc in front of you, hitting enemies for 240% damage.",
            "Slice in an arc in front of you, hitting enemies for 260% damage.",
            "Slice in an arc in front of you, hitting enemies for 280% damage.",
            "Slice in an arc in front of you, hitting enemies for 300% damage.",
    }, null, new Cleave());

    public static final Spell JUDGEMENT = new Spell("Judgement", 10, 5, 1, 2, new String[] {
            "Call upon the sword gods to smite enemies in a large radius for 250% damage.",
            "Call upon the sword gods to smite enemies in a large radius for 275% damage.",
            "Call upon the sword gods to smite enemies in a large radius for 300% damage.",
            "Call upon the sword gods to smite enemies in a large radius for 325% damage.",
            "Call upon the sword gods to smite enemies in a large radius for 350% damage.",
    }, new Object[] {
            CLEAVE,
            5,
            LEAP,
            5,
            SWORD_SPIRIT,
            5,
    }, new Judgement());

    public static final Spell HEALTHY_DIET = new Spell("Healthy Diet", 0, 5, 1, 8, new String[] {
            "Gain +50 HP.",
            "Gain +75 HP.",
            "Gain +125 HP.",
            "Gain +200 HP.",
            "Gain +300 HP.",
    }, null, new PassiveSpellEffect());

    public static final Spell SWORD_MASTERY = new Spell("Sword Mastery", 0, 5, 2, 8, new String[] {
            "Deal 2% increased base damage.",
            "Deal 4% increased base damage.",
            "Deal 6% increased base damage.",
            "Deal 8% increased base damage.",
            "Deal 10% increased base damage.",
    }, null, new PassiveSpellEffect());

    private static final Spell[] SPELL_LIST = {
            CLEAVE,
            LEAP,
            GRAVITY,
            ANTIGRAVITY,
            SWORD_SPIRIT,
            HYPER_BODY,
            JUDGEMENT,
            HEALTHY_DIET,
            SWORD_MASTERY
    };
    // DON'T FORGET TO ADD TO SPELL_LIST

    public static final Spellbook INSTANCE = new SpellbookCrusader();

    @Override
    public Spell[] getSpellList() {
        return SPELL_LIST;
    }

}
