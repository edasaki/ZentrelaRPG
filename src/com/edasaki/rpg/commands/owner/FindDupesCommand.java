package com.edasaki.rpg.commands.owner;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.edasaki.core.sql.SQLManager;
import com.edasaki.core.utils.RMessages;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.utils.RSerializer;

public class FindDupesCommand extends RPGAbstractCommand {

    public FindDupesCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        p.sendMessage(ChatColor.GREEN + "Seeking duped items...");
        StringBuilder sb2 = new StringBuilder();
        for (int k = 0; k < args.length; k++) {
            sb2.append(args[k]);
            sb2.append(' ');
        }
        String toFilter = sb2.toString().trim();
        long start = System.currentTimeMillis();
        RScheduler.scheduleAsync(plugin, () -> {
            AutoCloseable[] ac_dub = SQLManager.prepare("select name, inventory, bank from main;");
            try {
                PreparedStatement request_items = (PreparedStatement) ac_dub[0];
                AutoCloseable[] ac_trip = SQLManager.executeQuery(request_items);
                ResultSet rs = (ResultSet) ac_trip[0];
                RMessages.announce("Finished query: " + (System.currentTimeMillis() - start) + "ms");
                int count = 0;
                HashMap<String, Integer> map = new HashMap<String, Integer>();
                while (rs.next()) {
                    String name = rs.getString("name");
                    String inventory = rs.getString("inventory");
                    String bank = rs.getString("bank");
                    //                    System.out.println(name == null ? "" : name);
                    //                    System.out.println(inventory == null ? "" : inventory);
                    //                    System.out.println(bank == null ? "" : bank);
                    RScheduler.schedule(plugin, () -> {
                        p.getInventory().clear();
                        if (inventory != null) {
                            HashMap<Integer, ItemStack> dInv = deserializeInventory(inventory);
                            if (dInv != null) {
                                for (Entry<Integer, ItemStack> e : dInv.entrySet()) {
                                    p.getInventory().setItem(e.getKey(), e.getValue());
                                    ItemStack item = e.getValue();
                                    StringBuilder sb = new StringBuilder();
                                    if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                                        sb.append(item.getItemMeta().getDisplayName());
                                        sb.append('#');
                                    } else {
                                        sb.append(item.getType());
                                        sb.append('#');
                                    }
                                    if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                                        for (String s : item.getItemMeta().getLore()) {
                                            sb.append(s);
                                            sb.append('@');
                                        }
                                    }
                                    String key = sb.toString().trim();
                                    map.compute(key, (k, v) -> {
                                        if (v == null)
                                            return 1;
                                        return v + 1;
                                    });
                                }
                            }
                        }
                        pd.bank.clear();
                        if (bank != null) {
                            HashMap<Integer, ItemStack> dBank = deserializeBank(bank);
                            if (dBank != null) {
                                for (Entry<Integer, ItemStack> e : dBank.entrySet()) {
                                    pd.bank.setItem(e.getKey(), e.getValue());
                                    ItemStack item = e.getValue();
                                    StringBuilder sb = new StringBuilder();
                                    if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                                        sb.append(item.getItemMeta().getDisplayName());
                                        sb.append('#');
                                    } else {
                                        sb.append(item.getType());
                                        sb.append('#');
                                    }
                                    if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                                        for (String s : item.getItemMeta().getLore()) {
                                            sb.append(s);
                                            sb.append('@');
                                        }
                                    }
                                    String key = sb.toString().trim();
                                    map.compute(key, (k, v) -> {
                                        if (v == null)
                                            return 1;
                                        return v + 1;
                                    });
                                }
                            }
                        }
                        RMessages.announce("set inventory and bank to " + name);
                    }, count++);
                }
                RScheduler.schedule(plugin, new Runnable() {
                    public void run() {
                        RMessages.announce("sorting... " + map.size());
                        for (Entry<String, Integer> e : map.entrySet()) {
                            if (e.getValue() > 1) {
                                if (toFilter.length() == 0 || (toFilter.length() > 0 && (e.getValue() + "- " + e.getKey()).matches(toFilter))) {
                                    RMessages.announce(e.getValue() + "- " + e.getKey());
                                }
                            }
                        }
                        RMessages.announce("done!");
                    }
                }, count++);
                SQLManager.close(ac_dub);
                SQLManager.close(ac_trip);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

    private HashMap<Integer, ItemStack> deserializeInventory(String s) {
        HashMap<Integer, ItemStack> map = new HashMap<Integer, ItemStack>();
        if (s == null)
            return map;
        String[] data = s.split("@");
        if (data.length == 0 || (data.length == 1 && data[0].equals("")))
            return map;
        for (String temp : data) {
            try {
                // don't use split in case item serialization contains ::
                String a = temp.substring(0, temp.indexOf("::"));
                String b = temp.substring(temp.indexOf("::") + "::".length());
                int k = Integer.parseInt(a);
                ItemStack item = RSerializer.deserializeItemStack(b);
                map.put(k, item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    private HashMap<Integer, ItemStack> deserializeBank(String s) {
        HashMap<Integer, ItemStack> map = new HashMap<Integer, ItemStack>();
        if (s == null)
            return null;
        String[] data = s.split("@");
        if (data.length == 0 || (data.length == 1 && data[0].equals("")))
            return null;
        for (String temp : data) {
            try {
                // don't use split in case item serialization contains ::
                String a = temp.substring(0, temp.indexOf("::"));
                String b = temp.substring(temp.indexOf("::") + "::".length());
                int k = Integer.parseInt(a);
                ItemStack item = RSerializer.deserializeItemStack(b);
                map.put(k, item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

}
