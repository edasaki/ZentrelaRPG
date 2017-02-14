package com.edasaki.rpg.commands.owner;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class StopCommand extends RPGAbstractCommand {

    public StopCommand(String... commandNames) {
        super(commandNames);
    }

    private static long lastCommand = 0;

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (System.currentTimeMillis() - lastCommand < 5000) {
            plugin.getServer().shutdown();
        } else {
            lastCommand = System.currentTimeMillis();
            sender.sendMessage("Are you sure you want to stop the server? Re-use command within 5 seconds to shutdown.");
        }
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {

    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
