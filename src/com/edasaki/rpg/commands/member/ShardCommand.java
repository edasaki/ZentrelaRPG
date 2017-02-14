package com.edasaki.rpg.commands.member;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.economy.EconomyManager;

public class ShardCommand extends RPGAbstractCommand {

    public ShardCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(final Player p, PlayerDataRPG pd, String[] args) {
        if (p.getWorld().getName().equalsIgnoreCase(SakiRPG.TUTORIAL_WORLD)) {
            p.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "Sorry! You can't use this command in the tutorial!");
            p.sendMessage(ChatColor.GRAY + "> " + ChatColor.AQUA + "Please finish the tutorial first. Feel free to ask for help!");
            return;
        }
        p.sendMessage("The shard system is incomplete!");
        p.sendMessage("In the future, shards will be used to upgrade equipment, so make sure to stock up!");
        EconomyManager.openShardMenu(p, pd);
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
