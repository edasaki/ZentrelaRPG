package com.edasaki.rpg.commands.member;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.core.utils.fanciful.FancyMessage;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.regions.Region;
import com.edasaki.rpg.regions.RegionManager;
import com.edasaki.rpg.warps.WarpCallback;
import com.edasaki.rpg.warps.WarpManager;

public class TeleportAcceptCommand extends RPGAbstractCommand {

    // Maps from Requester -> Target
    private static HashMap<String, String> tpRequests = new HashMap<String, String>();

    private static HashMap<String, String> accepted = new HashMap<String, String>();
    private static HashMap<String, Long> acceptedTime = new HashMap<String, Long>();

    private static HashMap<String, Long> lastRequest = new HashMap<String, Long>();

    public static void cleanup(String name) {
        tpRequests.remove(name);
        accepted.remove(name);
        acceptedTime.remove(name);
        lastRequest.remove(name);
    }

    public TeleportAcceptCommand(String... commandNames) {
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
        if (args.length != 1 && args.length != 2) {
            p.sendMessage(ChatColor.RED + "Incorrect command format!");
            p.sendMessage(ChatColor.RED + ">> /tpa <name>");
        } else if (args.length == 1) {
            String s = args[0];
            Player target = plugin.getServer().getPlayerExact(s);
            if (target == p) {
                p.sendMessage(ChatColor.RED + "You can't teleport to yourself, silly!");
            } else if (target != null && target.isOnline()) {
                PlayerDataRPG pd2 = plugin.getPD(target);
                if (pd2.isIgnoring(pd)) {
                    pd.sendMessage(ChatColor.RED + pd2.getName() + " is ignoring you and is not receiving your requests.");
                    return;
                }
                if (pd2 == null || !target.getWorld().equals(p.getWorld())) {
                    p.sendMessage(ChatColor.RED + "That player is currently in a place you can't teleport to!");
                } else {
                    if (lastRequest.containsKey(p.getName()) && System.currentTimeMillis() - lastRequest.get(p.getName()) < 15000) {
                        p.sendMessage(ChatColor.RED + "You can only send a teleport request once every 15 seconds!");
                    } else {
                        lastRequest.put(p.getName(), System.currentTimeMillis());
                        target.sendMessage("");
                        target.sendMessage(ChatColor.GRAY + "> " + ChatColor.AQUA + p.getName() + ChatColor.GREEN + " wants to teleport to you!");
                        FancyMessage fm = new FancyMessage();
                        fm.text(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Click here " + ChatColor.YELLOW + "to allow " + p.getName() + " to teleport to you.");
                        fm.color(ChatColor.YELLOW);
                        fm.command("/tpa -accept- " + p.getName());
                        fm.send(target);
                        target.sendMessage("");
                        p.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "You have sent " + target.getName() + " a teleport request.");
                        tpRequests.put(p.getName(), target.getName());
                    }
                }
            } else {
                p.sendMessage(ChatColor.RED + "Could not find online player '" + s + "'.");
            }
        } else if (args.length == 2) {
            if (args[0].equals("-accept-")) {
                Player target = plugin.getServer().getPlayerExact(args[1]);
                if (target == p) {
                    p.sendMessage(ChatColor.RED + "You can't teleport to yourself, silly!");
                } else if (target != null && target.isOnline()) {
                    if (tpRequests.containsKey(target.getName()) && tpRequests.remove(target.getName()).equals(p.getName())) {
                        PlayerDataRPG pd2 = plugin.getPD(target);
                        if (pd2 == null || !target.getWorld().equals(p.getWorld())) {
                            p.sendMessage(ChatColor.RED + "That player is currently in a place where they cannot teleport to you!");
                        } else {
                            accepted.put(target.getName(), p.getName());
                            acceptedTime.put(target.getName(), System.currentTimeMillis());
                            target.sendMessage(ChatColor.GRAY + "> " + ChatColor.AQUA + p.getName() + ChatColor.GREEN + " has accepted your teleport request!");
                            FancyMessage fm = new FancyMessage();
                            fm.text(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Click here " + ChatColor.YELLOW + "to teleport to " + p.getName() + ". Stand still!");
                            fm.command("/tpa -confirm- " + p.getName());
                            fm.send(target);
                            target.sendMessage("");
                            p.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "You have accepted " + target.getName() + "'s teleport request.");
                            p.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "They can now teleport to you within the next 60 seconds.");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "That player's teleport request has expired.");
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Could not find online player '" + args[1] + "'.");
                }
            } else if (args[0].equals("-confirm-")) {
                Player target = plugin.getServer().getPlayerExact(args[1]);
                if (target == p) {
                    p.sendMessage(ChatColor.RED + "You can't teleport to yourself, silly!");
                } else if (target != null && target.isOnline()) {
                    PlayerDataRPG pd2 = plugin.getPD(target);
                    if (pd2 == null || !target.getWorld().equals(p.getWorld())) {
                        p.sendMessage(ChatColor.RED + "That player is currently in a place where they cannot teleport to you!");
                    } else {
                        if (accepted.containsKey(p.getName()) && accepted.get(p.getName()).equals(target.getName())) {
                            if (acceptedTime.containsKey(p.getName()) && System.currentTimeMillis() - acceptedTime.get(p.getName()) < 60000) {
                                p.sendMessage(ChatColor.GRAY + "> " + ChatColor.YELLOW + "Teleporting to " + target.getName() + " now... Please stand still.");
                                Location loc = target.getLocation();
                                Region r = RegionManager.getRegion(loc);
                                p.sendMessage("");
                                p.sendMessage(ChatColor.GRAY + "> " + ChatColor.YELLOW + "Your destination has a recommended level of " + ChatColor.WHITE + ChatColor.BOLD + r.recLevel + ChatColor.YELLOW + ".");
                                if (r.dangerLevel == 3) {
                                    p.sendMessage("");
                                    p.sendMessage(ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + " WARNING. You are teleporting to a Danger Level 3 area, where PvP is allowed!");
                                    p.sendMessage("");
                                } else if (r.dangerLevel == 4) {
                                    p.sendMessage("");
                                    p.sendMessage(ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + " WARNING. You are teleporting to a Danger Level 4 area, where" + ChatColor.RED + ChatColor.BOLD + " YOU WILL LOSE ITEMS " + ChatColor.DARK_AQUA + ChatColor.BOLD + "if you die in PvP!");
                                    p.sendMessage("");
                                }
                                p.sendMessage(ChatColor.GRAY + "> " + ChatColor.YELLOW + "If you wish to cancel your teleport, move 1 block or more.");
                                WarpManager.warp(p, loc, new WarpCallback() {
                                    @Override
                                    public void complete(boolean warpSuccess) {
                                        if (warpSuccess) {
                                            target.sendMessage(ChatColor.GRAY + "> " + ChatColor.YELLOW + p.getName() + " has just teleported to you!");
                                            accepted.remove(p.getName());
                                            acceptedTime.remove(p.getName());
                                        }
                                    }
                                }, null, 7);
                            } else {
                                p.sendMessage("");
                                p.sendMessage(ChatColor.RED + "Your permission to teleport to " + target.getName() + " has expired!");
                                p.sendMessage(ChatColor.RED + "Automatically sending a new request...");
                                p.performCommand("/tpa " + target.getName());
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "That player's teleport request has expired.");
                        }
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Could not find online player '" + args[1] + "'.");
                }
            } else {
                p.sendMessage(ChatColor.RED + "Incorrect command format!");
                p.sendMessage(ChatColor.RED + ">> /tpa <name>");
            }
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
