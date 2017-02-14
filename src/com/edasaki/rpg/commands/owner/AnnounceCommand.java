package com.edasaki.rpg.commands.owner;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.core.utils.RMessages;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class AnnounceCommand extends RPGAbstractCommand {

    public AnnounceCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String[] lines = new String[] { "", "" };
        if (args.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (int k = 0; k < args.length; k++)
                sb.append(args[k] + " ");
            String[] data = sb.toString().trim().split("@");
            for (int k = 0; k < data.length; k++) {
                lines[k] = ChatColor.translateAlternateColorCodes('&', data[k]);
            }
        }
        for (Player p2 : plugin.getServer().getOnlinePlayers()) {
            RMessages.sendTitle(p2, lines[0], lines[1], 20, 100, 20);
        }
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {

    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
