package com.edasaki.rpg.commands.member;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class Spell1Command extends RPGAbstractCommand {

    public Spell1Command(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(final Player p, PlayerDataRPG pd, String[] args) {
        if (pd.spell_RLL != null) {
            pd.spell_RLL.cast(p, pd);
        } else {
            p.sendMessage(ChatColor.RED + "You don't have any spell bound to the " + ChatColor.GOLD + "RLL" + ChatColor.RED + " spellcast!");
            p.sendMessage(ChatColor.RED + "You can bind a spell to the " + ChatColor.GOLD + "RLL" + ChatColor.RED + " spellcast using " + ChatColor.YELLOW + "/spell" + ChatColor.RED + "!");
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
