package com.edasaki.rpg.commands.member;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.core.utils.RTime;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class PlayTimeCommand extends RPGAbstractCommand {

    public PlayTimeCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(final Player p, PlayerDataRPG pd, String[] args) {
        if (args.length == 0) {
            p.sendMessage("");
            p.sendMessage(ChatColor.GRAY + "> " + ChatColor.AQUA + p.getName() + "'s Zentrelan Adventures:");
            p.sendMessage(ChatColor.GRAY + "> " + ChatColor.WHITE + "You joined Zentrela at " + ChatColor.YELLOW + pd.joinDate.format(RTime.JOIN_DATE_FORMATTER) + ChatColor.WHITE + ".");
            p.sendMessage(ChatColor.GRAY + "> " + ChatColor.WHITE + "You have spent " + ChatColor.YELLOW + RTime.formatMinutes(pd.getTimePlayed()) + ChatColor.WHITE + " on Zentrela.");
        } else if (args.length == 1) {
            Player p2 = plugin.getServer().getPlayerExact(args[0]);
            PlayerDataRPG pd2 = plugin.getPD(p2);
            if (pd2 != null && pd2.isValid()) {
                p.sendMessage("");
                p.sendMessage(ChatColor.GRAY + "> " + ChatColor.AQUA + pd2.getName() + "'s Zentrelan Adventures:");
                p.sendMessage(ChatColor.GRAY + "> " + ChatColor.WHITE + pd2.getName() + " joined Zentrela at " + ChatColor.YELLOW + pd2.joinDate.format(RTime.JOIN_DATE_FORMATTER) + ChatColor.WHITE + ".");
                p.sendMessage(ChatColor.GRAY + "> " + ChatColor.WHITE + pd2.getName() + " has spent " + ChatColor.YELLOW + RTime.formatMinutes(pd2.getTimePlayed()) + ChatColor.WHITE + " on Zentrela.");
            } else {
                p.sendMessage(ChatColor.RED + args[0] + " is not online.");
            }
        } else {
            p.sendMessage(ChatColor.RED + "/playtime [name]");
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
