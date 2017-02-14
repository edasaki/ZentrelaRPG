package com.edasaki.rpg.shops;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.edasaki.rpg.items.ItemManager;

public class ShopItem {

    public ItemStack item;
    public int cost;
    public ItemStack display;

    public ShopItem(String id, int cost) {
        this(id, cost, "shards");
    }

    public ShopItem(String id, int cost, String unit) {
        if (ItemManager.itemIdentifierToRPGItemMap.containsKey(id)) {
            this.item = ItemManager.itemIdentifierToRPGItemMap.get(id).generate();
            this.cost = cost;
            ItemStack temp = new ItemStack(item.getType());
            ArrayList<String> lore = new ArrayList<String>();
            if (temp.hasItemMeta() && temp.getItemMeta().hasLore())
                lore.addAll(temp.getItemMeta().getLore());
            lore.add("");
            lore.add(ChatColor.GRAY + "Click to buy this item for " + ChatColor.YELLOW + cost + " " + unit + ChatColor.GRAY + ".");
            ItemMeta im = temp.getItemMeta();
            im.setDisplayName(item.hasItemMeta() && item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().toString());
            im.setLore(lore);
            temp.setItemMeta(im);
            display = temp;
        } else {
            try {
                throw new Exception("Could not load shop item with id " + id + " and cost " + cost);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.item = null;
            this.cost = -1;
            this.display = null;
        }
    }

}
