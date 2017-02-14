package com.edasaki.rpg.packets;

import java.util.HashMap;

import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.SakiRPG;

import io.netty.channel.Channel;
import net.minecraft.server.v1_10_R1.EntityPlayer;
import net.minecraft.server.v1_10_R1.PacketPlayOutUpdateTime;

public class PacketManager extends AbstractManagerRPG {

    public static TinyProtocol protocol;

    private static HashMap<String, Long> targetTime = new HashMap<String, Long>();
    private static HashMap<String, Long> currentTime = new HashMap<String, Long>();

    public PacketManager(SakiRPG plugin) {
        super(plugin);
    }

    public static void registerTime(Player p, long time) {
        targetTime.put(p.getName(), time);
        EntityPlayer nmsPlayer = ((CraftPlayer) p).getHandle();
        // the time in the packet below doesn't matter, it's re-initialized in the method below
        nmsPlayer.playerConnection.sendPacket(new PacketPlayOutUpdateTime(24000, time, true));
    }

    @Override
    public void initialize() {
        try {
            protocol = new TinyProtocol(plugin) {

                @Override
                public Object onPacketInAsync(Player sender, Channel channel, Object packet) {
                    return super.onPacketInAsync(sender, channel, packet);
                }

                @Override
                public Object onPacketOutAsync(Player receiver, Channel channel, Object packet) {
                    if (packet != null && receiver != null && packet instanceof PacketPlayOutUpdateTime) {
                        String name = receiver.getName();
                        if (targetTime.containsKey(name)) {
                            if (currentTime.containsKey(name)) {
                                long curr = currentTime.get(name);
                                long target = targetTime.get(name);
                                long diff = target - curr;
                                final long min = 150;
                                //                                RMessages.announce("diff of " + diff);
                                if (Math.abs(diff) < min) {
                                    targetTime.remove(name);
                                    currentTime.put(name, target);
                                    //                                    RMessages.announce("small diff, sending time of " + target);
                                    return new PacketPlayOutUpdateTime(24000, target, true);
                                } else {
                                    long add = (long) (diff * 0.035);
                                    float sign = Math.signum(diff);
                                    if (Math.abs(add) < min)
                                        add = (long) (sign * min);
                                    long a = curr + add;
                                    currentTime.put(name, a);
                                    //                                    RMessages.announce("large diff, sending time of " + a);
                                    return new PacketPlayOutUpdateTime(24000, a, true);
                                }
                            } else {
                                long a = targetTime.remove(name);
                                //                                RMessages.announce("no curr, sending time of " + a);
                                currentTime.put(name, a);
                                return new PacketPlayOutUpdateTime(24000, a, true);
                            }
                        }
                        return null;
                    }
                    return super.onPacketOutAsync(receiver, channel, packet);
                }

            };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
