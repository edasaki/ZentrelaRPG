package com.edasaki.rpg.commands.beta;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.items.ItemManager;
import com.edasaki.rpg.items.RPGItem;

public class TestEquipsCommand extends RPGAbstractCommand {

    public TestEquipsCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        //        EquipItem ei = new EquipItem(EquipType.CHESTPLATE, 1);
        //        ei.name = ChatColor.YELLOW + "Noob Testin' Chestplate";
        //        //        ei.maxHP = 100;
        //        //        ei.maxHPMultiplier = 10;
        //        //        ei.defense = 1;
        //        //        ei.defenseMultiplier = 10;
        //        pd.getPlayer().getInventory().addItem(ei.generate());
        //
        //        ei = new EquipItem(EquipType.BOOTS, 1);
        //        ei.name = ChatColor.YELLOW + "Noob Testin' Boots";
        //        pd.getPlayer().getInventory().addItem(ei.generate());
        //
        //        ei = new EquipItem(EquipType.SWORD, 1);
        //        ei.name = ChatColor.YELLOW + "Noob Testin' Sword";
        //        //        ei.damageLow = 1;
        //        //        ei.damageHigh = 10;
        //        //        ei.critChance = 10;
        //        pd.getPlayer().getInventory().addItem(ei.generate());
        //
        //        ei = new EquipItem(EquipType.BOW, 1);
        //        ei.name = ChatColor.YELLOW + "Noob Testin' Bow";
        //        //        ei.damageLow = 1;
        //        //        ei.damageHigh = 10;
        //        //        ei.critChance = 70;
        //        pd.getPlayer().getInventory().addItem(ei.generate());

        RPGItem item = ItemManager.itemIdentifierToRPGItemMap.get("hp_potion_1");
        pd.getPlayer().getInventory().addItem(item.generate());
        pd.getPlayer().getInventory().addItem(item.generate());
        pd.getPlayer().getInventory().addItem(item.generate());
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
