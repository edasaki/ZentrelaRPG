package com.edasaki.rpg.spells;

import java.util.function.IntToDoubleFunction;

import com.edasaki.rpg.spells.wizard.BubbleBeam;
import com.edasaki.rpg.spells.wizard.BubbleBurst;
import com.edasaki.rpg.spells.wizard.Fireball;
import com.edasaki.rpg.spells.wizard.Firestorm;
import com.edasaki.rpg.spells.wizard.FlameWave;
import com.edasaki.rpg.spells.wizard.FlashI;
import com.edasaki.rpg.spells.wizard.FlashII;
import com.edasaki.rpg.spells.wizard.FlashIII;
import com.edasaki.rpg.spells.wizard.LightningBolt;
import com.edasaki.rpg.spells.wizard.RainbowBurst;

public class SpellbookWizard extends Spellbook {

    public static final Spell FIREBALL = new Spell("Fireball", 3, 15, 1, 0, "Shoot a fireball doing %.0f%% damage.", new IntToDoubleFunction[] {
            (x) -> {
                return 125 + 15 * x;
            }
    }, null, new Fireball());

    public static final Spell BUBBLE_BEAM = new Spell("Bubble Beam", 4, 10, 2, 0, new String[] {
            "Fire a beam of bubbles dealing 55% damage every 0.5 seconds for 2 seconds.",
            "Fire a beam of bubbles dealing 65% damage every 0.5 seconds for 2 seconds.",
            "Fire a beam of bubbles dealing 75% damage every 0.5 seconds for 2 seconds.",
            "Fire a beam of bubbles dealing 85% damage every 0.5 seconds for 2 seconds.",
            "Fire a beam of bubbles dealing 95% damage every 0.5 seconds for 2 seconds.",
            "Fire a beam of bubbles dealing 105% damage every 0.5 seconds for 2 seconds.",
            "Fire a beam of bubbles dealing 115% damage every 0.5 seconds for 2 seconds.",
            "Fire a beam of bubbles dealing 125% damage every 0.5 seconds for 2 seconds.",
            "Fire a beam of bubbles dealing 135% damage every 0.5 seconds for 2 seconds.",
            "Fire a beam of bubbles dealing 145% damage every 0.5 seconds for 2 seconds.",
    }, null, new BubbleBeam());

    public static final Spell RAINBOW_BURST = new Spell("Rainbow Burst", 4, 10, 3, 0, new String[] {
            "A burst of rainbows shoot out in a circle, dealing 140% damage to enemies hit and knocking them away.",
            "A burst of rainbows shoot out in a circle, dealing 160% damage to enemies hit and knocking them away.",
            "A burst of rainbows shoot out in a circle, dealing 180% damage to enemies hit and knocking them away.",
            "A burst of rainbows shoot out in a circle, dealing 200% damage to enemies hit and knocking them away.",
            "A burst of rainbows shoot out in a circle, dealing 220% damage to enemies hit and knocking them away.",
            "A burst of rainbows shoot out in a circle, dealing 240% damage to enemies hit and knocking them away.",
            "A burst of rainbows shoot out in a circle, dealing 260% damage to enemies hit and knocking them away.",
            "A burst of rainbows shoot out in a circle, dealing 280% damage to enemies hit and knocking them away.",
            "A burst of rainbows shoot out in a circle, dealing 300% damage to enemies hit and knocking them away.",
            "A burst of rainbows shoot out in a circle, dealing 320% damage to enemies hit and knocking them away.",
    }, null, new RainbowBurst());

    public static final Spell LIGHTNING_BOLT = new Spell("Lightning Bolt", 4, 8, 4, 0, new String[] {
            "Call down a bolt of lightning from the skies, striking nearby enemies for 120% damage.",
            "Call down a bolt of lightning from the skies, striking nearby enemies for 140% damage.",
            "Call down a bolt of lightning from the skies, striking nearby enemies for 160% damage.",
            "Call down a bolt of lightning from the skies, striking nearby enemies for 190% damage.",
            "Call down a bolt of lightning from the skies, striking nearby enemies for 220% damage.",
            "Call down a bolt of lightning from the skies, striking nearby enemies for 260% damage.",
            "Call down a bolt of lightning from the skies, striking nearby enemies for 300% damage.",
            "Call down a bolt of lightning from the skies, striking nearby enemies for 350% damage.",
    }, null, new LightningBolt());

