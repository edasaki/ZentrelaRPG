package com.edasaki.rpg.commands.owner;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class BurnCommand extends RPGAbstractCommand {

    public BurnCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        String s = args[0];
        Player target = plugin.getServer().getPlayerExact(s);
        if (target != null && target.isOnline() && plugin.getPD(target) != null) {
            plugin.getPD(target).giveBurn(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            p.sendMessage("Gave " + target.getName() + " " + args[1] + " seconds of tier " + args[2] + " burn.");
        } else {
            p.sendMessage(ChatColor.RED + "Could not find online player '" + s + "'.");
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
