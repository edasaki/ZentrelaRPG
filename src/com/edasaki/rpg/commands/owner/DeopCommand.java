package com.edasaki.rpg.commands.owner;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class DeopCommand extends RPGAbstractCommand {

    public DeopCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        String s = args[0];
        Player target = plugin.getServer().getPlayerExact(s);
        if (target != null && target.isOnline() && plugin.getPD(target) != null) {
            if (target.isOp()) {
                target.setOp(false);
                p.sendMessage("Deopped " + target.getName() + ".");
                target.sendMessage("You were deopped by " + p.getName() + ".");
            } else {
                p.sendMessage(target.getName() + " is not an op in the first place, lol.");
            }
        } else {
            p.sendMessage(ChatColor.RED + "Could not find online player '" + s + "'.");
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
