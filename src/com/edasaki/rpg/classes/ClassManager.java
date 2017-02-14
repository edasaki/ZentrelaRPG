package com.edasaki.rpg.classes;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import com.edasaki.core.menus.MenuManager;
import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.items.ItemManager;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.VillagerSpellbook;

public class ClassManager extends AbstractManagerRPG {

    //    private static final String ONE_STAR = "\u2738";
    //    private static final String TWO_STAR = "\u2738\u2738";
    //    private static final String THREE_STAR = "\u2738\u2738\u2738";
    //    private static final String FOUR_STAR = "\u2738\u2738\u2738\u2738";
    //    private static final String FIVE_STAR = "\u2738\u2738\u2738\u2738\u2738";

    private static final String ONE_STAR = "\u2B1B\u2B1C\u2B1C\u2B1C\u2B1C";
    private static final String TWO_STAR = "\u2B1B\u2B1B\u2B1C\u2B1C\u2B1C";
    private static final String THREE_STAR = "\u2B1B\u2B1B\u2B1B\u2B1C\u2B1C";
    private static final String FOUR_STAR = "\u2B1B\u2B1B\u2B1B\u2B1B\u2B1C";
    private static final String FIVE_STAR = "\u2B1B\u2B1B\u2B1B\u2B1B\u2B1B";

    private enum Spellcast {
        RLL, RLR, RRL, RRR;
    }

