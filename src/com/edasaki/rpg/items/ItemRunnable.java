package com.edasaki.rpg.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import com.edasaki.rpg.PlayerDataRPG;

public abstract class ItemRunnable {

    public abstract void run(Event event, Player p, PlayerDataRPG pd);

    protected void takeOneItemInstant(Player p) {
        ItemStack item = p.getEquipment().getItemInMainHand();
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
            p.getEquipment().setItemInMainHand(item);
        } else {
            p.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
        }
    }
}
