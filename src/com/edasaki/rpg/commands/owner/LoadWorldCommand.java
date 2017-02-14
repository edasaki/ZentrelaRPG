package com.edasaki.rpg.commands.owner;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class LoadWorldCommand extends RPGAbstractCommand {

    public LoadWorldCommand(String... s) {
        super(s);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        World w = plugin.getServer().createWorld(new WorldCreator(args[0]));
        sender.sendMessage("Loaded world " + w.getName() + ".");
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {

    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {

    }

}
