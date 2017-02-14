package com.edasaki.rpg.economy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.Function;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.SakiRPG;

@SuppressWarnings("unused")
public class ShardManager extends AbstractManagerRPG {

    private static ItemStack shard;
    private static ItemStack cube;
    private static ItemStack purified;

    public ShardManager(SakiRPG plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {
        shard = new ItemStack(Material.QUARTZ);
        ItemMeta im = shard.getItemMeta();
        im.setDisplayName(ChatColor.YELLOW + "Shard");
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GRAY + "The official currency of Zentrela!");
        lore.add("");
        lore.add(ChatColor.GRAY + "Use " + ChatColor.YELLOW + "/shard " + ChatColor.GRAY + "to manage your Shards.");
        im.setLore(lore);
        shard.setItemMeta(im);

        cube = new ItemStack(Material.QUARTZ_BLOCK);
        im = cube.getItemMeta();
        im.setDisplayName(ChatColor.YELLOW + "Shard Cube");
        lore = new ArrayList<String>();
        lore.add(ChatColor.GRAY + "A cube constructed from 64 Shards.");
        lore.add("");
        lore.add(ChatColor.GRAY + "Use " + ChatColor.YELLOW + "/shard " + ChatColor.GRAY + "to manage your Shards.");
        im.setLore(lore);
        cube.setItemMeta(im);

        purified = new ItemStack(Material.SEA_LANTERN);
        im = purified.getItemMeta();
        im.setDisplayName(ChatColor.YELLOW + "Purified Shard Cube");
        lore = new ArrayList<String>();
        lore.add(ChatColor.GRAY + "A Purified Cube made from 64 Shard Cubes.");
        lore.add("");
        lore.add(ChatColor.GRAY + "Use " + ChatColor.YELLOW + "/shard " + ChatColor.GRAY + "to manage your Shards.");
        im.setLore(lore);
        purified.setItemMeta(im);
    }

    /*
     * ItemStack checks
     */

    private static boolean isShard(ItemStack item) {
        return isShardType(item, shard);
    }

    private static boolean isCube(ItemStack item) {
        return isShardType(item, cube);
    }

    private static boolean isPurifiedCube(ItemStack item) {
        return isShardType(item, purified);
    }

    private static boolean isShardType(ItemStack item, ItemStack type) {
        if (item == null)
            return false;
        if (type == null)
            return false;
        if (item.getType() != type.getType())
            return false;
        if (!item.hasItemMeta())
            return false;
        ItemMeta im = item.getItemMeta();
        if (!im.hasDisplayName())
            return false;
        if (im.getDisplayName().equals(type.getItemMeta().getDisplayName())) {
            im.setLore(type.getItemMeta().getLore());
            item.setItemMeta(im);
            return true;
        }
        return false;
    }

    /*
     * Giving shards
     */

    public static void giveCurrency(Player p, int amount) {
        if (amount >= 64 * 64) {
            int numPurified = amount / (64 * 64);
            givePurified(p, numPurified);
            amount -= numPurified * 64 * 64;
        }
        if (amount <= 0)
            return;
        if (amount >= 64) {
            int numCubes = amount / 64;
            giveCubes(p, numCubes);
            amount -= numCubes * 64;
        }
        if (amount <= 0)
            return;
        giveShards(p, amount);
    }

    protected static void giveShards(Player p, int amount) {
        giveModularized(p, amount, shard, "shards");
    }

    protected static void giveCubes(Player p, int amount) {
        giveModularized(p, amount, cube, "cubes");
    }

    protected static void givePurified(Player p, int amount) {
        giveModularized(p, amount, purified, "purified cubes");
    }

