package com.edasaki.rpg.help;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.edasaki.core.menus.MenuItem;

public class ListedCommand implements Comparable<ListedCommand> {
    public String name, strippedName, desc;
    public String[] fullLore;

    @Override
    public int compareTo(ListedCommand o) {
        return this.strippedName.compareTo(o.strippedName);
    }

    @Override
    public String toString() {
        return this.strippedName;
    }

    protected MenuItem generate(int row, int col) {
        MenuItem mi = new MenuItem(row, col, new ItemStack(Material.PAPER), name, fullLore, null);
        return mi;
    }
}
