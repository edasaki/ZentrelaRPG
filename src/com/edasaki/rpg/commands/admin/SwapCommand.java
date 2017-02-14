package com.edasaki.rpg.commands.admin;

import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class SwapCommand extends RPGAbstractCommand {

    public static HashSet<String> noTP = new HashSet<String>();

    public SwapCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(final Player p, PlayerDataRPG pd, String[] args) {
        if (args.length != 1) {
            p.sendMessage(ChatColor.RED + "Incorrect command format!");
            p.sendMessage(ChatColor.RED + ">> /swap <name>");
        } else if (args.length == 1) {
            String s = args[0];
            Player target = plugin.getServer().getPlayerExact(s);
            if (target != null && target.isOnline()) {
                ItemStack mainhand = target.getInventory().getItemInMainHand();
                ItemStack offhand = target.getInventory().getItemInOffHand();
                target.getInventory().setItemInMainHand(offhand);
                target.getInventory().setItemInOffHand(mainhand);
                target.updateInventory();
                p.sendMessage("Swapped items for target.");
                target.sendMessage("Your main and offhand were swapped by " + p.getName() + ".");
            } else {
                p.sendMessage(ChatColor.RED + "Could not find online player '" + s + "'.");
            }
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
