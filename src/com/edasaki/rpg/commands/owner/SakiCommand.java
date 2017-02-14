package com.edasaki.rpg.commands.owner;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.core.utils.RMessages;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class SakiCommand extends RPGAbstractCommand {

    public static final String PREFIX = ChatColor.GRAY + "[0] " + ChatColor.AQUA + ChatColor.BOLD + "Bot " + ChatColor.WHITE + "Rensa: " + ChatColor.GOLD;
    
    public SakiCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        StringBuilder sb = new StringBuilder("");
        for (String s : args) {
            sb.append(s + " ");
        }
        String message = PREFIX + ChatColor.translateAlternateColorCodes('&', sb.toString().trim());
        RMessages.announce(message);
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {

    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
