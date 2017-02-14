package com.edasaki.rpg.horses;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.edasaki.core.menus.MenuManager;
import com.edasaki.core.utils.RMath;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.economy.ShardManager;
import com.edasaki.rpg.npcs.NPCEntity;
import com.edasaki.rpg.npcs.NPCType;

public class HorseVillager extends NPCEntity {

    public HorseVillager(int id, String name, double x, double y, double z, String world) {
        super(id, name, NPCType.VILLAGER, x, y, z, world);
    }

    public static String getStars(int i) {
        StringBuilder sb = new StringBuilder("");
        for (int k = 0; k < i; k++)
            sb.append("\u2B1B");
        if (i < 10) {
            for (int k = 0; k < 10 - i; k++)
                sb.append("\u2B1C");
        }
        return ChatColor.GOLD + sb.toString();
    }

    public static String getArmorString(PlayerDataRPG pd) {
        if (pd.horseArmor == null) {
            return ChatColor.GRAY + "No Armor";
        }
        switch (pd.horseArmor) {
            case IRON_BARDING:
                return ChatColor.WHITE + "Iron Armor";
            case GOLD_BARDING:
                return ChatColor.GOLD + "Gold Armor";
            case DIAMOND_BARDING:
                return ChatColor.AQUA + "Diamond Armor";
            default:
                return ChatColor.GRAY + "No Armor";
        }
    }

    public static String getAppearanceString(PlayerDataRPG pd) {
        StringBuilder sb = new StringBuilder();
        if (pd.horseBaby)
            sb.append("Baby ");
        switch (pd.horseVariant) {
            case DONKEY:
                sb.append("Donkey");
                return sb.toString();
            case HORSE:
                break;
            case MULE:
                sb.append("Mule");
                return sb.toString();
            case SKELETON_HORSE:
                sb.append("Skeleton Horse");
                return sb.toString();
            case UNDEAD_HORSE:
                sb.append("Zombie Horse");
                return sb.toString();
            default:
                sb.append("Bugged Horse (report this to Misaka pls)");
                return sb.toString();
        }
        switch (pd.horseColor) {
            case BLACK:
                sb.append("Black");
                break;
            case BROWN:
                sb.append("Brown");
                break;
            case CHESTNUT:
                sb.append("Chestnut");
                break;
            case CREAMY:
                sb.append("Creamy");
                break;
            case DARK_BROWN:
                sb.append("Dark Brown");
                break;
            case GRAY:
                sb.append("Gray");
                break;
            case WHITE:
                sb.append("White");
                break;
            default:
                break;
        }
        sb.append(" Horse ");
        switch (pd.horseStyle) {
            case BLACK_DOTS:
                sb.append("with Black Dots");
                break;
            case NONE:
                break;
            case WHITE:
                sb.append("with White Legs");
                break;
            case WHITEFIELD:
                sb.append("with Whitefield");
                break;
            case WHITE_DOTS:
                sb.append("with White Dots");
                break;
            default:
                break;
        }
        return sb.toString();
    }

