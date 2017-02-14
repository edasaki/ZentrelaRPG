package com.edasaki.rpg.commands.member;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class OnlineCommand extends RPGAbstractCommand {

    public OnlineCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("");
        int size = plugin.getServer().getOnlinePlayers().size();
        StringBuilder sb = new StringBuilder();
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            sb.append(plugin.getPD(p).getChatRankPrefix() + p.getName());
            sb.append("   ");
        }
        sender.sendMessage(sb.toString().trim().replace("   ", ", "));
        if (size != 1) {
            sender.sendMessage(ChatColor.YELLOW + "There are currently " + ChatColor.AQUA + size + ChatColor.YELLOW + " players online.");
        } else {
            sender.sendMessage(ChatColor.YELLOW + "There is currently " + ChatColor.AQUA + "1" + ChatColor.YELLOW + " player online.");
        }
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
