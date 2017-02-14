package com.edasaki.rpg.commands.owner;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class MisakaCommand extends RPGAbstractCommand {

    public MisakaCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        StringBuilder sb = new StringBuilder("");
        for (String s : args) {
            sb.append(s + " ");
        }
        String message = ChatColor.translateAlternateColorCodes('&', sb.toString().trim());
        for (Player p2 : plugin.getServer().getOnlinePlayers()) {
            p2.sendMessage(message);
        }
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {

    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
