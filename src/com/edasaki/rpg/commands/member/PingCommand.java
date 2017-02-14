package com.edasaki.rpg.commands.member;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class PingCommand extends RPGAbstractCommand {

    public PingCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(final Player p, PlayerDataRPG pd, String[] args) {
        int ping = (((CraftPlayer) p).getHandle()).ping;
        p.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "Your ping is " + ChatColor.WHITE + ping + ChatColor.GREEN + ".");
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
