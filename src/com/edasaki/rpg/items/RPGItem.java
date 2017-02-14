package com.edasaki.rpg.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class RPGItem {

    public String identifier;
    public String name = null;
    public Material material = null;
    public String description = null;

    public boolean soulbound;

    public abstract ItemStack generate();
}
