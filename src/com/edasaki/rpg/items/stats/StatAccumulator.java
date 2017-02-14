package com.edasaki.rpg.items.stats;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.edasaki.rpg.PlayerDataRPG;

public class StatAccumulator {

    public int level = 1;

    private int damageLow = 0;
    private int damageHigh = 0;
    private int maxHP = 0;
    private int maxHPMultiplier = 0;
    private int defense = 0;
    private int defenseMultiplier = 0;
    private int speed = 0;
    private int critChance = 0;
    private int critDamage = 0;
    private int rarityFinder = 0;
    private int manaRegenRate = 0;
    private int spellDamage = 0;
    private int attackDamage = 0;
    private int hpRegen = 0;
    private int attackSpeed = 0;

    public void parseAndAccumulate(String s) {
        for (StatParser sp : StatParser.values()) {
            sp.parse(this, s);
        }
    }

    public void setDamage(int low, int high) {
        this.damageLow = low;
        this.damageHigh = high;
    }

    public void setHP(int hp) {
        this.maxHP = hp;
    }

    public void setHPMultiplier(int maxHPMultiplier) {
        this.maxHPMultiplier = maxHPMultiplier;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setDefenseMultiplier(int defenseMultiplier) {
        this.defenseMultiplier = defenseMultiplier;
    }

    public void setCritChance(int chance) {
        this.critChance = chance;
    }

    public void setCritDamage(int damage) {
        this.critDamage = damage;
    }

    public void setRarityFinder(int percent) {
        this.rarityFinder = percent;
    }

    public void setManaRegenRate(int percent) {
        this.manaRegenRate = percent;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setSpellDamage(int spellDamage) {
        this.spellDamage = spellDamage;
    }

    public void setAttackDamage(int val) {
        this.attackDamage = val;
    }

    public void setHPRegen(int val) {
        this.hpRegen = val;
    }

    public void setAttackSpeed(int val) {
        this.attackSpeed = val;
    }

    public void setSage(boolean sage) {
        // only use multiply so that stats that are originally 0 are still 0
        this.damageLow *= 1.2;
        this.damageHigh *= 1.2;
        this.maxHP *= 1.2;
        this.maxHPMultiplier *= 1.1;
        this.defense *= 1.2;
        this.defenseMultiplier *= 1.1;
        this.critChance *= 1.3;
        this.critDamage *= 1.1;
    }

    public static void clearStats(PlayerDataRPG pd) {
        pd.damageLow = 0;
        pd.damageHigh = 0;
        pd.maxHP = 0;
        pd.maxHPMultiplier = 0;
        pd.defense = 0;
        pd.defenseMultiplier = 0;
        pd.speed = 0;
        pd.critChance = 0;
        pd.critDamage = 150;
        pd.rarityFinder = 0;
        pd.manaRegenRate = 0;
        pd.spellDamage = 0;
        pd.attackDamage = 0;
        pd.lifesteal = 0;
        pd.hpRegen = 0;
        pd.attackSpeed = 0;
    }

    public void apply(PlayerDataRPG pd) {
        pd.damageLow += damageLow;
        pd.damageHigh += damageHigh;
        pd.maxHP += maxHP;
        pd.maxHPMultiplier += maxHPMultiplier;
        pd.defense += defense;
        pd.defenseMultiplier += defenseMultiplier;
        pd.speed += speed;
        pd.critChance += critChance;
        pd.critDamage += critDamage;
        pd.rarityFinder += rarityFinder;
        pd.manaRegenRate += manaRegenRate;
        pd.spellDamage += spellDamage;
        pd.attackDamage += attackDamage;
        //        pd.lifesteal += lifesteal;
        pd.hpRegen += hpRegen;
        pd.attackSpeed += attackSpeed;
        // #####
        // DON'T FORGET TO ADD TO LORE LOL (SCROLL DOWN)
        // #####
    }

    public static void finalizeStats(PlayerDataRPG pd) {
        // multipliers are applied here and nowhere else
        pd.maxHPMultiplier /= 100;
        pd.maxHP += (int) Math.ceil((pd.baseMaxHP + pd.maxHP) * pd.maxHPMultiplier);
        pd.defenseMultiplier /= 100;
        pd.defense += (int) Math.ceil(pd.defense * pd.defenseMultiplier);
        pd.speed /= 100; //+50% speed -> 0.5
        pd.speed++; //0.5 -> 1.5
        pd.speed *= 0.2; //1.5 -> 0.3 [base player speed is 0.2]
        if (pd.speed > 1.0)
            pd.speed = 1.0f;
        pd.getPlayer().setWalkSpeed(pd.speed);
        pd.critChance /= 100;
        pd.critDamage /= 100;
        pd.spellDamage /= 100;
        pd.spellDamage++;
        pd.attackDamage /= 100;
        pd.attackDamage++;
        pd.lifesteal /= 100;
        pd.hpRegen /= 100;
        pd.hpRegen++;
        pd.attackSpeed /= 100;
        //can't do 0 base damage
        if (pd.damageLow < 1)
            pd.damageLow = 1;
        if (pd.damageHigh < 1)
            pd.damageHigh = 1;
    }

    public List<String> lore() {
        ArrayList<String> lore = new ArrayList<String>();
        boolean space = true;
        lore.add(StatParser.LEVEL.format(level));
        if (damageLow > 0 && damageHigh > 0) {
            lore.add(StatParser.DAMAGE.format(damageLow, damageHigh));
        }
        space = checkSpace(space, lore);
        if (maxHP > 0) {
            lore.add(StatParser.HP.format(maxHP));
            space = true;
        }
        if (maxHPMultiplier > 0) {
            lore.add(StatParser.HP_MULT.format(maxHPMultiplier));
            space = true;
        }
        if (defense > 0) {
            lore.add(StatParser.DEFENSE.format(defense));
            space = true;
        }
        if (defenseMultiplier > 0) {
            lore.add(StatParser.DEFENSE_MULT.format(defenseMultiplier));
            space = true;
        }
        space = checkSpace(space, lore);
        if (critChance > 0) {
            lore.add(StatParser.CRIT_CHANCE.format(critChance));
            space = true;
        }
        if (critDamage > 0) {
            lore.add(StatParser.CRIT_DAMAGE.format(critDamage));
            space = true;
        }
        space = checkSpace(space, lore);
        if (rarityFinder > 0) {
            lore.add(StatParser.RARITY_FINDER.format(rarityFinder));
            space = true;
        }
        if (manaRegenRate > 0) {
            lore.add(StatParser.MANA_REGEN.format(manaRegenRate));
            space = true;
        }
        if (speed > 0) {
            lore.add(StatParser.SPEED.format(speed));
            space = true;
        }
        if (spellDamage > 0) {
            lore.add(StatParser.SPELL_DAMAGE.format(spellDamage));
            space = true;
        }
        if (attackDamage > 0) {
            lore.add(StatParser.ATTACK_DAMAGE.format(attackDamage));
            space = true;
        }
        if (hpRegen > 0) {
            lore.add(StatParser.HP_REGEN.format(hpRegen));
            space = true;
        }
        if (attackSpeed > 0) {
            lore.add(StatParser.ATTACK_SPEED.format(attackSpeed));
            space = true;
        }
        space = checkSpace(space, lore);
        if (lore.size() > 0 && lore.get(lore.size() - 1).length() == 0)
            lore.remove(lore.size() - 1);
        return lore;
    }

    private boolean checkSpace(boolean needsSpace, ArrayList<String> lore) {
        if (needsSpace)
            lore.add("");
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Field f : StatAccumulator.class.getDeclaredFields()) {
            try {
                sb.append(f.toString() + ":" + f.get(this) + ", ");
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
