package com.edasaki.rpg.commands.owner;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class SetSpawnCommand extends RPGAbstractCommand {

    public SetSpawnCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        Location loc = p.getLocation();
        p.getWorld().setSpawnLocation(loc.getBlockX(), loc.getBlockY() + 1, loc.getBlockZ());
        p.sendMessage("Set spawn to " + p.getWorld().getSpawnLocation());
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
