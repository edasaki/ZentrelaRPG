package com.edasaki.rpg.commands.member;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.core.utils.RMath;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.trades.TradeManager;

public class TradeCommand extends RPGAbstractCommand {

    private HashMap<String, Long> lastCommand = new HashMap<String, Long>();

    public TradeCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

    }

    @Override
    public void executePlayer(final Player p, PlayerDataRPG pd, String[] args) {
        if (p.getWorld().getName().equalsIgnoreCase(SakiRPG.TUTORIAL_WORLD)) {
            p.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "Sorry! You can't use this command in the tutorial!");
            p.sendMessage(ChatColor.GRAY + "> " + ChatColor.AQUA + "Please finish the tutorial first. Feel free to ask for help!");
            return;
        }
        if (lastCommand.containsKey(p.getName()) && System.currentTimeMillis() - lastCommand.get(p.getName()) < 10000) {
            p.sendMessage(ChatColor.RED + "You can only send one trade request every 10 seconds.");
        } else {
            if (args.length < 1) {
                p.sendMessage(ChatColor.RED + "Incorrect command format!");
                p.sendMessage(ChatColor.RED + ">> /trade <name> [message]");
            } else {
                String s = args[0];
                Player target = plugin.getServer().getPlayerExact(s);
                if (target == p) {
                    p.sendMessage(ChatColor.RED + "You can't trade yourself, silly!");
                } else {
                    if (target != null && target.isOnline() && plugin.getPD(target) != null) {
                        PlayerDataRPG pd2 = plugin.getPD(target);
                        if (pd2.isIgnoring(pd)) {
                            pd.sendMessage(ChatColor.RED + pd2.getName() + " is ignoring you and is not receiving your requests.");
                            return;
                        }
                        if (pd != null && pd2.isValid()) {
                            if (pd.region.dangerLevel <= 2 && pd2.region.dangerLevel <= 2) {
                                if (RMath.flatDistance(p.getLocation(), target.getLocation()) < TradeManager.TRADE_RANGE) {
                                    StringBuilder sb = new StringBuilder();
                                    for (int k = 1; k < args.length; k++) {
                                        sb.append(args[k]);
                                        sb.append(' ');
                                    }
                                    TradeManager.offerTrade(p, pd, target, pd2, sb.toString().trim());
                                } else {
                                    p.sendMessage(ChatColor.RED + "You can only trade players within " + TradeManager.TRADE_RANGE + " blocks of you!");
                                }
                            } else {
                                p.sendMessage(ChatColor.RED + "Trades can only be done in Danger Levels 1 & 2.");
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "Could not find online player '" + s + "'.");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "Could not find online player '" + s + "'.");
                    }
                }
            }
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
