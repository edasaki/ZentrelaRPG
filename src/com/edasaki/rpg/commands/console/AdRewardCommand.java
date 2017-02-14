package com.edasaki.rpg.commands.console;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.rewards.RewardsManager;

public class AdRewardCommand extends RPGAbstractCommand {

    public AdRewardCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String s = args[0];
        Player target = plugin.getServer().getPlayerExact(s);
        if (target != null && target.isOnline() && plugin.getPD(target) != null) {
//            PlayerData pd = plugin.getPD(target);
            RewardsManager.givePoints(target.getName(), 1);
        } else {
            sender.sendMessage(ChatColor.RED + "Could not find online player '" + s + "'.");
        }
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