    @Override
    public void interact(Player p, PlayerDataRPG pd) {
        if (pd.horseVariant == null) {
            double rand = Math.random();
            if (rand < 0.05) { // 5% 
                pd.horseVariant = Horse.Variant.SKELETON_HORSE;
            } else if (rand < 0.10) { // 5%
                pd.horseVariant = Horse.Variant.UNDEAD_HORSE;
            } else if (rand < 0.20) {// 10%
                pd.horseVariant = Horse.Variant.DONKEY;
            } else if (rand < 0.30) { // 10%
                pd.horseVariant = Horse.Variant.MULE;
            } else { // 70%
                pd.horseVariant = Horse.Variant.HORSE;
            }
        }
        if (pd.horseColor == null) {
            pd.horseColor = RMath.randObject(Horse.Color.values());
        }
        if (pd.horseStyle == null) {
            pd.horseStyle = RMath.randObject(Horse.Style.values());
        }
        if (pd.horseSpeed == 0 && pd.horseJump == 0) {
            Inventory i = MenuManager.createMenu(p, "The Zentrela Stables", 6, new Object[][] {
                    { 0, 0, Material.BOOK, ChatColor.GREEN + "About Horses", new Object[] {
                            ChatColor.WHITE,
                            "Travel faster with a horse!",
                            null,
                            "",
                            ChatColor.LIGHT_PURPLE,
                            "Use /horse to ride your horse.",
                            null,
                            "",
                            ChatColor.RED,
                            "You cannot deal damage while riding a horse!",
                    }, new Runnable() {
                        public void run() {
                        }
                    }
                    },
                    { 1, 4, Material.SADDLE, ChatColor.GREEN + "Your Horse", new Object[] {
                            ChatColor.AQUA,
                            "You don't have a horse!",
                    }, new Runnable() {
                        public void run() {
                        }
                    }
                    },
                    { 3, 4, Material.QUARTZ, ChatColor.GOLD + "Buy a Horse", new Object[] {
                            ChatColor.AQUA,
                            "Buy a horse for " + ChatColor.YELLOW + "50" + " " + ChatColor.YELLOW + "shards" + ChatColor.AQUA + "!",
                            null,
                            "",
                            ChatColor.GRAY,
                            "Click here to make your purchase!",
                    }, new Runnable() {
                        public void run() {
                            if (ShardManager.countCurrency(p) >= 50) {
                                ShardManager.takeCurrency(p, 50);
                                pd.horseSpeed = 1;
                                pd.horseJump = 1;
                                pd.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "Congratulations! You now own a horse!");
                                p.closeInventory();
                            } else {
                                pd.sendMessage(ChatColor.RED + "You don't have 50 shards with you!");
                                p.closeInventory();
                            }
                        }
                    }
                    },
            });
            p.openInventory(i);
        } else {
            Inventory i = MenuManager.createMenu(p, "The Zentrela Stables", 6, new Object[][] {
                    { 0, 0, Material.BOOK, ChatColor.GREEN + "About Horses", new Object[] {
                            ChatColor.WHITE,
                            "Travel faster with a horse!",
                            null,
                            "",
                            ChatColor.LIGHT_PURPLE,
                            "Use /horse to ride your horse.",
                            null,
                            "",
                            ChatColor.RED,
                            "You cannot deal damage while riding a horse!",
                    }, new Runnable() {
                        public void run() {
                        }
                    }
                    },
                    { 0, 4, Material.SADDLE, ChatColor.GREEN + "Your Horse", new Object[] {
                            ChatColor.DARK_AQUA,
                            "Speed",
                            ChatColor.GOLD,
                            getStars(pd.horseSpeed),
                            ChatColor.DARK_AQUA,
                            "Jump",
                            ChatColor.GOLD,
                            getStars(pd.horseJump),
                            null,
                            "",
                            ChatColor.WHITE,
                            getArmorString(pd),
                            null,
                            "",
                            ChatColor.YELLOW,
                            getAppearanceString(pd),
                    }, new Runnable() {
                        public void run() {
                        }
                    }
                    },
                    { 2, 4, Material.QUARTZ, ChatColor.GREEN + "Buy a New Horse", new Object[] {
                            ChatColor.AQUA,
                            "Get a random horse!",
                            ChatColor.YELLOW,
                            "The new horse will be totally random! It could be super fast, or super slow.",
                            ChatColor.DARK_AQUA,
                            "Horses with high speed and jump are rare, so you may have to keep getting new horses until you find a great one!",
                            ChatColor.DARK_RED,
                            "Your current horse will be deleted if you get a new horse!",
                            ChatColor.RED,
                            "Your horse's armor will not be deleted, and will transfer to your new horse.",
                            null,
                            "",
                            ChatColor.YELLOW,
                            "A new horse costs 200 shards.",
                            ChatColor.GRAY,
                            "Click here to buy a new horse!",

                    }, new Runnable() {
                        public void run() {
                            if (ShardManager.countCurrency(p) >= 200) {
                                ShardManager.takeCurrency(p, 200);
                                giveRandomHorse(pd);
                                pd.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "You got a new horse!");
                                p.closeInventory();
                            } else {
                                pd.sendMessage(ChatColor.RED + "You don't have 200 shards with you!");
                                p.closeInventory();
                            }
                        }
                    }
                    },
                    { 4, 2, Material.BARRIER, ChatColor.GREEN + "Remove Armor", new Object[] {
                            ChatColor.AQUA,
                            "Take away your horse's armor!",
                            ChatColor.DARK_AQUA,
                            "Horse armors are purely cosmetic and let you show off how rich you are.",
                            ChatColor.AQUA,
                            "When you get a new horse, your current armor will remain.",
                            ChatColor.DARK_AQUA,
                            "You can only own one horse armor at a time. If you buy one and then buy another, you will not have the first one anymore.",
                            null,
                            "",
                            ChatColor.GRAY,
                            "Click here to PERMANENTLY remove your horse armor. You will NOT get it back!",

                    }, new Runnable() {
                        public void run() {
                            pd.horseArmor = null;
                            pd.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "Your horse no longer has armor.");
                        }
                    }
                    },
                    { 4, 3, Material.IRON_BARDING, ChatColor.GREEN + "Switch to Iron Armor", new Object[] {
                            ChatColor.AQUA,
                            "Give your horse Iron Armor!",
                            ChatColor.DARK_AQUA,
                            "Horse armors are purely cosmetic and let you show off how rich you are.",
                            ChatColor.AQUA,
                            "When you get a new horse, your current armor will remain.",
                            ChatColor.DARK_AQUA,
                            "You can only own one horse armor at a time. If you buy one and then buy another, you will not have the first one anymore.",
                            null,
                            "",
                            ChatColor.YELLOW,
                            "Iron Armor costs 3000 shards.",
                            ChatColor.GRAY,
                            "Click here to buy Iron Armor for your horse!",

                    }, new Runnable() {
                        public void run() {
                            if (ShardManager.countCurrency(p) >= 3000) {
                                ShardManager.takeCurrency(p, 3000);
                                pd.horseArmor = Material.IRON_BARDING;
                                pd.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "You bought some fancy Iron Armor for your horse!");
                                p.closeInventory();
                            } else {
                                pd.sendMessage(ChatColor.RED + "You don't have 3000 shards with you!");
                                p.closeInventory();
                            }
                        }
                    }
                    },
                    { 4, 4, Material.GOLD_BARDING, ChatColor.GREEN + "Switch to Gold Armor", new Object[] {
                            ChatColor.AQUA,
                            "Give your horse Gold Armor!",
                            ChatColor.DARK_AQUA,
                            "Horse armors are purely cosmetic and let you show off how rich you are.",
                            ChatColor.AQUA,
                            "When you get a new horse, your current armor will remain.",
                            ChatColor.DARK_AQUA,
                            "You can only own one horse armor at a time. If you buy one and then buy another, you will not have the first one anymore.",
                            null,
                            "",
                            ChatColor.YELLOW,
                            "Gold Armor costs 10000 shards.",
                            ChatColor.GRAY,
                            "Click here to buy Gold Armor for your horse!",

                    }, new Runnable() {
                        public void run() {
                            if (ShardManager.countCurrency(p) >= 10000) {
                                ShardManager.takeCurrency(p, 10000);
                                pd.horseArmor = Material.GOLD_BARDING;
                                pd.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "You bought some super fancy Gold Armor for your horse!");
                                p.closeInventory();
                            } else {
                                pd.sendMessage(ChatColor.RED + "You don't have 10000 shards with you!");
                                p.closeInventory();
                            }
                        }
                    }
                    },
                    { 4, 5, Material.DIAMOND_BARDING, ChatColor.GREEN + "Switch to Diamond Armor", new Object[] {
                            ChatColor.AQUA,
                            "Give your horse Diamond Armor!",
                            ChatColor.DARK_AQUA,
                            "Horse armors are purely cosmetic and let you show off how rich you are.",
                            ChatColor.AQUA,
                            "When you get a new horse, your current armor will remain.",
                            ChatColor.DARK_AQUA,
                            "You can only own one horse armor at a time. If you buy one and then buy another, you will not have the first one anymore.",
                            null,
                            "",
                            ChatColor.YELLOW,
                            "Diamond Armor costs 50000 shards.",
                            ChatColor.GRAY,
                            "Click here to buy Diamond Armor for your horse!",

                    }, new Runnable() {
                        public void run() {
                            if (ShardManager.countCurrency(p) >= 50000) {
                                ShardManager.takeCurrency(p, 50000);
                                pd.horseArmor = Material.DIAMOND_BARDING;
                                pd.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "You bought some ULTRA fancy Diamond Armor for your horse!");
                                p.closeInventory();
                            } else {
                                pd.sendMessage(ChatColor.RED + "You don't have 50000 shards with you!");
                                p.closeInventory();
                            }
                        }
                    }
                    },
            });
            p.openInventory(i);
        }
    }

    private static void giveRandomHorse(PlayerDataRPG pd) {
        double rand = Math.random();
        int value = 1;
        if (rand < 0.01) { // 1%
            value = 10;
        } else if (rand < 0.02) { // 1%
            value = 9;
        } else if (rand < 0.04) { // 2%
            value = 8;
        } else if (rand < 0.08) { // 4%
            value = 7;
        } else if (rand < 0.12) { // 4%
            value = 6;
        } else if (rand < 0.20) { // 8%
            value = 5;
        } else if (rand < 0.35) { // 15%
            value = 4;
        } else if (rand < 0.60) { // 25%
            value = 3;
        } else if (rand < 0.95) { // 35%
            value = 2;
        } else { // 5%
            value = 1;
        }
        pd.horseSpeed = value;
        rand = Math.random();
        value = 1;
        if (rand < 0.01) { // 1%
            value = 10;
        } else if (rand < 0.02) { // 1%
            value = 9;
        } else if (rand < 0.04) { // 2%
            value = 8;
        } else if (rand < 0.08) { // 4%
            value = 7;
        } else if (rand < 0.12) { // 4%
            value = 6;
        } else if (rand < 0.20) { // 8%
            value = 5;
        } else if (rand < 0.35) { // 15%
            value = 4;
        } else if (rand < 0.60) { // 25%
            value = 3;
        } else if (rand < 0.95) { // 35%
            value = 2;
        } else { // 5%
            value = 1;
        }
        pd.horseJump = value;
        pd.horseBaby = Math.random() < 0.1 ? true : false;
        pd.horseColor = RMath.randObject(Horse.Color.values());
        pd.horseStyle = RMath.randObject(Horse.Style.values());
        rand = Math.random();
        if (rand < 0.05) { // 5% 
            pd.horseVariant = Horse.Variant.SKELETON_HORSE;
        } else if (rand < 0.10) { // 5%
            pd.horseVariant = Horse.Variant.UNDEAD_HORSE;
        } else if (rand < 0.20) {// 10%
            pd.horseVariant = Horse.Variant.DONKEY;
        } else if (rand < 0.30) { // 10%
            pd.horseVariant = Horse.Variant.MULE;
        } else { // 70%
            pd.horseVariant = Horse.Variant.HORSE;
        }
        pd.save();
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.DARK_AQUA;
    }

}
