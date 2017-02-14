package com.edasaki.rpg.spells;

import java.util.function.IntToDoubleFunction;

import com.edasaki.rpg.spells.alchemist.Bomb;
import com.edasaki.rpg.spells.alchemist.Gravitron;
import com.edasaki.rpg.spells.alchemist.HeatWall;
import com.edasaki.rpg.spells.alchemist.Minefield;
import com.edasaki.rpg.spells.alchemist.MysteryDrink;
import com.edasaki.rpg.spells.alchemist.Transmute;

public class SpellbookAlchemist extends Spellbook {

    public static final Spell BOMB = new Spell("Bomb", 2, 10, 1, 0, "Throw a bomb that explodes after 1 second, dealing %.0f%% damage to nearby enemies.", new IntToDoubleFunction[] {
            (x) -> {
                return 150 + 20 * x;
            } }, null, new Bomb());

    public static final Spell GRAVITRON = new Spell("Gravitron", 3, 5, 2, 0, "Throw a gravity device that pulls in enemies towards it every second for %.0f seconds.", new IntToDoubleFunction[] {
            (x) -> {
                return 1 + x;
            } }, new Object[] {
                    BOMB,
                    2
            }, new Gravitron());

    public static final Spell HEAT_WALL = new Spell("Heat Wall", 4, 10, 2, 1, "Throw a heat wall device that creates flame pulses for 3 seconds dealing %.0f%% damage per second.", new IntToDoubleFunction[] {
            (x) -> {
                return 125 + 25 * x;
            } }, new Object[] {
                    BOMB,
                    3
            }, new HeatWall());

    public static final Spell MINEFIELD = new Spell("Minefield", 8, 15, 1, 1, "Scatter %.0f mines randomly around you, exploding for %.0f%% damage if activated by a nearby enemy.", new IntToDoubleFunction[] {
            (x) -> {
                if (x <= 5)
                    return 3;
                else if (x <= 10)
                    return 4;
                else
                    return 5;
            },
            (x) -> {
                return 120 + 10 * x;
            } }, new Object[] { BOMB, 6 }, new Minefield());

    public static final Spell TRANSMUTE = new Spell("Transmute", 1, 6, 3, 0, "Convert your HP to mana, losing %.0f%% of your maximum HP in exchange for %.0f Mana. The 1 Mana spell cost is also refunded.", new IntToDoubleFunction[] {
            (x) -> {
                return 30 - 2 * x;
            },
            (x) -> {
                if (x <= 2)
                    return 2;
                else if (x <= 4)
                    return 3;
                else
                    return 4;
            } }, null, new Transmute());

    public static final Spell MYSTERY_DRINK = new Spell("Mystery Drink", 4, 3, 3, 1, new String[] {
            "Consume a mystery elixir that gives you and nearby party members a random buff for 5 seconds.",
            "Consume a mystery elixir that gives you and nearby party members a random buff for 7 seconds.",
            "Consume a mystery elixir that gives you and nearby party members a random buff for 11 seconds.",
    }, null, new MysteryDrink());

    public static final Spell SPEEDY_BREWER = new Spell("Speedy Brewer", 0, 3, 1, 8, new String[] {
            "Increase your attack speed by 10%.",
            "Increase your attack speed by 20%.",
            "Increase your attack speed by 30%.",
    }, null, new PassiveSpellEffect());

    public static final Spell POTION_MASTERY = new Spell("Potion Mastery", 0, 5, 2, 8, new String[] {
            "Gain +10% healing from consumable healing potions.",
            "Gain +20% healing from consumable healing potions.",
            "Gain +30% healing from consumable healing potions.",
            "Gain +40% healing from consumable healing potions.",
            "Gain +50% healing from consumable healing potions.",
    }, null, new PassiveSpellEffect());

    private static final Spell[] SPELL_LIST = {
            BOMB,
            MINEFIELD,
            GRAVITRON,
            HEAT_WALL,
            /*QUICK_SMOKE,*/ TRANSMUTE,
            MYSTERY_DRINK,
            SPEEDY_BREWER,
            POTION_MASTERY
    };
    // DON'T FORGET TO ADD TO SPELL_LIST

    public static final Spellbook INSTANCE = new SpellbookAlchemist();

    @Override
    public Spell[] getSpellList() {
        return SPELL_LIST;
    }

}
