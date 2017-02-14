package com.edasaki.rpg.spells;

import com.edasaki.rpg.spells.archer.ArrowRain;
import com.edasaki.rpg.spells.archer.DoubleShot;
import com.edasaki.rpg.spells.archer.ExplodingArrow;
import com.edasaki.rpg.spells.archer.Hurricane;
import com.edasaki.rpg.spells.archer.QuadShot;
import com.edasaki.rpg.spells.archer.TripleShot;

public class SpellbookArcher extends Spellbook {

    public static final Spell DOUBLE_SHOT = new Spell("Double Shot", 1, 10, 1, 0, new String[] {
            "Shoot two arrows dealing 80% damage each.",
            "Shoot two arrows dealing 90% damage each.",
            "Shoot two arrows dealing 100% damage each.",
            "Shoot two arrows dealing 110% damage each.",
            "Shoot two arrows dealing 120% damage each.",
            "Shoot two arrows dealing 130% damage each.",
            "Shoot two arrows dealing 140% damage each.",
            "Shoot two arrows dealing 150% damage each.",
            "Shoot two arrows dealing 160% damage each.",
            "Shoot two arrows dealing 170% damage each.",
    }, null, new DoubleShot());

    public static final Spell TRIPLE_SHOT = new Spell("Triple Shot", 2, 10, 1, 1, new String[] {
            "Shoot three arrows dealing 70% damage each.",
            "Shoot three arrows dealing 80% damage each.",
            "Shoot three arrows dealing 90% damage each.",
            "Shoot three arrows dealing 100% damage each.",
            "Shoot three arrows dealing 110% damage each.",
            "Shoot three arrows dealing 120% damage each.",
            "Shoot three arrows dealing 130% damage each.",
            "Shoot three arrows dealing 140% damage each.",
            "Shoot three arrows dealing 150% damage each.",
            "Shoot three arrows dealing 160% damage each.",
    }, new Object[] {
            DOUBLE_SHOT,
            10
    }, new TripleShot());

    public static final Spell QUAD_SHOT = new Spell("Quad Shot", 3, 10, 1, 2, new String[] {
            "Shoot four arrows dealing 60% damage each.",
            "Shoot four arrows dealing 70% damage each.",
            "Shoot four arrows dealing 80% damage each.",
            "Shoot four arrows dealing 90% damage each.",
            "Shoot four arrows dealing 100% damage each.",
            "Shoot four arrows dealing 110% damage each.",
            "Shoot four arrows dealing 120% damage each.",
            "Shoot four arrows dealing 130% damage each.",
            "Shoot four arrows dealing 140% damage each.",
            "Shoot four arrows dealing 150% damage each.",
    }, new Object[] {
            TRIPLE_SHOT,
            10
    }, new QuadShot());

    public static final Spell ARROW_RAIN = new Spell("Arrow Rain", 4, 10, 2, 0, new String[] {
            "Summon a shower of arrows dealing 110% damage each.",
            "Summon a shower of arrows dealing 120% damage each.",
            "Summon a shower of arrows dealing 130% damage each.",
            "Summon a shower of arrows dealing 140% damage each.",
            "Summon a shower of arrows dealing 150% damage each.",
            "Summon a shower of arrows dealing 160% damage each.",
            "Summon a shower of arrows dealing 170% damage each.",
            "Summon a shower of arrows dealing 180% damage each.",
            "Summon a shower of arrows dealing 190% damage each.",
            "Summon a shower of arrows dealing 200% damage each.",
    }, null, new ArrowRain());

