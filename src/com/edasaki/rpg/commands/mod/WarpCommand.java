package com.edasaki.rpg.commands.mod;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.core.PlayerData;
import com.edasaki.core.commands.AbstractCommand;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.warps.WarpManager;

public class WarpCommand extends AbstractCommand {

    public WarpCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerData pd, String[] args) {
        if (p.getWorld().getName().equalsIgnoreCase(SakiRPG.TUTORIAL_WORLD)) {
            p.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "Sorry! You can't use this command in the tutorial!");
            p.sendMessage(ChatColor.GRAY + "> " + ChatColor.AQUA + "Please finish the tutorial first. Feel free to ask for help!");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (String s : args) {
            sb.append(s);
            sb.append(" ");
        }
        String name = sb.toString().trim().toLowerCase();
        Location dest = WarpManager.warps.get(name);
        if (dest == null) {
            p.sendMessage("Could not find warp \"" + name + "\"");
            p.sendMessage("Valid warps are: ");
            p.sendMessage(WarpManager.warps.keySet().toString());
        } else {
            p.sendMessage("Warping to \"" + name + "\".");
            p.teleport(dest);
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
