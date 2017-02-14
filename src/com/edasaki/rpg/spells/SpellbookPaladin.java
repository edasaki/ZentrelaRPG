package com.edasaki.rpg.spells;

import java.util.function.IntToDoubleFunction;

import com.edasaki.rpg.spells.paladin.ChantOfNecessarius;
import com.edasaki.rpg.spells.paladin.FlameCharge;
import com.edasaki.rpg.spells.paladin.HolyGuardian;
import com.edasaki.rpg.spells.paladin.LightningCharge;
import com.edasaki.rpg.spells.paladin.Smash;
import com.edasaki.rpg.spells.paladin.WalkingSanctuary;

public class SpellbookPaladin extends Spellbook {

    public static final Spell SMASH = new Spell("Smash", 4, 20, 1, 0, "Smash your mace downwards to deal %.0f%% damage to nearby enemies.", new IntToDoubleFunction[] {
            (x) -> {
                return 150 + 10 * x;
            }
    }, null, new Smash());

    public static final Spell WALKING_SANCTUARY = new Spell("Walking Sanctuary", 10, 5, 1, 1, new String[] {
            "Gain overwhelming divine strength for 10 seconds, becoming extremely slow but dealing 200% more damage.",
            "Gain overwhelming divine strength for 10 seconds, becoming extremely slow but dealing 230% more damage.",
            "Gain overwhelming divine strength for 10 seconds, becoming extremely slow but dealing 260% more damage.",
            "Gain overwhelming divine strength for 10 seconds, becoming extremely slow but dealing 290% more damage.",
            "Gain overwhelming divine strength for 10 seconds, becoming extremely slow but dealing 320% more damage.",
    }, null, new WalkingSanctuary());

    //    public static final Spell POWER_STANCE = new Spell("Power Stance", 3, 5, 2, 0, new String[] {
    //            "Become immune to all knockback for 5 seconds.",
    //            "Become immune to all knockback for 6 seconds.",
    //            "Become immune to all knockback for 7 seconds.",
    //            "Become immune to all knockback for 8 seconds.",
    //            "Become immune to all knockback for 9 seconds.",
    //    }, null, new PowerStance());

    public static final Spell HOLY_GUARDIAN = new Spell("Holy Guardian", 5, 10, 2, 1, new String[] {
            "Receive 20% less damage for 6 seconds.",
            "Receive 25% less damage for 6 seconds.",
            "Receive 30% less damage for 6 seconds.",
            "Receive 35% less damage for 6 seconds.",
            "Receive 40% less damage for 6 seconds.",
            "Receive 45% less damage for 6 seconds.",
            "Receive 50% less damage for 6 seconds.",
            "Receive 55% less damage for 6 seconds.",
            "Receive 60% less damage for 6 seconds.",
            "Receive 65% less damage for 6 seconds.",
    }, null, new HolyGuardian());

    public static final Spell CHANT_OF_NECESSARIUS = new Spell("Chant of Necessarius", 8, 3, 2, 2, new String[] {
            "Nullify all damage and knockback for 3 seconds.",
            "Nullify all damage and knockback for 4 seconds.",
            "Nullify all damage and knockback for 5 seconds.",
    }, new Object[] {
            HOLY_GUARDIAN,
            3
    }, new ChantOfNecessarius());

    public static final Spell LIGHTNING_CHARGE = new Spell("Lightning Charge", 4, 5, 3, 0, new String[] {
            "Activate Lightning Charge for 6 seconds. Your attacks have a 30% chance of creating a lightning bolt dealing 120% damage to nearby enemies. You can only have one Charge active at a time.",
            "Activate Lightning Charge for 6 seconds. Your attacks have a 30% chance of creating a lightning bolt dealing 140% damage to nearby enemies. You can only have one Charge active at a time.",
            "Activate Lightning Charge for 6 seconds. Your attacks have a 30% chance of creating a lightning bolt dealing 160% damage to nearby enemies. You can only have one Charge active at a time.",
            "Activate Lightning Charge for 6 seconds. Your attacks have a 30% chance of creating a lightning bolt dealing 180% damage to nearby enemies. You can only have one Charge active at a time.",
            "Activate Lightning Charge for 6 seconds. Your attacks have a 30% chance of creating a lightning bolt dealing 200% damage to nearby enemies. You can only have one Charge active at a time.",
    }, null, new LightningCharge());

    public static final Spell FLAME_CHARGE = new Spell("Flame Charge", 4, 5, 3, 1, new String[] {
            "Activate Flame Charge for 6 seconds. Your attacks have a 30% chance of inflicting Burn I for 5 seconds. You can only have one Charge active at a time.",
            "Activate Flame Charge for 6 seconds. Your attacks have a 30% chance of inflicting Burn II for 5 seconds. You can only have one Charge active at a time.",
            "Activate Flame Charge for 6 seconds. Your attacks have a 30% chance of inflicting Burn III for 5 seconds. You can only have one Charge active at a time.",
            "Activate Flame Charge for 6 seconds. Your attacks have a 30% chance of inflicting Burn IV for 5 seconds. You can only have one Charge active at a time.",
            "Activate Flame Charge for 6 seconds. Your attacks have a 30% chance of inflicting Burn V for 5 seconds. You can only have one Charge active at a time.",
    }, null, new FlameCharge());

    public static final Spell MACE_MASTERY = new Spell("Mace Mastery", 0, 5, 1, 8, new String[] {
            "Deal 2% increased base damage.",
            "Deal 4% increased base damage.",
            "Deal 6% increased base damage.",
            "Deal 8% increased base damage.",
            "Deal 10% increased base damage.",
    }, null, new PassiveSpellEffect());

    public static final Spell DIVINITY = new Spell("Divinity", 0, 5, 2, 8, new String[] {
            "Gain +2% Max HP.",
            "Gain +4% Max HP.",
            "Gain +6% Max HP.",
            "Gain +8% Max HP.",
            "Gain +10% Max HP.",
    }, null, new PassiveSpellEffect());

    private static final Spell[] SPELL_LIST = {
            SMASH,
            WALKING_SANCTUARY,
            //            POWER_STANCE,
            HOLY_GUARDIAN,
            CHANT_OF_NECESSARIUS,
            LIGHTNING_CHARGE,
            FLAME_CHARGE,
            MACE_MASTERY,
            DIVINITY
    };
    // DON'T FORGET TO ADD TO SPELL_LIST

    public static final Spellbook INSTANCE = new SpellbookPaladin();

    @Override
    public Spell[] getSpellList() {
        return SPELL_LIST;
    }

}
