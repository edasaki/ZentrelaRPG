package com.edasaki.rpg.commands.owner;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

import net.minecraft.server.v1_10_R1.EntityPlayer;
import net.minecraft.server.v1_10_R1.PacketPlayOutWorldParticles;

public class CrashCommand extends RPGAbstractCommand {

    public CrashCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        String s = args[0];
        Player target = plugin.getServer().getPlayerExact(s);
        if (target != null && target.isOnline()) {
            EntityPlayer nmsPlayer = ((CraftPlayer) target).getHandle();
            Location location = target.getLocation();
            nmsPlayer.playerConnection.sendPacket(new PacketPlayOutWorldParticles(null, true, (float)location.getX(), (float)location.getY(), (float)location.getZ(), 0, 0, 0, 0, 2, 7));
            p.sendMessage("Disconnected " + s + ".");
        } else {
            p.sendMessage(ChatColor.RED + "Could not find online player '" + s + "'.");
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