    public ClassManager(SakiRPG plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {

    }

    public static void changeClass(Player p, PlayerDataRPG pd, String className, ClassType classType, String weaponName) {
        if (pd.region == null || pd.region.dangerLevel > 1) {
            pd.sendMessage(ChatColor.GRAY + "> " + ChatColor.RED + "You can only change classes in " + ChatColor.YELLOW + "Danger Level 1" + ChatColor.RED + " regions!");
            p.closeInventory();
            return;
        }
        boolean canChange = true;
        if (pd.classType == null || pd.classType == ClassType.VILLAGER) {
            p.sendMessage("");
            p.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "It looks like this is your first time choosing a class!");
            p.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "Here is a beginner weapon to help you get started.");
            p.getInventory().addItem(ItemManager.itemIdentifierToRPGItemMap.get(weaponName).generate());
        } else {
            //            if (pd.level >= 15) {
            //                int cost = 100;
            //                if (pd.level <= 30) {
            //                    cost = pd.level * 3;
            //                } else if (pd.level <= 40) {
            //                    cost = pd.level * 5;
            //                } else if (pd.level <= 50) {
            //                    cost = pd.level * 6;
            //                } else if (pd.level <= 60) {
            //                    cost = pd.level * 8;
            //                } else if (pd.level <= 70) {
            //                    cost = pd.level * 10;
            //                } else if (pd.level <= 80) {
            //                    cost = pd.level * 25;
            //                } else if (pd.level <= 90) {
            //                    cost = pd.level * 50;
            //                } else if (pd.level <= 100) {
            //                    cost = pd.level * 75;
            //                } else {
            //                    cost = pd.level * 100;
            //                }
            //                if (ShardManager.countCurrency(p) < cost) {
            //                    p.sendMessage(ChatColor.RED + "At your level, a class change costs " + ChatColor.YELLOW + cost + " shards" + ChatColor.RED + "!");
            //                    p.sendMessage(ChatColor.RED + "You do not have " + cost + " shards with you.");
            //                    okay = false;
            //                } else {
            //                    ShardManager.takeCurrency(p, cost);
            //                }
            //            }
        }
        if (canChange) {
            pd.resetSpells();
            pd.clearBuffs();
            pd.classType = classType;
            p.sendMessage(ChatColor.GREEN + "You are now a " + className + "!");
            p.closeInventory();
        }
    }

    public static void showAllClassMenu(final Player p, final PlayerDataRPG pd) {
        if (p.getWorld().getName().equalsIgnoreCase(SakiRPG.TUTORIAL_WORLD)) {
            p.sendMessage(ChatColor.GRAY + "> " + ChatColor.GREEN + "Sorry! You can't use " + ChatColor.YELLOW + ChatColor.BOLD + "/class" + ChatColor.GREEN + " until you finish the tutorial!");
            p.sendMessage(ChatColor.GRAY + "> " + ChatColor.AQUA + "Please use " + ChatColor.YELLOW + ChatColor.BOLD + "/class" + ChatColor.AQUA + " again once you get out of here!");
            return;
        }
        Inventory i = MenuManager.createMenu(p, "Zentrela Classes", 1, new Object[][] {
                { 0, 0, Material.BOOK, ChatColor.GREEN + "About Classes", new Object[] {
                        ChatColor.WHITE,
                        "Everyone in Zentrela can choose a class!",
                        ChatColor.WHITE,
                        "Classes have their own spells and use a specific weapon type to cast their spells.",
                        ChatColor.WHITE,
                        "You can change classes at any time using /class! There is a small fee for changing classes that increases as you level up.",
                        null,
                        "",
                        ChatColor.LIGHT_PURPLE,
                        "Difficulty",
                        ChatColor.YELLOW,
                        "How hard it is to master a class",
                        ChatColor.LIGHT_PURPLE,
                        "Normal Damage ",
                        ChatColor.YELLOW,
                        "Strength of normal left-click attacks, including buff spells and passives.",
                        ChatColor.LIGHT_PURPLE,
                        "Spell Damage ",
                        ChatColor.YELLOW,
                        "Strength of spells that deal damage directly.",
                        ChatColor.LIGHT_PURPLE,
                        "Survivability ",
                        ChatColor.YELLOW,
                        "How well the class can survive danger."
                }, new Runnable() {
                    public void run() {
                    }
                }
                },
                { 0, 2, Material.IRON_SWORD, ChatColor.AQUA + "Crusader", new Object[] {
                        ChatColor.GRAY,
                        "Sword Users",
                        ChatColor.GREEN,
                        "A well-balanced class with good offense and defense.",
                        null,
                        "",
                        //332
                        ChatColor.YELLOW,
                        ONE_STAR + ChatColor.WHITE + " Difficulty",
                        ChatColor.YELLOW,
                        THREE_STAR + ChatColor.WHITE + " Normal Damage",
                        ChatColor.YELLOW,
                        THREE_STAR + ChatColor.WHITE + " Spell Damage",
                        ChatColor.YELLOW,
                        THREE_STAR + ChatColor.WHITE + " Survivability",
                        null,
                        "",
                        ChatColor.AQUA,
                        "Crusaders are masters of the sword, and also the easiest class to play! Crusaders have a good variety of spells that allow them to alternate between a spell-spamming and left-clicking playstyle. If you want to be able to experience all Zentrela has to offer but aren't great at combat, Crusader is a good class for you!",
                }, new Runnable() {
                    public void run() {
                        ClassManager.changeClass(p, pd, "Crusader", ClassType.CRUSADER, "beginner_sword");
                    }
                }
                },
                { 0, 3, Material.GOLD_SPADE, ChatColor.AQUA + "Paladin", new Object[] {
                        ChatColor.GRAY,
                        "Mace Users",
                        ChatColor.GREEN,
                        "A tanky class that focuses on surviving all danger.",
                        null,
                        "",
                        //415
                        ChatColor.YELLOW,
                        ONE_STAR + ChatColor.WHITE + " Difficulty",
                        ChatColor.YELLOW,
                        FOUR_STAR + ChatColor.WHITE + " Normal Damage",
                        ChatColor.YELLOW,
                        ONE_STAR + ChatColor.WHITE + " Spell Damage",
                        ChatColor.YELLOW,
                        FIVE_STAR + ChatColor.WHITE + " Survivability",
                        null,
                        "",
                        ChatColor.AQUA,
                        "Paladins are defenders of righteousness, and are strong melee fighters. With several damage reduction spells and a variety of melee attack buffs, Paladins very powerful when face to face with an enemy. They excel at fighting monsters and bosses but struggle in PvP because of their lower mobility.",
                }, new Runnable() {
                    public void run() {
                        ClassManager.changeClass(p, pd, "Paladin", ClassType.PALADIN, "beginner_mace");
                    }
                }
                },
                { 0, 4, Material.SHEARS, ChatColor.AQUA + "Assassin", new Object[] {
                        ChatColor.GRAY,
                        "Dagger Users",
                        ChatColor.GREEN,
                        "A stealthy class that hard with spells but dies easily.",
                        null,
                        "",
                        //251
                        ChatColor.YELLOW,
                        THREE_STAR + ChatColor.WHITE + " Difficulty",
                        ChatColor.YELLOW,
                        TWO_STAR + ChatColor.WHITE + " Normal Damage",
                        ChatColor.YELLOW,
                        FIVE_STAR + ChatColor.WHITE + " Spell Damage",
                        ChatColor.YELLOW,
                        ONE_STAR + ChatColor.WHITE + " Survivability",
                        null,
                        "",
                        ChatColor.AQUA,
                        "Assassins hide in the shadows as they stalk their prey. Assassins can combo their spells for massive damage, and rely on stealth to cast spells as they approach their target. They are extremely dangerous in PvP because of their high burst damage, but have difficulty dealing with enemies after their initial attack.",
                }, new Runnable() {
                    public void run() {
                        ClassManager.changeClass(p, pd, "Assassin", ClassType.ASSASSIN, "beginner_dagger");
                    }
                }
                },
                { 0, 5, new Potion(PotionType.INSTANT_HEAL, 1).toItemStack(1), ChatColor.AQUA + "Alchemist", new Object[] {
                        ChatColor.GRAY,
                        "Elixir Users",
                        ChatColor.GREEN,
                        "A class that throws potions and is great at killing big groups.",
                        null,
                        "",
                        //252
                        ChatColor.YELLOW,
                        FIVE_STAR + ChatColor.WHITE + " Difficulty",
                        ChatColor.YELLOW,
                        TWO_STAR + ChatColor.WHITE + " Normal Damage",
                        ChatColor.YELLOW,
                        FIVE_STAR + ChatColor.WHITE + " Spell Damage",
                        ChatColor.YELLOW,
                        TWO_STAR + ChatColor.WHITE + " Survivability",
                        null,
                        "",
                        ChatColor.AQUA,
                        "Alchemists are a unique class that fights using special contraptions. Their normal attack deals damage in an area but reloads slowly, and their spells produce items like bombs and heat walls that are very deadly but hard to use. Alchemists can massacre groups of monsters, but struggle in PvP due to their high difficulty.",
                }, new Runnable() {
                    public void run() {
                        ClassManager.changeClass(p, pd, "Alchemist", ClassType.ALCHEMIST, "beginner_elixir");
                    }
                }
                },
                { 0, 6, Material.DIAMOND_HOE, ChatColor.AQUA + "Reaper", new Object[] {
                        ChatColor.GRAY,
                        "Scythe Users",
                        ChatColor.GREEN,
                        "A class that sacrifices HP for strong spells and quick healing.",
                        null,
                        "",
                        //144
                        ChatColor.YELLOW,
                        FOUR_STAR + ChatColor.WHITE + " Difficulty",
                        ChatColor.YELLOW,
                        ONE_STAR + ChatColor.WHITE + " Normal Damage",
                        ChatColor.YELLOW,
                        FOUR_STAR + ChatColor.WHITE + " Spell Damage",
                        ChatColor.YELLOW,
                        FOUR_STAR + ChatColor.WHITE + " Survivability",
                        null,
                        "",
                        ChatColor.AQUA,
                        "Reapers interact heavily with HP. Their spells can drain HP from enemies, sacrifice their own HP for more damage, or even heal themselves up! Although Reapers cannot deal massive amounts of damage in one hit, their healing ability helps them outlast and defeat even the most formidable enemies.",
                }, new Runnable() {
                    public void run() {
                        ClassManager.changeClass(p, pd, "Reaper", ClassType.REAPER, "beginner_scythe");
                    }
                }
                },
                { 0, 7, Material.BOW, ChatColor.AQUA + "Archer", new Object[] {
                        ChatColor.GRAY,
                        "Bow Users",
                        ChatColor.GREEN,
                        "A class that fights from long range. Bows have infinite arrows!",
                        null,
                        "",
                        //252
                        ChatColor.YELLOW,
                        FOUR_STAR + ChatColor.WHITE + " Difficulty",
                        ChatColor.YELLOW,
                        THREE_STAR + ChatColor.WHITE + " Normal Damage",
                        ChatColor.YELLOW,
                        FIVE_STAR + ChatColor.WHITE + " Spell Damage",
                        ChatColor.YELLOW,
                        TWO_STAR + ChatColor.WHITE + " Survivability",
                        null,
                        "",
                        ChatColor.AQUA,
                        "Archers fight from afar using their bows, which are fired by left-clicking and don't require any arrows. Archers have access to strong burst damage by using spells that fire several arrows at once. Archers are powerful against players and monsters alike, and are limited only by the accuracy of their shots.",
                }, new Runnable() {
                    public void run() {
                        ClassManager.changeClass(p, pd, "Archer", ClassType.ARCHER, "beginner_bow");
                    }
                }
                },
                { 0, 8, Material.STICK, ChatColor.AQUA + "Wizard", new Object[] {
                        ChatColor.GRAY,
                        "Wand Users",
                        ChatColor.GREEN,
                        "A magical class that uses cool spells to destroy their enemies.",
                        null,
                        "",
                        //244
                        ChatColor.YELLOW,
                        THREE_STAR + ChatColor.WHITE + " Difficulty",
                        ChatColor.YELLOW,
                        TWO_STAR + ChatColor.WHITE + " Normal Damage",
                        ChatColor.YELLOW,
                        FOUR_STAR + ChatColor.WHITE + " Spell Damage",
                        ChatColor.YELLOW,
                        FOUR_STAR + ChatColor.WHITE + " Survivability",
                        null,
                        "",
                        ChatColor.AQUA,
                        "Wizards are the traditional magician class, and have a highly varied playstyle based on what spells are used. They are a highly flexible class that always has the right spells for the occasion. Wizards excel at fighting monsters, and also pose a threat in PvP where their distancing abilities are difficult to handle.",
                }, new Runnable() {
                    public void run() {
                        ClassManager.changeClass(p, pd, "Wizard", ClassType.WIZARD, "beginner_wand");
                    }
                }
                },
        });
        p.openInventory(i);
    }

    public static void showClassSpellMenu(final Player p, final PlayerDataRPG pd) {
        showClassSpellMenu(p, pd, false);
    }

    public static void showClassSpellMenu(final Player p, final PlayerDataRPG pd, final boolean villager) {
        Inventory inventory = MenuManager.createMenu(p, (villager ? "Villager" : pd.classType.toString()) + " Spell Book", 6, new Object[][] {
                {
                        0,
                        0,
                        Material.BOOK_AND_QUILL,
                        ChatColor.AQUA + "Spellcast 1 " + ChatColor.YELLOW + "-" + ChatColor.GOLD + " RLL",
                        new Object[] {
                                ChatColor.GREEN,
                                pd.spell_RLL != null ? pd.spell_RLL.name : "None",
                                null,
                                "",
                                ChatColor.GRAY,
                                "Click to choose a spell for RLL.",
                                null,
                                "",
                                ChatColor.AQUA,
                                "To cast this spell, press " + ChatColor.YELLOW + "Right-Left-Left" + ChatColor.AQUA + " while holding a weapon of your class." },
                        new Runnable() {
                            public void run() {
                                showSpellcastChoices(Spellcast.RLL, p, pd, villager);
                            }
                        }
                },
                {
                        0,
                        1,
                        Material.BOOK_AND_QUILL,
                        ChatColor.AQUA + "Spellcast 2 " + ChatColor.YELLOW + "-" + ChatColor.GOLD + " RLR",
                        new Object[] {
                                ChatColor.GREEN,
                                pd.spell_RLR != null ? pd.spell_RLR.name : "None",
                                null,
                                "",
                                ChatColor.GRAY,
                                "Click to choose a spell for RLR.",
                                null,
                                "",
                                ChatColor.AQUA,
                                "To cast this spell, press " + ChatColor.YELLOW + "Right-Left-Right" + ChatColor.AQUA + " while holding a weapon of your class." },
                        new Runnable() {
                            public void run() {
                                showSpellcastChoices(Spellcast.RLR, p, pd, villager);
                            }
                        }
                },
                {
                        0,
                        2,
                        Material.BOOK_AND_QUILL,
                        ChatColor.AQUA + "Spellcast 3 " + ChatColor.YELLOW + "-" + ChatColor.GOLD + " RRL",
                        new Object[] {
                                ChatColor.GREEN,
                                pd.spell_RRL != null ? pd.spell_RRL.name : "None",
                                null,
                                "",
                                ChatColor.GRAY,
                                "Click to choose a spell for RRL.",
                                null,
                                "",
                                ChatColor.AQUA,
                                "To cast this spell, press " + ChatColor.YELLOW + "Right-Right-Left" + ChatColor.AQUA + " while holding a weapon of your class." },
                        new Runnable() {
                            public void run() {
                                showSpellcastChoices(Spellcast.RRL, p, pd, villager);
                            }
                        }
                },
                {
                        0,
                        3,
                        Material.BOOK_AND_QUILL,
                        ChatColor.AQUA + "Spellcast 4 " + ChatColor.YELLOW + "-" + ChatColor.GOLD + " RRR",
                        new Object[] {
                                ChatColor.GREEN,
                                pd.spell_RRR != null ? pd.spell_RRR.name : "None",
                                null,
                                "",
                                ChatColor.GRAY,
                                "Click to choose a spell for RRR.",
                                null,
                                "",
                                ChatColor.AQUA,
                                "To cast this spell, press " + ChatColor.YELLOW + "Right-Right-Right" + ChatColor.AQUA + " while holding a weapon of your class." },
                        new Runnable() {
                            public void run() {
                                showSpellcastChoices(Spellcast.RRR, p, pd, villager);
                            }
                        }
                },
                {
                        0,
                        7,
                        Material.BOOK,
                        ChatColor.GOLD + "Spell Points (SP)",
                        new Object[] {
                                ChatColor.GOLD,
                                "Current SP: " + pd.sp,
                                null,
                                "",
                                ChatColor.AQUA,
                                "Use SP to increase the level of your spells and passives! Each level costs 1 SP.",
                                null,
                                "",
                                ChatColor.GREEN,
                                "You get 1 SP per level.",
                                null,
                                "",
                                ChatColor.BLUE,
                                "Some strong spells must be unlocked by reaching a required level in a weaker spell.",
                                null,
                                "",
                                ChatColor.RED,
                                "You can use " + ChatColor.YELLOW + "/resetsp" + ChatColor.RED + " to reset your SP and get it all back, but it costs gold at higher levels!",
                                null,
                                "",
                                ChatColor.RED,
                                "Use your SP carefully!"
                        },
                        new Runnable() {
                            public void run() {
                                showAllClassMenu(p, pd);
                            }
                        }
                },
                {
                        0,
                        8,
                        Material.NAME_TAG,
                        ChatColor.GOLD + "Change Class",
                        new Object[] {
                                ChatColor.GREEN,
                                "You can change your class for free at any time!",
                                //                                "You can change your class for free until level 15.",
                                null,
                                "",
                                ChatColor.RED,
                                "You can only change your class in Danger Level 1 regions."
                        //                                ChatColor.DARK_RED + ChatColor.BOLD.toString() + "Warning" + ChatColor.RED + ": Class changes cost more shards at high levels, so choose your class wisely!" 
                        },
                        new Runnable() {
                            public void run() {
                                showAllClassMenu(p, pd);
                            }
                        }
                },
        });
        if (villager || pd.classType == null || pd.classType == ClassType.VILLAGER) {
            inventory = addClassSpells(inventory, p, pd, true);
            if (pd.classType != null && pd.classType != ClassType.VILLAGER) {
                inventory = MenuManager.modifyMenu(p, inventory, new Object[][] {
                        {
                                0,
                                5,
                                Material.LEATHER_CHESTPLATE,
                                ChatColor.GOLD + "View " + pd.classType.toString() + " Spellbook",
                                new Object[] { ChatColor.GREEN, "For " + pd.classType.toString().toLowerCase() + " eyes only." },
                                new Runnable() {
                                    public void run() {
                                        showClassSpellMenu(p, pd);
                                    }
                                }
                        },
                });
            } else {
                inventory = MenuManager.modifyMenu(p, inventory, new Object[][] {
                        {
                                0,
                                5,
                                Material.LEATHER_CHESTPLATE,
                                ChatColor.GOLD + "Choose a class!",
                                new Object[] { ChatColor.GREEN, "You'll get some sweet new spells to use!" },
                                new Runnable() {
                                    public void run() {
                                        showAllClassMenu(p, pd);
                                    }
                                }
                        },
                });
            }
        } else {
            inventory = MenuManager.modifyMenu(p, inventory, new Object[][] {
                    {
                            0,
                            5,
                            Material.LEATHER_CHESTPLATE,
                            ChatColor.GOLD + "View Villager Spellbook",
                            new Object[] {
                                    ChatColor.GREEN,
                                    "Spells you learned back in the good ol' days.",
                                    null,
                                    "",
                                    ChatColor.AQUA,
                                    "Spells gained from events and quests will also show up in the Villager Spellbook." },
                            new Runnable() {
                                public void run() {
                                    showClassSpellMenu(p, pd, true);
                                }
                            }
                    },
            });
            inventory = addClassSpells(inventory, p, pd, false);
        }
        p.openInventory(inventory);
    }

    private static void showSpellcastChoices(final Spellcast spellcast, final Player p, final PlayerDataRPG pd, final boolean villager) {
        ArrayList<Spell> spells = new ArrayList<Spell>();
        for (Spell s : VillagerSpellbook.INSTANCE.getSpellList())
            spells.add(s);
        if (pd.classType != null && pd.classType != ClassType.VILLAGER) {
            Spell[] spellList = pd.getSpellList();
            if (spellList != null)
                for (Spell s : spellList)
                    spells.add(s);
        }
        String title = "Choose a spell to bind to " + spellcast.toString() + ".";
        Object[] remove = { 0, 0, Material.BARRIER, ChatColor.RED + "Disable Spellcast", new Object[] { ChatColor.GRAY + ChatColor.ITALIC.toString(), "Make " + spellcast + " do nothing.",
        }, new Runnable() {
            public void run() {
                switch (spellcast) {
                    case RLL:
                        p.sendMessage(ChatColor.GREEN + "Disabled spellcast " + ChatColor.YELLOW + "RLL" + ChatColor.GREEN + "!");
                        pd.spell_RLL = null;
                        showClassSpellMenu(p, pd, villager);
                        break;
                    case RLR:
                        p.sendMessage(ChatColor.GREEN + "Disabled spellcast " + ChatColor.YELLOW + "RLR" + ChatColor.GREEN + "!");
                        pd.spell_RLR = null;
                        showClassSpellMenu(p, pd, villager);
                        break;
                    case RRL:
                        p.sendMessage(ChatColor.GREEN + "Disabled spellcast " + ChatColor.YELLOW + "RRL" + ChatColor.GREEN + "!");
                        pd.spell_RRL = null;
                        showClassSpellMenu(p, pd, villager);
                        break;
                    case RRR:
                        p.sendMessage(ChatColor.GREEN + "Disabled spellcast " + ChatColor.YELLOW + "RRR" + ChatColor.GREEN + "!");
                        pd.spell_RRR = null;
                        showClassSpellMenu(p, pd, villager);
                        break;
                }
            }
        }
        };
        ArrayList<Object[]> obj = new ArrayList<Object[]>();
        obj.add(remove);
        int row = 0;
        int col = 1;
        for (Spell s : spells) {
            Object[] genned = generateShort(spellcast, row, col++, s, p, pd, villager);
            if (genned != null)
                obj.add(genned);
            else
                col--;
            if (col >= 9) {
                row++;
                col = 0;
            }
            if (row >= 6) {
                p.sendMessage(ChatColor.RED + "Error code 102 - spell display. Please report this to Misaka or Edasaki!");
                break;
            }
        }
        int rows = (obj.size() + 8) / 9; //round up int division = (num + divisor - 1) / divisor
        if (rows == 0)
            rows = 1;
        Inventory inventory = MenuManager.createMenu(p, title, rows, obj.toArray(new Object[obj.size()][]));
        p.openInventory(inventory);
    }

    public static Inventory addClassSpells(Inventory inventory, Player p, PlayerDataRPG pd, boolean villager) {
        ArrayList<Object[]> obj = new ArrayList<Object[]>();
        if (villager) {
            for (Spell s : VillagerSpellbook.INSTANCE.getSpellList())
                obj.add(generate(s, p, pd, true));
        } else {
            if (pd.classType != null && pd.classType != ClassType.VILLAGER) {
                Spell[] spellList = pd.getSpellList();
                if (spellList != null)
                    for (Spell s : spellList)
                        obj.add(generate(s, p, pd, false));
            }
        }
        inventory = MenuManager.modifyMenu(p, inventory, obj.toArray(new Object[obj.size()][]));
        return inventory;
    }

    private static Object[] generateShort(final Spellcast spellcast, int row, int col, final Spell s, final Player p, final PlayerDataRPG pd, final boolean villager) {
        boolean passive = s.isPassive();
        if (passive)
            return null;
        final int currentLevel = pd.getSpellLevel(s);
        if (currentLevel <= 0 || currentLevel > s.maxLevel)
            return null;
        Object[] o = new Object[6];
        o[0] = row;
        o[1] = col;
        if (pd.usingSpell(s))
            o[2] = Material.SLIME_BALL;
        else
            o[2] = Material.SNOW_BALL;
        o[3] = ChatColor.GREEN + s.name + ChatColor.GRAY + " - " + ChatColor.DARK_AQUA + s.manaCost + " Mana";
        ArrayList<Object> info = new ArrayList<Object>();
        info.add(ChatColor.GRAY + ChatColor.ITALIC.toString());
        info.add("Click to bind this spell to " + spellcast.toString() + ".");
        info.add(ChatColor.GOLD);
        info.add("Current Level: " + currentLevel);
        info.add(null);
        info.add("");
        info.add(ChatColor.AQUA);
        info.add(s.getDescription(currentLevel));
        o[4] = info.toArray(new Object[info.size()]);
        Runnable r = new Runnable() {
            public void run() {
                switch (spellcast) {
                    case RLL:
                        p.sendMessage(ChatColor.GREEN + "Bound " + ChatColor.YELLOW + s.name + ChatColor.GREEN + " to spellcast " + ChatColor.YELLOW + "RLL" + ChatColor.GREEN + "!");
                        pd.spell_RLL = s;
                        showClassSpellMenu(p, pd, villager);
                        break;
                    case RLR:
                        p.sendMessage(ChatColor.GREEN + "Bound " + ChatColor.YELLOW + s.name + ChatColor.GREEN + " to spellcast " + ChatColor.YELLOW + "RLR" + ChatColor.GREEN + "!");
                        pd.spell_RLR = s;
                        showClassSpellMenu(p, pd, villager);
                        break;
                    case RRL:
                        p.sendMessage(ChatColor.GREEN + "Bound " + ChatColor.YELLOW + s.name + ChatColor.GREEN + " to spellcast " + ChatColor.YELLOW + "RRL" + ChatColor.GREEN + "!");
                        pd.spell_RRL = s;
                        showClassSpellMenu(p, pd, villager);
                        break;
                    case RRR:
                        p.sendMessage(ChatColor.GREEN + "Bound " + ChatColor.YELLOW + s.name + ChatColor.GREEN + " to spellcast " + ChatColor.YELLOW + "RRR" + ChatColor.GREEN + "!");
                        pd.spell_RRR = s;
                        showClassSpellMenu(p, pd, villager);
                        break;
                }
            }
        };
        o[5] = r;
        return o;
    }

    public static Object[] generate(final Spell s, final Player p, final PlayerDataRPG pd, final boolean villager) {
        boolean passive = s.isPassive();
        final int currentLevel = pd.getSpellLevel(s);
        Object[] o = new Object[6];
        o[0] = s.row;
        o[1] = s.col;
        if (passive) {
            if (currentLevel > 0) {
                o[2] = Material.PAPER;
                o[3] = ChatColor.GREEN + s.name + ChatColor.GRAY + " - " + ChatColor.DARK_AQUA + "Passive";
            } else {
                o[2] = Material.EMPTY_MAP;
                o[3] = ChatColor.DARK_RED + s.name + ChatColor.GRAY + " - " + ChatColor.DARK_AQUA + "Passive";
            }
        } else {
            if (currentLevel > 0) {
                if (pd.usingSpell(s))
                    o[2] = Material.SLIME_BALL;
                else
                    o[2] = Material.SNOW_BALL;
                o[3] = ChatColor.GREEN + s.name + ChatColor.GRAY + " - " + ChatColor.DARK_AQUA + s.manaCost + " Mana";
            } else {
                o[2] = Material.FIREWORK_CHARGE;
                o[3] = ChatColor.DARK_RED + s.name + ChatColor.GRAY + " - " + ChatColor.DARK_AQUA + s.manaCost + " Mana";
            }
        }
        ArrayList<Object> info = new ArrayList<Object>();
        boolean canLevel = currentLevel < s.maxLevel;
        for (Entry<Spell, Integer> e : s.requirements.entrySet()) {
            if (pd.getSpellLevel(e.getKey()) < e.getValue()) {
                info.add(ChatColor.RED + ChatColor.ITALIC.toString());
                info.add("Requires Level " + e.getValue() + " in " + e.getKey().name);
                canLevel = false;
            }
        }
        if (canLevel) {
            info.add(ChatColor.GRAY + ChatColor.ITALIC.toString());
            if (passive)
                info.add("Click to level up this passive.");
            else
                info.add("Click to level up this spell.");
        }
        info.add(ChatColor.GOLD);
        info.add("Current Level: " + currentLevel);
        info.add(ChatColor.BLUE);
        info.add("Max Level: " + s.maxLevel);
        if (currentLevel > 0 && currentLevel <= s.maxLevel) {
            info.add(null);
            info.add("");
            info.add(ChatColor.AQUA);
            info.add("Current:");
            info.add(ChatColor.AQUA);
            info.add(s.getDescription(currentLevel));
        }
        if (currentLevel < s.maxLevel) {
            info.add(null);
            info.add("");
            info.add(ChatColor.YELLOW);
            info.add("Next Level:");
            info.add(ChatColor.YELLOW);
            info.add(s.getDescription(currentLevel + 1));
        }
        o[4] = info.toArray(new Object[info.size()]);
        final boolean hasReqs = canLevel;
        Runnable r = new Runnable() {
            public void run() {
                if (System.currentTimeMillis() - pd.lastMenuClick < PlayerDataRPG.MENU_CLICK_RATE_LIMIT)
                    return;
                pd.lastMenuClick = System.currentTimeMillis();
                if (currentLevel < s.maxLevel) {
                    if (hasReqs) {
                        if (pd.sp > 0) {
                            if (pd.levelSpell(s)) {
                                pd.sp--;
                                p.sendMessage(ChatColor.GREEN + "You leveled up " + ChatColor.YELLOW + s.name + ChatColor.GREEN + " to level " + pd.getSpellLevel(s) + "!");
                                Inventory inventory = p.getOpenInventory().getTopInventory();
                                inventory = addClassSpells(inventory, p, pd, villager);
                            } else {
                                p.sendMessage(ChatColor.RED + "Oops, something went wrong! Please try again.");
                                p.closeInventory();
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "You don't have any SP! Level up for more SP.");
                            p.closeInventory();
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "You do not meet the requirements for this spell!");
                        p.closeInventory();
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "You've already mastered " + ChatColor.YELLOW + s.name + ChatColor.RED + "!");
                    p.closeInventory();
                }
            }
        };
        o[5] = r;
        return o;
    }
}
