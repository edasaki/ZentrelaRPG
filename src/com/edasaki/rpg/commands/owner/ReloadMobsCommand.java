package com.edasaki.rpg.commands.owner;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.core.utils.RMessages;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.mobs.MobManager;

public class ReloadMobsCommand extends RPGAbstractCommand {

    public ReloadMobsCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        MobManager.reload();
        RMessages.announce(ChatColor.RED + "Mobs and spawns reloaded for updates. Sorry for the inconvenience.");
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {

    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
