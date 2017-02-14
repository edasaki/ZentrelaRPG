package com.edasaki.rpg.commands.owner;

import java.lang.reflect.Field;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.core.PlayerData;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class ReflectGetCommand extends RPGAbstractCommand {

    public ReflectGetCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        try {
            if (args.length == 1) {
                try {
                    Field f = PlayerDataRPG.class.getDeclaredField(args[0]);
                    f.setAccessible(true);
                    Object o = f.get(pd);
                    p.sendMessage("Value is " + o);
                } catch (Exception e) {
                    try {
                        Field f = PlayerData.class.getDeclaredField(args[0]);
                        f.setAccessible(true);
                        Object o = f.get(pd);
                        p.sendMessage("Value is " + o);
                    } catch (Exception e2) {
                        e.printStackTrace();
                        e2.printStackTrace();
                    }
                }
            } else if (args.length == 2) {
                pd = plugin.getPD(plugin.getServer().getPlayer(args[0]));
                if (pd == null) {
                    p.sendMessage("Could not get data for player " + args[0]);
                } else {
                    try {
                        Field f = PlayerDataRPG.class.getDeclaredField(args[1]);
                        f.setAccessible(true);
                        Object o = f.get(pd);
                        p.sendMessage("Value is " + o);
                    } catch (Exception e) {
                        try {
                            Field f = PlayerData.class.getDeclaredField(args[1]);
                            f.setAccessible(true);
                            Object o = f.get(pd);
                            p.sendMessage("Value is " + o);
                        } catch (Exception e2) {
                            e.printStackTrace();
                            e2.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
        try {
            if (args.length == 2) {
                PlayerDataRPG pd = plugin.getPD(plugin.getServer().getPlayer(args[0]));
                if (pd == null) {
                    sender.sendMessage("Could not get data for player " + args[0]);
                } else {
                    try {
                        Field f = PlayerDataRPG.class.getDeclaredField(args[1]);
                        f.setAccessible(true);
                        Object o = f.get(pd);
                        sender.sendMessage("Value is " + o);
                    } catch (Exception e) {
                        try {
                            Field f = PlayerData.class.getDeclaredField(args[1]);
                            f.setAccessible(true);
                            Object o = f.get(pd);
                            sender.sendMessage("Value is " + o);
                        } catch (Exception e2) {
                            e.printStackTrace();
                            e2.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
