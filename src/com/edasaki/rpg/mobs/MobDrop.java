package com.edasaki.rpg.mobs;

import org.bukkit.inventory.ItemStack;

import com.edasaki.rpg.items.RPGItem;

public final class MobDrop {

    public final RPGItem item;
    public final int minAmount;
    public final int maxAmount;
    public final double chance;

    public ItemStack generate() {
        return item.generate();
    }

    public boolean roll() {
        return Math.random() < chance;
    }

    public MobDrop(RPGItem item, int minAmount, int maxAmount, double chance) {
        this.item = item;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.chance = chance;
    }
}
