package com.edasaki.rpg.general;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RMessages;
import com.edasaki.core.utils.RParticles;
import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.commands.owner.MobSpawnCommand;
import com.edasaki.rpg.treegens.BigTreeGen;

import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.NBTCompressedStreamTools;
import net.minecraft.server.v1_10_R1.NBTTagCompound;
import net.minecraft.server.v1_10_R1.WorldGenerator;

public class SchematicManager extends AbstractManagerRPG {

    public static ItemStack terraformItem;
    public static ItemStack blockLocItem;
    public static ItemStack mobSpawnItem;
    private static final String TERRAFORM_NAME = ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "Terraformer v1.0";
    private static final String BLOCK_LOC_NAME = ChatColor.GREEN + ChatColor.BOLD.toString() + "BlockLoc v1.0";
    private static final String MOB_SPAWN_NAME = ChatColor.RED + ChatColor.BOLD.toString() + "MobSpawner v1.0";
    public static HashMap<UUID, SchematicUserConfig> configs = new HashMap<UUID, SchematicUserConfig>();

    public static class SchematicUserConfig {
        public ArrayList<Object[][]> history;
        public ArrayList<Schematic> loadedSchematics;

        public int ylower = -1;
        public int yupper = 0;
        public int xlower = 0;
        public int xupper = 0;
        public int zlower = 0;
        public int zupper = 0;
        public int mode = 0; //0 = paste, 1 = grow, 2 = tree
        public int growRadius = 5;
        public double growDensity = 0.3;
        public ArrayList<Material> filter;
        public ArrayList<ArrayList<Block>> lastGrow;

        public boolean verbose = false;

        public Schematic getRandomLoaded() {
            if (loadedSchematics.size() == 0)
                return null;
            return loadedSchematics.get((int) (Math.random() * loadedSchematics.size()));
        }

        public SchematicUserConfig() {
            history = new ArrayList<Object[][]>();
            loadedSchematics = new ArrayList<Schematic>();
            filter = new ArrayList<Material>();
            lastGrow = new ArrayList<ArrayList<Block>>();
        }
    }

    public SchematicManager(SakiRPG plugin) {
        super(plugin);
    }

    public static SchematicUserConfig getConfig(Player p) {
        if (configs.containsKey(p.getUniqueId()))
            return configs.get(p.getUniqueId());
        SchematicUserConfig cfg = new SchematicUserConfig();
        configs.put(p.getUniqueId(), cfg);
        return cfg;
    }

    @Override
    public void initialize() {
        terraformItem = new ItemStack(Material.APPLE);
        ItemMeta im = terraformItem.getItemMeta();
        im.setDisplayName(TERRAFORM_NAME);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GRAY + "Left-click = paste random loaded schematic");
        lore.add(ChatColor.GRAY + "Right-click = undo last paste");
        terraformItem.setItemMeta(im);

        blockLocItem = new ItemStack(Material.DIRT);
        im = blockLocItem.getItemMeta();
        im.setDisplayName(BLOCK_LOC_NAME);
        blockLocItem.setItemMeta(im);

