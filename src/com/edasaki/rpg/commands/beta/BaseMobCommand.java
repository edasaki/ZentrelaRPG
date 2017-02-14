package com.edasaki.rpg.commands.beta;

import java.util.ArrayList;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.edasaki.core.utils.entities.CustomZombie;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.mobs.MobAttribute;
import com.edasaki.rpg.mobs.MobBalance;
import com.edasaki.rpg.mobs.MobDrop;
import com.edasaki.rpg.mobs.MobType;
import com.edasaki.rpg.mobs.spells.MobSpell;

public class BaseMobCommand extends RPGAbstractCommand {

    public BaseMobCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        try {
            int count = args.length > 2 ? Integer.parseInt(args[1]) : 1;
            for (int k = 0; k < count; k++) {
                MobType mt = new MobType();
                mt.identifier = "db_83_mob_" + mt.level;
                mt.entityClass = CustomZombie.class;
                mt.level = Integer.parseInt(args[0]);
                mt.name = "Base Level " + mt.level + " Mob";
                mt.prefixes = new ArrayList<String>();
                mt.suffixes = new ArrayList<String>();
                mt.exp = MobBalance.getMobEXP(mt.level, 1);
                mt.damageLow = MobBalance.getMobDamage(mt.level, 1, 1)[0];
                mt.damageHigh = MobBalance.getMobDamage(mt.level, 1, 1)[1];
                mt.maxHP = MobBalance.getMobHP(mt.level, 1);
                mt.equips = new ArrayList<ItemStack>();
                mt.attributes = new ArrayList<MobAttribute>();
                mt.spells = new ArrayList<MobSpell>();
                mt.drops = new ArrayList<MobDrop>();
                mt.spawn(p.getTargetBlock((Set<Material>) null, 100).getLocation().add(0, 1, 0));
                p.sendMessage("Spawned base mob of level " + mt.level);
                p.sendMessage("Values: EXP: " + mt.exp + ", Damage: " + mt.damageLow + "-" + mt.damageHigh + ", HP: " + mt.maxHP);
            }
        } catch (Exception e) {
            p.sendMessage("/basemob <level> [amount]");
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
