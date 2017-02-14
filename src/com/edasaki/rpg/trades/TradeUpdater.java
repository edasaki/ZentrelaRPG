package com.edasaki.rpg.trades;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RUtils;

public class TradeUpdater {

    public HashSet<Integer> aSlots;
    public HashSet<Integer> bSlots;

    public boolean aReady;
    public boolean bReady;

    public UUID aUUID;
    public UUID bUUID;

    private Runnable r = null;

    public void tryComplete() {
        if (!(aReady && bReady))
            return;
        Player a = TradeManager.plugin.getServer().getPlayer(aUUID);
        Player b = TradeManager.plugin.getServer().getPlayer(bUUID);
        boolean aInvalid = a == null || !a.isOnline() || !a.isValid();
        boolean bInvalid = b == null || !b.isOnline() || !b.isValid();
        if (aInvalid && !bInvalid) {
            b.sendMessage(ChatColor.RED + "Error: Could not find player to trade.");
            b.closeInventory();
            return;
        } else if (!aInvalid && bInvalid) {
            a.sendMessage(ChatColor.RED + "Error: Could not find player to trade.");
            a.closeInventory();
            return;
        } else if (aInvalid && bInvalid) {
            return;
        }
        if (RMath.flatDistance(a.getLocation(), b.getLocation()) >= TradeManager.TRADE_RANGE) {
            a.sendMessage(ChatColor.RED + "You can only trade players within " + TradeManager.TRADE_RANGE + " blocks of you!");
            b.sendMessage(ChatColor.RED + "You can only trade players within " + TradeManager.TRADE_RANGE + " blocks of you!");
            a.closeInventory();
            b.closeInventory();
            return;
        }
        int aDiff = bSlots.size() - aSlots.size();
        int bDiff = aSlots.size() - bSlots.size();
        if (!RUtils.hasEmptySpaces(a, aDiff)) {
            a.sendMessage(ChatColor.RED + "You don't have enough inventory space to trade!");
            a.sendMessage(ChatColor.RED + "Empty some slots and try again!");
            b.sendMessage(ChatColor.RED + a.getName() + " didn't have enough inventory space to trade!");
            a.closeInventory();
            b.closeInventory();
            return;
        }
        if (!RUtils.hasEmptySpaces(b, bDiff)) {
            a.sendMessage(ChatColor.RED + b.getName() + " didn't have enough inventory space to trade!");
            b.sendMessage(ChatColor.RED + "You don't have enough inventory space to trade!");
            b.sendMessage(ChatColor.RED + "Empty some slots and try again!");
            a.closeInventory();
            b.closeInventory();
            return;
        }
        ArrayList<ItemStack> aItems = new ArrayList<ItemStack>();
        ArrayList<ItemStack> bItems = new ArrayList<ItemStack>();
        for (int slot : aSlots) {
            aItems.add(a.getInventory().getItem(slot));
        }
        for (int slot : bSlots) {
            bItems.add(b.getInventory().getItem(slot));
        }
        for (int slot : aSlots) {
            a.getInventory().setItem(slot, bItems.isEmpty() ? new ItemStack(Material.AIR) : bItems.remove(0));
        }
        for (ItemStack item : bItems) {
            a.getInventory().addItem(item);
        }
        for (int slot : bSlots) {
            b.getInventory().setItem(slot, aItems.isEmpty() ? new ItemStack(Material.AIR) : aItems.remove(0));
        }
        for (ItemStack item : aItems) {
            b.getInventory().addItem(item);
        }
        TradeManager.currentTrade.remove(a.getUniqueId());
        TradeManager.currentTrade.remove(b.getUniqueId());
        a.closeInventory();
        b.closeInventory();
        a.sendMessage(ChatColor.GREEN + "Trade complete! You and " + b.getName() + " have exchanged items!");
        b.sendMessage(ChatColor.GREEN + "Trade complete! You and " + a.getName() + " have exchanged items!");
    }

    public void resetStates() {
        this.aReady = false;
        this.bReady = false;
    }

    public void set(Runnable r) {
        this.r = r;
    }

    public void update() {
        if (r != null) {
            r.run();
        }
    }

}