    public static final Spell BUBBLE_BURST = new Spell("Bubble Burst", 4, 10, 2, 1, new String[] {
            "A burst of bubbles shoot out in a circle, dealing 100% damage to enemies hit and briefly slowing them.",
            "A burst of bubbles shoot out in a circle, dealing 110% damage to enemies hit and briefly slowing them.",
            "A burst of bubbles shoot out in a circle, dealing 120% damage to enemies hit and briefly slowing them.",
            "A burst of bubbles shoot out in a circle, dealing 130% damage to enemies hit and briefly slowing them.",
            "A burst of bubbles shoot out in a circle, dealing 140% damage to enemies hit and briefly slowing them.",
            "A burst of bubbles shoot out in a circle, dealing 150% damage to enemies hit and briefly slowing them.",
            "A burst of bubbles shoot out in a circle, dealing 160% damage to enemies hit and briefly slowing them.",
            "A burst of bubbles shoot out in a circle, dealing 170% damage to enemies hit and briefly slowing them.",
            "A burst of bubbles shoot out in a circle, dealing 180% damage to enemies hit and briefly slowing them.",
            "A burst of bubbles shoot out in a circle, dealing 190% damage to enemies hit and briefly slowing them.",
    }, new Object[] { BUBBLE_BEAM, 5
    }, new BubbleBurst());

    public static final Spell FLAME_WAVE = new Spell("Flame Wave", 4, 10, 1, 1, new String[] {
            "Shoot out 3 flame bolts that deal 200% damage each.",
            "Shoot out 3 flame bolts that deal 220% damage each.",
            "Shoot out 5 flame bolts that deal 240% damage each.",
            "Shoot out 5 flame bolts that deal 260% damage each.",
            "Shoot out 7 flame bolts that deal 280% damage each.",
            "Shoot out 3 flame bolts that deal 300% damage each.",
            "Shoot out 3 flame bolts that deal 320% damage each.",
            "Shoot out 5 flame bolts that deal 340% damage each.",
            "Shoot out 5 flame bolts that deal 360% damage each.",
            "Shoot out 7 flame bolts that deal 380% damage each.",
    }, new Object[] { FIREBALL, 3
    }, new FlameWave());

    public static final Spell FIRESTORM = new Spell("Firestorm", 7, 3, 1, 2, new String[] {
            "Summon a storm of fire for 4 seconds that randomly strikes nearby enemies for 125% damage per hit.",
            "Summon a storm of fire for 5 seconds that randomly strikes nearby enemies for 175% damage per hit.",
            "Summon a storm of fire for 6 seconds that randomly strikes nearby enemies for 225% damage per hit.",
    }, new Object[] { FLAME_WAVE, 3
    }, new Firestorm());

    public static final Spell FLASH_I = new Spell("Flash I", 2, 1, 5, 0, new String[] {
            "Instantly teleport a short distance forward. Cannot pass through blocks.",
    }, null, new FlashI());

    public static final Spell FLASH_II = new Spell("Flash II", 3, 1, 5, 1, new String[] {
            "Instantly teleport a medium distance forward. Cannot pass through blocks.",
    }, new Object[] { FLASH_I, 1
    }, new FlashII());

    public static final Spell FLASH_III = new Spell("Flash III", 4, 1, 5, 2, new String[] {
            "Instantly teleport a long distance forward. Cannot pass through blocks.",
    }, new Object[] { FLASH_II, 1
    }, new FlashIII());

    public static final Spell MANA_TIDE = new Spell("Mana Tide", 0, 5, 1, 8, new String[] {
            "Increase Mana Regen rate by 15%",
            "Increase Mana Regen rate by 30%",
            "Increase Mana Regen rate by 45%",
            "Increase Mana Regen rate by 60%",
            "Increase Mana Regen rate by 75%",
    }, null, new PassiveSpellEffect());

    public static final Spell WISDOM = new Spell("Wisdom", 0, 6, 2, 8, new String[] {
            "Gain +5% EXP from all sources.",
            "Gain +7% EXP from all sources.",
            "Gain +9% EXP from all sources.",
            "Gain +11% EXP from all sources.",
            "Gain +13% EXP from all sources.",
            "Gain +15% EXP from all sources.",
    }, null, new PassiveSpellEffect());

    private static final Spell[] SPELL_LIST = {
            FIREBALL,
            FLAME_WAVE,
            FIRESTORM,
            BUBBLE_BEAM,
            BUBBLE_BURST,
            RAINBOW_BURST,
            LIGHTNING_BOLT,
            FLASH_I,
            FLASH_II,
            FLASH_III,
            MANA_TIDE,
            WISDOM
    };
    // DON'T FORGET TO ADD TO SPELL_LIST

    public static final Spellbook INSTANCE = new SpellbookWizard();

    @Override
    public Spell[] getSpellList() {
        return SPELL_LIST;
    }

}
