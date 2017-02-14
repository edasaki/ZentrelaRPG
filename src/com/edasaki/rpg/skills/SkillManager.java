package com.edasaki.rpg.skills;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.edasaki.core.menus.MenuManager;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RTicks;
import com.edasaki.core.utils.fanciful.FancyMessage;
import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.drops.DropManager;

import net.minecraft.server.v1_10_R1.NBTTagCompound;
import net.minecraft.server.v1_10_R1.NBTTagList;
import net.minecraft.server.v1_10_R1.NBTTagString;

public class SkillManager extends AbstractManagerRPG {

    private HashMap<Block, Byte> dataMap = new HashMap<Block, Byte>();

    private static FancyMessage TUTORIAL;

    {
        TUTORIAL = new FancyMessage();
        TUTORIAL.text(">> Click here for a link to the skills tutorial!").color(ChatColor.YELLOW).link("http://zentrela.net/skills");
    }

    public SkillManager(SakiRPG plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {

    }

    public static void openMenu(Player p) {
        Inventory i = MenuManager.createMenu(p, "Skills", 5, new Object[][] {
                {
                        1,
                        1,
                        Material.WOOD_AXE,
                        ChatColor.AQUA + "Woodcutting",
                        new Object[] {
                                ChatColor.GRAY,
                                "Click for a link to the detailed skills tutorial!",
                                null,
                                "",
                                ChatColor.AQUA,
                                "Woodcutting allows you to cut down trees and harvest wood from them!",
                                ChatColor.GREEN,
                                "Logs are required for Crafting, Smithing, and Firemaking.",
                                ChatColor.AQUA,
                                "Higher woodcutting levels will allow you to harvest higher quality wood.",
                                null,
                                "",
                                ChatColor.GRAY,
                                "You can buy a starter axe from any town's supplies store. Better axes may be found from monsters."
                        },
                        new Runnable() {
                            public void run() {
                                p.sendMessage("");
                                TUTORIAL.send(p);
                            }
                        }
                },
                {
                        1,
                        2,
                        Material.STONE_PICKAXE,
                        ChatColor.AQUA + "Mining",
                        new Object[] {
                                ChatColor.GRAY,
                                "Click for a link to the detailed skills tutorial!",
                                null,
                                "",
                                ChatColor.AQUA,
                                "Mining allows you to mine rocks for ore!",
                                ChatColor.GREEN,
                                "Ores are required for Crafting and Smithing higher level equipment.",
                                ChatColor.AQUA,
                                "Higher mining levels will allow you to mine more valuable ores.",
                                null,
                                "",
                                ChatColor.GRAY,
                                "You can buy a starter pickaxe from any town's supplies store. Better pickaxes may be found from monsters."
                        },
                        new Runnable() {
                            public void run() {
                                p.sendMessage("");
                                TUTORIAL.send(p);
                            }
                        }
                },
                {
                        1,
                        2,
                        Material.FISHING_ROD,
                        ChatColor.AQUA + "Fishing",
                        new Object[] {
                                ChatColor.GRAY,
                                "Click for a link to the detailed skills tutorial!",
                                null,
                                "",
                                ChatColor.AQUA,
                                "Fishing allows you to catch a variety of fish from water!",
                                ChatColor.GREEN,
                                "Fish are used in Cooking.",
                                ChatColor.AQUA,
                                "Higher fishing levels will help you catch rarer fish.",
                                null,
                                "",
                                ChatColor.GRAY,
                                "You can buy a starter fishing rod from any town's supplies store. Better rods may be found from monsters."
                        },
                        new Runnable() {
                            public void run() {
                                p.sendMessage("");
                                TUTORIAL.send(p);
                            }
                        }
                },
                {
                        3,
                        1,
                        Material.WORKBENCH,
                        ChatColor.AQUA + "Crafting",
                        new Object[] {
                                ChatColor.GRAY,
                                "Click for a link to the detailed skills tutorial!",
                                null,
                                "",
                                ChatColor.AQUA,
                                "Crafting lets you make equipment to use in battle!",
                                ChatColor.GREEN,
                                "Go to any Crafting Table to begin crafting.",
                                ChatColor.AQUA,
                                "Blueprints for items will be displayed in the Crafting Menu.",
                                null,
                                "",
                                ChatColor.GRAY,
                                "Higher level Crafting will unlock blueprints for better equipment!"
                        },
                        new Runnable() {
                            public void run() {
                                p.sendMessage("");
                                TUTORIAL.send(p);
                            }
                        }
                },
                {
                        3,
                        2,
                        Material.COOKED_BEEF,
                        ChatColor.AQUA + "Cooking",
                        new Object[] {
                                ChatColor.GRAY,
                                "Click for a link to the detailed skills tutorial!",
                                null,
                                "",
                                ChatColor.AQUA,
                                "Cooking makes delicious food!",
                                ChatColor.GREEN,
                                "Find a Campfire (made using Firemaking) to start cooking.",
                                ChatColor.AQUA,
                                "Food can give a variety of buffs and special effects.",
                                null,
                                "",
                                ChatColor.GRAY,
                                "Higher level Cooking will unlock recipes for super delicious food!"
                        },
                        new Runnable() {
                            public void run() {
                                p.sendMessage("");
                                TUTORIAL.send(p);
                            }
                        }
                },
                {
                        3,
                        3,
                        Material.ANVIL,
                        ChatColor.AQUA + "Smithing",
                        new Object[] {
                                ChatColor.GRAY,
                                "Click for a link to the detailed skills tutorial!",
                                null,
                                "",
                                ChatColor.AQUA,
                                "Smithing will allow you to repair broken equipment!",
                                ChatColor.GREEN,
                                "Get to an Anvil to fix your stuff!",
                                ChatColor.AQUA,
                                "Equipment that reaches 0 durability will no longer give stats. Smithing will restore that durability!",
                                null,
                                "",
                                ChatColor.GRAY,
                                "Higher level Smithing reduces the costs of repairing equipment."
                        },
                        new Runnable() {
                            public void run() {
                                p.sendMessage("");
                                TUTORIAL.send(p);
                            }
                        }
                },
        });
        p.openInventory(i);
    }

    public static void giveAxe(Player p) {
        net.minecraft.server.v1_10_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(new ItemStack(Material.IRON_AXE));
        NBTTagCompound nbtTag = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        list.add(new NBTTagString("minecraft:log"));
        list.add(new NBTTagString("minecraft:log2"));
        nbtTag.set("CanDestroy", list);
        nbtTag.setBoolean("Unbreakable", true);
        nmsItem.setTag(nbtTag);
        ItemStack item = CraftItemStack.asCraftMirror(nmsItem);
        p.getInventory().addItem(item);
    }

    @EventHandler
    public void onWoodBreak(BlockBreakEvent event) {
        final Block b = event.getBlock();
        if (b != null && (b.getType() == Material.LOG || b.getType() == Material.LOG_2)) {
            Material handType = event.getPlayer().getItemInHand().getType();
            if (handType == Material.WOOD_AXE || handType == Material.STONE_AXE || handType == Material.IRON_AXE || handType == Material.GOLD_AXE || handType == Material.DIAMOND_AXE) {
                dataMap.put(b, b.getData());
                b.setType(Material.WOOL);
                b.setData(DyeColor.BLACK.getWoolData());
                Player p = event.getPlayer();
                event.getPlayer().sendMessage("> +1 Woodcutting EXP");
                Location loc = b.getLocation();
                Vector v = p.getLocation().subtract(loc).toVector().normalize().multiply(0.5);
                DropManager.dropItem(new ItemStack(Material.LOG), loc, event.getPlayer().getUniqueId()).setVelocity(v);
                RScheduler.schedule(plugin, () -> {
                    b.setType(Material.LOG);
                    b.setData(dataMap.getOrDefault(b, (byte) 0));
                }, RTicks.seconds(2));
            }
        }
    }

}
