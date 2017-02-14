package com.edasaki.rpg.help;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.edasaki.core.menus.MenuItem;
import com.edasaki.core.menus.MenuManager;
import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.items.ItemBalance;

public class HelpManager extends AbstractManagerRPG {

    private static ArrayList<MenuItem> list = new ArrayList<MenuItem>();

    public HelpManager(SakiRPG plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {
        ItemStack aboutItem = new ItemStack(Material.SNOW_BALL);
        aboutItem.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        list.add(new MenuItem(5, 0, new ItemStack(Material.BOOK), ChatColor.AQUA + "Chat Hovers", new String[] {
                ChatColor.GRAY + "Hover over people's names in chat to see more info about them.",
                ChatColor.GRAY + "Click on player's names for information on their equipment and more!"
        }, null));
        list.add(new MenuItem(5, 2, new ItemStack(Material.BOOK), ChatColor.AQUA + "Item Rarities", new String[] {
                ChatColor.GRAY + "The following is the order of Item Rarities in Zentrela (from most common to most rare).",
                "",
                ItemBalance.getRarityColor(0) + "Common",
                ItemBalance.getRarityColor(1) + ItemBalance.getRarityName(1),
                ItemBalance.getRarityColor(2) + ItemBalance.getRarityName(2),
                ItemBalance.getRarityColor(3) + ItemBalance.getRarityName(3),
                ItemBalance.getRarityColor(4) + ItemBalance.getRarityName(4),
                ItemBalance.getRarityColor(5) + ItemBalance.getRarityName(5),
                ItemBalance.getRarityColor(6) + ItemBalance.getRarityName(6),
        }, null));
        list.add(new MenuItem(5, 4, new ItemStack(Material.BOOK), ChatColor.AQUA + "Equipment Tiers", new String[] {
                ChatColor.GRAY + "There are 5 tiers of equipment. Stronger monsters can drop higher tiered equipment.",
                "",
                ChatColor.GRAY + "Tier 1 - Recruit",
                ChatColor.WHITE + "Tier 2 - Novice",
                ChatColor.GREEN + "Tier 3 - Apprentice",
                ChatColor.GOLD + "Tier 4 - Veteran",
                ChatColor.AQUA + "Tier 5 - Expert"
        }, null));
        list.add(new MenuItem(5, 6, new ItemStack(Material.BOOK), ChatColor.AQUA + "Spellcasting", new String[] {
                ChatColor.GRAY + "Spellcasting is a super important part of enjoying Zentrela, so we talk about it a lot!",
                "",
                ChatColor.GRAY + "There are 4 \"spellcasts\" which are used to actually cast your spells. These are RLL, RLR, RRL, and RRR.",
                "",
                ChatColor.WHITE + "To use a spellcast, just hold a weapon and click Right and Left on your mouse.",
                "",
                ChatColor.GRAY + "If you need more help, just type " + ChatColor.YELLOW + "!spells " + ChatColor.GRAY + "in chat for a useful video tutorial!",
        }, null));
        list.add(new MenuItem(5, 8, new ItemStack(Material.BOOK), ChatColor.AQUA + "Particles", new String[] {
                ChatColor.GRAY + "Many spells and effects in Zentrela depend on particles.",
                "",
                ChatColor.GRAY + "Please turn on particles so that you can see spells from players and mobs! It's hard to fight things that are invisible."
        }, null));
        list.add(new MenuItem(0, 4, aboutItem, ChatColor.AQUA + "About Zentrela", new String[] {
                ChatColor.GREEN + "Almost all of Zentrela's content is coded exclusively for the server! This means that we are always able to add and adjust features, since we write the code.",
                "",
                ChatColor.GRAY + "Check out our website at " + ChatColor.AQUA + "www.Zentrela.net" + ChatColor.GRAY + " to join the awesome community!",
                "",
                ChatColor.YELLOW + "The papers below have command details in ALPHABETICAL order for your convenience.",
        }, null));

        ItemStack orderItem = new ItemStack(Material.NETHER_STAR);
        orderItem.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        list.add(new MenuItem(0, 8, orderItem, ChatColor.AQUA + "Menu Help", new String[] {
                ChatColor.GREEN + "The commands shown here are ordered alphabetically for your convenience!",
                "",
                ChatColor.GRAY + "Be sure to check out the full list of commands at the top left.",
                "",
                ChatColor.YELLOW + "Have fun in Zentrela!",
        }, null));
        list.addAll(HelpCommandsSingleton.generated);
        list.addAll(HelpCommandsSingleton.fullListGenerated);
    }

    public static void openMenu(Player p) {
        Inventory inventory = MenuManager.createMenu(p, "Zentrela Help Menu", 6, list);
        p.openInventory(inventory);
    }

}
