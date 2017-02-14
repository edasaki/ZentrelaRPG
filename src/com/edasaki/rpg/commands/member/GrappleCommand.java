package com.edasaki.rpg.commands.member;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.fun.GrappleManager;

public class GrappleCommand extends RPGAbstractCommand {

    public GrappleCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        GrappleManager.supply(p);
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
