package com.edasaki.rpg.commands.owner;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class HealCommand extends RPGAbstractCommand {

    public HealCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        if (args.length > 0) {
            if (plugin.getServer().getPlayer(args[0]) != null) {
                PlayerDataRPG pd2 = plugin.getPD(plugin.getServer().getPlayerExact(args[0]));
                if (pd2 != null && pd2.isValid()) {
                    pd2.heal(pd2.getCurrentMaxHP());
                    pd.sendMessage(ChatColor.RED + "Healed " + pd2.getName() + " to full HP.");
                }
            }
        } else {
            pd.heal(pd.getCurrentMaxHP());
            pd.sendMessage(ChatColor.RED + "Healed self to full HP.");
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
