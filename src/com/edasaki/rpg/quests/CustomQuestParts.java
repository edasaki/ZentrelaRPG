package com.edasaki.rpg.quests;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;

public class CustomQuestParts {

    public static boolean check(int id, Player p, PlayerDataRPG pd, Quest quest, QuestVillager qv) {
        if (id == 1) {
            if (!pd.castedFirework) {
                qv.say(p, "Cast Firework, and I'll warp you to the next part! If you need help, check out the room behind me!");
                return false;
            } else {
                qv.say(p, "Great job! Talk to me again and I'll warp you!");
                return true;
            }
        } else if (id == 2) {
            if (!pd.usedTrinketCommand) {
                qv.say(p, "Try out the " + ChatColor.YELLOW + "/trinket" + ChatColor.RESET + " command! Talk to me again once you've done that.");
                return false;
            } else {
                qv.say(p, "Did you equip a Trinket? You can change them any time! Talk to me again to warp to the " + ChatColor.GOLD + ChatColor.BOLD + "LAST PART" + ChatColor.RESET + " of the tutorial!");
                return true;
            }
        } else if (id == 3) {
            if (pd.soared) {
                qv.say(p, "You're back!");
                return true;
            } else {
                pd.sendMessage(ChatColor.RED + "You'd better try Soaring before you talk to Misako.");
                pd.sendMessage(ChatColor.RED + "Maybe Rina tricked you and it doesn't work at all!");
                pd.sendMessage(ChatColor.AQUA + "To use Soaring, " + ChatColor.GREEN + "sneak (SHIFT key) on the ground for a second, and then jump up and stop sneaking.");
                return false;
            }
        }
        return false;
    }

}