    private static void giveModularized(Player p, int amount, ItemStack base, String name) {
        if (amount <= 0)
            return;
        ItemStack item = base.clone();
        item.setAmount(amount);
        HashMap<Integer, ItemStack> remaining = p.getInventory().addItem(item);
        if (!remaining.isEmpty()) {
            int extra = 0;
            for (Entry<Integer, ItemStack> e : remaining.entrySet()) {
                int remainingAmount = e.getValue().getAmount();
                extra += remainingAmount;
                while (remainingAmount > 0) {
                    ItemStack temp = base.clone();
                    if (remainingAmount > temp.getMaxStackSize())
                        temp.setAmount(temp.getMaxStackSize());
                    else
                        temp.setAmount(remainingAmount);
                    remainingAmount -= temp.getAmount();
                    p.getWorld().dropItem(p.getEyeLocation(), temp);
                }
            }
            p.sendMessage(ChatColor.RED + "You ran out of inventory space for " + name + " you received.");
            p.sendMessage(ChatColor.RED.toString() + extra + " extra " + name + " were dropped around you.");
        }
    }

    /*
     * Counting shards
     */

    public static int countCurrency(Player p) {
        Inventory i = p.getInventory();
        return countCurrency(i);
    }

    public static int countCurrency(Inventory i) {
        int count = 0;
        for (ItemStack item : i.getContents()) {
            if (isShard(item))
                count += item.getAmount();
            else if (isCube(item))
                count += item.getAmount() * 64;
            else if (isPurifiedCube(item))
                count += item.getAmount() * 64 * 64;
        }
        return count;
    }

    /*
     * Taking shards
     */

    public static int takeShards(Player p, int numberOfShards) {
        return takeModularized(p, numberOfShards, ShardManager::isShard);
    }

    public static int takeCubes(Player p, int numberOfCubes) {
        return takeModularized(p, numberOfCubes, ShardManager::isCube);
    }

    public static int takePurified(Player p, int numberOfPurified) {
        return takeModularized(p, numberOfPurified, ShardManager::isPurifiedCube);
    }

    private static int takeModularized(Player p, int num, Function<ItemStack, Boolean> f) {
        for (int k = 0; k < p.getInventory().getSize(); k++) {
            ItemStack item = p.getInventory().getItem(k);
            if (f.apply(item)) {
                if (item.getAmount() > num) {
                    item.setAmount(item.getAmount() - num);
                    p.getInventory().setItem(k, item);
                    num = 0;
                } else {
                    num -= item.getAmount();
                    item.setAmount(0);
                    p.getInventory().setItem(k, item);
                }
            }
            if (num <= 0)
                break;
        }
        return num;
    }

    public static boolean takeCurrency(Player p, int amount) {
        if (countCurrency(p) < amount) {
            p.sendMessage(ChatColor.RED + "You don't have enough shards!");
            return false;
        }
        amount = takeShards(p, amount);

        if (amount == 0)
            return true;

        int cubesToTake = 1 + (amount / 64); // round up
        int remCubes = takeCubes(p, cubesToTake);

        amount -= (cubesToTake - remCubes) * 64;

        if (amount > 0) {
            int purifiedToTake = 1 + (amount / (64 * 64));
            int remPurified = takePurified(p, purifiedToTake);
            amount -= (purifiedToTake - remPurified) * 64 * 64;
        }

        if (amount == 0)
            return true;

        if (amount < 0) {
            giveCurrency(p, -1 * amount);
            return true;
        } else {
            System.out.println("ERROR! Took currency but returned false!!!");
            return false;
        }
    }

    private static int countShardsOnly(Player p) {
        return countModularized(p, ShardManager::isShard);
    }

    private static int countCubesOnly(Player p) {
        return countModularized(p, ShardManager::isCube);
    }

    private static int countPurifiedOnly(Player p) {
        return countModularized(p, ShardManager::isPurifiedCube);
    }

    private static int countModularized(Player p, Function<ItemStack, Boolean> f) {
        Inventory i = p.getInventory();
        int count = 0;
        for (ItemStack item : i.getContents()) {
            if (f.apply(item))
                count += item.getAmount();
        }
        return count;
    }

}
