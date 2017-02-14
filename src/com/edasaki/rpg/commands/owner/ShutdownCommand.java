package com.edasaki.rpg.commands.owner;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.core.utils.RMessages;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RTicks;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class ShutdownCommand extends RPGAbstractCommand {

    private boolean cancel = false;

    //    private boolean inProgress = false;

    public ShutdownCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        StringBuilder sb = new StringBuilder();
        if (args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("cancel")) {
            //            inProgress = false;
            cancel = true;
            sender.sendMessage("Shutdown cancelled");
            return;
        }
        //        if (inProgress) {
        //            sender.sendMessage("Shutdown already in progress. Cancel it first.");
        //            return;
        //        }
        int min = Integer.parseInt(args[0]);
        String message = null;
        if (args.length > 1) {
            for (int k = 1; k < args.length; k++) {
                sb.append(args[k]);
                sb.append(' ');
            }
            message = sb.toString().trim();
        }
        final String fMessage = message;
        //        inProgress = true;
        sender.sendMessage("Scheduled shutdown for " + min + " minutes");
        RScheduler.schedule(plugin, new Runnable() {
            int count = min;

            public void run() {
                if (cancel) {
                    cancel = false;
                    //                    inProgress = false;
                    return;
                }
                if (count > 0) {
                    RMessages.announce(ChatColor.translateAlternateColorCodes('&', "&c&l The server will be shutting down in &e&l" + count + " minute" + (count != 1 ? "s" : "") + "&c&l."));
                    if (count <= 5)
                        RMessages.announce(ChatColor.translateAlternateColorCodes('&', "&e&l Prepare to log off. &c&lSorry for the inconvenience!"));
                    if (fMessage != null) {
                        RMessages.announce(ChatColor.translateAlternateColorCodes('&', "&c&l The reason for the shutdown is:"));
                        RMessages.announce(ChatColor.translateAlternateColorCodes('&', "&7&l > &e&l" + fMessage));
                    }
                    int time = 1;
                    if (count >= 30)
                        time = 10;
                    else if (count >= 10)
                        time = 5;
                    count -= time;
                    RScheduler.schedule(plugin, this, RTicks.minutes(time));
                } else {
                    RMessages.announce(ChatColor.translateAlternateColorCodes('&', "&c&l The server will be shutting down in &e&l10 seconds&c&l."));
                    RMessages.announce(ChatColor.translateAlternateColorCodes('&', "&e&l Prepare to log off. &c&lSorry for the inconvenience!"));
                    if (fMessage != null) {
                        RMessages.announce(ChatColor.translateAlternateColorCodes('&', "&c&l The reason for the shutdown is:"));
                        RMessages.announce(ChatColor.translateAlternateColorCodes('&', "&7&l > &e&l" + fMessage));
                    }
                    RScheduler.schedule(plugin, new Runnable() {
                        public void run() {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "maintenance owner");
                        }
                    }, RTicks.seconds(10));
                }
            }
        });
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {

    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
