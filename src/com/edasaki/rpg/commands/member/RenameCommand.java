package com.edasaki.rpg.commands.member;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.items.ItemManager;

public class RenameCommand extends RPGAbstractCommand {

    private HashMap<String, Long> lastInfo = new HashMap<String, Long>();

    public RenameCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(final Player p, PlayerDataRPG pd, String[] args) {
        if (ItemManager.hasItem(p, ItemManager.getItemForIdentifier("item_rename"))) {
            if (!lastInfo.containsKey(p.getName()) || (System.currentTimeMillis() - lastInfo.get(p.getName()) > 30000)) {
                //info
                p.sendMessage("info");
            } else {
                p.sendMessage("rename");
                //rename
            }
        } else {
            p.sendMessage(ChatColor.RED + "/rename can only be used with an Item Renamer in your inventory!");
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
