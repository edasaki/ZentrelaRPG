package com.edasaki.rpg.soaring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.edasaki.core.menus.MenuItem;
import com.edasaki.core.menus.MenuManager;
import com.edasaki.core.unlocks.Unlock;
import com.edasaki.core.utils.RMessages;
import com.edasaki.core.utils.RParticles;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.economy.ShardManager;
import com.edasaki.rpg.items.ItemManager;
import com.edasaki.rpg.particles.EffectFactory;
import com.edasaki.rpg.particles.custom.misc.FlightLaunchEffect;

import de.slikey.effectlib.util.ParticleEffect;

public class SoaringManager extends AbstractManagerRPG {

    private HashMap<UUID, Long> startedSneak = new HashMap<UUID, Long>();

    public SoaringManager(SakiRPG pl) {
        super(pl);
    }

    @Override
    public void initialize() {

    }

    private MenuItem generateUnlock(PlayerDataRPG pd, Player p, Unlock u, int row, int col, String display, String description, String[] price, ItemStack item) {
        if (pd.unlocked(u)) {
            MenuItem mi = new MenuItem(row, col, item, ChatColor.WHITE + display, new String[] {
                    pd.activeSoaring == u ? ChatColor.DARK_GRAY + "Currently active." : ChatColor.GRAY + "Click to switch to " + display + ".",
                    "",
                    ChatColor.AQUA + description,
            }, () -> {
                pd.activeSoaring = u;
                pd.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "You have switched to " + ChatColor.AQUA + display + ChatColor.GREEN + ".");
                p.closeInventory();
            });
            return mi;
        } else {
            String[] desc = new String[3 + price.length];
            desc[0] = ChatColor.GOLD + "Unlock Cost:";
            desc[desc.length - 2] = "";
            desc[desc.length - 1] = ChatColor.GRAY + "Click here to unlock " + display + "!";
            for (int k = 0; k < price.length; k++) {
                desc[k + 1] = price[k];
            }
            MenuItem mi = new MenuItem(row, col, item, ChatColor.WHITE + display, desc, () -> {
                if (pd.unlocked(u))
                    p.closeInventory();
                int shardPrice = Integer.parseInt(ChatColor.stripColor(price[0]).replaceAll("[^0-9]", ""));
                int fragPrice = Integer.parseInt(ChatColor.stripColor(price[1]).replaceAll("[^0-9]", ""));
                System.out.println("Price: " + shardPrice + ", " + fragPrice);
                boolean purchase = ShardManager.countCurrency(p) >= shardPrice && ItemManager.count(p, "power_fragment") >= fragPrice;
                if (purchase) {
                    ShardManager.takeCurrency(p, shardPrice);
                    ItemManager.take(p, "power_fragment", fragPrice);
                    pd.addUnlock(u);
                    u.sendMessage(pd);
                    pd.activeSoaring = u;
                    p.closeInventory();
                } else {
                    pd.sendMessage(ChatColor.RED + "You can't afford that!");
                }
            });
            return mi;
        }
    }

    public void showMenu(PlayerDataRPG pd) {
        if (!pd.isValid())
            return;
        Player p = pd.getPlayer();
        ArrayList<MenuItem> menu = new ArrayList<MenuItem>();
        menu.add(new MenuItem(0, 4, new ItemStack(Material.BOOK_AND_QUILL), ChatColor.AQUA + ChatColor.BOLD.toString() + "Soaring", new String[] {
                ChatColor.AQUA + "Fly through the skies with Soaring!",
                ChatColor.YELLOW + "To begin Soaring, simply sneak on the ground for a second, then jump up and stop sneaking while you're in the air!"
        }, null));
        menu.add(new MenuItem(0, 8, new ItemStack(Material.PAPER), ChatColor.GOLD + "Additional Notes", new String[] {
                ChatColor.AQUA + "You cannot deal damage while Soaring.",
                ChatColor.GREEN + "If you take damage while Soaring, you will stop and be knocked to the ground.",
                ChatColor.RED + "You can't die from fall damage, but you can drop as low as 1 HP.",
                ChatColor.YELLOW + "Soaring can only be started from the ground, you can't jump off a cliff and then start Soaring.",
                ChatColor.DARK_AQUA + "Soaring stamina is used up " + ChatColor.RED + ChatColor.BOLD + "5x" + ChatColor.DARK_AQUA + " faster in Danger Level 3 and 4."
        }, null));
        ItemStack basic = new ItemStack(Material.INK_SACK, 1, DyeColor.GRAY.getDyeData());
        ItemStack intermediate = new ItemStack(Material.INK_SACK, 1, DyeColor.LIME.getDyeData());
        ItemStack advanced = new ItemStack(Material.INK_SACK, 1, DyeColor.PINK.getDyeData());
        ItemStack expert = new ItemStack(Material.INK_SACK, 1, DyeColor.MAGENTA.getDyeData());
        ItemStack master = new ItemStack(Material.INK_SACK, 1, DyeColor.PURPLE.getDyeData());
        if (pd.unlocked(Unlock.SOARING_SPEED_BASIC)) {
            // disable
            menu.add(new MenuItem(2, 0, new ItemStack(Material.BARRIER), ChatColor.WHITE + "Disable Soaring", new String[] {
                    ChatColor.GRAY + "Click to disable Soaring.",
                    "",
                    ChatColor.GRAY + "You can turn it back on at any time by using this menu!",
            }, () -> {
                pd.activeSoaring = null;
                pd.sendMessage(ChatColor.GRAY + "> " + ChatColor.RED + "Soaring is now disabled!");
                p.closeInventory();
            }));
            // 5 soaring speed tiers
            menu.add(new MenuItem(2, 2, basic, ChatColor.WHITE + "Basic Soaring", new String[] {
                    pd.activeSoaring == Unlock.SOARING_SPEED_BASIC ? ChatColor.DARK_GRAY + "Currently active." : ChatColor.GRAY + "Click to switch to Basic Soaring.",
                    "",
                    ChatColor.AQUA + "Float through the skies at a leisurely pace."
            }, () -> {
                pd.activeSoaring = Unlock.SOARING_SPEED_BASIC;
                pd.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "You have switched to " + ChatColor.AQUA + "Basic Soaring" + ChatColor.GREEN + ".");
                p.closeInventory();
            }));
            menu.add(generateUnlock(pd, p, Unlock.SOARING_SPEED_INTERMEDIATE, 2, 3, "Intermediate Soaring", "Travel through the skies at a good speed.", new String[] {
                    ChatColor.GRAY + "- " + ChatColor.YELLOW + "500 Shards",
                    ChatColor.GRAY + "- " + ChatColor.GREEN + "5 Power Fragments",
            }, intermediate));
            menu.add(generateUnlock(pd, p, Unlock.SOARING_SPEED_ADVANCED, 2, 4, "Advanced Soaring", "Travel through the skies at a fast speed.", new String[] {
                    ChatColor.GRAY + "- " + ChatColor.YELLOW + "5000 Shards",
                    ChatColor.GRAY + "- " + ChatColor.GREEN + "15 Power Fragments",
            }, advanced));
            menu.add(generateUnlock(pd, p, Unlock.SOARING_SPEED_EXPERT, 2, 5, "Expert Soaring", "Soar through the skies with great speed.", new String[] {
                    ChatColor.GRAY + "- " + ChatColor.YELLOW + "25000 Shards",
                    ChatColor.GRAY + "- " + ChatColor.GREEN + "100 Power Fragments",
            }, expert));
            menu.add(generateUnlock(pd, p, Unlock.SOARING_SPEED_MASTER, 2, 6, "Master Soaring", "Blaze through the air with your amazing Master Soaring!", new String[] {
                    ChatColor.GRAY + "- " + ChatColor.YELLOW + "100000 Shards",
                    ChatColor.GRAY + "- " + ChatColor.GREEN + "500 Power Fragments",
            }, master));
            // upgrade stamina
            if (pd.maxSoaringStamina < 30) {
                menu.add(new MenuItem(4, 4, new ItemStack(Material.COOKED_BEEF), ChatColor.YELLOW + "Upgrade Stamina", new String[] {
                        ChatColor.BLUE + "Current Stamina: " + ChatColor.AQUA + " " + pd.maxSoaringStamina + " seconds",
                        "",
                        ChatColor.GRAY + "Upgrade your soaring stamina to fly longer!",
                        "",
                        ChatColor.GOLD + "Click here to pay " + ChatColor.YELLOW + ChatColor.BOLD + getStaminaUpgradePrice(pd.maxSoaringStamina) + ChatColor.GOLD + " Shards to upgrade your stamina to " + ChatColor.AQUA + (pd.maxSoaringStamina + 1) + " seconds" + ChatColor.GOLD + "!"
                }, () -> {
                    if (ShardManager.countCurrency(p) >= getStaminaUpgradePrice(pd.maxSoaringStamina)) {
                        ShardManager.takeCurrency(p, getStaminaUpgradePrice(pd.maxSoaringStamina));
                        pd.maxSoaringStamina++;
                        pd.save();
                        p.closeInventory();
                        pd.sendMessage(ChatColor.GREEN + "You've upgraded your Soaring Stamina to " + ChatColor.AQUA + pd.maxSoaringStamina + " seconds" + ChatColor.GREEN + "!");
                    }
                }));
            } else {

                menu.add(new MenuItem(4, 4, new ItemStack(Material.COOKED_BEEF), ChatColor.YELLOW + "Soaring Stamina", new String[] {
                        ChatColor.BLUE + "Current Stamina: " + ChatColor.AQUA + " " + pd.maxSoaringStamina + " seconds",
                        "",
                        ChatColor.AQUA + "You have maxed out your stamina!",
                        "",
                        ChatColor.AQUA + "I guess you're really rich...",
                }, () -> {
                }));
            }

        } else {
            menu.add(new MenuItem(2, 2, basic, ChatColor.WHITE + "Basic Soaring", new String[] {
                    ChatColor.AQUA + "Complete the quest " + ChatColor.WHITE + "To The Skies!" + ChatColor.AQUA + " to unlock Basic Soaring!",
                    ChatColor.GRAY + "Check " + ChatColor.YELLOW + "/quests" + ChatColor.GRAY + " to see where the quest is!"
            }, null));
            menu.add(new MenuItem(2, 3, intermediate, ChatColor.WHITE + "Intermediate Soaring", new String[] {
                    ChatColor.AQUA + "Complete the quest " + ChatColor.WHITE + "To The Skies!" + ChatColor.AQUA + " to unlock Basic Soaring!",
                    ChatColor.GRAY + "Check " + ChatColor.YELLOW + "/quests" + ChatColor.GRAY + " to see where the quest is!"
            }, null));
            menu.add(new MenuItem(2, 4, advanced, ChatColor.WHITE + "Advanced Soaring", new String[] {
                    ChatColor.AQUA + "Complete the quest " + ChatColor.WHITE + "To The Skies!" + ChatColor.AQUA + " to unlock Basic Soaring!",
                    ChatColor.GRAY + "Check " + ChatColor.YELLOW + "/quests" + ChatColor.GRAY + " to see where the quest is!"
            }, null));
            menu.add(new MenuItem(2, 5, expert, ChatColor.WHITE + "Expert Soaring", new String[] {
                    ChatColor.AQUA + "Complete the quest " + ChatColor.WHITE + "To The Skies!" + ChatColor.AQUA + " to unlock Basic Soaring!",
                    ChatColor.GRAY + "Check " + ChatColor.YELLOW + "/quests" + ChatColor.GRAY + " to see where the quest is!"
            }, null));
            menu.add(new MenuItem(2, 6, master, ChatColor.WHITE + "Master Soaring", new String[] {
                    ChatColor.AQUA + "Complete the quest " + ChatColor.WHITE + "To The Skies!" + ChatColor.AQUA + " to unlock Basic Soaring!",
                    ChatColor.GRAY + "Check " + ChatColor.YELLOW + "/quests" + ChatColor.GRAY + " to see where the quest is!"
            }, null));
        }
        Inventory inv = MenuManager.createMenu(pd.getPlayer(), ChatColor.BLUE + ChatColor.BOLD.toString() + "Soaring", 6, menu);
        p.closeInventory();
        p.openInventory(inv);
    }

    private int getStaminaUpgradePrice(int current) {
        switch (current) {
            case 1:
                return 1;
            case 2:
                return 1;
            case 3:
                return 50;
            case 4:
                return 100;
            case 5:
                return 150;
            case 6:
                return 300;
            case 7:
                return 800;
            case 8:
                return 1400;
            case 9:
                return 2100;
            case 10:
                return 2900;
            case 11:
                return 3800;
            case 12:
                return 5500;
            case 13:
                return 7500;
            case 14:
                return 10000;
            case 15:
                return 15000;
            case 16:
                return 21000;
            case 17:
                return 28000;
            case 18:
                return 36000;
            case 19:
                return 45000;
            case 20:
                return 55000;
            case 21:
                return 66000;
            case 22:
                return 80000;
            case 23:
                return 95000;
            case 24:
                return 115000;
            case 25:
                return 150000;
            case 26:
                return 200000;
            case 27:
                return 265000;
            case 28:
                return 350000;
            case 29:
                return 500000;
            default:
                break;
        }
        return Integer.MAX_VALUE;
    }

    @EventHandler
    public void onToggleSneak(PlayerToggleSneakEvent event) {
        Player p = event.getPlayer();
        if (p.isGliding() && event.isSneaking()) {
            p.setGliding(false);
            RMessages.sendActionBar(p, ChatColor.RESET + "");
        } else {
            boolean inAir = false;
            inAir = (p.getLocation().getY() - (int) p.getLocation().getY()) > 0.01 || p.getLocation().add(0, -1, 0).getBlock().getType() == Material.AIR;
            long diff = System.currentTimeMillis() - startedSneak.getOrDefault(p.getUniqueId(), System.currentTimeMillis());
            PlayerDataRPG pd = plugin.getPD(p);
            if (pd != null && pd.hasState("switch_basic_soaring")) {
                pd.activeSoaring = Unlock.SOARING_SPEED_BASIC;
                pd.removeState("switch_basic_soaring");
            }
            if (pd != null && pd.activeSoaring != null && pd.loadedSQL && diff > 470 && !p.isFlying() && !event.isSneaking() && !p.isGliding() && p.getLocation().getBlock().getType() == Material.AIR && inAir) {
                if (System.currentTimeMillis() - pd.lastDamagedGlide < 10000) {
                    if (System.currentTimeMillis() - pd.lastDamagedGlideMessage > 3000) {
                        pd.sendMessage(ChatColor.RED + "You cannot Soar while in combat!");
                        pd.lastDamagedGlideMessage = System.currentTimeMillis();
                    }
                    return;
                }
                if (pd.soaringStamina <= 0) {
                    update(p, pd, 0);
                } else {
                    pd.soared = true;
                    double speed = 0.3;
                    switch (pd.activeSoaring) {
                        case SOARING_SPEED_BASIC:
                            speed = 0.35;
                            break;
                        case SOARING_SPEED_INTERMEDIATE:
                            speed = 0.55;
                            break;
                        case SOARING_SPEED_ADVANCED:
                            speed = 0.75;
                            break;
                        case SOARING_SPEED_EXPERT:
                            speed = 0.95;
                            break;
                        case SOARING_SPEED_MASTER:
                            speed = 1.25;
                            break;
                        default:
                            break;
                    }
                    final double fSpeed = speed;
                    FlightLaunchEffect effect = new FlightLaunchEffect(EffectFactory.em(), p.getLocation());
                    effect.run();
                    p.setVelocity(p.getLocation().getDirection().setY(0).normalize().setY(0.6));
                    update(p, pd, 0);
                    RScheduler.schedule(plugin, () -> {
                        p.setGliding(true);
                        p.setVelocity(p.getLocation().getDirection().setY(0).normalize().multiply(fSpeed).setY(0.2));
                        RScheduler.schedule(plugin, new Runnable() {
                            int counter = 0;

                            public void run() {
                                if (!p.isGliding()) {
                                    RMessages.sendActionBar(p, ChatColor.RESET + "");
                                    return;
                                }
                                counter++;
                                if (counter % 20 == 0) // one second
                                    pd.soaringStamina--;
                                if (counter % 3 == 0) {
                                    if (p.getLocation().getBlock().getType() != Material.AIR) {
                                        p.setGliding(false);
                                        //                                        RMessages.announce("Cancellingv1 " + p.getName() + " - in wall");
                                        RMessages.sendActionBar(p, ChatColor.RESET + "");
                                        return;
                                    } else if (p.getLocation().add(0, 1, 0).getBlock().getType() != Material.AIR) {
                                        p.setGliding(false);
                                        //                                        RMessages.announce("Cancellingv2 " + p.getName() + " - in wall");
                                        RMessages.sendActionBar(p, ChatColor.RESET + "");
                                        return;
                                    } else if (p.getLocation().add(p.getLocation().getDirection().normalize().multiply(1.5)).getBlock().getType() != Material.AIR) {
                                        p.setGliding(false);
                                        //                                        RMessages.announce("Cancellingv3 " + p.getName() + " - in wall");
                                        RMessages.sendActionBar(p, ChatColor.RESET + "");
                                        return;
                                    } else if (p.getLocation().add(p.getLocation().getDirection().normalize().multiply(1.5)).add(0, 1, 0).getBlock().getType() != Material.AIR) {
                                        p.setGliding(false);
                                        //                                        RMessages.announce("Cancellingv4 " + p.getName() + " - in wall");
                                        RMessages.sendActionBar(p, ChatColor.RESET + "");
                                        return;
                                    } else if (p.getLocation().add(p.getLocation().getDirection().normalize().multiply(2.5)).getBlock().getType() != Material.AIR) {
                                        p.setGliding(false);
                                        //                                        RMessages.announce("Cancellingv5 " + p.getName() + " - in wall");
                                        RMessages.sendActionBar(p, ChatColor.RESET + "");
                                        return;
                                    } else if (p.getLocation().add(p.getLocation().getDirection().normalize().multiply(3.5)).getBlock().getType() != Material.AIR) {
                                        p.setGliding(false);
                                        //                                        RMessages.announce("Cancellingv6 " + p.getName() + " - in wall");
                                        RMessages.sendActionBar(p, ChatColor.RESET + "");
                                        return;
                                    } else if (p.getLocation().add(p.getLocation().getDirection().normalize().multiply(2.5)).add(0, 1, 0).getBlock().getType() != Material.AIR) {
                                        p.setGliding(false);
                                        //                                        RMessages.announce("Cancellingv7 " + p.getName() + " - in wall");
                                        RMessages.sendActionBar(p, ChatColor.RESET + "");
                                        return;
                                    }
                                }
                                update(p, pd, counter);
                                if (pd.soaringStamina <= 0) {
                                    p.setGliding(false);
                                } else {
                                    if (counter % 2 == 0) {
                                        RParticles.showWithOffset(ParticleEffect.CLOUD, p.getLocation(), 0.35, 1);
                                        p.setVelocity(p.getLocation().getDirection().normalize().multiply(fSpeed));
                                        p.setFallDistance(0f);
                                    }
                                    if (p.isGliding())
                                        RScheduler.schedule(plugin, this, 1);
                                }
                            }
                        });
                    }, 10);
                }
            }
            if (event.isSneaking() && p.getLocation().add(0, -1, 0).getBlock().getType() != Material.AIR) {
                startedSneak.remove(p.getUniqueId());
                startedSneak.put(p.getUniqueId(), System.currentTimeMillis());
                if (event.isSneaking()) {
                    RScheduler.schedule(plugin, () -> {
                        if (p != null && p.isOnline() && !p.isGliding() && p.isSneaking()) {
                            RParticles.showWithOffset(ParticleEffect.CLOUD, p.getLocation().add(0, 1, 0), 1.2, 10);
                        }
                    }, 10);
                }
            } else {
                startedSneak.remove(p.getUniqueId());
            }
        }
    }

    private void update(Player p, PlayerDataRPG pd, int counter) {
        if (pd.soaringStamina <= 0) {
            RMessages.sendActionBar(p, ChatColor.RED + ChatColor.BOLD.toString() + "Out of Soaring Stamina!");
        } else {
            double full = 50;
            StringBuilder sb = new StringBuilder();
            double percent = (pd.soaringStamina * full - full * (counter % 20) / 20.0) / (double) (pd.maxSoaringStamina * full);
            int green = (int) Math.ceil(percent * full);
            int red = (int) Math.floor(full - green);
            for (int k = 0; k < red; k++) {
                sb.append(ChatColor.RED);
                //            sb.append('|');
                sb.append('\u258e');
            }
            for (int k = 0; k < green; k++) {
                sb.append(ChatColor.GREEN);
                sb.append('\u258e');
            }
            //        RMessages.sendTitle(p, "", sb.toString(), 0, 20, 20);
            RMessages.sendActionBar(p, sb.toString());
        }
    }

    @EventHandler
    public void onEntityToggleGlideEvent(EntityToggleGlideEvent event) {
        if (event.getEntity().getLocation().add(0, -1, 0).getBlock().getType() == Material.AIR)
            event.setCancelled(true);
    }
}
