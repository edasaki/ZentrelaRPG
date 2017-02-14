package com.edasaki.rpg.commands.owner;

import java.util.HashSet;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.mobs.MobManager;
import com.edasaki.rpg.mobs.MobType;

public class SpawnMobCommand extends RPGAbstractCommand {

    public SpawnMobCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        String name = args[0];
        int amount = args.length > 1 ? Integer.parseInt(args[1]) : 1;
        MobType mt = MobManager.mobTypes.get(name);
        if (mt == null) {
            p.sendMessage("Could not find mob " + name + ".");
        } else {
            if (amount > 25)
                amount = 25;
            for (int k = 0; k < amount; k++) {
                mt.spawn(p.getTargetBlock((HashSet<Byte>) null, 100).getLocation().add(0, 1.25, 0));
            }
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
