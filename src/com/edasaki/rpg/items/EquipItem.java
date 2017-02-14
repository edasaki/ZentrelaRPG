package com.edasaki.rpg.items;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.edasaki.core.utils.RFormatter;
import com.edasaki.rpg.items.stats.StatAccumulator;

public class EquipItem extends RPGItem {

    public int tier;

    public StatAccumulator stats;

    public org.bukkit.Color leatherColor = null;

    @Override
    public ItemStack generate() {
        ItemStack item = new ItemStack(material);
        ItemMeta im = item.getItemMeta();
        // prepend reset to clear the default name coloring
        im.setDisplayName(ChatColor.RESET + name);
        // Get lore from string
        List<String> lore = stats.lore();
        if (soulbound) {
            lore.add(ChatColor.RED + ChatColor.ITALIC.toString() + "Soulbound");
        }
        // Get description, if it exists
        if (description != null && description.length() > 0) {
            if (lore.size() > 0 && lore.get(lore.size() - 1).length() > 0)
                lore.add("");
            lore.addAll(RFormatter.stringToLore(description, ChatColor.GRAY));
        }

        im.setLore(lore);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        im.addItemFlags(ItemFlag.HIDE_DESTROYS);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        im.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        // Add color for leather armors
        if (leatherColor != null) {
            ((LeatherArmorMeta) im).setColor(leatherColor);
        }

        item.setItemMeta(im);
        return item;
    }

    public EquipItem(Material material) {
        this.material = material;
        this.stats = new StatAccumulator();
    }

}
