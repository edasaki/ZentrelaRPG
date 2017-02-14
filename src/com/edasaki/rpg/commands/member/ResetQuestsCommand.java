package com.edasaki.rpg.commands.member;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.core.utils.fanciful.FancyMessage;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.quests.Quest;
import com.edasaki.rpg.quests.QuestManager;

public class ResetQuestsCommand extends RPGAbstractCommand {

    public ResetQuestsCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(final Player p, PlayerDataRPG pd, String[] args) {
        if (p.getWorld().getName().equalsIgnoreCase(SakiRPG.TUTORIAL_WORLD)) {
            p.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "Sorry! You can't use this command in the tutorial!");
            p.sendMessage(ChatColor.GRAY + "> " + ChatColor.AQUA + "Please finish the tutorial first. Feel free to ask for help!");
            return;
        }
        StringBuilder sb = new StringBuilder();
        boolean confirmed = false;
        if (args.length > 0 && args[0].equals("-confirm-"))
            confirmed = true;
        for (int k = confirmed ? 1 : 0; k < args.length; k++) {
            sb.append(args[k]);
            sb.append(' ');
        }
        String questName = sb.toString().trim();
        Quest toReset = null;
        for (Quest q : QuestManager.activeQuestList) {
            if (q.name.equalsIgnoreCase(questName)) {
                toReset = q;
                break;
            }
        }
        if (toReset == null) {
            p.sendMessage(ChatColor.RED + "There is no quest named " + questName + "!");
            p.sendMessage(ChatColor.RED + "To reset a quest, you must use the command like this:");
            p.sendMessage(ChatColor.RED + "/resetquest <QUEST_NAME>");
        } else {
            if (!confirmed) {
                p.sendMessage("");
                p.sendMessage(ChatColor.YELLOW + "You are about to reset ALL quest progress on:");
                p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "  " + toReset.name);
                p.sendMessage(ChatColor.YELLOW + "Are you SURE you want to reset this quest?");
                p.sendMessage("");
                FancyMessage fm = new FancyMessage();
                fm.text("Yes, I am sure! Click here to reset the quest.");
                fm.color(ChatColor.GREEN);
                fm.command("/resetquests -confirm- " + toReset);
                fm.send(p);
                fm = new FancyMessage();
                p.sendMessage("");
            } else {
                p.sendMessage("");
                p.sendMessage(ChatColor.GREEN + "Your quest has been reset!");
                pd.questProgress.remove(toReset.identifier);
                pd.save();
            }
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
