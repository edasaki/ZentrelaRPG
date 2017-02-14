package com.edasaki.rpg.commands.member;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class WorldBossArenaCommand extends RPGAbstractCommand {

    public WorldBossArenaCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(final Player p, PlayerDataRPG pd, String[] args) {
        pd.sendMessage("Currently disabled. Coming soon (in 1 or 2 patches");
        //        plugin.getInstance(WorldBossManager.class).enterLobby(p);
        //        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7> &dA mysterious force is pulling you out of this dimension..."));
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
