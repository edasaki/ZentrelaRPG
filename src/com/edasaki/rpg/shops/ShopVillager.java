package com.edasaki.rpg.shops;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.edasaki.core.menus.MenuManager;
import com.edasaki.core.utils.RUtils;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.economy.ShardManager;
import com.edasaki.rpg.npcs.NPCEntity;
import com.edasaki.rpg.npcs.NPCType;

public class ShopVillager extends NPCEntity {

    private String shopName = "Shop";
    private ArrayList<ShopItem> shopItems = new ArrayList<ShopItem>();

    private String dialogue;

    public ShopVillager(int id, String name, String shopName, String dialogue, double x, double y, double z, String world) {
        super(id, name, NPCType.VILLAGER, x, y, z, world);
        this.shopName = shopName;
        this.dialogue = dialogue;
    }

    public void addItem(ShopItem si) {
        shopItems.add(si);
    }

    private boolean postprocessed = false;
    private int rows = 0;

    public void postProcess() {
        postprocessed = true;
        rows = (shopItems.size() + 8) / 9; //round up int division = (num + divisor - 1) / divisor
        if (rows == 0)
            rows = 1;
    }

    @Override
    public void interact(final Player p, PlayerDataRPG pd) {
        if (!postprocessed)
            postProcess();
        this.say(p, dialogue);
        int row = 0;
        int col = 0;
        ArrayList<Object[]> display = new ArrayList<Object[]>();
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
                                if (ShardManager.countCurrency(p) >= si.cost) {
                                    if (RUtils.hasEmptySpaces(p, 1)) {
                                        p.getInventory().addItem(si.item);
                                        ShardManager.takeCurrency(p, si.cost);
                                        p.sendMessage(ChatColor.GREEN + "You bought " + si.display.getItemMeta().getDisplayName() + ChatColor.GREEN + " for " + ChatColor.YELLOW + si.cost + " shards" + ChatColor.GREEN + ".");
                                    } else {
                                        p.sendMessage(ChatColor.RED + "You don't have enough inventory space to buy anything!");
                                    }
                                } else {
                                    p.sendMessage(ChatColor.RED + "You don't have enough shards to afford that!");
                                }
                            }
                        }
                    }
            });
        }
        Inventory inventory = MenuManager.createMenu(p, shopName, rows, display.toArray(new Object[display.size()][]));
        p.openInventory(inventory);
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.GREEN;
    }

}
