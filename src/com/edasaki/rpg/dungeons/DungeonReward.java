package com.edasaki.rpg.dungeons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.edasaki.rpg.items.EquipType;
import com.edasaki.rpg.items.RPGItem;
import com.edasaki.rpg.items.RandomItemGenerator;

public class DungeonReward {

    public RPGItem item = null;
    public int min, max;
    public double baseChance;
    private int diff;

    public int tier = -1;

    public DungeonReward(RPGItem item, int min, int max, double baseChance) {
        this.item = item;
        this.min = min;
        this.max = max;
        this.baseChance = baseChance;
        this.diff = max - min;
    }

    public DungeonReward(int tier, int min, int max, double baseChance) {
        this.tier = tier;
        this.min = min;
        this.max = max;
        this.baseChance = baseChance;
        this.diff = max - min;
    }

    private ItemStack getRoll() {
        if (item != null)
            return item.generate();
        if (tier >= 1 && tier <= 5)
            return RandomItemGenerator.generateEquip(EquipType.random(), tier);
        try {
            throw new Exception("Error: DungeonReward roll: " + item + ", " + tier);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RandomItemGenerator.generateEquip(EquipType.random(), 1);
    }

    public List<ItemStack> roll() {
        ArrayList<ItemStack> arr = new ArrayList<ItemStack>();
        for (int k = 0; k < min; k++) {
            if (Math.random() < baseChance)
                arr.add(getRoll());
        }
        double curr = baseChance;
        for (int k = 0; k < diff; k++) {
            if (Math.random() < curr) {
                arr.add(getRoll());
            }
            curr *= 0.85;
        }
        return arr;
    }

}
