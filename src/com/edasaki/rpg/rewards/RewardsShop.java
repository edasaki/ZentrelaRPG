package com.edasaki.rpg.rewards;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.edasaki.core.menus.MenuManager;
import com.edasaki.core.utils.RUtils;
import com.edasaki.rpg.shops.ShopItem;

public class RewardsShop {

    private static String shopName = "Rewards Shop";
    private static ArrayList<ShopItem> shopItems = new ArrayList<ShopItem>();

    private static final Object[][] ITEMS = {
            { "mana_pot_1", 1 },
            { "mana_pot_2", 2 },
            { "mana_pot_3", 3 },
    };

    public static void initialize() {
        shopItems.clear();
        for (Object[] o : ITEMS)
            addItem(new ShopItem((String) o[0], (int) o[1], "reward points"));
    }

    public static void addItem(ShopItem si) {
        shopItems.add(si);
    }

    private static boolean postprocessed = false;
    private static int rows = 0;

    public static void postProcess() {
        postprocessed = true;
        rows = (shopItems.size() + 8) / 9; //round up int division = (num + divisor - 1) / divisor
        if (rows == 0)
            rows = 1;
    }

    public static void openMenu(final Player p, final int rewardPoints) {
        if (!postprocessed)
            postProcess();
        int row = 0;
        int col = 0;
        ArrayList<Object[]> display = new ArrayList<Object[]>();
        final String name = p.getName();
        for (final ShopItem si : shopItems) {
            if (col >= 9) {
                row++;
                col = 0;
            }
            ItemStack displayItem = si.display;
            display.add(new Object[] {
                    row,
                    col++,
                    displayItem,
                    displayItem.hasItemMeta() && displayItem.getItemMeta().hasDisplayName() ? displayItem.getItemMeta().getDisplayName() : displayItem.getType().toString(),
                    displayItem.hasItemMeta() && displayItem.getItemMeta().hasLore() ? displayItem.getItemMeta().getLore() : new Object[] {
                            null,
                            "",

                    }, new Runnable() {
                        public void run() {
                            if (p != null && p.isOnline()) {
                                if (rewardPoints >= si.cost) {
                                    if (RUtils.hasEmptySpaces(p, 1)) {
                                        p.closeInventory();
                                        p.getInventory().addItem(si.item);
                                        RewardsManager.takePoints(name, si.cost, null);
                                        p.sendMessage(ChatColor.GREEN + "You bought " + si.display.getItemMeta().getDisplayName() + ChatColor.GREEN + " for " + ChatColor.YELLOW + si.cost + " reward points" + ChatColor.GREEN + ".");
                                    } else {
                                        p.sendMessage(ChatColor.RED + "You don't have enough inventory space to buy anything!");
                                    }
                                } else {
                                    p.sendMessage(ChatColor.RED + "You don't have enough reward points to afford that!");
                                }
                            }
                        }
                    }
            });
        }
        Inventory inventory = MenuManager.createMenu(p, shopName + " - " + rewardPoints + " Point" + (rewardPoints != 1 ? "s" : ""), rows, display.toArray(new Object[display.size()][]));
        p.openInventory(inventory);
    }

}
