package com.edasaki.rpg.buycraft;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.edasaki.core.badges.Badge;
import com.edasaki.core.players.Rank;
import com.edasaki.core.utils.RMessages;
import com.edasaki.core.utils.RUtils;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.items.ItemManager;
import com.edasaki.rpg.items.RPGItem;

public enum BuycraftReward {

    KNIGHT_RANK("Knight Rank", true, new BuycraftExecutor() {
        @Override
        public boolean execute(Player p, PlayerDataRPG pd) {
            pd.setRank(Rank.KNIGHT);
            return true;
        }
    }),
    LORD_RANK("Lord Rank", true, new BuycraftExecutor() {
        @Override
        public boolean execute(Player p, PlayerDataRPG pd) {
            pd.setRank(Rank.LORD);
            return true;
        }
    }),
    PRINCE_RANK("Prince Rank", true, new BuycraftExecutor() {
        @Override
        public boolean execute(Player p, PlayerDataRPG pd) {
            pd.setRank(Rank.PRINCE);
            return true;
        }
    }),
    PRINCESS_RANK("Princess Rank", true, new BuycraftExecutor() {
        @Override
        public boolean execute(Player p, PlayerDataRPG pd) {
            pd.setRank(Rank.PRINCESS);
            return true;
        }
    }),
    ITEM_THING("Item Thing", false, new BuycraftExecutor() {
        @Override
        public boolean execute(Player p, PlayerDataRPG pd) {
            HashMap<RPGItem, Integer> giveItems = new HashMap<RPGItem, Integer>();
            giveItems.put(ItemManager.itemIdentifierToRPGItemMap.get("hp_potion_1"), 10);
            return give(p, pd, giveItems);
        }
    }),
    SUPPORTER_BADGE("Supporter Badge", true, new BuycraftExecutor() {
        @Override
        public boolean execute(Player p, PlayerDataRPG pd) {
            pd.badges.add(Badge.SUPPORTER);
            return true;
        }
    });

    ;

    // This is used for writing executes b/c something is fked up in Eclipse's content assist in anonymous classes in enums
    @SuppressWarnings("unused")
    private void testMethod(Player p, PlayerDataRPG pd) {
        HashMap<RPGItem, Integer> giveItems = new HashMap<RPGItem, Integer>();
        giveItems.put(ItemManager.itemIdentifierToRPGItemMap.get("hp_potion_1"), 10);
    }

    private static boolean give(Player p, PlayerDataRPG pd, HashMap<RPGItem, Integer> giveItems) {
        boolean hasSpace = true;
        if (!RUtils.hasEmptySpaces(p, giveItems.size())) {
            p.sendMessage("");
            p.sendMessage(ChatColor.GRAY + ">> " + ChatColor.RED + "You don't have enough inventory space for your purchase!");
            p.sendMessage(ChatColor.GRAY + ">> " + ChatColor.RED + "You need " + ChatColor.YELLOW + giveItems.size() + ChatColor.RED + " empty slots.");
            p.sendMessage(ChatColor.GRAY + ">> " + ChatColor.RED + "Please clear out some inventory space and relog.");
            hasSpace = false;
        }
        if (hasSpace) {
            for (Entry<RPGItem, Integer> e : giveItems.entrySet()) {
                ItemStack item = e.getKey().generate();
                item.setAmount(e.getValue());
                p.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "You received " + e.getKey().name + ChatColor.WHITE + ".");
                p.getInventory().addItem(item);
            }
            return true;
        } else {
            return false;
        }
    }

    private String name;
    private boolean canGiveInLobby;
    private BuycraftExecutor executor;

    BuycraftReward(String name, boolean canGiveInLobby, BuycraftExecutor executor) {
        this.name = name;
        this.canGiveInLobby = canGiveInLobby;
        this.executor = executor;
    }

    public boolean getCanGiveInLobby() {
        return canGiveInLobby;
    }

    public String getName() {
        return name;
    }

    public boolean execute(Player p, PlayerDataRPG pd) {
        boolean ret = executor.execute(p, pd);
        if (ret) {
            RMessages.announce(ChatColor.GRAY + "----------------------------------------------------");
            RMessages.announce(ChatColor.GOLD + "            " + ChatColor.BOLD + p.getName() + ChatColor.GRAY + " just bought " + ChatColor.GREEN + ChatColor.BOLD + this.name + ChatColor.GRAY + "!");
            RMessages.announce(ChatColor.GRAY + "            Thanks for your support, " + ChatColor.GOLD + ChatColor.BOLD + p.getName() + ChatColor.GRAY + "! " + ChatColor.RED + "\u2764");
            RMessages.announce(ChatColor.GRAY + "----------------------------------------------------");
            System.out.println("Distributed reward to " + p.getName());
        }
        return ret;
    }
}
