package com.edasaki.rpg.commands.beta;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.items.ItemManager;
import com.edasaki.rpg.items.RPGItem;

public class TestPotionsCommand extends RPGAbstractCommand {

    public TestPotionsCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        while (pd.getPlayer().getInventory().firstEmpty() != -1) {
            RPGItem item = ItemManager.itemIdentifierToRPGItemMap.get("hp_potion_4");
            pd.getPlayer().getInventory().addItem(item.generate());
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
