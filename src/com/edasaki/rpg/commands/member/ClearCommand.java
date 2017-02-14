package com.edasaki.rpg.commands.member;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class ClearCommand extends RPGAbstractCommand {

    private HashMap<UUID, Long> lastTime = new HashMap<UUID, Long>();

    public ClearCommand(String... commandNames) {
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
        if (!lastTime.containsKey(p.getUniqueId()) || System.currentTimeMillis() - lastTime.get(p.getUniqueId()) > 5000) {
            p.sendMessage(ChatColor.RED + "Are you sure you want to clear your inventory?");
            p.sendMessage(ChatColor.RED + "Armor that you have equipped will not be deleted.");
            p.sendMessage(ChatColor.RED + "Anything that you are not wearing will be deleted.");
            p.sendMessage(ChatColor.RED + "Use " + ChatColor.YELLOW + "/clear" + ChatColor.RED + " again within 5 seconds if you are sure!");
            lastTime.put(p.getUniqueId(), System.currentTimeMillis());
        } else {
            p.closeInventory();
            for (int k = 0; k <= 35; k++)
                p.getInventory().setItem(k, null);
            p.updateInventory();
            lastTime.remove(p.getUniqueId());
            p.sendMessage(ChatColor.GREEN + "Inventory cleared!");
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
