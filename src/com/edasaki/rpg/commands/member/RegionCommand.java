package com.edasaki.rpg.commands.member;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class RegionCommand extends RPGAbstractCommand {

    public RegionCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        if(pd.region != null) {
           pd.region.sendWelcome(p, null); 
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
