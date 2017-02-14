package com.edasaki.rpg.commands.builder;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.general.SchematicManager;

public class BlockLocCommand extends RPGAbstractCommand {

    public BlockLocCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        SchematicManager.giveBlockItem(p);
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
