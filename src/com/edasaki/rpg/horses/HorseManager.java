package com.edasaki.rpg.horses;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftHorse;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftInventoryHorse;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;

import com.edasaki.core.utils.REntities;
import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RParticles;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.entities.CustomHorse;
import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.npcs.NPCManager;

import de.slikey.effectlib.util.ParticleEffect;
import net.minecraft.server.v1_10_R1.AttributeInstance;
import net.minecraft.server.v1_10_R1.EntityInsentient;
import net.minecraft.server.v1_10_R1.GenericAttributes;

public class HorseManager extends AbstractManagerRPG {

    public static ArrayList<HorseVillager> horseVillagers = new ArrayList<HorseVillager>();

    public static ArrayList<UUID> horseUUIDs = new ArrayList<UUID>();

    public HorseManager(SakiRPG plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {
        reload();
    }

    public static void reload() {
        for (HorseVillager hv : horseVillagers) {
            NPCManager.unregister(hv);
        }
        horseVillagers.clear();
        File dir = new File(plugin.getDataFolder(), "horses");
        if (!dir.exists())
            dir.mkdirs();
        for (File f : dir.listFiles()) {
            if (f.getName().endsWith(".txt")) {
                readLocs(f);
            }
        }
    }

    @EventHandler
    public void onHorseInv(InventoryClickEvent event) {
        if (event.getInventory() instanceof CraftInventoryHorse)
            event.setCancelled(true);
    }

    @EventHandler
    public void onDismount(VehicleExitEvent event) {
        if (event.getExited() instanceof Player) {
            Player p = (Player) event.getExited();
            if (plugin.getPD(p) != null)
                plugin.getPD(p).riding = false;
        }
        if (horseUUIDs.contains(event.getVehicle().getUniqueId())) {
            Vehicle v = event.getVehicle();
            RScheduler.schedule(plugin, new Runnable() {
                public void run() {
                    if (v != null && v.isValid()) {
                        if (v instanceof Horse) {
                            if (((CraftHorse) v).getHandle() instanceof CustomHorse) {
                                ((CustomHorse) ((CraftHorse) v).getHandle()).rearUp();
                            }
                        }
                    }
                }
            }, 10);
            RScheduler.schedule(plugin, new Runnable() {
                public void run() {
                    if (v != null && v.isValid()) {
                        RParticles.show(ParticleEffect.CLOUD, v.getLocation().add(0, 1, 0), 10);
                        v.remove();
                    }
                }
            }, 35);
        }
    }

    public static Horse createHorse(Player p, PlayerDataRPG pd) {
        Horse horse = (Horse) REntities.createLivingEntity(CustomHorse.class, p.getLocation());
        horse.setTamed(true);
        setHorseSpeed(horse, getSpeed(pd.horseSpeed));
        horse.setJumpStrength(getJump(pd.horseJump));
        horse.setColor(getColor(pd));
        horse.setStyle(getStyle(pd));
        horse.setVariant(getVariant(pd));
        horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        if (pd.horseArmor != null)
            horse.getInventory().setArmor(new ItemStack(pd.horseArmor));
        if (pd.horseBaby) {
            horse.setBaby();
            horse.setAgeLock(true);
        }
        horse.setCustomName(p.getName() + "'s Horse");
        horse.setCustomNameVisible(true);
        pd.riding = true;
        horse.setPassenger(p);
        horseUUIDs.add(horse.getUniqueId());
        return horse;
    }

    public static double getSpeed(int tier) {
        return 0.1500 + 0.0225 * tier;
    }

    //Jump 0.4 - 1.0
    //Speed 0.1125 - 0.3375 (player speed is 0.1)
    public static double getJump(int tier) {
        return 0.38 + 0.065 * tier;
    }

    public static Horse.Color getColor(PlayerDataRPG pd) {
        if (pd.horseColor == null)
            pd.horseColor = RMath.randObject(Horse.Color.values());
        return pd.horseColor;
    }

    public static Horse.Style getStyle(PlayerDataRPG pd) {
        if (pd.horseStyle == null)
            pd.horseStyle = RMath.randObject(Horse.Style.values());
        return pd.horseStyle;
    }

    public static Horse.Variant getVariant(PlayerDataRPG pd) {
        if (pd.horseVariant == null)
            pd.horseVariant = RMath.randObject(Horse.Variant.values());
        return pd.horseVariant;
    }

    public static void setHorseSpeed(Horse horse, double speed) {
        AttributeInstance attributes = ((EntityInsentient) ((CraftLivingEntity) horse).getHandle()).getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
        attributes.setValue(speed);
    }

    private static void readLocs(File f) {
        Scanner scan = null;
        int id = 1;
        try {
            scan = new Scanner(f);
            String s;
            while (scan.hasNextLine()) {
                s = scan.nextLine().trim();
                if (s.length() == 0 || s.startsWith("//") || s.startsWith("#"))
                    continue;
                String[] data = s.split(" ");
                double x = Double.parseDouble(data[0]);
                double y = Double.parseDouble(data[1]);
                double z = Double.parseDouble(data[2]);
                String world = data[3];
                World w = plugin.getServer().getWorld(world);
                if (w == null) {
                    Log.error("null world for file " + f + " line " + s);
                    continue;
                }
                HorseVillager gv = new HorseVillager(id++, "Horse Dealer", x, y, z, world);
                gv.register();
                horseVillagers.add(gv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (scan != null)
                scan.close();
        }
    }

}
