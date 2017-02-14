package com.edasaki.rpg.help;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.edasaki.core.menus.MenuItem;

public class HelpCommandsSingleton {
    protected static final Object[][] COMMANDS = {
            {
                    "/spell",
                    "/sp /spl",
                    "Open your spell menu.",
                    "Need help? Type " + ChatColor.YELLOW + "!spells " + ChatColor.GRAY + "in chat for a link to a quick video guide on the Spell Menu!"
            },
            {
                    "/shard",
                    "/eco /bal",
                    "Open the shard menu.",
                    "Salvage shards from equipment, convert between Shard Cubes and Shards, and more!"
            },
            {
                    "/trade <name>",
                    "",
                    "Send a trade request to another player.",
                    "Trades are the safest way to sell your items!"
            },
            {
                    "/party",
                    "/p",
                    "Basic party system command.",
                    "Talk in a party by adding \\ to the front of your messages, like this:",
                    "\\Hi guys!",
            },
            {
                    "/quest",
                    "/q",
                    "Open the quest log.",
                    "The quest log contains COORDINATES for each quest, which can be extremely helpful!"
            },
            {
                    "/trinket",
                    "/t",
                    "Open the trinket menu.",
                    "Trinkets help you in many different ways, and can be changed at any time."
            },
            {
                    "/horse",
                    "/h",
                    "Ride your horse.",
                    "Better horses can be bought at stables throughout Zentrela."
            },
            {
                    "/class",
                    "",
                    "Open the class menu.",
                    "This will automatically open the spell menu instead if you have a class already."
            },
            {
                    "/effects",
                    "/e",

                    "Open the effects menu.",
                    "Effects are prizes from Cosmetic Crates. Type " + ChatColor.YELLOW + "!crates " + ChatColor.GRAY + "in chat for more info!"
            },
            {
                    "/rewards",
                    "",
                    "Open the rewards menu.",
                    "The rewards menu contains goodies that can be purchased with Reward Points. Reward Points are earned by voting."
            },
            {
                    "/sell",
                    "/salvage",
                    "Open the salvage menu.",
                    "A shortcut for the salvage menu located in /shard.",
            },
            {
                    "/roll [x]",
                    "",
                    "Roll a number between 1 and x (1 and 100 if you don't specify x).",
                    "The roll result is only sent to your party.",
            },
            {
                    "/clear",
                    "",
                    "Clear your inventory.",
                    "Be careful! This will PERMANENTLY delete all items from your inventory!",
            },
            {
                    "/played <name>",
                    "",
                    "View statistics on how long a person has played Zentrela.",
                    "You can just do /played without a <name> if you want your own information."
            },
            {
                    "/region",
                    "",
                    "Show your current region.",
                    "Remember, PvP is allowed in Danger Levels 3 and 4, but you only lose items in Danger Level 4!"
            },
            {
                    "/msg [name] <...> " + ChatColor.YELLOW + "and " + ChatColor.BOLD + "/r <...>",
                    "",
                    "Use the messaging system.",
                    "/msg <name> <message> will send a message to <name>.",
                    "/r <messsage> will send a message to the last person you talked to."
            },
            {
                    "/lookup <name>",
                    "",
                    "View a player's punishment history.",
                    "This will show all bans and mutes. Kicks are not recorded."
            },
            {
                    "/spy [name]",
                    "/info",
                    "View extra information about a player.",
                    "This is the same as clicking on a person's name in chat.",
            },
            {
                    "/online",
                    "",
                    "View a list of all online players.",
                    "The list is sorted by rank and name.",
            },
            {
                    "/options",
                    "/o /opt",
                    "Opens the options menu.",
                    "Have a suggestion for a new option? Post it on the Zentrela forums at zentrela.net!",
            },
            {
                    "/guild",
                    "/g",
                    "Open the guild menu.",
                    "COMING SOON. Guilds will be released in the near future."
            },
            {
                    "/bank",
                    "",
                    "Open your bank.",
                    "Your bank can only be accessed in Danger Level 1 zones."
            },
            {
                    "/spawn",
                    "",
                    "Teleport to the nearest spawn point.",
                    "You must stand still to warp.",
                    "May only be used once every 10 minutes."
            },
            {
                    "/ignore [name]",
                    "",
                    "Ignore a player.",
                    "You will not see any messages or requests from ignored players. Ignores are reset every time you log on."
            },
            {
                    "/ping",
                    "",
                    "Get your ping to the server.",
                    "If you are lagging badly, the /ping command may not work properly."
            },
            {
                    "/top",
                    "/leaderboard",
                    "View the top players in Zentrela!",
                    "The 5 best players in 5 categories are displayed."
            },
            {
                    "/worldboss",
                    "",
                    "Enter the World Boss Arena lobby.",
                    "You can spend your World Boss Points here."
            }
    };

    protected static ArrayList<MenuItem> generated = new ArrayList<MenuItem>();
    protected static ArrayList<MenuItem> fullListGenerated = new ArrayList<MenuItem>();
    private static ArrayList<ListedCommand> temp = new ArrayList<ListedCommand>();

    static {
        for (Object[] o : HelpCommandsSingleton.COMMANDS) {
            String name = (String) o[0];
            String alias = (String) o[1];
            String strippedName = ChatColor.stripColor(name);
            name = ChatColor.YELLOW + ChatColor.BOLD.toString() + name + ChatColor.YELLOW + " " + alias;
            String desc = (String) o[2];
            String[] fullLore = new String[3 + o.length - 3];
            fullLore[0] = "";
            fullLore[1] = ChatColor.GREEN + desc;
            fullLore[2] = "";
            for (int k = 3; k < o.length; k++) {
                fullLore[k] = ChatColor.GRAY + (String) o[k];
            }
            ListedCommand lc = new ListedCommand();
            lc.name = name;
            lc.strippedName = strippedName;
            lc.desc = desc;
            lc.fullLore = fullLore;
            temp.add(lc);
        }
        int row = 1;
        int col = 0;
        Collections.sort(temp);
        ArrayList<String> fullList = new ArrayList<String>();
        for (ListedCommand lc : temp) {
            generated.add(lc.generate(row, col));
            col++;
            if (col >= 9) {
                row++;
                col = 0;
                if (row >= 5) {
                    System.out.println("ERROR: Ran out of space in /help.");
                    break;
                }
            }
            fullList.add(ChatColor.YELLOW + lc.strippedName + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + lc.desc);
        }

        ArrayList<String> temp = new ArrayList<String>();
        col = 0;
        int page = 1;
        for (int k = 0; k < fullList.size(); k++) {
            if (k > 0 && k % 7 == 0) {
                MenuItem mi = new MenuItem(0, col, new ItemStack(Material.BOOK_AND_QUILL), ChatColor.DARK_AQUA + "Commands List - Page " + page, temp, null);
                fullListGenerated.add(mi);
                temp = new ArrayList<String>();
                col++;
                page++;
            }
            temp.add(fullList.get(k));
        }
        //        System.out.println("Generated: " + generated);
    }
}
