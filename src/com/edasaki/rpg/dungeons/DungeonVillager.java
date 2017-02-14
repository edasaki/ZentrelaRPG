package com.edasaki.rpg.dungeons;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.edasaki.core.menus.MenuManager;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.npcs.NPCEntity;
import com.edasaki.rpg.npcs.NPCType;
import com.edasaki.rpg.warps.WarpCallback;
import com.edasaki.rpg.warps.WarpManager;

public class DungeonVillager extends NPCEntity {

    public Dungeon dungeon;
    public boolean isExit;

    public DungeonVillager(int id, String name, double x, double y, double z, String world) {
        super(id, name, NPCType.VILLAGER, x, y, z, world);
    }

    public Location getTPLoc() {
        return this.getClonedLoc().add(0, 0.5, 0);
    }

    @Override
    public void interact(final Player p, PlayerDataRPG pd) {
        if (isExit) {
            say(p, "I'll warp you out of here now. Stand still.");
            WarpManager.warp(p, this.dungeon.dungeonMaster.getTPLoc(), new WarpCallback() {
                @Override
                public void complete(boolean warpSuccess) {
                    if (warpSuccess) {
                        dungeon.players.remove(pd.getUUID().toString());
                        say(p, "See you later.");
                    } else {
                        say(p, "Sticking around? Let me know when you want to leave.");
                    }
                }
            });
        } else {
            try {
                pd.save();
                ArrayList<String> desc = this.dungeon.getDisplay();
                ItemStack tp = new ItemStack(Material.ENDER_PEARL);
                tp.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                Inventory inv = MenuManager.createMenu(p, "Dungeon Information", 5, new Object[][] {
                        {
                                1,
                                4,
                                Material.BOOK_AND_QUILL,
                                ChatColor.GREEN + this.dungeon.dungeonName,
                                desc,
                                new Runnable() {
                                    public void run() {
                                    }
                                }
                        },
                        {
                                3,
                                4,
                                tp,
                                ChatColor.GREEN + "Click to Warp",
                                new Object[] {
                                        ChatColor.GRAY,
                                        "Enter the dungeon!",
                                },
                                new Runnable() {
                                    public void run() {
                                        say(p, "I'll warp you into the dungeon now. Stand still.");
                                        p.closeInventory();
                                        WarpManager.warp(p, dungeon.dungeonSpawn, new WarpCallback() {
                                            @Override
                                            public void complete(boolean warpSuccess) {
                                                if (warpSuccess) {
                                                    dungeon.players.add(pd.getUUID().toString());
                                                    say(p, "Good luck!");
                                                } else {
                                                    say(p, "Let me know if you still want to enter the dungeon.");
                                                }
                                            }
                                        });
                                    }
                                }
                        },
                });
                p.openInventory(inv);
            } catch (Exception e) {
                say(p, "Error code 103 - Please report this to Misaka or Edasaki!");
                e.printStackTrace();
            }
        }
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.RED;
    }

}
