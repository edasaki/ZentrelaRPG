package com.edasaki.rpg.commands.owner;

import java.lang.reflect.Field;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.core.SakiCore;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.classes.ClassType;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class ReflectCommand extends RPGAbstractCommand {

    public ReflectCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        try {
            if (args.length == 2) {
                Field f = SakiCore.getPDClass().getDeclaredField(args[0]);
                f.setAccessible(true);
                Object o = f.get(pd);
                Class<?> c = f.getType();
                if (c == int.class || c == Integer.class) {
                    f.setInt(pd, Integer.parseInt(args[1]));
                } else if (c == long.class || c == Long.class) {
                    f.setLong(pd, Long.parseLong(args[1]));
                } else if (c == double.class || c == Double.class) {
                    f.setDouble(pd, Double.parseDouble(args[1]));
                } else if (c == String.class) {
                    f.set(pd, args[1]);
                } else if (c == boolean.class) {
                    f.setBoolean(pd, Boolean.parseBoolean(args[1]));
                } else if (c == ClassType.class) {
                    f.set(pd, ClassType.getClassType(args[1]));
                }
                p.sendMessage("Updated value from " + o + " to " + f.get(pd));
            } else if (args.length == 3) {
                pd = plugin.getPD(plugin.getServer().getPlayer(args[0]));
                if (pd == null) {
                    p.sendMessage("Could not get data for player " + args[0]);
                } else {
                    Field f = PlayerDataRPG.class.getDeclaredField(args[1]);
                    f.setAccessible(true);
                    Object o = f.get(pd);
                    Class<?> c = f.getType();
                    if (c == int.class || c == Integer.class) {
                        f.setInt(pd, Integer.parseInt(args[2]));
                    } else if (c == long.class || c == Long.class) {
                        f.setLong(pd, Long.parseLong(args[2]));
                    } else if (c == double.class || c == Double.class) {
                        f.setDouble(pd, Double.parseDouble(args[2]));
                    } else if (c == String.class) {
                        f.set(pd, args[2]);
                    } else if (c == boolean.class) {
                        f.setBoolean(pd, Boolean.parseBoolean(args[2]));
                    } else if (c == ClassType.class) {
                        f.set(pd, ClassType.getClassType(args[1]));
                    }
                    p.sendMessage("Updated value for " + args[0] + " from " + o + " to " + f.get(pd));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
        try {
            if (args.length == 3) {
                PlayerDataRPG pd = plugin.getPD(plugin.getServer().getPlayer(args[0]));
                if (pd == null) {
                    sender.sendMessage("Could not get data for player " + args[0]);
                } else {
                    Field f = PlayerDataRPG.class.getDeclaredField(args[1]);
                    f.setAccessible(true);
                    Object o = f.get(pd);
                    Class<?> c = f.getType();
                    if (c == int.class || c == Integer.class) {
                        f.setInt(pd, Integer.parseInt(args[2]));
                    } else if (c == long.class || c == Long.class) {
                        f.setLong(pd, Long.parseLong(args[2]));
                    } else if (c == double.class || c == Double.class) {
                        f.setDouble(pd, Double.parseDouble(args[2]));
                    } else if (c == String.class) {
                        f.set(pd, args[2]);
                    } else if (c == boolean.class) {
                        f.setBoolean(pd, Boolean.parseBoolean(args[2]));
                    }
                    sender.sendMessage("Updated value for " + args[0] + " from " + o + " to " + f.get(pd));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
