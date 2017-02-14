package com.edasaki.rpg.commands.owner;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.quests.QuestManager;

public class CompleteQuestCommand extends RPGAbstractCommand {

    public CompleteQuestCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(final Player p, PlayerDataRPG pd, String[] args) {
        if (!QuestManager.quests.containsKey(args[0])) {
            pd.sendMessage("quest with id " + args[0] + " does not exist");
            return;
        }
        pd.questProgress.put(args[0], 1000);
        p.sendMessage("Added completed quest: " + args[0]);
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
