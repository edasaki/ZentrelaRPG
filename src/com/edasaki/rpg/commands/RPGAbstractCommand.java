package com.edasaki.rpg.commands;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.edasaki.core.PlayerData;
import com.edasaki.core.commands.AbstractCommand;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;

public abstract class RPGAbstractCommand extends AbstractCommand {

    public static SakiRPG plugin;

    @Override
    public void executePlayer(Player p, PlayerData pd, String[] args) {
    }

    @Override
    public final boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command == null)
            return false;
        if (sender == null)
            return false;
        if (!command.getName().equalsIgnoreCase(getName()))
            return false;
        if (sender instanceof Player) {
            Player p = (Player) sender;
            PlayerDataRPG pd = plugin.getPD(p);
            if (pd == null || !pd.loadedSQL) {
                p.sendMessage(ChatColor.RED + "Please wait a moment while your data is loaded.");
                return true;
            }
            if (!pd.loadedSQL && !(command.getLabel().equalsIgnoreCase("iknow") || command.getLabel().equalsIgnoreCase("db"))) {
                p.sendMessage(ChatColor.RED + "Commands cannot be used in the lobby.");
                return true;
            }
            if (!pd.check(requiredRank)) {
                p.sendMessage(ChatColor.RED + "You don't have permission to use that command!");
                return true;
            }
            execute(sender, args);
            executePlayer(p, pd, args);
        } else if (sender instanceof ConsoleCommandSender) {
            execute(sender, args);
            executeConsole(sender, args);
        }
        System.out.println("Executing /" + command.getName() + " for " + sender.getName() + " with args " + Arrays.toString(args));
        return true;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        return this.onCommand(sender, this, commandLabel, args);
    }

    /*
     * Run by both player and console executions.
     */
    public abstract void execute(CommandSender sender, String[] args);

    /*
     * Special execution for player command.
     */
    public abstract void executePlayer(Player p, PlayerDataRPG pd, String[] args);

    /*
     * Special execution for console command.
     */
    public abstract void executeConsole(CommandSender sender, String[] args);

    public RPGAbstractCommand(String... commandNames) {
        super(commandNames[0]);
        if (commandNames.length > 1)
            for (int k = 1; k < commandNames.length; k++)
                getAliases().add(commandNames[k]);
    }
}
