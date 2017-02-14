package com.edasaki.rpg.commands.owner;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.core.chat.ChatManager;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class ShadowMuteCommand extends RPGAbstractCommand {

    public ShadowMuteCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Use as /shadowmute <name>");
        } else if (args.length == 1) {
            Player p2 = plugin.getServer().getPlayer(args[0]);
            if (p2 != null && p2.isValid() && p2.isOnline()) {
                if(ChatManager.shadowMute.contains(p2.getName())) {
                    sender.sendMessage(ChatColor.GREEN + p2.getName() + " was taken off the shadowmute list.");
                    ChatManager.shadowMute.remove(p2.getName());
                } else {
                    sender.sendMessage(ChatColor.GREEN + p2.getName() + " was shadowmuted!");
                    ChatManager.shadowMute.add(p2.getName());
                }
            } else {
                sender.sendMessage("User is not online.");
            }
        }
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {

    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
