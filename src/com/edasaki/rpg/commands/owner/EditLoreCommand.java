package com.edasaki.rpg.commands.owner;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class EditLoreCommand extends RPGAbstractCommand {

    public EditLoreCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        ItemStack item = p.getInventory().getItemInMainHand();
        if (item.hasItemMeta()) {
            ItemMeta im = item.getItemMeta();
            ArrayList<String> lore = new ArrayList<String>();
            if (im.hasLore())
                lore.addAll(im.getLore());
            StringBuilder sb = new StringBuilder();
            for (int k = 1; k < args.length; k++) {
                sb.append(args[k]);
                sb.append(' ');
            }
            String newLine = ChatColor.translateAlternateColorCodes('&', sb.toString().trim());
            if (args[0].equalsIgnoreCase("append")) {
                lore.add(newLine);
            } else {
                lore.set(Integer.parseInt(args[0]), newLine);
            }
            im.setLore(lore);
            item.setItemMeta(im);
            p.sendMessage("Updated item.");
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
