package com.edasaki.rpg.trades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.edasaki.core.chat.ChatFilter;
import com.edasaki.core.menus.MenuGeneralRunnable;
import com.edasaki.core.menus.MenuManager;
import com.edasaki.core.options.SakiOption;
import com.edasaki.core.utils.RSound;
import com.edasaki.core.utils.fanciful.FancyMessage;
import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.chat.ChatManager;
import com.edasaki.rpg.items.ItemManager;

public class TradeManager extends AbstractManagerRPG {

    public static final int TRADE_RANGE = 25;

    public static HashMap<String, String> lastSentRequest = new HashMap<String, String>();
    public static HashMap<UUID, UUID> currentTrade = new HashMap<UUID, UUID>();

    public TradeManager(SakiRPG plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {

    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (currentTrade.containsKey(uuid)) {
            UUID other = currentTrade.remove(uuid);
            Player otherP = plugin.getServer().getPlayer(other);
            if (otherP != null && otherP.isOnline() && otherP.isValid()) {
                if (currentTrade.containsKey(other))
                    currentTrade.remove(other);
                event.getPlayer().sendMessage(ChatColor.RED + "The trade with " + otherP.getName() + " was canceled.");
                otherP.sendMessage(ChatColor.RED + "The trade with " + event.getPlayer().getName() + " was canceled.");
                otherP.closeInventory();
            } else {
                event.getPlayer().sendMessage(ChatColor.RED + "The trade was canceled.");
            }
        }
    }

    public static void offerTrade(final Player aPlayer, PlayerDataRPG pd1, final Player bPlayer, PlayerDataRPG pd2, String message) {
        // check to see if the receiver already offered to trade
        if (lastSentRequest.containsKey(bPlayer.getName()) && lastSentRequest.get(bPlayer.getName()).equals(aPlayer.getName())) {
            final String aName = aPlayer.getName();
            final String bName = bPlayer.getName();

            final ArrayList<ItemStack> aItems = new ArrayList<ItemStack>();
            final ArrayList<ItemStack> bItems = new ArrayList<ItemStack>();

            final HashSet<Integer> aSlots = new HashSet<Integer>();
            final HashSet<Integer> bSlots = new HashSet<Integer>();

            final TradeUpdater updater = new TradeUpdater();

            updater.aUUID = aPlayer.getUniqueId();
            updater.bUUID = bPlayer.getUniqueId();

            updater.aSlots = aSlots;
            updater.bSlots = bSlots;

            final Inventory aInv = MenuManager.createMenu(aPlayer, "Trading with " + bPlayer.getName(), 6, build(aItems, bItems, bName, updater, true));
            aPlayer.closeInventory();
            aPlayer.openInventory(aInv);

            final Inventory bInv = MenuManager.createMenu(bPlayer, "Trading with " + aPlayer.getName(), 6, build(bItems, aItems, aName, updater, false));
            bPlayer.closeInventory();
            bPlayer.openInventory(bInv);

            updater.set(new Runnable() {
                public void run() {
                    MenuManager.modifyMenu(aPlayer, aInv, build(aItems, bItems, bName, updater, true));
                    MenuManager.modifyMenu(bPlayer, bInv, build(bItems, aItems, aName, updater, false));
                }
            });

            MenuManager.addMenuGeneralClick(aPlayer, buildClicker(aItems, updater, true));
            MenuManager.addMenuGeneralClick(bPlayer, buildClicker(bItems, updater, false));

            lastSentRequest.remove(aPlayer.getName());
            lastSentRequest.remove(bPlayer.getName());
            aPlayer.sendMessage("");
            bPlayer.sendMessage("");
            aPlayer.sendMessage(ChatColor.GRAY + ">> " + ChatColor.GREEN + "The trade window with " + bPlayer.getName() + " has opened.");
            bPlayer.sendMessage(ChatColor.GRAY + ">> " + ChatColor.GREEN + "The trade window with " + aPlayer.getName() + " has opened.");
            currentTrade.put(aPlayer.getUniqueId(), bPlayer.getUniqueId());
            currentTrade.put(bPlayer.getUniqueId(), aPlayer.getUniqueId());
        } else {
            aPlayer.sendMessage("");
            bPlayer.sendMessage("");
            RSound.playSound(bPlayer, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
            FancyMessage fm = new FancyMessage();
            fm.text(">> ");
            fm.color(ChatColor.GRAY);
            fm.then("You received a trade request from ");
            fm.color(ChatColor.GREEN);
            fm.then(aPlayer.getName());
            ChatManager.chatHover(fm, pd1);
            fm.color(ChatColor.AQUA);
            fm.then("!");
            fm.color(ChatColor.GREEN);
            fm.send(bPlayer);
            //            receiver.sendMessage(ChatColor.GRAY + ">> " + ChatColor.GREEN + "You received a trade request from " + ChatColor.AQUA + offerer.getName() + ChatColor.GREEN + "!");
            fm = new FancyMessage();
            fm.text(">> ");
            fm.color(ChatColor.GRAY);
            fm.then("Type ");
            fm.color(ChatColor.GREEN);
            fm.then("/trade " + aPlayer.getName());
            fm.color(ChatColor.YELLOW);
            fm.command("/trade " + aPlayer.getName());
            fm.then(" or ");
            fm.color(ChatColor.GREEN);
            fm.then("Click Here");
            fm.color(ChatColor.YELLOW);
            fm.command("/trade " + aPlayer.getName());
            fm.then(" to open the trade window.");
            fm.color(ChatColor.GREEN);
            fm.send(bPlayer);
            if (message.length() > 0)
                bPlayer.sendMessage(ChatColor.GRAY + ">> " + ChatColor.GREEN + "Trade Message: " + ChatColor.AQUA + (pd2.getOption(SakiOption.CHAT_FILTER) ? ChatFilter.getFiltered(message) : message));
            else
                bPlayer.sendMessage(ChatColor.GRAY + ">> " + ChatColor.GREEN + "Trade Message: " + ChatColor.AQUA + "Hi, " + bPlayer.getName() + "! Would you like to trade?");

            fm = new FancyMessage();
            fm.text(">> ");
            fm.color(ChatColor.GRAY);
            fm.then("You sent a trade request to ");
            fm.color(ChatColor.GREEN);
            fm.then(bPlayer.getName());
            ChatManager.chatHover(fm, pd2);
            fm.color(ChatColor.AQUA);
            fm.then(".");
            fm.color(ChatColor.GREEN);
            fm.send(aPlayer);
            lastSentRequest.put(aPlayer.getName(), bPlayer.getName());
        }
    }

    private static MenuGeneralRunnable<?> buildClicker(final ArrayList<ItemStack> selfItems, final TradeUpdater updater, final boolean isA) {
        return new MenuGeneralRunnable<PlayerDataRPG>() {
            @Override
            public void execute(Player p, PlayerDataRPG pd, ItemStack item, int slot) {
                if (plugin.getInstance(ItemManager.class).isSoulbound(item)) {
                    p.sendMessage(ChatColor.RED + "You cannot trade Soulbound items!");
                    return;
                }
                if (isA ? updater.aSlots.contains(slot) : updater.bSlots.contains(slot)) {
                    p.sendMessage(ChatColor.RED + "You have already offered this item.");
                    return;
                }
                if (isA)
                    updater.aSlots.add(slot);
                else
                    updater.bSlots.add(slot);
                ItemStack displayItem = item.clone();
                displayItem.setDurability(item.getDurability());
                displayItem.setData(item.getData());
                ArrayList<String> lore = new ArrayList<String>();
                if (item.hasItemMeta() && item.getItemMeta().hasLore())
                    lore.addAll(item.getItemMeta().getLore());
                ItemMeta im = displayItem.getItemMeta();
                lore.add("");
                lore.add(ChatColor.YELLOW + "Offered by: " + ChatColor.GREEN + p.getName());
                im.setLore(lore);
                displayItem.setItemMeta(im);
                selfItems.add(displayItem);
                updater.resetStates();
                updater.update();
            }
        };
    }

    private static final int[][] SELF_SLOTS = {
            { 0, 0 },
            { 0, 1 },
            { 0, 2 },
            { 0, 3 },
            { 1, 0 },
            { 1, 1 },
            { 1, 2 },
            { 1, 3 },
            { 2, 0 },
            { 2, 1 },
            { 2, 2 },
            { 2, 3 },
            { 3, 0 },
            { 3, 1 },
            { 3, 2 },
            { 3, 3 },
            { 4, 0 },
            { 4, 1 },
            { 4, 2 },
            { 4, 3 },
            { 5, 0 },
            { 5, 1 },
            { 5, 2 },
            { 5, 3 },
    };

    private static final int[][] OTHER_SLOTS = {
            { 0, 5 },
            { 0, 6 },
            { 0, 7 },
            { 0, 8 },
            { 1, 5 },
            { 1, 6 },
            { 1, 7 },
            { 1, 8 },
            { 2, 5 },
            { 2, 6 },
            { 2, 7 },
            { 2, 8 },
            { 3, 5 },
            { 3, 6 },
            { 3, 7 },
            { 3, 8 },
            { 4, 5 },
            { 4, 6 },
            { 4, 7 },
            { 4, 8 },
            { 5, 5 },
            { 5, 6 },
            { 5, 7 },
            { 5, 8 },
    };

    private static final int[] BAR_ROWS = { 1, 2, 4 };

    private static Object[][] build(final ArrayList<ItemStack> selfItems, ArrayList<ItemStack> otherItems, String oName, final TradeUpdater updater, final boolean isA) {

        ArrayList<Object[]> display = new ArrayList<Object[]>();
        display.add(new Object[] {
                0,
                4,
                Material.BARRIER,
                ChatColor.RED + "Clear Offer",
                new Object[] {
                        ChatColor.YELLOW,
                        "Remove all items from your trade offer.",
                        null,
                        "",
                        ChatColor.GREEN,
                        "This will set both player's trade status to Not Ready.",

                }, new Runnable() {
                    public void run() {
                        if (isA ? updater.aSlots != null : updater.bSlots != null) {
                            if (isA)
                                updater.aSlots.clear();
                            else
                                updater.bSlots.clear();
                            selfItems.clear();
                            updater.resetStates();
                            updater.update();
                        }
                    }
                }
        });
        if (isA ? updater.bReady : updater.aReady) {
            display.add(new Object[] {
                    3,
                    4,
                    new ItemStack(Material.WOOL, 1, DyeColor.LIME.getWoolData()),
                    ChatColor.YELLOW + oName + ": " + ChatColor.GREEN + "Ready to Trade!",
                    new Object[] {
                            ChatColor.YELLOW,
                            oName + " has accepted the current offer.",
                            null,
                            "",
                            ChatColor.GRAY,
                            "To accept the trade, click your own ready button.",
                            null,
                            "",
                            ChatColor.RED,
                            "Please check to make sure you're getting what you want before you accept!"
                    },
                    new Runnable() {
                        public void run() {
                        }
                    }
            });
        } else {
            display.add(new Object[] {
                    3,
                    4,
                    new ItemStack(Material.WOOL, 1, DyeColor.RED.getWoolData()),
                    ChatColor.YELLOW + oName + ": " + ChatColor.RED + "Not Ready",
                    new Object[] {
                            ChatColor.YELLOW,
                            oName + " is not ready to trade.",

                    }, new Runnable() {
                        public void run() {
                        }
                    }
            });
        }
        if (isA ? updater.aReady : updater.bReady) {
            display.add(new Object[] {
                    5,
                    4,
                    new ItemStack(Material.WOOL, 1, DyeColor.LIME.getWoolData()),
                    ChatColor.YELLOW + "You: " + ChatColor.GREEN + "Ready to Trade!",
                    new Object[] {
                            ChatColor.YELLOW,
                            "You are ready to trade!",
                            null,
                            "",
                            ChatColor.GRAY,
                            "Click here to change your status to \"Not Ready\".",
                            null,
                            "",
                            ChatColor.RED,
                            "If the offer is changed, your status will be set to \"Not Ready\".",
                            null,
                            "",
                            ChatColor.YELLOW,
                            "The trade will be completed when both players are \"Ready\"."
                    },
                    new Runnable() {
                        public void run() {
                            if (isA)
                                updater.aReady = false;
                            else
                                updater.bReady = false;
                            updater.update();
                        }
                    }
            });
        } else {
            display.add(new Object[] {
                    5,
                    4,
                    new ItemStack(Material.WOOL, 1, DyeColor.RED.getWoolData()),
                    ChatColor.YELLOW + "You: " + ChatColor.RED + "Not Ready",
                    new Object[] {
                            ChatColor.YELLOW,
                            "You are not ready to trade.",
                            null,
                            "",
                            ChatColor.GRAY,
                            "Click here to accept the current trade offer.",
                            null,
                            "",
                            ChatColor.RED,
                            "If the offer is changed, your status will be set to \"Not Ready\".",
                            null,
                            "",
                            ChatColor.YELLOW,
                            "The trade will be completed when both players are \"Ready\"."
                    },
                    new Runnable() {
                        public void run() {
                            if (isA)
                                updater.aReady = true;
                            else
                                updater.bReady = true;
                            updater.update();
                            if (updater.aReady && updater.bReady)
                                updater.tryComplete();
                        }
                    }
            });
        }
        for (int row : BAR_ROWS) {
            display.add(new Object[] {
                    row,
                    4,
                    Material.IRON_FENCE,
                    ChatColor.RESET + "",
                    new Object[] {
                            ChatColor.YELLOW,
                            "<-- ",
                            ChatColor.YELLOW,
                            "Your Offer",
                            null,
                            "",
                            ChatColor.GREEN,
                            "-->",
                            ChatColor.GREEN,
                            oName + "'s Offer",
                            null,
                            "",

                    }, new Runnable() {
                        public void run() {
                        }
                    }
            });
        }

        for (int k = 0; k < SELF_SLOTS.length; k++) {
            int[] coord = SELF_SLOTS[k];
            if (k < selfItems.size()) {
                ItemStack item = selfItems.get(k);
                display.add(new Object[] {
                        coord[0],
                        coord[1],
                        item,
                        item.hasItemMeta() && item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : "",
                        item.hasItemMeta() && item.getItemMeta().hasLore() ? item.getItemMeta().getLore() : new Object[] {
                                null,
                                "",

                        }, new Runnable() {
                            public void run() {
                            }
                        }
                });
            } else {
                display.add(new Object[] { coord[0], coord[1], null });
            }
        }

        for (int k = 0; k < OTHER_SLOTS.length; k++) {
            int[] coord = OTHER_SLOTS[k];
            if (k < otherItems.size()) {
                ItemStack item = otherItems.get(k);
                display.add(new Object[] {
                        coord[0],
                        coord[1],
                        item,
                        item.hasItemMeta() && item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : "",
                        item.hasItemMeta() && item.getItemMeta().hasLore() ? item.getItemMeta().getLore() : new Object[] {
                                null,
                                "",

                        }, new Runnable() {
                            public void run() {
                            }
                        }
                });
            } else {
                display.add(new Object[] { coord[0], coord[1], null });
            }
        }
        return display.toArray(new Object[display.size()][]);
    }

}