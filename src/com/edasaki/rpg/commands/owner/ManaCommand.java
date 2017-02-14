package com.edasaki.rpg.commands.owner;

import java.util.HashSet;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class ManaCommand extends RPGAbstractCommand {

    public static HashSet<String> infMana = new HashSet<String>();
    
    public ManaCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        if (infMana.contains(p.getName())) {
            p.sendMessage("you have mana costs now noob");
            infMana.remove(p.getName());
        } else {
            p.sendMessage("infinite mana op!!");
            pd.mana = 10;
            infMana.add(p.getName());
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
