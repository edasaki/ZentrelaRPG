package com.edasaki.rpg.commands.owner;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.items.EquipType;
import com.edasaki.rpg.items.RandomItemGenerator;

public class GenerateItemsCommand extends RPGAbstractCommand {

    public GenerateItemsCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        if (args.length != 2 && args.length != 3) {
            p.sendMessage("Use as /generateitems [type] [level] [amount]");
        }
        try {
            int amount = args.length == 3 ? Integer.parseInt(args[2]) : 1;
            int level = Integer.parseInt(args[1]);
            if (args[0].equalsIgnoreCase("set")) {
                for (int k = 0; k < amount; k++) {
                    p.getInventory().addItem(RandomItemGenerator.generateEquip(EquipType.HELMET, level));
                    p.getInventory().addItem(RandomItemGenerator.generateEquip(EquipType.CHESTPLATE, level));
                    p.getInventory().addItem(RandomItemGenerator.generateEquip(EquipType.LEGGINGS, level));
                    p.getInventory().addItem(RandomItemGenerator.generateEquip(EquipType.BOOTS, level));
                }
            } else {
                EquipType et = EquipType.valueOf(args[0].toUpperCase());
                if (amount > p.getInventory().getSize())
                    amount = p.getInventory().getSize();
                for (int k = 0; k < amount; k++)
                    p.getInventory().addItem(RandomItemGenerator.generateEquip(et, level));
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
