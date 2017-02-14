package com.edasaki.rpg.commands.member;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.soaring.SoaringManager;

public class FlightCommand extends RPGAbstractCommand {

    public FlightCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(final Player p, PlayerDataRPG pd, String[] args) {
        plugin.getInstance(SoaringManager.class).showMenu(pd);
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
