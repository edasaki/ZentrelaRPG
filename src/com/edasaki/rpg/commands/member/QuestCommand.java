package com.edasaki.rpg.commands.member;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.quests.QuestManager;

public class QuestCommand extends RPGAbstractCommand {

    public QuestCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(final Player p, PlayerDataRPG pd, String[] args) {
        QuestManager.showQuestMenu(pd);
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
