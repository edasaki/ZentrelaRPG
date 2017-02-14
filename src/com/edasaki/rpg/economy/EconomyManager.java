package com.edasaki.rpg.economy;

import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.edasaki.core.menus.MenuGeneralRunnable;
import com.edasaki.core.menus.MenuManager;
import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.items.EquipType;
import com.edasaki.rpg.items.ItemBalance;

public class EconomyManager extends AbstractManagerRPG {

    private static final int RARITY_1 = 2;
    private static final int RARITY_2 = 4;
    private static final int RARITY_3 = 8;
    private static final int RARITY_4 = 12;
    private static final int RARITY_5 = 16;
    private static final int RARITY_6 = 20;

    public EconomyManager(SakiRPG plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {

    }

    public static void openShardMenu(final Player p, final PlayerDataRPG pd) {
        p.closeInventory();
        Inventory menu = MenuManager.createMenu(p, "Shard Management", 1, new Object[][] {
                {
                        0,
                        0,
                        Material.ANVIL,
                        ChatColor.AQUA + "Salvage Equipment",
                        new Object[] {
                                ChatColor.DARK_AQUA,
                                "Turn extra equipment into Shards!",
                                null,
                                "",
                                ChatColor.YELLOW,
                                "1 Level " + ChatColor.GRAY + "=" + ChatColor.GOLD + " 1 Shard",
                                null,
                                "",
                                ChatColor.YELLOW,
                                "Higher rarity items reward more shards.",
                                null,
                                "",
                                ChatColor.RED,
                                "Salvaged items are destroyed forever and cannot be retrieved! Only salvage things you're sure you don't need!"

                        },
                        new Runnable() {
                            public void run() {
                                openSalvageMenu(p, pd);
                            }
                        }
                },
                {
                        0,
                        2,
                        Material.HOPPER,
                        ChatColor.WHITE + "Condense Shards in Inventory",
                        new Object[] {
                                ChatColor.GREEN,
                                "Click to convert all shards in your inventory to their most condensed states.",
                                null,
                                "",
                                ChatColor.AQUA,
                                "For example, if you have 120 Shards you'll end up with 1 Shard Cube and 56 Shards."
                        },
                        new Runnable() {
                            public void run() {
                                int amt = ShardManager.countCurrency(p);
                                if (ShardManager.takeCurrency(p, amt))
                                    ShardManager.giveCurrency(p, amt);
                            }
                        }
                },
                {
                        0,
                        3,
                        Material.QUARTZ,
                        ChatColor.WHITE + "Convert " + ChatColor.YELLOW + "1 Shard Cube",
                        new Object[] {
                                ChatColor.GREEN,
                                "Click to convert 1 Shard Cube into a stack of 64 Shards.",
                                null,
                                "",
                                ChatColor.RED,
                                "WARNING: If you don't have enough inventory space, the converted item will dropped."
                        },
                        new Runnable() {
                            public void run() {
                                if (p.getInventory().firstEmpty() != -1) {
                                    if (ShardManager.takeCubes(p, 1) == 0) {
                                        ShardManager.giveShards(p, 64);
                                        p.sendMessage(ChatColor.GREEN + "You received 64 Shards in exchange for 1 Shard Cube.");
                                    } else {
                                        p.sendMessage(ChatColor.RED + "You don't have any Shard Cubes!");
                                    }
                                } else {
                                    p.sendMessage(ChatColor.RED + "You must have 1 open inventory slot when converting shards!");
                                }
                            }
                        }
                },
                {
                        0,
                        4,
                        Material.QUARTZ_BLOCK,
                        ChatColor.WHITE + "Convert " + ChatColor.YELLOW + "1 Purified Cube",
                        new Object[] {
                                ChatColor.GREEN,
                                "Click to convert 1 Purified Shard Cube into a stack of 64 Shard Cubes.",
                                null,
                                "",
                                ChatColor.RED,
                                "WARNING: If you don't have enough inventory space, the converted item will dropped."
                        },
                        new Runnable() {
                            public void run() {
                                if (p.getInventory().firstEmpty() != -1) {
                                    if (ShardManager.takePurified(p, 1) == 0) {
                                        ShardManager.giveCubes(p, 64);
                                        p.sendMessage(ChatColor.GREEN + "You received 64 Shard Cubes in exchange for 1 Purified Shard Cube.");
                                    } else {
                                        p.sendMessage(ChatColor.RED + "You don't have any Purified Shard Cubes!");
                                    }
                                } else {
                                    p.sendMessage(ChatColor.RED + "You must have 1 open inventory slot when converting shards!");
                                }
                            }
                        }
                },

                {
                        0,
                        6,
                        Material.NETHER_STAR,
                        ChatColor.GOLD + "Refine Shards",
                        new Object[] {
                                ChatColor.GRAY,
                                "Refine " + ChatColor.YELLOW + "Shards" + ChatColor.GRAY + " into " + ChatColor.YELLOW + "Crystals.",
                                null,
                                "",
                                ChatColor.LIGHT_PURPLE,
                                "Crystals are used for upgrading your equipment.",
                                null,
                                "",
                                ChatColor.RED,
                                "Coming soon!"
                        //                                ChatColor.DARK_PURPLE,
                        //                                "Use " + ChatColor.YELLOW + "/enchant" + ChatColor.DARK_PURPLE + " to "
                        },
                        new Runnable() {
                            public void run() {
                                p.sendMessage(ChatColor.RED + "Coming soon(tm)!");
                            }
                        }
                },
                {
                        0,
                        8,
                        Material.CHEST,
                        ChatColor.GOLD + "Open Bank",
                        new Object[] {
                                ChatColor.GRAY,
                                "Access your bank.",
                                null,
                                "",
                                ChatColor.LIGHT_PURPLE,
                                "Banks can only be used in Danger Level 1 regions.",
                        },
                        new Runnable() {
                            public void run() {
                                pd.openBank();
                            }
                        }
                },
        });
        p.openInventory(menu);
    }

    public static void openSalvageMenu(final Player p, final PlayerDataRPG pd) {
        p.closeInventory();
        final Inventory menu = MenuManager.createMenu(p, "Salvage Equipment", 6, new Object[][] {
                {
                        0,
                        0,
                        Material.ANVIL,
                        ChatColor.AQUA + "Salvaging Guide",
                        new Object[] {
                                ChatColor.YELLOW,
                                "Click an equip in your inventory to add it to the Salvage List.",
                                null,
                                "",
                                ChatColor.GREEN,
                                "When you are ready to salvage the equips displayed, click the Shard at the top-left of this menu.",
                                null,
                                "",
                                ChatColor.RED,
                                "Salvaged items are destroyed FOREVER, so make sure you are salvaging the right items!",
                                null,
                                "",
                                ChatColor.GRAY,
                                "Only equipment (armor and weapons) can be salvaged.",
                                null,
                                "",
                                ChatColor.YELLOW,
                                "1 Level " + ChatColor.GRAY + "=" + ChatColor.GOLD + " 1 Shard",
                                null,
                                "",
                                ChatColor.YELLOW,
                                "Higher rarity items reward more shards.",

                        },
                        new Runnable() {
                            public void run() {
                            }
                        }
                },
                {
                        0,
                        7,
                        Material.BARRIER,
                        ChatColor.RED + "Clear Salvage List",
                        new Object[] {
                                ChatColor.YELLOW,
                                "Clear the list of items you want to salvage."

                        },
                        new Runnable() {

                            public void run() {
                                openSalvageMenu(p, pd);
                            }
                        }
                },
                {
                        0,
                        8,
                        Material.QUARTZ,
                        ChatColor.RED + "Click to Salvage!",
                        new Object[] {
                                ChatColor.GOLD,
                                "Current Value: 0 Shards",
                                null,
                                "",
                                ChatColor.RED,
                                "Salvaged items are DESTROYED forever! There is no way to get them back!",
                                null,
                                "",
                                ChatColor.YELLOW,
                                "Make sure you know which items you are destroying!"

                        },
                        new Runnable() {
                            public void run() {
                            }
                        }
                },
        });
        p.openInventory(menu);
        MenuManager.addMenuGeneralClick(p, new MenuGeneralRunnable<PlayerDataRPG>() {

            public HashSet<Integer> usedSlots = new HashSet<Integer>();
            public int totalWorth = 0;

            @Override
            public void execute(final Player p, PlayerDataRPG pd, ItemStack item, int slot) {
                if (pd.currentModifiableInventory != null && item.hasItemMeta()) {
                    Inventory i = pd.currentModifiableInventory;
                    ItemMeta im = item.getItemMeta();
                    if (i.firstEmpty() == -1) {
                        p.sendMessage(ChatColor.RED + "You cannot salvage any more items.");
                        return;
                    }
                    // only salvage equips
                    if (!(EquipType.isArmor(item) || EquipType.isWeapon(item))) {
                        p.sendMessage(ChatColor.RED + "This item cannot be salvaged.");
                        return;
                    }
                    if (im.hasDisplayName() && im.hasLore()) {
                        int level = -1;
                        for (String s : im.getLore()) {
                            if (s.contains("Level")) {
                                s = ChatColor.stripColor(s).replaceAll("[^0-9]", "");
                                level = Integer.parseInt(s);
                                break;
                            }
                        }
                        if (level > 0) {
                            if (usedSlots.contains(slot)) {
                                p.sendMessage(ChatColor.RED + "This item is already on the salvage list.");
                                return;
                            }
                            usedSlots.add(slot);
                            int rarity = 0;
                            String dispName = im.getDisplayName();
                            for (int k = 0; k < ItemBalance.RARITY_NAMES.length; k++) {
                                String s = ItemBalance.RARITY_NAMES[k];
                                if (dispName.contains(s)) {
                                    rarity = k;
                                }
                            }
                            int worth = level > 200 ? 200 : level;
                            switch (rarity) {
                                default:
                                case 0:
                                    break;
                                case 1:
                                    worth *= RARITY_1;
                                    break;
                                case 2:
                                    worth *= RARITY_2;
                                    break;
                                case 3:
                                    worth *= RARITY_3;
                                    break;
                                case 4:
                                    worth *= RARITY_4;
                                    break;
                                case 5:
                                    worth *= RARITY_5;
                                    break;
                                case 6:
                                case 7: // just in case lol, 6 should be godlike
                                    worth *= RARITY_6;
                                    break;
                            }
                            totalWorth += worth;
                            final int fWorth = worth;

                            ItemStack newItem = new ItemStack(item.getType());
                            ItemMeta newMeta = newItem.getItemMeta();
                            newItem.setData(item.getData());
                            newItem.setDurability(item.getDurability());
                            newMeta.setDisplayName(im.getDisplayName());
                            newMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                            newMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            newMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                            newItem.setItemMeta(newMeta);
                            int currCol = -1;
                            int currRow = -1;
                            int emptySlot = i.firstEmpty();
                            currRow = emptySlot / 9;
                            currCol = emptySlot % 9;
                            final int fCol = currCol;
                            final int fRow = currRow;
                            MenuManager.modifyMenu(p, i, new Object[][] {
                                    {
                                            currRow,
                                            currCol,
                                            newItem,
                                            im.getDisplayName(),
                                            new Object[] {
                                                    ChatColor.GRAY,
                                                    "Level " + ChatColor.YELLOW + level + ChatColor.GRAY + " equip worth " + ChatColor.GOLD + worth + ChatColor.GRAY + " Shard" + (worth == 1 ? "" : "s") + ".",
                                                    null,
                                                    "",
                                                    ChatColor.GRAY,
                                                    "Click to remove.",

                                            }, new Runnable() {
                                                public void run() {
                                                    MenuManager.modifyMenu(p, i, new Object[][] {
                                                            {
                                                                    fRow,
                                                                    fCol,
                                                                    null,
                                                                    im.getDisplayName(),
                                                                    new Object[] {
                                                                            ChatColor.GRAY,
                                                                            "removed. error code 111: non-removed menu item",

                                                                    }, new Runnable() {
                                                                        public void run() {
                                                                        }
                                                                    }
                                                            }
                                                    });
                                                    usedSlots.remove(slot);
                                                    totalWorth -= fWorth;
                                                    MenuManager.modifyMenu(p, i, new Object[][] {
                                                            {
                                                                    0,
                                                                    8,
                                                                    Material.QUARTZ,
                                                                    ChatColor.RED + "Click to Salvage!",
                                                                    new Object[] {
                                                                            ChatColor.GOLD,
                                                                            "Current Value: " + (totalWorth) + " Shards",
                                                                            null,
                                                                            "",
                                                                            ChatColor.RED,
                                                                            "Salvaged items are DESTROYED forever! There is no way to get them back!",
                                                                            null,
                                                                            "",
                                                                            ChatColor.YELLOW,
                                                                            "Make sure you know which items you are destroying!"

                                                                    }, new Runnable() {

                                                                        public void run() {
                                                                            p.closeInventory();
                                                                            for (int i : usedSlots) {
                                                                                p.getInventory().clear(i);
                                                                            }
                                                                            ShardManager.giveCurrency(p, totalWorth);
                                                                            p.sendMessage(ChatColor.GREEN + "You received " + ChatColor.YELLOW + totalWorth + ChatColor.GREEN + " Shards!");
                                                                            usedSlots.clear();
                                                                            totalWorth = 0;
                                                                        }
                                                                    } }, });
                                                }
                                            }
                                    },
                            });

                            MenuManager.modifyMenu(p, i, new Object[][] {
                                    {
                                            0,
                                            8,
                                            Material.QUARTZ,
                                            ChatColor.RED + "Click to Salvage!",
                                            new Object[] {
                                                    ChatColor.GOLD,
                                                    "Current Value: " + (totalWorth) + " Shards",
                                                    null,
                                                    "",
                                                    ChatColor.RED,
                                                    "Salvaged items are DESTROYED forever! There is no way to get them back!",
                                                    null,
                                                    "",
                                                    ChatColor.YELLOW,
                                                    "Make sure you know which items you are destroying!"

                                            }, new Runnable() {

                                                public void run() {
                                                    p.closeInventory();
                                                    for (int i : usedSlots) {
                                                        p.getInventory().clear(i);
                                                    }
                                                    ShardManager.giveCurrency(p, totalWorth);
                                                    p.sendMessage(ChatColor.GREEN + "You received " + ChatColor.YELLOW + totalWorth + ChatColor.GREEN + " Shards!");
                                                    usedSlots.clear();
                                                    totalWorth = 0;
                                                }
                                            } }, });
                            p.sendMessage(ChatColor.GRAY + ">> " + im.getDisplayName() + ChatColor.YELLOW + " added to salvage list.");
                        } else {
                            p.sendMessage(ChatColor.RED + "This item cannot be salvaged.");
                        }
                    }
                }
            }
        });
        pd.currentModifiableInventory = menu; // must be set after inv opens b/c of null on invclose
    }

}