    public static final Spell HURRICANE = new Spell("Hurricane", 8, 15, 2, 1, new String[] {
            "Shoot a barrage of 16 arrows dealing 26% damage each.",
            "Shoot a barrage of 17 arrows dealing 27% damage each.",
            "Shoot a barrage of 18 arrows dealing 28% damage each.",
            "Shoot a barrage of 19 arrows dealing 29% damage each.",
            "Shoot a barrage of 20 arrows dealing 30% damage each.",
            "Shoot a barrage of 21 arrows dealing 31% damage each.",
            "Shoot a barrage of 22 arrows dealing 32% damage each.",
            "Shoot a barrage of 23 arrows dealing 33% damage each.",
            "Shoot a barrage of 24 arrows dealing 34% damage each.",
            "Shoot a barrage of 25 arrows dealing 35% damage each.",
            "Shoot a barrage of 26 arrows dealing 36% damage each.",
            "Shoot a barrage of 27 arrows dealing 37% damage each.",
            "Shoot a barrage of 28 arrows dealing 38% damage each.",
            "Shoot a barrage of 29 arrows dealing 39% damage each.",
            "Shoot a barrage of 30 arrows dealing 40% damage each.",
    }, new Object[] {
            ARROW_RAIN,
            6
    }, new Hurricane());

    //    public static final Spell BLUNT_ARROW = new Spell("Blunt Arrow", 2, 1, 3, 0, new String[] {
    //            "Fire a blunt arrow that knocks an enemy a short distance away.",
    //    }, null, new BluntArrow());

    public static final Spell EXPLODING_ARROW = new Spell("Exploding Arrow", 2, 10, 3, 1, new String[] {
            "Fire an arrow that explodes on impact with an enemy to deal 130% damage to nearby enemies.",
            "Fire an arrow that explodes on impact with an enemy to deal 150% damage to nearby enemies.",
            "Fire an arrow that explodes on impact with an enemy to deal 170% damage to nearby enemies.",
            "Fire an arrow that explodes on impact with an enemy to deal 190% damage to nearby enemies.",
            "Fire an arrow that explodes on impact with an enemy to deal 210% damage to nearby enemies.",
            "Fire an arrow that explodes on impact with an enemy to deal 230% damage to nearby enemies.",
            "Fire an arrow that explodes on impact with an enemy to deal 250% damage to nearby enemies.",
            "Fire an arrow that explodes on impact with an enemy to deal 270% damage to nearby enemies.",
            "Fire an arrow that explodes on impact with an enemy to deal 290% damage to nearby enemies.",
            "Fire an arrow that explodes on impact with an enemy to deal 310% damage to nearby enemies.",
    }, null, new ExplodingArrow());

    public static final Spell BOW_MASTERY = new Spell("Bow Mastery", 0, 5, 1, 7, new String[] {
            "Deal 2% increased base damage.",
            "Deal 4% increased base damage.",
            "Deal 6% increased base damage.",
            "Deal 8% increased base damage.",
            "Deal 10% increased base damage.",
    }, null, new PassiveSpellEffect());

    public static final Spell KEEN_EYES = new Spell("Keen Eyes", 0, 5, 1, 8, new String[] {
            "Gain +2% Crit Chance.",
            "Gain +4% Crit Chance.",
            "Gain +6% Crit Chance.",
            "Gain +8% Crit Chance.",
            "Gain +10% Crit Chance.",
    }, null, new PassiveSpellEffect());

    public static final Spell RAPID_FIRE = new Spell("Rapid Fire", 0, 3, 2, 8, new String[] {
            "Increase your attack speed by 10%.",
            "Increase your attack speed by 20%.",
            "Increase your attack speed by 30%.",
    }, null, new PassiveSpellEffect());

    private static final Spell[] SPELL_LIST = {
            DOUBLE_SHOT,
            TRIPLE_SHOT,
            QUAD_SHOT,
            ARROW_RAIN,
            HURRICANE,
            //            BLUNT_ARROW,
            EXPLODING_ARROW,
            BOW_MASTERY,
            KEEN_EYES,
            RAPID_FIRE
    };
    // DON'T FORGET TO ADD TO SPELL_LIST

    public static final Spellbook INSTANCE = new SpellbookArcher();

    @Override
    public Spell[] getSpellList() {
        return SPELL_LIST;
    }

}
