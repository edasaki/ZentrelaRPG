package com.edasaki.rpg.commands.owner;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.core.commands.mod.TeleportCommand;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class TPHideCommand extends RPGAbstractCommand {

    public TPHideCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {

        if (TeleportCommand.noTP.contains(p.getName())) {
            p.sendMessage("ppl can tp to you now.");
            TeleportCommand.noTP.remove(p.getName());
        } else {
            p.sendMessage("ppl can't tp to you anymore!");
            TeleportCommand.noTP.add(p.getName());
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
