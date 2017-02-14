package com.edasaki.rpg.commands.member;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class IgnoreCommand extends RPGAbstractCommand {

    public IgnoreCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        if (args.length != 1) {
            p.sendMessage(ChatColor.RED + "Incorrect command format!");
            p.sendMessage(ChatColor.RED + ">> /ignore <name>");
        } else {
            String name = args[0];
            String lower = name.toLowerCase();
            if (pd.ignored.contains(lower)) {
                pd.ignored.remove(lower);
                pd.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "You have un-ignored " + name + ".");
            } else {
                pd.ignored.add(lower);
                pd.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "You have ignored " + name + ".");
                pd.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "Use \"/ignore " + name + "\" again to un-ignore " + name + ".");
            }
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
