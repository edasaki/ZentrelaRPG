package com.edasaki.rpg.commands.member;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.core.chat.ChatManager;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class WhisperCommand extends RPGAbstractCommand {

    public WhisperCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        if (args.length < 1) {
            p.sendMessage(ChatColor.RED + "Incorrect command format!");
            p.sendMessage(ChatColor.RED + ">> /w <name> <message>");
        } else {
            String s = args[0];
            StringBuilder sb = new StringBuilder();
            for (int k = 1; k < args.length; k++) {
                sb.append(args[k]);
                sb.append(' ');
            }
            ChatManager.sendWhisper(p, s, pd, sb.toString());
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
