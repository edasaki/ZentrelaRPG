package com.edasaki.rpg.commands.member;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.warps.WarpCallback;
import com.edasaki.rpg.warps.WarpManager;

public class SpawnCommand extends RPGAbstractCommand {

    private HashMap<String, Long> last = new HashMap<String, Long>();

    public SpawnCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        if (System.currentTimeMillis() - last.getOrDefault(p.getUniqueId().toString(), 0l) < 10 * 60 * 1000) {
            p.sendMessage(ChatColor.GRAY + "You can only use /spawn once every 10 minutes!");
            int diff = (int)Math.ceil(((10 * 60 * 1000) - (System.currentTimeMillis() - last.getOrDefault(p.getUniqueId().toString(), 0l)))/1000.0);
            p.sendMessage(ChatColor.GRAY + "Wait " + diff + " more seconds!");
            return;
        }
        if (p.getWorld().getName().equalsIgnoreCase(SakiRPG.TUTORIAL_WORLD)) {
            p.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "Sorry! You can't use this command in the tutorial!");
            p.sendMessage(ChatColor.GRAY + "> " + ChatColor.AQUA + "Please finish the tutorial first. Feel free to ask for help!");
            return;
        }
        WarpManager.warp(p, pd.getRespawnLocation(p.getLocation()), new WarpCallback() {
            @Override
            public void complete(boolean warpSuccess) {
                if (warpSuccess)
                    last.put(p.getUniqueId().toString(), System.currentTimeMillis());
            }
        });
        p.sendMessage(ChatColor.GRAY + "> " + ChatColor.AQUA + "Warping you to the nearest spawn point...");
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
