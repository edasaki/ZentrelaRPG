package com.edasaki.rpg.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import com.edasaki.core.utils.RMessages;
import com.edasaki.rpg.SakiRPG;
import com.google.common.io.BaseEncoding;

import net.minecraft.server.v1_10_R1.NBTCompressedStreamTools;
import net.minecraft.server.v1_10_R1.NBTTagCompound;

public class RSerializer {

    private static final String LOCATION_DIVIDER = "||LOC||";

    public static Object[] deserializeArray(String s) {
        ByteArrayInputStream in = new ByteArrayInputStream(Base64.decodeBase64(s.getBytes()));
        try {
            return (Object[]) new ObjectInputStream(in).readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object deserializeObject(String s) {
        ByteArrayInputStream in = new ByteArrayInputStream(Base64.decodeBase64(s.getBytes()));
        try {
            return new ObjectInputStream(in).readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String serialize(Object o) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            new ObjectOutputStream(out).writeObject(o);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(Base64.encodeBase64(out.toByteArray()));
    }

    public static Location deserializeLocation(String s) {
        if (s.length() == 0)
            return SakiRPG.getTutorialSpawn();
        String[] data = s.split(Pattern.quote(LOCATION_DIVIDER));
        if (data.length == 0)
            return SakiRPG.getTutorialSpawn();
        try {
            double x = Double.parseDouble(data[0]);
            double y = Double.parseDouble(data[1]);
            if (y < 1)
                y = 1;
            double z = Double.parseDouble(data[2]);
            float yaw = Float.parseFloat(data[3]);
            float pitch = Float.parseFloat(data[4]);
            World w = SakiRPG.plugin.getServer().getWorld(data[5]);
            if (w == null) {
                w = SakiRPG.plugin.getServer().getWorld(SakiRPG.GAME_WORLD);
                RMessages.announce("ERROR DESERIALIZING LOCATION " + s);
                RMessages.announce("Please report this to Misaka if you see it!");
                return w.getSpawnLocation();
            }
            return new Location(w, x, y, z, yaw, pitch);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Corrupted location save: " + s);
        }
        return SakiRPG.getTutorialSpawn();
    }

    public static String serializeLocation(Location loc) {
        StringBuilder sb = new StringBuilder();
        sb.append(loc.getX());
        sb.append(LOCATION_DIVIDER);
        if (loc.getY() < 1)
            sb.append("1.0");
        else
            sb.append(loc.getY());
        sb.append(LOCATION_DIVIDER);
        sb.append(loc.getZ());
        sb.append(LOCATION_DIVIDER);
        sb.append(loc.getYaw());
        sb.append(LOCATION_DIVIDER);
        sb.append(loc.getPitch());
        sb.append(LOCATION_DIVIDER);
        sb.append(loc.getWorld().getName());
        return sb.toString();
    }

    public static String serializeItemStack(ItemStack itemStack) {
        if (itemStack == null)
            return "null";

        ByteArrayOutputStream outputStream = null;
        NBTTagCompound tagComp = new NBTTagCompound();
        net.minecraft.server.v1_10_R1.ItemStack copy = CraftItemStack.asNMSCopy(itemStack);
        if (copy == null)
            return "null";
        copy.save(tagComp);
        outputStream = new ByteArrayOutputStream();
        try {
            NBTCompressedStreamTools.a(tagComp, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String str = BaseEncoding.base64().encode(outputStream.toByteArray());
        if (str.contains("@")) {
            try {
                RMessages.announce("Error code 109: Item serialization @. Please report this to Misaka or Edasaki.");
                throw new Exception("ItemStack serialization contains @ symbol. " + itemStack);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "null";
        }
        return str;
    }

    public static ItemStack deserializeItemStack(String itemStackString) {
        if (itemStackString == null || itemStackString.length() == 0 || itemStackString.equals("null"))
            return null;

        ByteArrayInputStream inputStream = new ByteArrayInputStream(BaseEncoding.base64().decode(itemStackString));
        NBTTagCompound tagComp;
        try {
            tagComp = NBTCompressedStreamTools.a(inputStream);
            net.minecraft.server.v1_10_R1.ItemStack item = net.minecraft.server.v1_10_R1.ItemStack.createStack(tagComp);
            ItemStack bukkitItem = CraftItemStack.asBukkitCopy(item);
            return bukkitItem;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
