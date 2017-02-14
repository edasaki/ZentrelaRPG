package com.edasaki.rpg.commands.owner;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.edasaki.core.sql.SQLManager;
import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RMessages;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.items.EquipType;
import com.edasaki.rpg.items.ItemBalance;
import com.edasaki.rpg.items.ItemManager;
import com.edasaki.rpg.items.RandomItemGenerator;
import com.edasaki.rpg.utils.RSerializer;

public class RerollCommand extends RPGAbstractCommand {

    public RerollCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("confirm")) {
            p.sendMessage(ChatColor.GREEN + "Beginning server-wide reroll...");
            long start = System.currentTimeMillis();
            RScheduler.scheduleAsync(plugin, () -> {
                AutoCloseable[] ac_dub = SQLManager.prepare("select name, inventory, bank from main;");
                try {
                    PreparedStatement request_items = (PreparedStatement) ac_dub[0];
                    AutoCloseable[] ac_trip = SQLManager.executeQuery(request_items);
                    ResultSet rs = (ResultSet) ac_trip[0];
                    RMessages.announce("Finished query: " + (System.currentTimeMillis() - start) + "ms");
                    int count = 0;
                    while (rs.next()) {
                        String name = rs.getString("name");
                        String inventory = rs.getString("inventory");
                        String bank = rs.getString("bank");
                        RScheduler.schedule(plugin, () -> {
                            p.getInventory().clear();
                            if (inventory != null) {
                                HashMap<Integer, ItemStack> dInv = deserializeInventory(inventory);
                                if (dInv != null) {
                                    for (Entry<Integer, ItemStack> e : dInv.entrySet()) {
                                        p.getInventory().setItem(e.getKey(), e.getValue());
                                    }
                                }
                            }
                            pd.bank.clear();
                            if (bank != null) {
                                HashMap<Integer, ItemStack> dBank = deserializeBank(bank);
                                if (dBank != null) {
                                    for (Entry<Integer, ItemStack> e : dBank.entrySet()) {
                                        pd.bank.setItem(e.getKey(), e.getValue());
                                    }
                                }
                            }
                            RMessages.announce("reroll " + name);
                            reroll(p.getInventory());
                            reroll(pd.bank);
                            final String fInv = serializeInventory(p.getInventory());
                            final String fBank = serializeBank(pd.bank);
                            RScheduler.scheduleAsync(plugin, () -> {
                                p.sendMessage("Running query...");
                                String query = "update main set inventory = ?, bank = ? where name = ?";
                                AutoCloseable[] ac_dub2 = SQLManager.prepare(query);
                                PreparedStatement stmt = (PreparedStatement) ac_dub2[0];
                                try {
                                    stmt.setString(1, fInv);
                                    stmt.setString(2, fBank);
                                    stmt.setString(3, name);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                SQLManager.execute(ac_dub2);
                                SQLManager.close(ac_dub2);
                                p.sendMessage("Modified " + args[1] + "'s inventory");
                            });
                        }, count++);
                    }
                    SQLManager.close(ac_dub);
                    SQLManager.close(ac_trip);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else {
            reroll(p.getInventory());
            reroll(pd.bank);
        }

    }

    private void reroll(Inventory inv) {
        for (int k = 0; k < inv.getSize(); k++) {
            ItemStack item = inv.getItem(k);
            if (item != null) {
                inv.setItem(k, reroll(item));
            }
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

    public static ItemStack reroll(ItemStack item) {
        if (!item.hasItemMeta())
            return item;
        if (!item.getItemMeta().hasDisplayName())
            return item;
        if (!item.getItemMeta().hasLore())
            return item;
        ItemMeta im = item.getItemMeta();
        int tier = -1;
        int rarity = 0;
        String setName = null;
        for (int k = 1; k < ItemBalance.RARITY_NAMES.length; k++) {
            if (im.getDisplayName().contains(ItemBalance.RARITY_NAMES[k])) {
                rarity = k;
            }
        }
        for (String s : ItemBalance.SET_PREFIXES) {
            if (im.getDisplayName().contains(s))
                setName = s;
        }
        boolean named = false;
        for (String s : ItemBalance.SAGE_NAMES) {
            if (im.getDisplayName().contains(s))
                named = true;
        }
        String identifier = null;
        for (Entry<String, String> e : ItemManager.itemNameToIdentifierMap.entrySet()) {
            String id = e.getKey().replaceAll(ChatColor.RESET.toString(), "");
            String name = im.getDisplayName().replaceAll(ChatColor.RESET.toString(), "");
            if (id.equals(name)) {
                identifier = e.getValue();
            }
        }
        if (identifier != null) {
            RMessages.announce(im.getDisplayName() + ", detected preset item");
            if (identifier.equalsIgnoreCase("needle") && im.getLore().toString().contains("Tier 5")) {
                identifier += "_2";
            }
            return ItemManager.getItemForIdentifier(identifier);
        } else {
            for (String s : im.getLore()) {
                if (s.contains("Tier"))
                    tier = Integer.parseInt(ChatColor.stripColor(s).replaceAll("[^0-9]", ""));
            }
            EquipType et = null;
            for (EquipType et2 : EquipType.values()) {
                if (et2.isType(item)) {
                    et = et2;
                    break;
                }
            }
            if (im.getDisplayName().contains("Elixir"))
                et = EquipType.ELIXIR;
            if (et != null && tier > 0) {
                RMessages.announce(im.getDisplayName() + ", Tier " + tier + ", Rarity " + rarity + " (" + ItemBalance.RARITY_NAMES[rarity] + "), Set Name " + setName + ", named? " + named);
                return RandomItemGenerator.generateEquip(et, getRandomLevel(tier), 0, rarity, setName, named);
            } else {
                RMessages.announce("failed to reroll " + im.getDisplayName());
            }
        }
        return item;
    }

    public static int getRandomLevel(int tier) {
        switch (tier) {
            default:
                RMessages.announce("WARNING REROLLED TIER " + tier);
            case 1:
                return RMath.randInt(1, 19);
            case 2:
                return RMath.randInt(20, 39);
            case 3:
                return RMath.randInt(40, 59);
            case 4:
                return RMath.randInt(60, 79);
            case 5:
                return RMath.randInt(80, 105);
        }
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

    private String serializeInventory(Inventory inv) {
        StringBuilder sb = new StringBuilder();
        ItemStack[] arr = inv.getContents();
        for (int k = 0; k < arr.length; k++) {
            if (arr[k] != null) {
                sb.append(k);
                sb.append("::");
                sb.append(RSerializer.serializeItemStack(arr[k]));
                sb.append("@");
            }
        }
        String s = sb.toString().trim();
        if (s.endsWith("@"))
            s = s.substring(0, s.length() - 1);
        return s;
    }

    private String serializeBank(Inventory inv) {
        StringBuilder sb = new StringBuilder();
        ItemStack[] arr = inv.getContents();
        for (int k = 0; k < arr.length; k++) {
            if (arr[k] != null) {
                sb.append(k);
                sb.append("::");
                sb.append(RSerializer.serializeItemStack(arr[k]));
                sb.append("@");
            }
        }
        String s = sb.toString().trim();
        if (s.endsWith("@"))
            s = s.substring(0, s.length() - 1);
        return s;
    }

}
