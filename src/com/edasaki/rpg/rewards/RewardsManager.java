package com.edasaki.rpg.rewards;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.edasaki.core.sql.SQLManager;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RScheduler.Halter;
import com.edasaki.core.utils.RTicks;
import com.edasaki.core.utils.fanciful.FancyMessage;
import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;

public class RewardsManager extends AbstractManagerRPG {

    public RewardsManager(SakiRPG plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {
        RewardsShop.initialize();
    }

    public static void openMenu(Player p, PlayerDataRPG pd) {
        String uuid = p.getUniqueId().toString();
        Halter h = new Halter();
        p.sendMessage("");
        FancyMessage fm = new FancyMessage("> ");
        fm.color(ChatColor.GRAY);
        fm.link("http://zentrela.net/vote.html");
        fm.then("You can earn Reward Points by voting!");
        fm.color(ChatColor.GREEN);
        fm.link("http://zentrela.net/vote.html");
        fm.then(" Click here for a link.");
        fm.color(ChatColor.YELLOW);
        fm.link("http://zentrela.net/vote.html");
        fm.send(p);
        RScheduler.scheduleRepeating(plugin, new Runnable() {
            private int count = 1;

            public void run() {
                p.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "Loading your Reward Points. Please wait... [" + count++ + "]");
                if (count > 10) {
                    p.sendMessage(ChatColor.GRAY + "> " + ChatColor.RED + "There was an error loading your Reward Points. Please try again later.");
                    h.halt = true;
                }
            }
        }, RTicks.seconds(1), h);
        RScheduler.scheduleAsync(plugin, () -> {
            AutoCloseable[] ac_dub = SQLManager.prepare("SELECT rewardPoints FROM main WHERE uuid = ?");
            try {
                PreparedStatement request_player_data = (PreparedStatement) ac_dub[0];
                request_player_data.setString(1, uuid);
                AutoCloseable[] ac_trip = SQLManager.executeQuery(request_player_data);
                ResultSet rs = (ResultSet) ac_trip[0];
                int amount = 0;
                if (rs.next()) {
                    amount = rs.getInt("rewardPoints");
                }
                int fAmount = amount;
                SQLManager.close(ac_dub);
                SQLManager.close(ac_trip);
                RScheduler.schedule(plugin, () -> {
                    h.halt = true;
                    if (p != null && p.isOnline()) {
                        p.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "You have " + ChatColor.YELLOW + fAmount + ChatColor.GREEN + " Reward Point" + (fAmount != 1 ? "s" : "") + ".");
                        openActualMenu(p, fAmount);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void openActualMenu(Player p, int points) {
        RewardsShop.openMenu(p, points);
    }

    public static void givePoints(String name, int amount) {
        givePoints(name, amount, null, null);
    }

    public static void givePoints(String name, int amount, String reason) {
        givePoints(name, amount, reason, null);
    }

    public static void givePoints(String name, int amount, String reason, Runnable asyncCallback) {
        RScheduler.scheduleAsync(plugin, () -> {
            AutoCloseable[] ac_dub2 = SQLManager.prepare("UPDATE main SET rewardPoints = rewardPoints + ? WHERE name = ?");
            PreparedStatement update_rewards = (PreparedStatement) ac_dub2[0];
            try {
                update_rewards.setInt(1, amount);
                update_rewards.setString(2, name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SQLManager.execute(ac_dub2);
            SQLManager.close(ac_dub2);
            Player p = plugin.getServer().getPlayerExact(name);
            if (p != null && p.isOnline()) {
                if (reason != null) {
                    p.sendMessage(reason);
                }
                p.sendMessage(ChatColor.GRAY + "> +" + ChatColor.WHITE + amount + ChatColor.GRAY + " Reward Points");
            }
            if (asyncCallback != null)
                RScheduler.schedule(plugin, asyncCallback);
            System.out.println("Gave " + amount + " reward points to " + name);
        });
    }

    public static void takePoints(String name, int amount, Runnable asyncCallback) {
        RScheduler.scheduleAsync(plugin, () -> {
            AutoCloseable[] ac_dub2 = SQLManager.prepare("UPDATE main SET rewardPoints = rewardPoints - ? WHERE name = ?");
            PreparedStatement update_rewards = (PreparedStatement) ac_dub2[0];
            try {
                update_rewards.setInt(1, amount);
                update_rewards.setString(2, name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SQLManager.execute(ac_dub2);
            SQLManager.close(ac_dub2);
            Player p = plugin.getServer().getPlayerExact(name);
            if (p != null && p.isOnline()) {
                p.sendMessage(ChatColor.GRAY + "> -" + ChatColor.WHITE + amount + ChatColor.GRAY + " Reward Points");
            }
            if (asyncCallback != null)
                RScheduler.schedule(plugin, asyncCallback);
            System.out.println("Removed " + amount + " reward points from " + name);
        });
    }

}
