package com.edasaki.rpg.commands.member;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.horses.HorseManager;

public class HorseCommand extends RPGAbstractCommand {

    private HashMap<String, Long> lastCommand = new HashMap<String, Long>();

    public HorseCommand(String... commandNames) {
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
        if (pd.riding || p.getVehicle() != null) {
            pd.sendMessage(ChatColor.RED + "You are already on a horse!");
            return;
        }
        if (lastCommand.containsKey(p.getName()) && System.currentTimeMillis() - lastCommand.get(p.getName()) < 5000) {
            p.sendMessage(ChatColor.RED + "You can only summon your horse once every 5 seconds.");
        } else {
            lastCommand.put(p.getName(), System.currentTimeMillis());
            if (pd.inCombat()) {
                pd.sendMessage(ChatColor.RED + "Horses cannot be used while in combat.");
                return;
            }
            if (pd.horseSpeed == 0 && pd.horseJump == 0) { // no horse
                pd.sendMessage(ChatColor.RED + "You don't own a horse. Talk to a " + ChatColor.AQUA + "Horse Dealer" + ChatColor.RED + " to get one!");
            } else {
                HorseManager.createHorse(p, pd);
            }
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
