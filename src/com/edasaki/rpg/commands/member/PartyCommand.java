package com.edasaki.rpg.commands.member;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.core.utils.RSound;
import com.edasaki.core.utils.fanciful.FancyMessage;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.parties.PartyManager;

public class PartyCommand extends RPGAbstractCommand {

    public PartyCommand(String... commandNames) {
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
        if (args.length == 0) {
            p.sendMessage("");
            p.sendMessage(ChatColor.YELLOW + " == " + ChatColor.GREEN + "Zentrela Party System" + ChatColor.YELLOW + " == ");
            p.sendMessage(ChatColor.DARK_AQUA + " Anyone can create or join a party.");
            p.sendMessage(ChatColor.BLUE + " Party members are immune to each other's damage.");
            p.sendMessage(ChatColor.AQUA + " Party members can talk in private with Party Chat.");
            p.sendMessage("");
            p.sendMessage(ChatColor.LIGHT_PURPLE + " /party help " + ChatColor.YELLOW + "-" + ChatColor.WHITE + " View all party commands");
            p.sendMessage(ChatColor.LIGHT_PURPLE + " /party bonus " + ChatColor.YELLOW + "-" + ChatColor.WHITE + " View party EXP bonuses");
            p.sendMessage("");
            FancyMessage fm = new FancyMessage();
            fm.text(" Click here ").color(ChatColor.YELLOW).command("/party create");
            fm.then("to create a new party!").color(ChatColor.GREEN).command("/party create");
            fm.send(p);
            p.sendMessage("");
        } else {
            if (args[0].equalsIgnoreCase("help")) {
                p.sendMessage("");
                p.sendMessage(ChatColor.YELLOW + " == " + ChatColor.GREEN + "Zentrela Party Commands" + ChatColor.YELLOW + " == ");
                p.sendMessage("");
                p.sendMessage(ChatColor.LIGHT_PURPLE + " /party create " + ChatColor.YELLOW + "-" + ChatColor.WHITE + " Create a new party");
                p.sendMessage(ChatColor.LIGHT_PURPLE + " /party invite <name> " + ChatColor.YELLOW + "-" + ChatColor.WHITE + " Invite a player to your party.");
                p.sendMessage(ChatColor.LIGHT_PURPLE + " /party leave " + ChatColor.YELLOW + "-" + ChatColor.WHITE + " Leave your party");
                p.sendMessage(ChatColor.LIGHT_PURPLE + " /party kick <name> " + ChatColor.YELLOW + "-" + ChatColor.WHITE + " Kick a player. (Party leader only)");
                p.sendMessage(ChatColor.LIGHT_PURPLE + " /party lootshare" + ChatColor.YELLOW + "-" + ChatColor.WHITE + " Toggle Lootshare. (Party leader only)");
                p.sendMessage("");
                p.sendMessage(ChatColor.AQUA + " Use Party Chat by adding a \\ to the front of your message.");
                p.sendMessage(ChatColor.GRAY + " Example:" + ChatColor.WHITE + " \\Come on guys, let's go!");
                p.sendMessage("");
            } else if (args[0].equalsIgnoreCase("bonus") || args[0].equalsIgnoreCase("bonuses")) {
                p.sendMessage("");
                p.sendMessage(ChatColor.YELLOW + " == " + ChatColor.GREEN + "Zentrela Party EXP Bonuses" + ChatColor.YELLOW + " == ");
                p.sendMessage("");
                p.sendMessage(ChatColor.DARK_AQUA + " Being in a party grants EXP bonuses!");
                p.sendMessage(ChatColor.YELLOW + " 2 Player Party " + ChatColor.GREEN + "=" + ChatColor.GOLD + " +10% EXP");
                p.sendMessage(ChatColor.YELLOW + " 3 Player Party " + ChatColor.GREEN + "=" + ChatColor.GOLD + " +15% EXP");
                p.sendMessage(ChatColor.YELLOW + " 4 Player Party " + ChatColor.GREEN + "=" + ChatColor.GOLD + " +20% EXP");
                p.sendMessage(ChatColor.YELLOW + " 5+ Player Party " + ChatColor.GREEN + "=" + ChatColor.GOLD + " +25% EXP");
            } else if (args[0].equalsIgnoreCase("create")) {
                PartyManager.createParty(p, pd);
            } else if (args[0].equalsIgnoreCase("invite")) {
                if (pd.party != null) {
                    if (args.length != 2) {
                        p.sendMessage(ChatColor.RED + "Invalid syntax. /party invite <name>");
                    } else {
                        String name = args[1];
                        Player target = plugin.getServer().getPlayer(name);
                        PlayerDataRPG pd2 = plugin.getPD(target);
                        if (pd2 == null || target == null || !target.isOnline()) {
                            p.sendMessage(ChatColor.RED + "Could not find online player " + name + ".");
                        } else {
                            if (pd2.isIgnoring(pd)) {
                                pd.sendMessage(ChatColor.RED + pd2.getName() + " is ignoring you and is not receiving your requests.");
                                return;
                            }
                            if (pd.party.isFull()) {
                                p.sendMessage(ChatColor.RED + "The party is full! No more players can be invited.");
                            } else {
                                pd.party.sendInvite(target, pd2, p);
                            }
                        }
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "You are not in a party! Create a party with " + ChatColor.YELLOW + "/party create" + ChatColor.RED + "!");
                }
            } else if (args[0].equalsIgnoreCase("accept")) {
                if (pd.invitedParty == null) {
                    p.sendMessage(ChatColor.RED + "You have not been invited to any party!");
                } else {
                    if (pd.party != null) {
                        p.sendMessage(ChatColor.RED + "You are already in a party!");
                    } else {
                        pd.invitedParty.addPlayer(p);
                        pd.party = pd.invitedParty;
                        pd.invitedParty = null;
                    }
                }
            } else if (args[0].equalsIgnoreCase("decline")) {
                if (pd.invitedParty != null)
                    pd.invitedParty.sendMessage(p.getName() + " has declined the party invitation.");
                pd.invitedParty = null;
                p.sendMessage(ChatColor.GRAY + ">> " + ChatColor.GREEN + "You have declined the party invitation.");
            } else if (args[0].equalsIgnoreCase("leave")) {
                if (pd.party == null) {
                    p.sendMessage(ChatColor.RED + "You are not in a party!");
                } else {
                    pd.party.leavePlayer(p.getUniqueId(), p.getName());
                }
            } else if (args[0].equalsIgnoreCase("kick")) {
                if (pd.party == null) {
                    p.sendMessage(ChatColor.RED + "You are not in a party!");
                } else {
                    if (!pd.party.isLeader(p)) {
                        p.sendMessage(ChatColor.RED + "You are not the party leader!");
                    } else {
                        String name = args[1];
                        Player target = plugin.getServer().getPlayer(name);
                        PlayerDataRPG pd2 = plugin.getPD(target);
                        if (pd2 == null || target == null || !target.isOnline()) {
                            p.sendMessage(ChatColor.RED + "Could not find online player " + name + ".");
                        } else {
                            if (pd2.party != pd.party) {
                                p.sendMessage(ChatColor.RED + name + " is not in your party.");
                            } else {
                                if (target == p || pd2 == pd) {
                                    p.sendMessage(ChatColor.RED + "You cannot kick yourself! Use " + ChatColor.YELLOW + "/party leave" + ChatColor.RED + " instead.");
                                } else {
                                    RSound.playSound(target, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
                                    pd.party.kickPlayer(target);
                                }
                            }
                        }
                    }
                }
            } else if (args[0].equalsIgnoreCase("lootshare")) {
                if (pd.party == null) {
                    p.sendMessage(ChatColor.RED + "You are not in a party!");
                } else {
                    if (!pd.party.isLeader(p)) {
                        p.sendMessage(ChatColor.RED + "You are not the party leader!");
                    } else {
                        pd.party.toggleLootshare();
                    }
                }
            } else {
                if (pd.party != null) {
                    StringBuilder sb = new StringBuilder("");
                    for (String s : args) {
                        sb.append(s + " ");
                    }
                    String message = sb.toString().trim();
                    pd.party.sendMessage(p, message);
                } else {
                    p.sendMessage(ChatColor.RED + "That is not a valid command!");
                    p.sendMessage(ChatColor.RED + "Check " + ChatColor.YELLOW + "/party help" + ChatColor.RED + " for more info on parties!");
                }
            }
        }

    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}