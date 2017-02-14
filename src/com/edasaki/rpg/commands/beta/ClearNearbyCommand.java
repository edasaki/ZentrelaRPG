package com.edasaki.rpg.commands.beta;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.drops.DropManager;

public class ClearNearbyCommand extends RPGAbstractCommand {

    public ClearNearbyCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        for (Entity e : p.getNearbyEntities(5, 5, 5)) {
            if (e instanceof Item) {
                DropManager.removeLabel((Item) e);
                e.remove();
            }
        }
        p.sendMessage("cleared nearby items");
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
