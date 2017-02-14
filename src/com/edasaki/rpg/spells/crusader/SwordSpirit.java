package com.edasaki.rpg.spells.crusader;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_10_R1.CraftServer;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.entity.Player;

import com.edasaki.core.utils.RParticles;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RTicks;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

import de.slikey.effectlib.util.ParticleEffect;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.PacketPlayOutBlockBreakAnimation;

public class SwordSpirit extends SpellEffect {

    private static int ID = 0;

    public static final String BUFF_ID = "sword spirit";

    @Override
    public boolean cast(final Player p, PlayerDataRPG pd, int level) {
        final Block target = p.getLocation().subtract(0, 0.5, 0).getBlock();
        final Location startLoc = target.getLocation().clone();
        ArrayList<Location> locs = new ArrayList<Location>();
        locs.add(p.getLocation());
        for (int dx = -2; dx <= 2; dx++)
            for (int dy = -2; dy <= 2; dy++)
                for (int dz = -2; dz <= 2; dz++)
                    if (Math.random() < 0.7)
                        locs.add(startLoc.clone().add(dx, dy, dz));
        for (Location loc : locs) {
            final Block b = loc.getBlock();
            final int myId = ID++;
            PacketPlayOutBlockBreakAnimation breakBlockPacket = new PacketPlayOutBlockBreakAnimation(myId, new BlockPosition(b.getX(), b.getY(), b.getZ()), 7);
            ((CraftServer) Spell.plugin.getServer()).getHandle().sendPacketNearby(null, b.getX(), b.getY(), b.getZ(), 120, ((CraftWorld) b.getWorld()).getHandle().dimension, breakBlockPacket);
            RScheduler.schedule(Spell.plugin, new Runnable() {
                public void run() {
                    PacketPlayOutBlockBreakAnimation breakBlockPacket = new PacketPlayOutBlockBreakAnimation(myId, new BlockPosition(b.getX(), b.getY(), b.getZ()), 10);
                    ((CraftServer) Spell.plugin.getServer()).getHandle().sendPacketNearby(null, b.getX(), b.getY(), b.getZ(), 120, ((CraftWorld) b.getWorld()).getHandle().dimension, breakBlockPacket);
                }
            }, RTicks.seconds(2));
        }
        locs.clear();
        locs.add(startLoc.clone().add(1, 0, 1));
        locs.add(startLoc.clone().add(1, 0, -1));
        locs.add(startLoc.clone().add(-1, 0, 1));
        locs.add(startLoc.clone().add(-1, 0, -1));
        for (Location loc : locs) {
            for (int k = 0; k < 5; k++) {
                RParticles.showWithSpeed(ParticleEffect.SPELL_INSTANT, loc, 0, 20);
                loc = loc.add(0, 1, 0);
            }
        }
        long duration = 0;
        double value = 0;
        switch (level) {
            case 1:
                duration = 4000;
                value = 1.2;
                break;
            case 2:
                duration = 4000;
                value = 1.25;
                break;
            case 3:
                duration = 5000;
                value = 1.3;
                break;
            case 4:
                duration = 5000;
                value = 1.35;
                break;
            case 5:
                duration = 6000;
                value = 1.4;
                break;
            case 6:
                duration = 6000;
                value = 1.45;
                break;
            case 7:
                duration = 7000;
                value = 1.5;
                break;
            case 8:
                duration = 7000;
                value = 1.55;
                break;
            case 9:
                duration = 8000;
                value = 1.6;
                break;
            case 10:
                duration = 8000;
                value = 1.65;
                break;
        }
        pd.giveBuff(SwordSpirit.BUFF_ID, value, duration);
        Spell.notify(p, "You feel a surge of power!");
        Spell.notifyDelayed(p, "", duration / 1000.0);
        return true;
    }

}
