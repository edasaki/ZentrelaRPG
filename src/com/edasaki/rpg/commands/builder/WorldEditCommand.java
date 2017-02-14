package com.edasaki.rpg.commands.builder;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import com.edasaki.core.players.Rank;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.general.EnvironmentManager;

public class WorldEditCommand extends RPGAbstractCommand {

    public static HashMap<UUID, PermissionAttachment> worldedit_pa = new HashMap<UUID, PermissionAttachment>();

    public WorldEditCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(final Player p, PlayerDataRPG pd, String[] args) {
        for (String s : EnvironmentManager.BUILD_WORLD) {
            if (p.getWorld().getName().equalsIgnoreCase(s) && !pd.check(Rank.BUILDER)) {
                p.sendMessage("Worldedit can only be used by builders in the build world.");
                return;
            }
        }
        worldedit_pa.put(p.getUniqueId(), p.addAttachment(plugin, "worldedit.*", true));
        p.sendMessage("You can now use worldedit. Note that the server now has CoreProtect so don't grief in the main world (/cw main) as I can see who did it and revert the changes (and ban you!).");
        /*
        if(args.length != 0 && args.length != 1) {
            p.sendMessage(ChatColor.RED + "Use as /givewe <name> or /givewe");
        } else if(args.length == 0) {
            p.addAttachment(plugin, "worldedit.*", true);
            p.sendMessage("You can now use worldedit. Note that the server now has CoreProtect so don't grief in the main world (/cw main) as I can see who did it and revert the changes (and ban you!).");
        
         * } else if(args.length == 1) {
            Player p2 = plugin.getServer().getPlayer(args[0]);
            if(p2 != null && p2.isValid() && p2.isOnline()) {
                p.sendMessage(ChatColor.GREEN + "Gave " + p2.getName() + " worldedit perms (temp).");
                p2.addAttachment(plugin, "worldedit.*", true);
                p2.sendMessage("You can now use worldedit. Note that the server now has CoreProtect so don't grief in the main world (/cw main) as I can see who did it and revert the changes (and ban you!).");
            } else {
                p.sendMessage("User is not online.");
            }
        }
        */
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
