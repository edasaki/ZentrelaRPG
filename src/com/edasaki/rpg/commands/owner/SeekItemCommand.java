package com.edasaki.rpg.commands.owner;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.items.EquipType;
import com.edasaki.rpg.items.ItemBalance;
import com.edasaki.rpg.items.RandomItemGenerator;

public class SeekItemCommand extends RPGAbstractCommand {

    public SeekItemCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        if (args.length != 4) {
            p.sendMessage("Use as /generateitems [type] [level] [amount] [rarity]");
        }
        try {
            int amount = Integer.parseInt(args[2]);
            int level = Integer.parseInt(args[1]);
            String rarity = args[3];
            if (args[0].equalsIgnoreCase("set")) {
                int rarityVal = 0;
                for (int k = 0; k < ItemBalance.RARITY_NAMES.length; k++) {
                    if (ItemBalance.RARITY_NAMES[k].equalsIgnoreCase(rarity)) {
                        rarityVal = k;
                        break;
                    }
                }
                for (int k = 0; k < amount; k++) {
                    p.getInventory().addItem(RandomItemGenerator.generateEquip(EquipType.HELMET, level, 0, rarityVal));
                    p.getInventory().addItem(RandomItemGenerator.generateEquip(EquipType.CHESTPLATE, level, 0, rarityVal));
                    p.getInventory().addItem(RandomItemGenerator.generateEquip(EquipType.LEGGINGS, level, 0, rarityVal));
                    p.getInventory().addItem(RandomItemGenerator.generateEquip(EquipType.BOOTS, level, 0, rarityVal));
                }
            } else {

                EquipType et = EquipType.valueOf(args[0].toUpperCase());
                int rarityVal = 0;
                for (int k = 0; k < ItemBalance.RARITY_NAMES.length; k++) {
                    if (ItemBalance.RARITY_NAMES[k].equalsIgnoreCase(rarity)) {
                        rarityVal = k;
                        break;
                    }
                }
                for (int k = 0; k < amount; k++)
                    p.getInventory().addItem(RandomItemGenerator.generateEquip(et, level, 0, rarityVal));
            }
        } catch (Exception e) {
            e.printStackTrace();
            p.sendMessage("Invalid type. Possible types are: ");
            StringBuilder sb = new StringBuilder();
            for (EquipType et : EquipType.values())
                sb.append(et + " ");
            p.sendMessage(sb.toString().trim().replace(" ", ", "));
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
