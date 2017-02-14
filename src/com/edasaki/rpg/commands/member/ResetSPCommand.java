package com.edasaki.rpg.commands.member;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class ResetSPCommand extends RPGAbstractCommand {

    private HashMap<UUID, Long> lastTime = new HashMap<UUID, Long>();

    public ResetSPCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(final Player p, PlayerDataRPG pd, String[] args) {
        if (!lastTime.containsKey(p.getUniqueId()) || System.currentTimeMillis() - lastTime.get(p.getUniqueId()) > 5000) {
            p.sendMessage(ChatColor.RED + "Are you sure you want to reset your SP?");
            p.sendMessage(ChatColor.RED + "Use " + ChatColor.YELLOW + "/resetsp" + ChatColor.RED + " again within 5 seconds if you are sure!");
            lastTime.put(p.getUniqueId(), System.currentTimeMillis());
        } else {
            pd.resetSpells();
            p.sendMessage(ChatColor.GREEN + "Your SP has been reset! You now have " + pd.sp + " SP.");
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