        mobSpawnItem = new ItemStack(Material.GOLDEN_CARROT);
        im = mobSpawnItem.getItemMeta();
        im.setDisplayName(MOB_SPAWN_NAME);
        mobSpawnItem.setItemMeta(im);
    }

    public static void giveItem(Player p) {
        p.getInventory().addItem(terraformItem.clone());
        p.sendMessage("You received the terraforming item.");
    }

    public static void giveBlockItem(Player p) {
        p.getInventory().addItem(blockLocItem.clone());
        p.sendMessage("You received the block loc item.");
    }

    public static void giveMobSpawnItem(Player p) {
        p.getInventory().addItem(mobSpawnItem.clone());
        p.sendMessage("You received the mob spawn item. " + mobSpawnItem);
    }

    private HashMap<String, Long> delay = new HashMap<String, Long>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (delay.containsKey(p.getName())) {
            if (System.currentTimeMillis() - delay.get(p.getName()) < 100)
                return;
        }
        delay.put(p.getName(), System.currentTimeMillis());
        ItemStack item = p.getEquipment().getItemInMainHand();
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals(TERRAFORM_NAME)) {
            if (getConfig(p).mode == 0) {
                if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    Block target = p.getTargetBlock((HashSet<Byte>) null, 100);
                    if (target == null || target.getType() == Material.AIR) {
                        p.sendMessage(ChatColor.RED + "Could not find target block.");
                        return;
                    } else {
                        SchematicUserConfig cfg = getConfig(p);
                        Schematic toPaste = cfg.getRandomLoaded();
                        if (toPaste == null) {
                            p.sendMessage("You have no loaded schematics.");
                        } else {
                            loadAndPaste(p, cfg.getRandomLoaded(), target.getLocation(), false);
                        }
                    }
                } else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    undo(p);
                }
            } else if (getConfig(p).mode == 1) {
                if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    grow(p, getConfig(p).growRadius);
                } else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    undoGrow(p);
                }
            } else if (getConfig(p).mode == 2) {
                if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    Block target = p.getTargetBlock((HashSet<Byte>) null, 100);
                    if (target != null) {
                        if (RParticles.isAirlike(target)) {
                            target = target.getWorld().getBlockAt(target.getLocation().add(0, -1, 0));
                            RMessages.announce("set block to " + target);
                        }
                        p.sendMessage("Generating tree.");
                        WorldGenerator wg = null;
                        wg = new BigTreeGen(true);
                        if (wg.generate((net.minecraft.server.v1_10_R1.World) ((CraftWorld) p.getWorld()).getHandle(), new Random(), new BlockPosition(target.getX(), target.getY(), target.getZ())))
                            p.sendMessage("Generated tree");
                        else
                            p.sendMessage("Failed");
                    } else {
                        p.sendMessage("No target block");
                    }
                } else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (BigTreeGen.undo()) {
                        p.sendMessage("undid big tree");
                    } else {
                        p.sendMessage("failed undo");
                    }
                }
            }
        } else if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals(MOB_SPAWN_NAME)) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                MobSpawnCommand.setLoc(p, p.getLocation());
            } else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                Block b = p.getTargetBlock((HashSet<Material>) null, 150);
                if (b != null) {
                    if (RParticles.isAirlike(b)) {
                        MobSpawnCommand.setLoc(p, b.getLocation().add(Math.random(), 0.15, Math.random()));
                    } else if (b.getRelative(BlockFace.UP) != null && RParticles.isAirlike(b.getRelative(BlockFace.UP))) {
                        MobSpawnCommand.setLoc(p, b.getRelative(BlockFace.UP).getLocation().add(Math.random(), 0.15, Math.random()));
                    } else {
                        p.sendMessage("Could not find valid air block");
                    }
                } else {
                    p.sendMessage("No target block");
                }
            }
        } else if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals(BLOCK_LOC_NAME)) {
            if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                event.setCancelled(true);
                Location loc = event.getClickedBlock().getLocation();
                p.sendMessage(ChatColor.GRAY + ">> " + ChatColor.YELLOW + "Block location" + ChatColor.GRAY + ": " + ChatColor.WHITE + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
            }
        }
    }

    public static void grow(Player p, int radius) {
        Block target = p.getTargetBlock((HashSet<Byte>) null, 100);
        int radius_squared = radius * radius;
        Block toHandle;
        SchematicUserConfig cfg = getConfig(p);
        ArrayList<Block> list = new ArrayList<Block>();
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                toHandle = target.getWorld().getHighestBlockAt(target.getX() + x, target.getZ() + z);
                if (toHandle.getType() == Material.AIR && toHandle.getRelative(BlockFace.DOWN).getType() == Material.GRASS) { // Block beneath is grass
                    if (target.getLocation().distanceSquared(toHandle.getLocation()) <= radius_squared) { // Block is in radius
                        double rand = Math.random();
                        if (rand < cfg.growDensity * 5 / 6) {
                            toHandle.setType(Material.LONG_GRASS);
                            toHandle.setData((byte) 1);
                            list.add(toHandle);
                        } else if (rand < cfg.growDensity) {
                            toHandle.setType(Material.RED_ROSE); //0 4 5 6 7 8
                            byte data = (byte) (Math.random() * 6);
                            if (data > 0)
                                data += 3; //1 + 3 = 4, 2 + 3 = 5, ..., 5 + 3 = 8
                            toHandle.setData(data);
                            list.add(toHandle);
                        }
                    }
                }
            }
        }
        cfg.lastGrow.add(list);
        if (SchematicManager.getConfig(p).verbose)
            p.sendMessage("Grew with radius " + radius + ".");
    }

    public static void undoGrow(Player p) {
        if (System.currentTimeMillis() - lastUndo < 200) {
            return;
        }
        lastUndo = System.currentTimeMillis();
        SchematicUserConfig cfg = getConfig(p);
        if (cfg.lastGrow.size() == 0) {
            p.sendMessage("No grow to undo.");
            return;
        }
        for (Block b : cfg.lastGrow.remove(cfg.lastGrow.size() - 1))
            b.setType(Material.AIR);
        p.sendMessage("Undid last grow.");
    }

    private static void loadAndPaste(Player p, Schematic sch, Location loc, boolean air) {
        p.sendMessage("Starting paste.");
        if (sch != null) {
            SchematicUserConfig cfg = getConfig(p);
            int xoffset = RMath.randInt(cfg.xlower, cfg.xupper);
            int yoffset = RMath.randInt(cfg.ylower, cfg.yupper);
            int zoffset = RMath.randInt(cfg.zlower, cfg.zupper);
            ArrayList<Object[]> hist = pasteSchematic(p, loc.getWorld(), loc, sch, xoffset, yoffset, zoffset, air);
            cfg.history.add(hist.toArray(new Object[hist.size()][]));
            p.sendMessage("Done pasting.");
        } else {
            p.sendMessage("Error loading schematic.");
        }
    }

    private static ArrayList<Object[]> pasteSchematic(Player p, World world, Location loc, Schematic schematic, int xoffset, int yoffset, int zoffset, boolean air) {
        short[] blocks = schematic.getBlocks();
        byte[] blockData = schematic.getData();

        short width = schematic.getWidth();
        short length = schematic.getLength();
        short height = schematic.getHeight();

        loc.setX(loc.getX() - width / 2);
        loc.setZ(loc.getZ() - length / 2);

        ArrayList<Object[]> hist = new ArrayList<Object[]>();

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                for (int z = 0; z < length; ++z) {
                    try {
                        int index = (y * length + z) * width + x;
                        Block block = new Location(world, loc.getX() + x + xoffset, loc.getY() + y + yoffset, loc.getZ() + z + zoffset).getBlock();
                        //                        if (!air && blocks[index] == 0)
                        //                            continue;
                        if (getConfig(p).filter.contains(Material.getMaterial(blocks[index])))
                            continue;
                        Object[] o = new Object[6];
                        o[0] = block.getWorld();
                        o[1] = block.getLocation();
                        o[2] = block.getType();
                        o[3] = block.getData();
                        block.setType(Material.AIR);
                        block.setType(Material.getMaterial(blocks[index]));
                        block.setData(blockData[index]);
                        o[4] = block.getType();
                        o[5] = block.getData();
                        hist.add(o);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return hist;
    }

    private static long lastUndo = 0;

    private static void undo(Player p) {
        if (System.currentTimeMillis() - lastUndo < 200) {
            return;
        }
        lastUndo = System.currentTimeMillis();
        if (!configs.containsKey(p.getUniqueId())) {
            p.sendMessage("Nothing to clear.");
            return;
        }
        ArrayList<Object[][]> arr = configs.get(p.getUniqueId()).history;
        if (arr.isEmpty()) {
            p.sendMessage("Nothing to clear.");
            return;
        }
        Object[][] obj = arr.remove(arr.size() - 1);
        for (Object[] o : obj) {
            Block temp = ((World) o[0]).getBlockAt(((Location) o[1]));
            temp.setType((Material) o[2]);
            temp.setData((byte) o[3]);
        }
        p.sendMessage("Undid last paste.");
    }

    public static Schematic loadSchematic(String name) {
        if (!name.endsWith(".schematic"))
            name = name + ".schematic";
        File file = new File(plugin.getDataFolder() + "/schematics/" + name);
        if (!file.exists())
            return null;
        try {
            FileInputStream stream = new FileInputStream(file);
            NBTTagCompound nbtdata = NBTCompressedStreamTools.a(stream);

            short width = nbtdata.getShort("Width");
            short height = nbtdata.getShort("Height");
            short length = nbtdata.getShort("Length");

            byte[] blocks = nbtdata.getByteArray("Blocks");
            byte[] data = nbtdata.getByteArray("Data");

            byte[] addId = new byte[0];

            if (nbtdata.hasKey("AddBlocks")) {
                addId = nbtdata.getByteArray("AddBlocks");
            }

            short[] sblocks = new short[blocks.length];
            for (int index = 0; index < blocks.length; index++) {
                if ((index >> 1) >= addId.length) {
                    sblocks[index] = (short) (blocks[index] & 0xFF);
                } else {
                    if ((index & 1) == 0) {
                        sblocks[index] = (short) (((addId[index >> 1] & 0x0F) << 8) + (blocks[index] & 0xFF));
                    } else {
                        sblocks[index] = (short) (((addId[index >> 1] & 0xF0) << 4) + (blocks[index] & 0xFF));
                    }
                }
            }

            stream.close();
            return new Schematic(name, sblocks, data, width, length, height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class Schematic {

        private short[] blocks;
        private byte[] data;
        private short width;
        private short length;
        private short height;

        public String name;

        public Schematic(String name, short[] blocks, byte[] data, short width, short length, short height) {
            this.name = name;
            this.blocks = blocks;
            this.data = data;
            this.width = width;
            this.length = length;
            this.height = height;
        }

        @Override
        public String toString() {
            return this.name;
        }

        /**
        * @return the blocks
        */
        public short[] getBlocks() {
            return blocks;
        }

        /**
        * @return the data
        */
        public byte[] getData() {
            return data;
        }

        /**
        * @return the width
        */
        public short getWidth() {
            return width;
        }

        /**
        * @return the length
        */
        public short getLength() {
            return length;
        }

        /**
        * @return the height
        */
        public short getHeight() {
            return height;
        }
    }

}
