package com.edasaki.rpg.commands.builder;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class VoxelSniperCommand extends RPGAbstractCommand {

    public VoxelSniperCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(final Player p, PlayerDataRPG pd, String[] args) {
        p.addAttachment(plugin, "voxelsniper.*", true);
        p.addAttachment(plugin, "voxelsniper.sniper", true);
        p.addAttachment(plugin, "voxelsniper.command.*", true);
        p.sendMessage("You can now use voxel sniper. Note that the server now has CoreProtect so don't grief in the main world (/cw main) as I can see who did it and revert the changes (and ban you!).");
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
