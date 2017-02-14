package com.edasaki.rpg.commands.member;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class Spell4Command extends RPGAbstractCommand {

    public Spell4Command(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(final Player p, PlayerDataRPG pd, String[] args) {
        if (pd.spell_RRR != null) {
            pd.spell_RRR.cast(p, pd);
        } else {
            p.sendMessage(ChatColor.RED + "You don't have any spell bound to the " + ChatColor.GOLD + "RRR" + ChatColor.RED + " spellcast!");
            p.sendMessage(ChatColor.RED + "You can bind a spell to the " + ChatColor.GOLD + "RRR" + ChatColor.RED + " spellcast using " + ChatColor.YELLOW + "/spell" + ChatColor.RED + "!");
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
