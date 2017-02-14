package com.edasaki.rpg.items;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.warps.WarpCallback;
import com.edasaki.rpg.warps.WarpChecker;
import com.edasaki.rpg.warps.WarpManager;

public class WarpItemRunnable extends ItemRunnable {

    private Location loc;
    private ItemStack item;

    public WarpItemRunnable(Location loc, ItemStack item) {
        this.loc = loc;
        this.item = item;
    }

    @Override
    public void run(Event event, Player p, PlayerDataRPG pd) {
        if (!(event instanceof PlayerInteractEvent))
            return;
        if (!(((PlayerInteractEvent) event).getAction() == Action.RIGHT_CLICK_AIR || ((PlayerInteractEvent) event).getAction() == Action.RIGHT_CLICK_BLOCK))
            return;
        WarpManager.warp(p, loc, new WarpCallback() {

            @Override
            public void complete(boolean warpSuccess) {
                if (warpSuccess) {
                    for (int k = 0; k < p.getInventory().getSize(); k++) {
                        ItemStack i = p.getInventory().getItem(k);
                        if (i != null && i.isSimilar(item)) {
                            if (i.getAmount() > 1) {
                                i.setAmount(i.getAmount() - 1);
                                p.getInventory().setItem(k, i);
                                p.updateInventory();
                            } else {
                                i.setType(Material.AIR);
                                i.setAmount(0);
                                p.getInventory().setItem(k, null);
                            }
                            break;
                        }
                    }
                }
            }

        }, new WarpChecker() {
            @Override
            public String check(Player p) {
                int total = 0;
                for (ItemStack i : p.getInventory()) {
                    if (i != null && i.isSimilar(item)) {
                        total += i.getAmount();
                    }
                }
                if (total > 0)
                    return null;
                return "Your Warp Scroll is missing!";
            }

        });

    }
}
