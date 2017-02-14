package com.edasaki.rpg.commands.member;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.core.punishments.Punishment;
import com.edasaki.core.sql.SQLManager;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class LookupCommand extends RPGAbstractCommand {

    public HashMap<String, Long> lastLookup = new HashMap<String, Long>();

    public LookupCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        if (args.length != 1) {
            p.sendMessage(ChatColor.RED + "/lookup <name>");
        } else {
            if (lastLookup.containsKey(p.getName()) && System.currentTimeMillis() - lastLookup.get(p.getName()) < 30000) {
                p.sendMessage(ChatColor.RED + "You can only use /lookup once every 30 seconds.");
            } else {
                lastLookup.put(p.getName(), System.currentTimeMillis());
                p.sendMessage(ChatColor.GREEN + "Looking up punishment history for " + ChatColor.YELLOW + args[0] + ChatColor.GREEN + ", please wait...");
                RScheduler.scheduleAsync(plugin, () -> {
                    AutoCloseable[] ac_dub = SQLManager.prepare("SELECT * FROM punishments WHERE name = ?");
                    try {
                        PreparedStatement request_punishment_status = (PreparedStatement) ac_dub[0];
                        request_punishment_status.setString(1, args[0]);
                        AutoCloseable[] ac_trip = SQLManager.executeQuery(request_punishment_status);
                        ResultSet rs = (ResultSet) ac_trip[0];
                        ArrayList<Punishment> puns = new ArrayList<Punishment>();
                        String name = args[0];
                        while (rs.next()) {
                            Punishment pun = new Punishment();
                            pun.load(rs);
                            puns.add(pun);
                            name = pun.name;
                        }
                        final String fname = name;
                        SQLManager.close(ac_dub);
                        SQLManager.close(ac_trip);
                        RScheduler.schedule(plugin, () -> {
                            if(p == null || !p.isOnline())
                                return;
                            p.sendMessage("");
                            p.sendMessage(ChatColor.GREEN + fname + "'s Punishment History");
                            p.sendMessage(ChatColor.RED + "=========================");
                            for(Punishment pun : puns) {
                                p.sendMessage("");
                                p.sendMessage(pun.getDisplay());
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
