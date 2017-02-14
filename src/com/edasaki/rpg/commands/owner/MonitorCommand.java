package com.edasaki.rpg.commands.owner;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.core.chat.ChatManager;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class MonitorCommand extends RPGAbstractCommand {

    public MonitorCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        if (ChatManager.monitors.contains(p.getName())) {
            ChatManager.monitors.remove(p.getName());
            p.sendMessage("Removed from monitors list.");
        } else {
            ChatManager.monitors.add(p.getName());
            p.sendMessage("Added to monitors list.");
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
