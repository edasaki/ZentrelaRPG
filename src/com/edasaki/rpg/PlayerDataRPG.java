package com.edasaki.rpg;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import com.edasaki.core.ExtraSaveable;
import com.edasaki.core.PlayerData;
import com.edasaki.core.SakiCore;
import com.edasaki.core.badges.Badge;
import com.edasaki.core.menus.MenuManager;
import com.edasaki.core.options.OptionsManager;
import com.edasaki.core.options.SakiOption;
import com.edasaki.core.players.PlayerDataFile;
import com.edasaki.core.players.Rank;
import com.edasaki.core.punishments.PunishmentManager;
import com.edasaki.core.shield.SakiShieldCore;
import com.edasaki.core.unlocks.Unlock;
import com.edasaki.core.utils.RFormatter;
import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RMessages;
import com.edasaki.core.utils.RParticles;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RScheduler.Halter;
import com.edasaki.core.utils.RTicks;
import com.edasaki.core.utils.fanciful.FancyMessage;
import com.edasaki.core.utils.gson.RGson;
import com.edasaki.rpg.buycraft.BuycraftManager;
import com.edasaki.rpg.buycraft.BuycraftReward;
import com.edasaki.rpg.chat.ChatManager;
import com.edasaki.rpg.classes.ClassType;
import com.edasaki.rpg.combat.DamageType;
import com.edasaki.rpg.commands.member.LocationCommand;
import com.edasaki.rpg.dungeons.Dungeon;
import com.edasaki.rpg.dungeons.DungeonManager;
import com.edasaki.rpg.economy.ShardManager;
import com.edasaki.rpg.general.SchematicManager;
import com.edasaki.rpg.general.SchematicManager.SchematicUserConfig;
import com.edasaki.rpg.general.StealthManager;
import com.edasaki.rpg.items.EquipType;
import com.edasaki.rpg.items.ItemBalance;
import com.edasaki.rpg.items.ItemManager;
import com.edasaki.rpg.items.stats.StatAccumulator;
import com.edasaki.rpg.mobs.MobAI;
import com.edasaki.rpg.mobs.MobData;
import com.edasaki.rpg.mobs.MobManager;
import com.edasaki.rpg.particles.EffectName;
import com.edasaki.rpg.particles.ParticleManager;
import com.edasaki.rpg.parties.Party;
import com.edasaki.rpg.parties.PartyManager;
import com.edasaki.rpg.players.HealType;
import com.edasaki.rpg.players.PlayerBuffHandler;
import com.edasaki.rpg.players.PlayerStatics;
import com.edasaki.rpg.quests.MobTrackerInfo;
import com.edasaki.rpg.quests.Quest;
import com.edasaki.rpg.quests.QuestManager;
import com.edasaki.rpg.regions.Region;
import com.edasaki.rpg.regions.RegionManager;
import com.edasaki.rpg.regions.areas.TriggerArea;
import com.edasaki.rpg.skills.Skill;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellManager;
import com.edasaki.rpg.spells.SpellManager.CastState;
import com.edasaki.rpg.spells.Spellbook;
import com.edasaki.rpg.spells.SpellbookAlchemist;
import com.edasaki.rpg.spells.SpellbookArcher;
import com.edasaki.rpg.spells.SpellbookAssassin;
import com.edasaki.rpg.spells.SpellbookCrusader;
import com.edasaki.rpg.spells.SpellbookPaladin;
import com.edasaki.rpg.spells.SpellbookReaper;
import com.edasaki.rpg.spells.SpellbookWizard;
import com.edasaki.rpg.spells.VillagerSpellbook;
import com.edasaki.rpg.spells.alchemist.MysteryDrink;
import com.edasaki.rpg.spells.assassin.DoubleStab;
import com.edasaki.rpg.spells.assassin.ShadowStab;
import com.edasaki.rpg.spells.assassin.SinisterStrike;
import com.edasaki.rpg.spells.crusader.SwordSpirit;
import com.edasaki.rpg.spells.paladin.ChantOfNecessarius;
import com.edasaki.rpg.spells.paladin.FlameCharge;
import com.edasaki.rpg.spells.paladin.HolyGuardian;
import com.edasaki.rpg.spells.paladin.LightningCharge;
import com.edasaki.rpg.spells.paladin.WalkingSanctuary;
import com.edasaki.rpg.spells.reaper.DarkBargain;
import com.edasaki.rpg.trinkets.Trinket;
import com.edasaki.rpg.trinkets.TrinketStat;
import com.edasaki.rpg.utils.RSerializer;
import com.edasaki.rpg.warps.WarpLocation;
import com.edasaki.rpg.warps.WarpManager;
import com.google.gson.reflect.TypeToken;

import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.ParticleEffect.BlockData;

public class PlayerDataRPG extends PlayerData implements ExtraSaveable {

    public static SakiRPG plugin;

    public static final int MAX_MANA = 10;
    public static final String[] DEATH_MESSAGES = new String[] {
            "Better luck next time!",
            "Game over. (not really)",
            "Another one bites the dust.",
            "Better tie your shoelaces next time.",
            "rekt.",
            "That wasn\\'t so bad, was it?",
            "Ouch.",
            ":(",
            "Looks like you couldn\\'t handle it!",
            "It happens to the best of us!",
            "But you respawned, so it\\'s OK!",
            "That\\'s a bit embarrassing...",
            "That was brutal.",
    };

    public static Scoreboard board = null;
    private static Location lobbyLoc;

    /*
     * Party stuff
     */

    public Party party = null;
    public Party invitedParty = null;

    /*
     * Spell stuff
     */
    public int sp = 1;
    private HashMap<Spell, Integer> spellLevels = new HashMap<Spell, Integer>();
    public Spell spell_RLL = null;
    public Spell spell_RLR = null;
    public Spell spell_RRL = null;
    public Spell spell_RRR = null;

    private PlayerBuffHandler buffHandler = new PlayerBuffHandler();

    /*
     * Combat stats
     */
    private long lastHurt;

    public int level;
    public long exp;
    public int hp, baseMaxHP;
    public int loadedHP;
    public int mana;

    public int maxHP, defense;
    public double maxHPMultiplier, defenseMultiplier;

    public int rarityFinder = 0;
    public int manaRegenRate = 0;

    public float speed; //default mc speed is 0.2
    public double critChance;
    public double critDamage = 1.50;

    public double spellDamage = 1.0, attackDamage = 1.0;
    public double lifesteal = 0;
    public double hpRegen = 1.0;
    public double attackSpeed = 1.0;

    public int damageLow;
    public int damageHigh;

    public ClassType classType;

    private boolean stealthed = false;

    private int poisonTicks = 0;
    private int poisonTier = 0;
    private int burnTicks = 0;
    private int burnTier = 0;

    private HashMap<String, Integer> armorSetCounter = new HashMap<String, Integer>();
    private String lastMessagedSetName = "";
    private int lastMessagedSetCount = 0;

    private boolean hasSetBonusHP = false;
    private boolean hasSetBonusDamage = false;
    private ItemStack lastSetItem = null;

    private boolean finishedLoadEquips = false;

    public HashMap<String, Integer> questProgress = new HashMap<String, Integer>();

    private ArrayList<ItemStack> armor = new ArrayList<ItemStack>();

    public boolean castedFirework;
    public boolean usedTrinketCommand;

    private HashMap<Skill, Long> skillEXP = new HashMap<Skill, Long>();

    /*
     * Region-related Stuff
     */
    public Region region;
    public long lastRegionCheck;
    public long lastRegionTimeUpdate;
    public long lastRegionChange;

    public TriggerArea lastArea;
    public long lastAreaCheck;
    public long lastAreaTriggered;

    public int safespotCounter = 0;

    /*
     * Last timers for actions
     */
    public static final long MENU_CLICK_RATE_LIMIT = 200;
    public long lastMenuClick = 0; //prevent double clicking
    private long lastDamaged = 0;
    public long lastDamagedGlide = 0;
    public long lastDamagedGlideMessage = 0;
    private long lastEnvironmentalDamaged = 0;
    private long lastDamagedNonEnvironmental = 0;
    private PlayerDataRPG lastDamagerPlayerData = null;
    private String lastDamagerPlayer = "";
    private long lastDamagerPlayerTime = 0;
    private String lastDamagerMob = "";
    private long lastDamagerMobTime = 0;
    private DamageType lastDamageType = DamageType.NORMAL;
    //    public HashMap<String, Long> lastAttackedByTimes = new HashMap<String, Long>();
    public long lastKnockback = 0;
    public long lastArrowShot = 0;
    public long lastPotionThrown = 0;
    public long lastWandShot = 0;

    public boolean dead;

    private BossBar bossBar;

    public Trinket trinket;
    private Trinket lastTrinket;
    public long nextTrinketCast;
    public HashMap<Trinket, Long> trinketExp;

    public Location loadedLocation;
    public GameMode loadedGamemode;
    public HashMap<Integer, ItemStack> loadedInventory;

    public long lastLoggedTime;
    public int timePlayed;
    public ZonedDateTime joinDate;
    public ZonedDateTime lastSeen = null;

    public int horseSpeed, horseJump;
    public boolean horseBaby;
    public Horse.Style horseStyle;
    public Horse.Color horseColor;
    public Horse.Variant horseVariant;
    public Material horseArmor;

    public boolean riding = false;

    public EffectName activeEffect = null;

    public ArrayList<BuycraftReward> pendingBuycraftRewards = new ArrayList<BuycraftReward>();

    public Inventory bank;

    public Unlock activeSoaring = null;
    public int soaringStamina = 0;
    public int maxSoaringStamina = 3;
    public boolean soared = false;

    private long lastShardCountTime = 0;
    private int lastShardCount = 0;

    public int mobKills, playerKills, deaths, bossKills;

    public int xmasPoints;

    private HashSet<String> equipStates = new HashSet<String>();

    // <mob ID -> <mob ID (checked with contains) -> count>> 
    private HashMap<String, HashMap<String, Integer>> mobCounter = new HashMap<String, HashMap<String, Integer>>();

    public PlayerDataRPG(Player p) {
        super(p);
        lastLoggedTime = System.currentTimeMillis();
        perms.setPermission("bukkit.command.me", false);
        perms.setPermission("minecraft.command.me", false);
        perms.setPermission("bukkit.command.help", false);
        perms.setPermission("minecraft.command.help", false);
        perms.setPermission("bukkit.command.version", false);
    }

    public void addNewMobCounter(String mobID, String associatedTrackerID) {
        if (!mobCounter.containsKey(mobID)) {
            HashMap<String, Integer> newMap = new HashMap<String, Integer>();
            newMap.put(associatedTrackerID, 0);
            mobCounter.put(mobID, newMap);
        } else {
            mobCounter.get(mobID).put(associatedTrackerID, 0);
        }
    }

    private HashMap<String, Long> lastNotifiedFinishedMobCounter = new HashMap<String, Long>();

    public void incrementMobCounter(String mob) {
        if (mobCounter.containsKey(mob)) {
            mobCounter.get(mob).replaceAll((k, v) -> v + 1);
            for (Entry<String, Integer> e : mobCounter.get(mob).entrySet()) {
                MobTrackerInfo mti = QuestManager.trackerInfo.get(e.getKey());
                if (mti != null) {
                    int val = this.getMobCount(mti.identifier, mti.mobsToTrack);
                    boolean finished = false;
                    if (val >= mti.requiredCount) {
                        val = mti.requiredCount;
                        finished = true;
                    }
                    if (finished) {
                        if (System.currentTimeMillis() - lastNotifiedFinishedMobCounter.getOrDefault(e.getKey(), 0l) > 15000) {
                            sendMessage(ChatColor.GRAY + "> " + ChatColor.AQUA + mti.trackerFinishedNotification);
                            lastNotifiedFinishedMobCounter.put(e.getKey(), System.currentTimeMillis());
                        }
                    } else {
                        sendMessage(ChatColor.GRAY + "> You have killed " + ChatColor.YELLOW + val + ChatColor.GRAY + "/" + ChatColor.GOLD + mti.requiredCount + " " + ChatColor.GRAY + mti.trackerMobName + " for " + mti.questName + ".");
                    }
                }
            }
        }
    }

    public void removeMobCounter(String associatedID, String... mobs) {
        for (String s : mobs) {
            if (mobCounter.containsKey(s)) {
                Map<String, Integer> map = mobCounter.get(s);
                map.remove(associatedID);
                if (map.size() == 0)
                    mobCounter.remove(s);
            }
        }
    }

    public int getMobCount(String associatedID, String... mobs) {
        int count = 0;
        for (String s : mobs) {
            if (mobCounter.containsKey(s)) {
                count += mobCounter.get(s).getOrDefault(associatedID, 0);
            }
        }
        return count;
    }

    /*
     * Loading and saving data
     */

    /**
    * Runs before data has been loaded from SQL database
    */
    @Override
    public void preLoad(final Player p) {
        if (lobbyLoc == null) {
            lobbyLoc = new Location(plugin.getServer().getWorld(SakiRPG.GAME_WORLD), -461.5, 24.2, -1453.5, 0, 0);
            //            World w = plugin.getServer().getWorld(SakiRPG.LOBBY_WORLD);
            //            lobbyLoc = w.getSpawnLocation().add(0.5, 0.3, 0.5);
            //            System.out.println("set lobby loc to " + lobbyLoc);
            //            lobbyLoc.setYaw(90f);
        }
        p.getInventory().clear();
        p.updateInventory();
        if (!SakiRPG.TEST_REALM)
            p.teleport(lobbyLoc);
        p.setGameMode(GameMode.ADVENTURE);
        p.setFlying(false);
        bank = Bukkit.createInventory(p, 54, p.getName() + "'s Bank");
    }

    /**
    * Runs after data has been loaded from SQL database
    */
    @Override
    public void postLoad(final Player p) {
        if (p != null && isValid()) {
            if (!PunishmentManager.ips_byUUID.containsKey(p.getUniqueId().toString())) {
                String ip = PunishmentManager.parseIP(p.getAddress());
                PunishmentManager.registerIP(p.getUniqueId(), ip);
            } else {
                knownIPs.add(PunishmentManager.ips_byUUID.get(p.getUniqueId().toString()));
            }
            dead = false;
            baseMaxHP = getBaseMaxHP();
            updateEquipmentStats();
            for (PotionEffect pe : p.getActivePotionEffects())
                p.removePotionEffect(pe.getType());
            manaRegenTask();
            hpDisplayAndRegenTask();
            specialEffectsTask();
            everySecondTask();
            equipEffectsTask();
            saveTask();
            statusEffectTask();
            updateHealthManaDisplay();
            RegionManager.checkRegion(p, this);
            checkBuycraft(true);
            p.getInventory().clear();
            p.setGameMode(loadedGamemode);
            for (Entry<Integer, ItemStack> e : loadedInventory.entrySet()) {
                p.getInventory().setItem(e.getKey(), e.getValue());
            }
            updateEquipmentStats();
            hp = loadedHP;
            p.teleport(loadedLocation);
            RMessages.sendTitle(p, " ", " ", 1, 1, 1);
            checkBuycraft(false); // not in lobby
            RegionManager.checkRegion(p, this);
        }
    }

    public void checkBuycraft(boolean inLobby) {
        if (getPlayer() == null)
            return;
        // Check buycraft rewards
        boolean executedReward = false;
        for (int k = 0; k < pendingBuycraftRewards.size(); k++) {
            BuycraftReward br = pendingBuycraftRewards.get(k);
            if ((inLobby ? br.getCanGiveInLobby() : true) && br.execute(getPlayer(), this)) {
                executedReward = true;
                BuycraftManager.removeReward(name, br);
                pendingBuycraftRewards.remove(k);
                k--;
            }
        }
        if (executedReward)
            save();
    }

    @Override
    public void setExtraLoadedData(boolean isNew, PlayerDataFile pdf) {
        Player p = getPlayer();
        if (p != null && isValid()) {
            try {
                if (isNew) {
                    RMessages.announce("* " + ChatColor.GREEN + "Please welcome " + ChatColor.YELLOW + ChatColor.BOLD + name + ChatColor.GREEN + " to Zentrela!");
                }
                // REMEMBER TO ADD DEFAULTS IN PlayerDataFile.java AND SQL table column!
                level = pdf.getInt("level");
                hp = pdf.getInt("hp");
                loadedHP = pdf.getInt("hp");
                mana = pdf.getInt("mana");
                classType = ClassType.getClassType(pdf.get("classType"));
                deserializeSPAllocation(pdf.get("spAllocation"));
                spell_RLL = getSpellForName(pdf.get("spell_RLL"));
                spell_RLR = getSpellForName(pdf.get("spell_RLR"));
                spell_RRL = getSpellForName(pdf.get("spell_RRL"));
                spell_RRR = getSpellForName(pdf.get("spell_RRR"));
                deserializeQuestProgress(pdf.get("questProgress"));
                exp = pdf.getLong("exp");
                trinket = Trinket.getTrinket(pdf.get("trinket"));
                deserializeTrinketExp(pdf.get("trinketExp"));
                loadedLocation = RSerializer.deserializeLocation(pdf.get("location"));
                try {
                    loadedGamemode = GameMode.valueOf(pdf.get("gamemode"));
                } catch (Exception e) {
                    loadedGamemode = GameMode.ADVENTURE;
                }
                loadedInventory = deserializeInventory(pdf.get("inventory"));
                deserializeKnownIPs(pdf.get("knownIPs"));
                timePlayed = pdf.getInt("timePlayed");
                String tempJoinDate = pdf.get("joinDate");
                deserializeBuycraft(pdf.get("buycraft"));
                if (tempJoinDate.trim().length() <= 0) {
                    joinDate = ZonedDateTime.now();
                } else {
                    try {
                        joinDate = ZonedDateTime.parse(tempJoinDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                        joinDate = ZonedDateTime.now();
                    }
                }
                String tempLastSeen = pdf.get("lastSeen");
                if (tempLastSeen.trim().length() <= 0) {
                    lastSeen = null;
                } else {
                    try {
                        lastSeen = ZonedDateTime.parse(tempLastSeen);
                    } catch (Exception e) {
                        e.printStackTrace();
                        lastSeen = null;
                    }
                }
                deserializeBank(pdf.get("bank"));

                horseSpeed = pdf.getInt("horseSpeed");
                horseJump = pdf.getInt("horseJump");
                horseBaby = pdf.getInt("horseBaby") != 0;
                try {
                    horseStyle = Horse.Style.valueOf(pdf.get("horseStyle"));
                } catch (Exception e) {
                    horseStyle = null;
                }
                try {
                    horseColor = Horse.Color.valueOf(pdf.get("horseColor"));
                } catch (Exception e) {
                    horseColor = null;
                }
                try {
                    horseVariant = Horse.Variant.valueOf(pdf.get("horseVariant"));
                } catch (Exception e) {
                    horseVariant = null;
                }

                try {
                    horseArmor = Material.valueOf(pdf.get("horseArmor"));
                } catch (Exception e) {
                    horseArmor = null;
                }

                try {
                    activeSoaring = Unlock.valueOf(pdf.get("activeSoaring"));
                } catch (Exception e) {
                    activeSoaring = null;
                }

                maxSoaringStamina = pdf.getInt("maxSoaringStamina");
                if (maxSoaringStamina < 3)
                    maxSoaringStamina = 3;

                deserializeBadges(pdf.get("badges"));

                Map<Skill, Long> temp = (HashMap<Skill, Long>) RGson.getGson().<HashMap<Skill, Long>> fromJson(pdf.get("skillEXP"), new TypeToken<HashMap<Skill, Long>>() {
                }.getType());
                if (temp != null)
                    skillEXP.putAll(temp);

                Map<String, HashMap<String, Integer>> temp2 = (HashMap<String, HashMap<String, Integer>>) RGson.getGson().<HashMap<String, HashMap<String, Integer>>> fromJson(pdf.get("mobCounter"), new TypeToken<HashMap<String, HashMap<String, Integer>>>() {
                }.getType());
                if (temp2 != null)
                    mobCounter.putAll(temp2);

                mobKills = pdf.getInt("mobKills");
                playerKills = pdf.getInt("playerKills");
                bossKills = pdf.getInt("bossKills");

                deaths = pdf.getInt("deaths");

                xmasPoints = pdf.getInt("xmasPoints");

                // Finished loading data
                if (name.equals("Misaka")) {
                    rank = Rank.OWNER;
                    ChatManager.monitors.add(getPlayer().getName());
                    //                } else if (rank == Rank.OWNER) {
                    //                    rank = Rank.VIP;
                    //                    RMessages.announce(ChatColor.RED + "Deranked invalid owner " + getPlayer().getName() + ".");
                    //                    RMessages.announce(ChatColor.RED + "If you see this message, please report it on the forums!");
                }
                if (check(Rank.BETA) || SakiRPG.TEST_REALM) {
                    FancyMessage fm = new FancyMessage("* " + rank.getChatRankDisplay() + name + " has logged on!");
                    ChatManager.chatHover(fm, this);
                    fm.send(plugin.getServer().getOnlinePlayers());
                }
                if (isNew)
                    save();
            } catch (Exception e) {
                e.printStackTrace();
                if (isValid()) {
                    p.kickPlayer("Error loading your save data! Don't worry - your data isn't lost. We're probably having connection problems.");
                }
            }
        }
    }

    @Override
    public PlayerDataFile extraSave(PlayerDataFile pdf) {
        pdf.put("level", level);
        pdf.put("hp", hp);
        pdf.put("mana", mana);
        pdf.put("classType", classType.name);
        pdf.put("knownIPs", serializeKnownIPs());
        pdf.put("timePlayed", getTimePlayed());
        pdf.put("joinDate", joinDate.toString());
        pdf.put("lastSeen", ZonedDateTime.now().toString());
        pdf.put("spell_RLL", spell_RLL == null ? "" : spell_RLL.name);
        pdf.put("spell_RLR", spell_RLR == null ? "" : spell_RLR.name);
        pdf.put("spell_RRL", spell_RRL == null ? "" : spell_RRL.name);
        pdf.put("spell_RRR", spell_RRR == null ? "" : spell_RRR.name);
        pdf.put("spAllocation", serializeSPAllocation());
        pdf.put("questProgress", serializeQuestProgress());
        pdf.put("exp", exp);
        pdf.put("trinket", trinket == null ? "" : trinket.toString());
        pdf.put("trinketExp", serializeTrinketExp());
        if (getCurrentDungeon() == null) // dungeon locations are not saved!
            pdf.put("location", RSerializer.serializeLocation(getPlayer().getLocation()));
        pdf.put("gamemode", getPlayer().getGameMode().toString());
        pdf.put("inventory", serializeInventory());
        pdf.put("bank", serializeBank());
        pdf.put("horseSpeed", horseSpeed);
        pdf.put("horseJump", horseJump);
        pdf.put("horseBaby", horseBaby ? 1 : 0);
        pdf.put("horseColor", horseColor == null ? "" : horseColor.toString());
        pdf.put("horseStyle", horseStyle == null ? "" : horseStyle.toString());
        pdf.put("horseVariant", horseVariant == null ? "" : horseVariant.toString());
        pdf.put("horseArmor", horseArmor == null ? "" : horseArmor.toString());
        pdf.put("badges", serializeBadges());
        pdf.put("skillEXP", RGson.getConciseGson().toJson(skillEXP));
        pdf.put("activeSoaring", activeSoaring == null ? "" : activeSoaring.toString());
        pdf.put("maxSoaringStamina", maxSoaringStamina);
        pdf.put("mobCounter", RGson.getConciseGson().toJson(mobCounter));
        pdf.put("lastShardCount", getShardCount(true));
        pdf.put("mobKills", mobKills);
        pdf.put("playerKills", playerKills);
        pdf.put("bossKills", bossKills);
        pdf.put("deaths", deaths);
        pdf.put("xmasPoints", xmasPoints);
        return pdf;
    }

    public int getShardCount(boolean forceRecount) {
        if (forceRecount || System.currentTimeMillis() - lastShardCountTime > 30000) {
            lastShardCountTime = System.currentTimeMillis();
            int res = 0;
            res += ShardManager.countCurrency(getPlayer());
            res += ShardManager.countCurrency(this.bank);
            lastShardCount = res;
            return res;
        }
        return lastShardCount;
    }

    public int getTimePlayed() {
        long diff = System.currentTimeMillis() - lastLoggedTime;
        diff /= 60000;
        int added = (int) Math.floor(diff);
        timePlayed += added;
        lastLoggedTime += added * 60000;
        return timePlayed;
    }

    private void deserializeBuycraft(String buycraft) {
        String[] data = buycraft.split(" ");
        for (String s : data) {
            BuycraftReward br = BuycraftManager.getReward(s);
            if (br != null) {
                System.out.println("Loaded pending reward " + br.toString() + " for user " + name);
                pendingBuycraftRewards.add(br);
            }
        }
    }

    private void deserializeKnownIPs(String temp) {
        if (temp != null) {
            for (String s : temp.split(" ")) {
                knownIPs.add(s);
            }
        }
    }

    private String serializeKnownIPs() {
        StringBuilder sb = new StringBuilder();
        for (String s : knownIPs) {
            sb.append(s);
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    private String serializeInventory() {
        StringBuilder sb = new StringBuilder();
        Player p = getPlayer();
        ItemStack[] arr = p.getInventory().getContents();
        for (int k = 0; k < arr.length; k++) {
            if (arr[k] != null) {
                sb.append(k);
                sb.append("::");
                sb.append(RSerializer.serializeItemStack(arr[k]));
                sb.append("@");
            }
        }
        String s = sb.toString().trim();
        if (s.endsWith("@"))
            s = s.substring(0, s.length() - 1);
        return s;
    }

    private HashMap<Integer, ItemStack> deserializeInventory(String s) {
        HashMap<Integer, ItemStack> map = new HashMap<Integer, ItemStack>();
        if (s == null)
            return map;
        String[] data = s.split("@");
        if (data.length == 0 || (data.length == 1 && data[0].equals("")))
            return map;
        for (String temp : data) {
            try {
                // don't use split in case item serialization contains ::
                String a = temp.substring(0, temp.indexOf("::"));
                String b = temp.substring(temp.indexOf("::") + "::".length());
                int k = Integer.parseInt(a);
                ItemStack item = RSerializer.deserializeItemStack(b);
                map.put(k, item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    private String serializeBank() {
        StringBuilder sb = new StringBuilder();
        ItemStack[] arr = bank.getContents();
        for (int k = 0; k < arr.length; k++) {
            if (arr[k] != null) {
                sb.append(k);
                sb.append("::");
                sb.append(RSerializer.serializeItemStack(arr[k]));
                sb.append("@");
            }
        }
        String s = sb.toString().trim();
        if (s.endsWith("@"))
            s = s.substring(0, s.length() - 1);
        return s;
    }

    private void deserializeBank(String s) {
        HashMap<Integer, ItemStack> map = new HashMap<Integer, ItemStack>();
        if (s == null)
            return;
        String[] data = s.split("@");
        if (data.length == 0 || (data.length == 1 && data[0].equals("")))
            return;
        for (String temp : data) {
            try {
                // don't use split in case item serialization contains ::
                String a = temp.substring(0, temp.indexOf("::"));
                String b = temp.substring(temp.indexOf("::") + "::".length());
                int k = Integer.parseInt(a);
                ItemStack item = RSerializer.deserializeItemStack(b);
                map.put(k, item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        bank.clear();
        for (Entry<Integer, ItemStack> e : map.entrySet()) {
            bank.setItem(e.getKey(), e.getValue());
        }
    }

    private String serializeBadges() {
        StringBuilder sb = new StringBuilder();
        for (Badge b : badges) {
            sb.append(b.toString());
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    private void deserializeBadges(String s) {
        String[] data = s.split(" ");
        for (String b : data) {
            b = b.trim();
            if (b.length() == 0)
                continue;
            try {
                badges.add(Badge.valueOf(b));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String serializeTrinketExp() {
        if (trinketExp == null)
            return "";
        StringBuilder sb = new StringBuilder();
        for (Entry<Trinket, Long> e : trinketExp.entrySet()) {
            sb.append(e.getKey().name);
            sb.append("::");
            sb.append(e.getValue());
            sb.append("@");
        }
        String s = sb.toString().trim();
        if (s.endsWith("@"))
            s = s.substring(0, s.length() - 1);
        return s;
    }

    private void deserializeTrinketExp(String s) {
        if (trinketExp == null)
            trinketExp = new HashMap<Trinket, Long>();
        trinketExp.clear();
        String[] data = s.split("@");
        if (data.length == 0 || (data.length == 1 && data[0].equals("")))
            return;
        for (String temp : data) {
            try {
                String[] data2 = temp.split("::");
                Trinket trinket = Trinket.getTrinket(data2[0]);
                if (trinket != null)
                    trinketExp.put(trinket, Long.parseLong(data2[1]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String serializeQuestProgress() {
        StringBuilder sb = new StringBuilder();
        for (Entry<String, Integer> e : this.questProgress.entrySet()) {
            //            sb.append(e.getKey().identifier);
            sb.append(e.getKey());
            sb.append("@@");
            sb.append(e.getValue());
            sb.append("###");
        }
        String s = sb.toString().trim();
        if (s.endsWith("###"))
            s = s.substring(0, s.length() - "###".length());
        return sb.toString().trim();
    }

    private void deserializeQuestProgress(String s) {
        String[] data = s.split("###");
        for (String entry : data) {
            if ((entry = entry.trim()).length() == 0)
                continue;
            String[] data2 = entry.split("@@");
            if (data2.length != 2) {
                RMessages.announce("ERROR: Loading player " + name + "'s quest progress.");
                RMessages.announce("Entry: " + entry);
            } else {
                String identifier = data2[0];
                int part = Integer.parseInt(data2[1]);
                questProgress.put(identifier, part);
            }
        }
    }

    private String serializeSPAllocation() {
        StringBuilder sb = new StringBuilder();
        for (Entry<Spell, Integer> e : this.spellLevels.entrySet()) {
            sb.append(e.getKey().toString().replace(" ", "__"));
            sb.append(' ');
            sb.append(e.getValue());
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    private void deserializeSPAllocation(String s) {
        String[] data = s.split(" ");
        this.spellLevels.clear();
        this.sp = level;
        boolean modified = false;
        for (int k = 0; k < data.length; k += 2) {
            try {
                String replaced = data[k].replace("__", " ");
                if (replaced.trim().length() == 0)
                    continue;
                Spell spell = getSpellForName(replaced);
                if (spell != null) {
                    int level = Integer.parseInt(data[k + 1]);
                    if (level > spell.maxLevel) {
                        modified = true;
                        level = spell.maxLevel;
                    }
                    this.sp -= level;
                    this.spellLevels.put(spell, level);
                } else {
                    modified = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (modified) {
            RScheduler.schedule(plugin, () -> {
                sendMessage(ChatColor.DARK_RED + " WARNING: " + ChatColor.RED + "Some of your spells may have changed!");
                sendMessage(ChatColor.RED + " Some spells you had points in were changed in a recent patch.");
                sendMessage(ChatColor.RED + " You may have spare spell points - please check in " + ChatColor.YELLOW + "/spell" + ChatColor.RED + ".");
            }, RTicks.seconds(2));
        }
        if (this.sp < 0)
            this.sp = 0;
    }

    @Override
    public void unload() {
        if (isStealthed())
            removeStealth();
        if (bossBar != null)
            bossBar.removeAll();
        bossBar = null;
        if (uuid != null) {
            ParticleManager.dispose(uuid);
            DungeonManager.dispose(uuid);
            MenuManager.clear(uuid);
            SchematicUserConfig cfg = SchematicManager.configs.remove(uuid);
            if (cfg != null) {
                cfg.history.clear();
                cfg.loadedSchematics.clear();
            }
        }
        if (name != null) {
            //            TeleportAcceptCommand.cleanup(name);
            WarpManager.cleanup(name);
            SakiShieldCore.clear(name);
        }
        Player p = getPlayer();
        if (p != null && p.isValid()) {
            for (PotionEffect pe : p.getActivePotionEffects())
                p.removePotionEffect(pe.getType());
        }
        super.unload();
    }

    public void quit() {
        try {
            if (SakiCore.TEST_REALM) {
                FancyMessage fm = new FancyMessage("* " + rank.getChatRankDisplay() + name + " has logged off.");
                ChatManager.chatHover(fm, this);
                fm.send(plugin.getServer().getOnlinePlayers());
            }
            if (party != null && uuid != null && name != null)
                party.leavePlayer(uuid, name);
            party = null;
            invitedParty = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.quit();
    }

    public ItemStack[] getBankContents() {
        return bank.getContents();
    }

    public void openBank() {
        if (!this.loadedSQL) {
            sendMessage(ChatColor.RED + "You can't open your bank right now.");
            return;
        }
        if (this.region != null && this.region.dangerLevel > 1) {
            sendMessage(ChatColor.RED + "Banks can only be used in Danger Level 1 regions.");
            return;
        }
        Player p = getPlayer();
        if (p != null && p.isValid()) {
            p.openInventory(bank);
        }
    }

    public Dungeon getCurrentDungeon() {
        for (Dungeon d : DungeonManager.dungeons) {
            if (d.checkRegion(this.region)) {
                return d;
            }
        }
        return null;
    }

    public Location getRespawnLocation(Location playerLoc) {
        try {
            Dungeon d = null;
            if ((d = getCurrentDungeon()) != null) {
                return d.dungeonMaster.getTPLoc();
            } else {
                Location destination = null;
                double shortest = -1;
                for (WarpLocation wl : WarpLocation.values()) {
                    Location warpLoc = wl.getLocation();
                    Region destRegion = RegionManager.getRegion(warpLoc);
                    if (destRegion == null || destRegion.recLevel > this.level) {
                        continue;
                    }
                    double distance = RMath.flatDistance(playerLoc, warpLoc);
                    if (shortest == -1 || distance < shortest) {
                        shortest = distance;
                        destination = warpLoc;
                    }
                }
                if (destination != null)
                    return destination;
                return WarpLocation.OLD_MARU_ISLAND.getMutableLocation();
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendMessage("Error code 107: Respawnloc. Please report this to Misaka or Edasaki!");
            return WarpLocation.OLD_MARU_ISLAND.getMutableLocation();
        }
    }
    /*
     * Trinkets
     */

    public long getTrinketExp(Trinket trinket) {
        if (trinket == null || !trinketExp.containsKey(trinket))
            return 0;
        return trinketExp.get(trinket);
    }

    /*
     * Quests
     */

    public int getQuestProgress(Quest q) {
        if (questProgress.containsKey(q.identifier)) {
            return questProgress.get(q.identifier);
        }
        return -1;
    }

    public void advanceQuest(Quest q) {
        if (questProgress.containsKey(q.identifier)) {
            questProgress.put(q.identifier, questProgress.get(q.identifier) + 1);
        } else {
            questProgress.put(q.identifier, 0);
        }
    }

    public boolean completedQuest(Quest q) {
        if (questProgress.containsKey(q.identifier)) {
            int curr = questProgress.get(q.identifier);
            if (curr >= q.parts.size() - 1)
                return true;
        }
        return false;
    }

    /*
     * Spells
     */

    public void clearBuffs() {
        buffHandler.endTime.clear();
        buffHandler.values.clear();
    }

    public boolean hasBuff(String s) {
        s = s.toLowerCase();
        return buffHandler.endTime.containsKey(s) && System.currentTimeMillis() < buffHandler.endTime.get(s) && buffHandler.values.containsKey(s);
    }

    public void removeBuff(String s) {
        s = s.toLowerCase();
        buffHandler.endTime.remove(s);
        buffHandler.values.remove(s);
    }

    public void giveBuff(String s, double value, long durationMilliseconds) {
        s = s.toLowerCase();
        buffHandler.endTime.put(s, System.currentTimeMillis() + durationMilliseconds);
        buffHandler.values.put(s, value);
    }

    public void giveBuff(String s, double value, long durationMilliseconds, final String message) {
        giveBuff(s, value, durationMilliseconds);
        RScheduler.schedule(plugin, new Runnable() {
            public void run() {
                sendMessage(ChatColor.GRAY + ">> " + ChatColor.GREEN + message);
            }
        }, RTicks.seconds(durationMilliseconds / 1000.0));
    }

    public double getBuffValue(String s) {
        s = s.toLowerCase();
        if (hasBuff(s))
            return buffHandler.values.get(s);
        else
            return 0;
    }

    public Spell getSpellForName(String name) {
        Spellbook sb = getSpellbook();
        Spell spell = sb.getSpell(name);
        if (spell != null)
            return spell;
        spell = VillagerSpellbook.INSTANCE.getSpell(name);
        if (spell != null)
            return spell;
        return null;
    }

    public void resetSpells() {
        spellLevels.clear();
        spell_RLL = null;
        spell_RLR = null;
        spell_RRL = null;
        spell_RRR = null;
        sp = level;
    }

    public boolean levelSpell(Spell s) {
        if (spellLevels.containsKey(s) && spellLevels.get(s) >= s.maxLevel)
            return false;
        if (spellLevels.containsKey(s)) {
            spellLevels.put(s, spellLevels.get(s) + 1);
        } else {
            spellLevels.put(s, 1);
        }
        return true;
    }

    public Spellbook getSpellbook() {
        switch (this.classType) {
            case CRUSADER:
                return SpellbookCrusader.INSTANCE;
            case PALADIN:
                return SpellbookPaladin.INSTANCE;
            case ASSASSIN:
                return SpellbookAssassin.INSTANCE;
            case ALCHEMIST:
                return SpellbookAlchemist.INSTANCE;
            case REAPER:
                return SpellbookReaper.INSTANCE;
            case ARCHER:
                return SpellbookArcher.INSTANCE;
            case WIZARD:
                return SpellbookWizard.INSTANCE;
            case VILLAGER:
            default:
                return VillagerSpellbook.INSTANCE;
        }
    }

    public Spell[] getSpellList() {
        return getSpellbook().getSpellList();
    }

    public boolean usingSpell(Spell s) {
        return spell_RLL == s || spell_RLR == s || spell_RRL == s || spell_RRR == s;
    }

    public int getSpellLevel(Spell s) {
        if (spellLevels.containsKey(s))
            return spellLevels.get(s);
        return 0;
    }

    /*
     * Tasks
     */

    // Special Effects
    public void specialEffectsTask() {
        final PlayerDataRPG me = this;
        RScheduler.schedule(plugin, new Runnable() {
            public void run() {
                if (isValid()) {
                    ParticleManager.tick(getPlayer(), me);
                    RScheduler.schedule(plugin, this, RTicks.seconds(0.5));
                } else {
                    ParticleManager.dispose(uuid);
                }
            }
        });
    }

    // things that tick once a second
    public void everySecondTask() {
        final PlayerDataRPG me = this;
        RScheduler.schedule(plugin, new Runnable() {
            public void run() {
                if (isValid()) {
                    if (me.region != null && Math.random() < 0.0025) {
                        int amt = RMath.randInt(1, 3);
                        //                        RMessages.announce("spawning gifts for " + getPlayer().getName());
                        for (int k = 0; k < amt; k++)
                            MobManager.createMob("gift_" + RMath.randInt(1, 7), getPlayer().getLocation());
                    }
                    RScheduler.schedule(plugin, this, RTicks.seconds(1));
                }
            }
        });
    }

    public void equipEffectsTask() {
        RScheduler.schedule(plugin, new Runnable() {
            public void run() {
                if (isValid()) {
                    Player p = getPlayer();
                    EntityEquipment ee = p.getEquipment();
                    if (ee.getHelmet() != null && ItemManager.isItem(ee.getHelmet(), "miner_helmet")) {
                        if (!equipStates.contains("miner_helmet")) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false), true);
                            equipStates.add("miner_helmet");
                        }
                    } else {
                        if (equipStates.contains("miner_helmet")) {
                            equipStates.remove("miner_helmet");
                            p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                        }
                    }
                    RScheduler.schedule(plugin, this, RTicks.seconds(1));
                }
            }
        });
    }

    public void manaRegenTask() {
        RScheduler.schedule(plugin, new Runnable() {
            public void run() {
                mana++;
                if (mana > MAX_MANA)
                    mana = MAX_MANA;
                updateHealthManaDisplay();
                RScheduler.schedule(plugin, this, getManaRegenRateTicks());
            }
        }, getManaRegenRateTicks());
    }

    public int getManaRegenRateTicks() {
        int rate = 40; // 2 seconds by default
        double multiplier = 1;
        if (classType == ClassType.WIZARD) {
            switch (getSpellLevel(SpellbookWizard.MANA_TIDE)) {
                default:
                case 1:
                    multiplier += 0.15;
                    break;
                case 2:
                    multiplier += 0.30;
                    break;
                case 3:
                    multiplier += 0.45;
                    break;
                case 4:
                    multiplier += 0.60;
                    break;
                case 5:
                    multiplier += 0.75;
                    break;
            }
        } else if (classType == ClassType.ASSASSIN) {
            if (isStealthed() && getSpellLevel(SpellbookAssassin.DARK_HARMONY) > 0)
                multiplier += 0.5;
        }
        rate /= multiplier;
        if (rate < 1)
            rate = 1;
        return rate;
    }

    public void hpDisplayAndRegenTask() {
        // HP Display
        if (board == null) {
            board = plugin.getServer().getScoreboardManager().getNewScoreboard();
            Objective objective = board.registerNewObjective("hpdisplay", "dummy");
            objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
            objective.setDisplayName(ChatColor.DARK_RED + "\u2764");
        }
        final Halter h = halter();
        RScheduler.scheduleRepeating(plugin, new Runnable() {
            int counter = 0;

            public void run() {
                if (isValid()) {
                    if (dead)
                        return;
                    counter++;
                    if (counter % 2 == 0) {
                        int regenAmount = (int) (Math.ceil((baseMaxHP + maxHP) * getHPRegenRate()));
                        if (getSpellLevel(SpellbookReaper.RAPID_RECOVERY) > 0) {
                            int missing = baseMaxHP + maxHP - hp;
                            switch (getSpellLevel(SpellbookReaper.RAPID_RECOVERY)) {
                                case 1:
                                    regenAmount += (int) (Math.ceil(missing * 0.0020));
                                    break;
                                case 2:
                                    regenAmount += (int) (Math.ceil(missing * 0.0025));
                                    break;
                                case 3:
                                    regenAmount += (int) (Math.ceil(missing * 0.0030));
                                    break;
                                case 4:
                                    regenAmount += (int) (Math.ceil(missing * 0.0035));
                                    break;
                                case 5:
                                    regenAmount += (int) (Math.ceil(missing * 0.0040));
                                    break;
                            }
                        }
                        if (hasBuff(MysteryDrink.REGEN_BUFF_ID))
                            regenAmount += (int) (Math.ceil((baseMaxHP + maxHP) * getBuffValue(MysteryDrink.REGEN_BUFF_ID)));
                        if (hasBuff(SinisterStrike.DEBUFF_ID))
                            regenAmount *= 0.25;
                        if (regenAmount < 1)
                            regenAmount = 1;
                        if (region != null && region.dangerLevel == 1)
                            regenAmount *= 2;
                        regenAmount *= hpRegen;
                        hp += regenAmount;
                    }
                    if (hp > baseMaxHP + maxHP && finishedLoadEquips)
                        hp = baseMaxHP + maxHP;
                    updateHealthManaDisplay();
                } else {
                    h.halt = true;
                }
            }
        }, RTicks.seconds(0.5), h);

        Team team = board.getTeam(rank.rankDisplayName);
        if (team == null) {
            team = board.registerNewTeam(rank.rankDisplayName);
            team.setPrefix(rank.nameColor + "");
            team.setAllowFriendlyFire(true);
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }
        team.addEntry(name);
        getPlayer().setScoreboard(board);
    }

    public void saveTask() {
        RScheduler.schedule(plugin, new Runnable() {
            public void run() {
                if (isValid()) {
                    save();
                    RScheduler.schedule(plugin, this, RTicks.seconds((int) (Math.random() * 20 + 30)));
                }
            }
        });
    }

    /*
     * Leveling
     */

    public double getHPRegenRate() {
        return 0.005; //0.5% by default
    }

    public int getBaseMaxHP() {
        return 90 + level * 50;
    }

    public int getCurrentMaxHP() {
        return getBaseMaxHP() + maxHP;
    }

    public void gainExp(long amount) {
        gainExp(amount, false);
    }

    public void gainExp(long amount, boolean penalty) {
        exp += amount;
        Player p = getPlayer();
        if (p == null || !p.isValid())
            return;
        int bonus = 0;
        if (classType == ClassType.WIZARD) {
            int lv;
            if ((lv = getSpellLevel(SpellbookWizard.WISDOM)) > 0) {
                switch (lv) {
                    case 1:
                        bonus = (int) Math.ceil(amount * 0.05);
                        break;
                    case 2:
                        bonus = (int) Math.ceil(amount * 0.07);
                        break;
                    case 3:
                        bonus = (int) Math.ceil(amount * 0.09);
                        break;
                    case 4:
                        bonus = (int) Math.ceil(amount * 0.11);
                        break;
                    case 5:
                        bonus = (int) Math.ceil(amount * 0.13);
                        break;
                    case 6:
                        bonus = (int) Math.ceil(amount * 0.15);
                        break;
                }
                if (bonus < 1)
                    bonus = 1;
            }
        }
        if (party != null) {
            bonus += (int) Math.ceil(amount * party.getExpMultiplier());
        }
        long tExp = amount;
        if (bonus > 0) {
            tExp += bonus;
            exp += bonus;
            if (getOption(SakiOption.EXP_MESSAGES))
                p.sendMessage(ChatColor.GRAY + ">> +" + amount + " (+" + bonus + ") EXP [" + exp + "/" + getExpForNextLevel() + "]" + (penalty ? " (Level Gap Penalty)" : ""));
        } else {
            if (getOption(SakiOption.EXP_MESSAGES))
                p.sendMessage(ChatColor.GRAY + ">> +" + amount + " EXP [" + exp + "/" + getExpForNextLevel() + "]" + (penalty ? " (Level Gap Penalty)" : ""));
        }
        if (trinket != null) {
            int pastLevel = Trinket.getTrinketLevel(getTrinketExp(trinket));
            if (trinketExp.containsKey(trinket)) {
                tExp += trinketExp.get(trinket);
            }
            trinketExp.put(trinket, tExp);
            int newLevel = Trinket.getTrinketLevel(tExp);
            if (newLevel > pastLevel)
                p.sendMessage(ChatColor.GRAY + ">> " + ChatColor.GOLD + "Your Trinket leveled up! It is now level " + ChatColor.YELLOW + newLevel + ChatColor.GOLD + ".");
            updateEquipmentStats();
        }
        if (exp >= getExpForNextLevel()) {
            long extra = exp - getExpForNextLevel();
            exp = 0;
            level++;
            this.sp++;
            exp = extra < getExpForNextLevel() ? extra : getExpForNextLevel() - 1;
            baseMaxHP = getBaseMaxHP();
            if (level >= 50) {
                String msg = ChatColor.GRAY + "> " + ChatColor.YELLOW + name + ChatColor.GRAY + " just leveled up to level " + ChatColor.YELLOW + level + "!";
                for (Player p2 : plugin.getServer().getOnlinePlayers()) {
                    PlayerDataRPG pd2 = plugin.getPD(p2);
                    if (pd2 != null && pd2.getOption(SakiOption.LEVEL_ANNOUNCEMENTS)) {
                        pd2.sendMessage(msg);
                    }
                }
            }
            p.sendMessage(ChatColor.GOLD + "**************************************");
            p.sendMessage(ChatColor.GOLD + " You leveled up! You are now level " + ChatColor.YELLOW + level + ChatColor.GOLD + ".");
            p.sendMessage(ChatColor.GOLD + " Your base Max HP increased to " + baseMaxHP + ".");
            p.sendMessage(ChatColor.GOLD + "**************************************");
            RParticles.spawnRandomFirework(p.getLocation());
            updateHealthManaDisplay();
        }
    }

    private int lastLevel = -1;
    private long cachedExp = -1;

    public long getExpForNextLevel() {
        long ret = 1;
        if (lastLevel == level && cachedExp > 0) {
            return cachedExp;
        }
        ret = PlayerStatics.getEXPForNextLevel(level);
        if (ret < 0) {
            RMessages.announce("Error code 105: EXP. Please report this to Misaka or Edasaki.");
            ret = Long.MAX_VALUE;
        }
        lastLevel = level;
        cachedExp = ret;
        return ret;
    }

    /*
     * Health and Mana
     */

    public void recoverMana(int amount) {
        mana += amount;
        if (mana > MAX_MANA)
            mana = MAX_MANA;
        sendMessage(ChatColor.GRAY + ">> " + ChatColor.DARK_AQUA + ChatColor.BOLD + "+" + amount + " Mana" + ChatColor.AQUA + " [" + this.mana + " Mana]");
    }

    public void heal(int healAmount) {
        heal(healAmount, HealType.NORMAL);
    }

    public void heal(int healAmount, HealType type) {
        if (hasBuff(SinisterStrike.DEBUFF_ID)) {
            sendMessage(ChatColor.RED + "You are crippled and have 75% reduced healing.");
            healAmount *= 0.25;
        }
        int bonus = 0;
        if (type == HealType.POTION && classType == ClassType.ALCHEMIST) {
            int lv;
            if ((lv = getSpellLevel(SpellbookAlchemist.POTION_MASTERY)) > 0) {
                switch (lv) {
                    case 1:
                        bonus = (int) Math.ceil(healAmount * 0.1);
                        break;
                    case 2:
                        bonus = (int) Math.ceil(healAmount * 0.2);
                        break;
                    case 3:
                        bonus = (int) Math.ceil(healAmount * 0.3);
                        break;
                    case 4:
                        bonus = (int) Math.ceil(healAmount * 0.4);
                        break;
                    case 5:
                        bonus = (int) Math.ceil(healAmount * 0.5);
                        break;
                }
                if (bonus < 1)
                    bonus = 1;
            }
        }
        if (classType == ClassType.REAPER) {
            int lv;
            if ((lv = getSpellLevel(SpellbookReaper.HEAL_ENHANCE)) > 0) {
                switch (lv) {
                    case 1:
                        bonus = (int) Math.ceil(healAmount * 0.10);
                        break;
                    case 2:
                        bonus = (int) Math.ceil(healAmount * 0.12);
                        break;
                    case 3:
                        bonus = (int) Math.ceil(healAmount * 0.14);
                        break;
                    case 4:
                        bonus = (int) Math.ceil(healAmount * 0.16);
                        break;
                    case 5:
                        bonus = (int) Math.ceil(healAmount * 0.18);
                        break;
                    case 6:
                        bonus = (int) Math.ceil(healAmount * 0.20);
                        break;
                }
                if (bonus < 1)
                    bonus = 1;
            }
        }
        this.hp += healAmount;
        this.hp += bonus;
        if (this.hp > this.getCurrentMaxHP())
            this.hp = this.getCurrentMaxHP();
        if (getOption(SakiOption.HEAL_MESSAGES)) {
            if (bonus > 0) {
                sendMessage(ChatColor.GRAY + ">> " + ChatColor.GREEN + ChatColor.BOLD + "+" + healAmount + " (+" + bonus + ") HP" + ChatColor.YELLOW + " [" + this.hp + " HP]");
            } else {
                sendMessage(ChatColor.GRAY + ">> " + ChatColor.GREEN + ChatColor.BOLD + "+" + healAmount + " HP" + ChatColor.YELLOW + " [" + this.hp + " HP]");
            }
        }
        updateHealthManaDisplay();
        Vector v = getPlayer().getLocation().getDirection().normalize().setY(0);
        RParticles.show(ParticleEffect.HEART, getPlayer().getLocation().add(v).add(0, 0.5 * getPlayer().getEyeHeight(), 0), 1);
    }

    public void damageSelfTrue(int damageAmount) {
        if (classType == ClassType.PALADIN) {
            if (hasBuff(HolyGuardian.BUFF_ID))
                damageAmount *= getBuffValue(HolyGuardian.BUFF_ID);
            if (hasBuff(ChantOfNecessarius.BUFF_ID))
                damageAmount = 0;
        }
        if (damageAmount < 1)
            damageAmount = 1;
        hp -= damageAmount;
        if (hp < 0)
            hp = 0;
        Player p = getPlayer();
        if (p != null) {
            OptionsManager.msgDamage(p, this, ChatColor.GRAY + ">> " + ChatColor.RED + ChatColor.BOLD + "-" + damageAmount + " HP" + ChatColor.YELLOW + " [" + hp + " HP]" + ChatColor.WHITE + " from yourself");
            p.damage(0.0);
            if (hp < 1) {
                if (p != null)
                    die();
            } else {
                updateHealthManaDisplay();
                if (board != null)
                    board.getObjective("hpdisplay").getScore(name).setScore(hp);
            }
        }
    }

    public boolean inCombat() {
        return System.currentTimeMillis() - lastDamagedNonEnvironmental < 10000;
    }

    public long getAttackSpeed() {
        long val = 1000;
        double mult = attackSpeed;
        int lv;
        if (classType == ClassType.ARCHER && (lv = getSpellLevel(SpellbookArcher.RAPID_FIRE)) > 0) {
            switch (lv) {
                case 1:
                    mult += 0.1;
                    break;
                case 2:
                    mult += 0.2;
                    break;
                case 3:
                    mult += 0.3;
                    break;
            }
        }
        if (classType == ClassType.ALCHEMIST && (lv = getSpellLevel(SpellbookAlchemist.SPEEDY_BREWER)) > 0) {
            switch (lv) {
                case 1:
                    mult += 0.1;
                    break;
                case 2:
                    mult += 0.2;
                    break;
                case 3:
                    mult += 0.3;
                    break;
            }
        }
        mult = 1 - mult;
        if (mult < 0.25)
            mult = 0.25;
        val *= mult;
        return val;
    }

    public boolean damage(int damageAmount, Entity damager, DamageType damageType) {
        return damage(damageAmount, damager, damageType, false);
    }

    public boolean damage(int damageAmount, Entity damager, DamageType damageType, boolean crit) {
        if (!loadedSQL)
            return false;
        if (dead)
            return false;
        // environmental attack speed limiter
        if (System.currentTimeMillis() - lastEnvironmentalDamaged < 600 && (damageType == DamageType.ENVIRONMENTAL_LAVA || damageType == DamageType.ENVIRONMENTAL_DROWNING || damageType == DamageType.ENVIRONMENTAL_FALL))
            return false;
        Player p = getPlayer();
        // check if player can be attacked
        if (p == null || !p.isValid())
            return false;
        if (!this.isPVE())
            return false;
        if (damager != null) {
            if (damager instanceof Player) { // attacked by player
                PlayerDataRPG damagerPD = plugin.getPD((Player) damager);
                if (!((Player) damager).isOnline() || damagerPD == null)
                    return false;
                if (!damagerPD.isPVP() || !this.isPVP() || PartyManager.sameParty((Player) damager, getPlayer()))
                    return false;
                if (damagerPD.riding)
                    return false;
                if (System.currentTimeMillis() - lastDamaged < damagerPD.getAttackSpeed() && damageType == DamageType.NORMAL)
                    return false;
            } else if (System.currentTimeMillis() - lastDamaged < 600 && damageType == DamageType.NORMAL) { // normal attacked by mob
                return false;
            }
        }
        switch (lastDamageType = damageType) {
            case NORMAL:
            case NORMAL_SPELL:
                damageAmount -= RMath.randInt(0, defense);
                lastDamagedNonEnvironmental = System.currentTimeMillis();
                break;
            case ENVIRONMENTAL_FALL:
                if (damageAmount >= hp)
                    damageAmount = hp - 1;
                break;
            case ENVIRONMENTAL_LAVA:
            case ENVIRONMENTAL_DROWNING:
            case ENVIRONMENTAL_INSTANT:
                //          case TRUE:
                break;
        }
        if (damageType == DamageType.ENVIRONMENTAL_LAVA || damageType == DamageType.ENVIRONMENTAL_DROWNING || damageType == DamageType.ENVIRONMENTAL_FALL)
            lastEnvironmentalDamaged = System.currentTimeMillis();
        else if (damageType == DamageType.NORMAL)
            lastDamaged = System.currentTimeMillis();
        if (classType == ClassType.PALADIN) {
            if (hasBuff(HolyGuardian.BUFF_ID))
                damageAmount *= getBuffValue(HolyGuardian.BUFF_ID);
            if (hasBuff(ChantOfNecessarius.BUFF_ID))
                damageAmount = 0;
        }
        if (hasBuff(Trinket.GUARDIAN_BUFF_ID)) {
            damageAmount *= getBuffValue(Trinket.GUARDIAN_BUFF_ID);
        }
        if (damager != null && damager != p && damager instanceof Player) {
            damageAmount *= 0.8; // PvP Damage Nerf
        }
        if (damageAmount < 1)
            damageAmount = 1;
        if (damageType == DamageType.NORMAL || damageType == DamageType.NORMAL_SPELL) {
            lastDamagedGlide = System.currentTimeMillis();
            if (damager != null && !(damager instanceof Player)) {
                safespotCounter = 0;
                MobAI.ignore.remove(getUUID());
                //                RMessages.announce("Resetting " + getName() + " counter after damaged.");
            }
            if (damageAmount > hp * 0.05) {
                p.leaveVehicle();
            }
        }
        if (p.isGliding())
            p.setGliding(false);
        hp -= damageAmount;
        if (hp < 0)
            hp = 0;
        if (damager != null && damager != p) {
            String nameToDisplayToPlayer = "an unknown source";
            if (damager instanceof Player) {
                PlayerDataRPG pd2 = plugin.getPD((Player) damager);
                if (pd2 != null) {
                    Player p2 = pd2.getPlayer();
                    if (p2 != null && p2.isValid()) {
                        p2.playSound(p2.getLocation(), Sound.ENTITY_PLAYER_HURT, 0.65f, 0.75f);
                        OptionsManager.msgDamage(p2, pd2, ChatColor.GRAY + ">> " + ChatColor.AQUA + ChatColor.BOLD + "-" + damageAmount + " HP" + ChatColor.WHITE + " to " + ChatColor.RED + ChatColor.BOLD + p.getName() + (crit ? ChatColor.GRAY.toString() + ChatColor.ITALIC + " *Critical Hit*" : ""));
                        lastDamagerPlayerData = pd2;
                        lastDamagerPlayerTime = System.currentTimeMillis();
                        lastDamagerPlayer = (nameToDisplayToPlayer = ChatColor.GRAY + "[" + pd2.level + "] " + ChatColor.YELLOW + damager.getName());
                        if (pd2.lifesteal > 0) {
                            pd2.heal((int) Math.ceil(damageAmount * pd2.lifesteal));
                        }
                        if (pd2.classType == ClassType.PALADIN) {
                            if (damageType == DamageType.NORMAL && pd2.hasBuff(LightningCharge.BUFF_ID) && Math.random() < 0.3) {
                                RParticles.sendLightning(p, p.getLocation());
                                Spell.damageNearby((int) (pd2.getDamage(true) * pd2.getBuffValue(LightningCharge.BUFF_ID)), p2, p.getLocation(), 3.0, new ArrayList<Entity>());
                                Spell.notify(pd2.getPlayer(), "Lightning strikes your enemy.");
                            }
                            if (damageType == DamageType.NORMAL && pd2.hasBuff(FlameCharge.BUFF_ID) && Math.random() < 0.3) {
                                RParticles.showWithOffset(ParticleEffect.FLAME, p.getEyeLocation(), 1.5, 15);
                                giveBurn(5, (int) pd2.getBuffValue(FlameCharge.BUFF_ID));
                                Spell.notify(pd2.getPlayer(), "You burn your enemy.");
                            }
                        }
                    }
                }
            } else if (MobManager.spawnedMobs.containsKey(damager.getUniqueId())) {
                MobData md = MobManager.spawnedMobs.get(damager.getUniqueId());
                if (md.ai != null)
                    md.ai.lastAttackTickCounter = 0;
                lastDamagerMob = (nameToDisplayToPlayer = md.fullName);
                lastDamagerMobTime = System.currentTimeMillis();
            }
            OptionsManager.msgDamage(p, this, ChatColor.GRAY + ">> " + ChatColor.RED + ChatColor.BOLD + "-" + damageAmount + " HP"

                    + ChatColor.YELLOW + " [" + hp + " HP]" + ChatColor.WHITE + " from " + ChatColor.GOLD + ChatColor.BOLD + nameToDisplayToPlayer

                    + (crit ? ChatColor.GRAY.toString() + ChatColor.ITALIC + " *Critical Hit*" : ""));
        }
        RParticles.showWithData(ParticleEffect.BLOCK_CRACK, p.getLocation().add(0, 1.5, 0), new BlockData(Material.REDSTONE_BLOCK, (byte) 0), 10);
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT, 0.85f, 0.85f);
        if (System.currentTimeMillis() - this.lastHurt > 1000) {
            p.playEffect(EntityEffect.HURT);
            this.lastHurt = System.currentTimeMillis();
        }
        if (hp < 1) {
            if (p != null)
                die();
        } else {
            updateHealthManaDisplay();
            if (board != null)
                board.getObjective("hpdisplay").getScore(name).setScore(hp);
        }
        if (isStealthed())
            removeStealth();
        return true;
    }

    public void die() {
        if (dead)
            return;
        if (!loadedSQL) {
            hp = 1;
            return;
        }
        Player p = getPlayer();
        if (p == null)
            return;
        deaths++;
        if (isStealthed())
            removeStealth();
        clearBuffs();
        switch (region.dangerLevel) {
            case 1:
            default:
                break;
            case 2:
                this.exp *= 0.85;
                break;
            case 3:
                this.exp *= 0.80;
                break;
            case 4:
                this.exp *= 0.75;
                ArrayList<Integer> rand = new ArrayList<Integer>();
                for (int k = 0; k < 36; k++) {
                    ItemStack item = p.getInventory().getItem(k);
                    if (!plugin.getInstance(ItemManager.class).isSoulbound(item)) {
                        if (item != null && item.getType() != Material.AIR)
                            rand.add(k);
                    }
                }
                int half = (int) Math.ceil(rand.size() / 2.0);
                while (rand.size() > half) {
                    rand.remove((int) (Math.random() * rand.size()));
                }
                for (int k : rand) {
                    ItemStack item = p.getInventory().getItem(k);
                    if (!plugin.getInstance(ItemManager.class).isSoulbound(item)) {
                        p.getInventory().setItem(k, null);
                        p.getWorld().dropItemNaturally(p.getLocation(), item);
                    }

                }
                ItemStack item = p.getInventory().getHelmet();
                if (!plugin.getInstance(ItemManager.class).isSoulbound(item)) {
                    if (item != null && Math.random() < 0.5) {
                        p.getInventory().setHelmet(null);
                        p.getWorld().dropItemNaturally(p.getLocation(), item);
                    }
                }
                item = p.getInventory().getChestplate();
                if (!plugin.getInstance(ItemManager.class).isSoulbound(item)) {
                    if (item != null && Math.random() < 0.5) {
                        p.getInventory().setChestplate(null);
                        p.getWorld().dropItemNaturally(p.getLocation(), item);
                    }
                }
                item = p.getInventory().getLeggings();
                if (!plugin.getInstance(ItemManager.class).isSoulbound(item)) {
                    if (item != null && Math.random() < 0.5) {
                        p.getInventory().setLeggings(null);
                        p.getWorld().dropItemNaturally(p.getLocation(), item);
                    }
                }
                item = p.getInventory().getBoots();
                if (!plugin.getInstance(ItemManager.class).isSoulbound(item)) {
                    if (item != null && Math.random() < 0.5) {
                        p.getInventory().setBoots(null);
                        p.getWorld().dropItemNaturally(p.getLocation(), item);
                    }
                }
                break;
        }
        save();
        RMessages.sendTitle(p, ChatColor.RED + "" + ChatColor.BOLD + "You just died!", ChatColor.RED + DEATH_MESSAGES[(int) (Math.random() * DEATH_MESSAGES.length)], 10, 60, 30);
        p.closeInventory();
        long curr = System.currentTimeMillis();
        // if not damaged by player within 15 sec or mob within 30 sec
        if (curr - lastDamagerPlayerTime > 15000 && curr - lastDamagerMobTime > 30000) {
            switch (lastDamageType) {
                case ENVIRONMENTAL_LAVA:
                    RMessages.death(ChatColor.GRAY + "[" + level + "] " + ChatColor.RED + p.getName() + " burned to death!");
                    break;
                case ENVIRONMENTAL_DROWNING:
                    RMessages.death(ChatColor.GRAY + "[" + level + "] " + ChatColor.RED + p.getName() + " drowned!");
                    break;
                case ENVIRONMENTAL_FALL:
                    RMessages.death(ChatColor.GRAY + "[" + level + "] " + ChatColor.RED + p.getName() + " fell from a high place!");
                    break;
                default:
                    RMessages.death(ChatColor.GRAY + "[" + level + "] " + ChatColor.RED + p.getName() + " died!");
                    break;
            }
        } else if (curr - lastDamagerPlayerTime <= 15000) {
            if (lastDamagerPlayerData != null && lastDamagerPlayerData.isValid()) {
                lastDamagerPlayerData.playerKills++;
            }
            RMessages.death(ChatColor.GRAY + "[" + level + "] " + ChatColor.YELLOW + p.getName() + ChatColor.RED + " was killed by " + ChatColor.YELLOW + lastDamagerPlayer + ChatColor.RED + "!");
        } else {
            RMessages.death(ChatColor.GRAY + "[" + level + "] " + ChatColor.YELLOW + p.getName() + ChatColor.RED + " was killed by " + ChatColor.YELLOW + lastDamagerMob + ChatColor.RED + "!");
        }
        dead = true;
        p.teleport(getRespawnLocation(p.getLocation()));
        for (PotionEffect pe : p.getActivePotionEffects()) {
            p.removePotionEffect(pe.getType());
        }
        RScheduler.schedule(plugin, new Runnable() {
            public void run() {
                dead = false;
                hp = baseMaxHP + maxHP;
                mana = PlayerDataRPG.MAX_MANA;
                lastDamaged = 0;
                lastDamagerPlayerTime = 0;
                lastDamagerMobTime = 0;
                lastDamageType = DamageType.NORMAL;
                updateHealthManaDisplay();
                setInvulnerable(5);
            }
        }, 5);
    }

    public void updateHealthManaDisplay() {
        if (!loadedSQL)
            return;
        Player p = getPlayer();
        if (p != null) {
            if (hp > baseMaxHP + maxHP && finishedLoadEquips)
                hp = baseMaxHP + maxHP;
            if (hp < 0)
                hp = 0;
            if (mana > MAX_MANA)
                mana = MAX_MANA;
            if (dead)
                return;
            double percent = ((double) hp) / (baseMaxHP + maxHP);
            StringBuilder sb = new StringBuilder();
            BarColor color = BarColor.GREEN;
            if (percent > 0.50) {
                sb.append(ChatColor.GREEN);
                //				sb.append(ChatColor.BOLD);
                sb.append(hp);
                sb.append(" ");
                sb.append(ChatColor.AQUA);
                sb.append(ChatColor.BOLD);
                sb.append("HP");
            } else if (percent > 0.20) {
                color = BarColor.YELLOW;
                sb.append(ChatColor.YELLOW);
                //				sb.append(ChatColor.BOLD);
                sb.append(hp);
                sb.append(" ");
                sb.append(ChatColor.AQUA);
                sb.append(ChatColor.BOLD);
                sb.append("HP");
            } else {
                color = BarColor.RED;
                sb.append(ChatColor.DARK_RED);
                sb.append(ChatColor.BOLD);
                sb.append("DANGER - ");
                sb.append(ChatColor.RED);
                sb.append(hp);
                sb.append(" ");
                sb.append(ChatColor.AQUA);
                sb.append(ChatColor.BOLD);
                sb.append("HP");
                sb.append(ChatColor.DARK_RED);
                sb.append(ChatColor.BOLD);
                sb.append(" - DANGER");
            }
            sb.append(ChatColor.RESET);
            if (poisonTicks > 0) {
                color = BarColor.WHITE;
                sb.append(ChatColor.YELLOW);
                sb.append(" | ");
                sb.append(ChatColor.DARK_PURPLE);
                sb.append(ChatColor.BOLD);
                sb.append("Poison ");
                sb.append(RFormatter.tierToRoman(poisonTier));
                sb.append(" (" + poisonTicks + "s)");
            }
            if (burnTicks > 0) {
                color = BarColor.WHITE;
                sb.append(ChatColor.YELLOW);
                sb.append(" | ");
                sb.append(ChatColor.RED);
                sb.append(ChatColor.BOLD);
                sb.append("Burn ");
                sb.append(RFormatter.tierToRoman(burnTier));
                sb.append(" (" + burnTicks + "s)");
            }
            if (getOption(SakiOption.PERM_COORDS)) {
                sb.append(ChatColor.YELLOW);
                sb.append(" | ");
                sb.append(ChatColor.WHITE);
                Location loc = p.getLocation();
                sb.append('[');
                sb.append(LocationCommand.roundToHalf(loc.getX()));
                sb.append(", ");
                sb.append(LocationCommand.roundToHalf(loc.getY()));
                sb.append(", ");
                sb.append(LocationCommand.roundToHalf(loc.getZ()));
                sb.append(']');
            }
            if (getOption(SakiOption.TRINKET_TIMER)) {
                double diff = ((double) nextTrinketCast - System.currentTimeMillis()) / 1000.0;
                sb.append(ChatColor.YELLOW);
                sb.append(" | ");
                sb.append(ChatColor.WHITE);
                if (diff > 0) {
                    sb.append(ChatColor.RED);
                    sb.append("Trinket in ");
                    sb.append(String.format("%.1fs", diff));
                } else {
                    sb.append(ChatColor.GREEN);
                    sb.append("Trinket Ready");
                }
            }
            //            RMessages.sendActionBar(p, sb.toString());
            if (percent > 1.0)
                percent = 1.0;
            if (percent < 0.01)
                percent = 0.01;
            if (bossBar == null) {
                bossBar = Bukkit.createBossBar(sb.toString(), BarColor.GREEN, BarStyle.SOLID);
                bossBar.addPlayer(p);
            }
            bossBar.setTitle(sb.toString().trim());
            bossBar.setProgress(percent);
            bossBar.setColor(color);

            if (board != null) {
                Score score = board.getObjective("hpdisplay").getScore(name);
                if (score != null)
                    score.setScore(hp);
            }
            PartyManager.updatePlayerForAll(name, hp, rank, party);

            // Don't conflict with soaring stamina display
            if (!p.isGliding() && SpellManager.casters.containsKey(p.getUniqueId())) {
                CastState cs = SpellManager.casters.get(p.getUniqueId()).state;
                if (cs != CastState.NONE) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(ChatColor.GOLD);
                    sb2.append(ChatColor.BOLD);
                    sb2.append(" ");
                    sb2.append(cs.toString());
                    RMessages.sendActionBar(p, sb2.toString());
                }
            }

            p.setFoodLevel(mana * 2);
            p.setLevel(level);
            p.setExp(((float) exp) / getExpForNextLevel());
            p.setMaxHealth(20.0);
            p.setHealthScaled(false);
            int i = (int) (hp / (double) (baseMaxHP + maxHP) * p.getMaxHealth());
            if (i < 1)
                i = 1;
            if (i > p.getMaxHealth())
                i = (int) (p.getMaxHealth());
            if (i != (int) p.getHealth()) {
                try {
                    p.setHealth(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setInvulnerable(int seconds) {
        lastDamaged = System.currentTimeMillis() + seconds * 1000;
    }

    private int stealthCounter = 0;

    public void giveStealth(int seconds) {
        stealthed = true;
        Player p = getPlayer();
        if (p != null && p.isValid())
            StealthManager.giveStealth(p, seconds);
        final int counter = stealthCounter;
        RScheduler.schedule(plugin, new Runnable() {
            public void run() {
                // if stealth counter hasn't changed, then stealth has not already been removed
                // if stealth has been removed, it wont be removed again (this way, past stealth timers won't affect new stealths)
                if (stealthCounter == counter)
                    removeStealth();
            }
        }, RTicks.seconds(seconds));
        RScheduler.schedule(plugin, new Runnable() {
            public void run() {
                if (stealthCounter == counter && isStealthed()) {
                    if (p != null && p.isValid()) {
                        RParticles.showWithOffset(ParticleEffect.SMOKE_NORMAL, p.getLocation().add(0, 1, 0), 1, 8);
                    }
                    RScheduler.schedule(plugin, this, RTicks.seconds(0.5));
                }
            }
        }, RTicks.seconds(0.5));
    }

    public void removeStealth() {
        if (!isStealthed())
            return;
        stealthCounter++;
        stealthed = false;
        Player p = getPlayer();
        if (p != null && p.isValid()) {
            StealthManager.removeStealth(p);
            for (PotionEffect pe : p.getActivePotionEffects())
                p.removePotionEffect(pe.getType());
        }
    }

    public boolean isStealthed() {
        return stealthed;
    }

    /*
     * Combat stuff
     */

    public void statusEffectTask() {
        final Halter h = halter();
        final PlayerDataRPG me = this;
        RScheduler.scheduleRepeating(plugin, new Runnable() {
            public void run() {
                if (isValid()) {
                    Player p = getPlayer();
                    if (me.soaringStamina < me.maxSoaringStamina && !p.isGliding())
                        me.soaringStamina++;
                    RegionManager.updateTime(p, me);
                    if (poisonTicks > 0) {
                        poisonTicks--;
                        Location loc = p.getLocation().add(0, p.getEyeHeight() * 0.6, 0);
                        BlockData data = new BlockData(Material.STAINED_CLAY, (byte) DyeColor.BLUE.getWoolData());
                        RParticles.showWithData(ParticleEffect.BLOCK_CRACK, loc, data, 10);
                        double multiplier = 0.001;
                        switch (poisonTier) {
                            default:
                            case 1:
                                multiplier = 0.01;
                                break;
                            case 2:
                                multiplier = 0.015;
                                break;
                            case 3:
                                multiplier = 0.020;
                                break;
                            case 4:
                                multiplier = 0.030;
                                break;
                            case 5:
                                multiplier = 0.050;
                                break;
                        }
                        int amount = (int) (multiplier * hp);
                        if (amount < 1)
                            amount = 1;
                        damage(amount, null, DamageType.ENVIRONMENTAL_INSTANT);
                        updateHealthManaDisplay();
                        OptionsManager.msgDamage(p, me, ChatColor.GRAY + ">> " + ChatColor.RED + ChatColor.BOLD + "-" + amount + " HP" + ChatColor.YELLOW + " [" + hp + " HP]" + ChatColor.WHITE + " from " + ChatColor.LIGHT_PURPLE + "Poison " + RFormatter.tierToRoman(poisonTier));
                    }
                    if (burnTicks > 0) {
                        burnTicks--;
                        Location loc = p.getLocation().add(0, p.getEyeHeight() * 0.6, 0);
                        BlockData data = new BlockData(Material.LAVA, (byte) 0);
                        RParticles.showWithData(ParticleEffect.BLOCK_CRACK, loc, data, 10);
                        double multiplier = 0.001;
                        switch (burnTier) {
                            default:
                            case 1:
                                multiplier = 0.01;
                                break;
                            case 2:
                                multiplier = 0.015;
                                break;
                            case 3:
                                multiplier = 0.020;
                                break;
                            case 4:
                                multiplier = 0.030;
                                break;
                            case 5:
                                multiplier = 0.050;
                                break;
                        }
                        int amount = (int) (multiplier * hp);
                        if (amount < 1)
                            amount = 1;
                        damage(amount, null, DamageType.ENVIRONMENTAL_INSTANT);
                        updateHealthManaDisplay();
                        OptionsManager.msgDamage(p, me, ChatColor.GRAY + ">> " + ChatColor.RED + ChatColor.BOLD + "-" + amount + " HP" + ChatColor.YELLOW + " [" + hp + " HP]" + ChatColor.WHITE + " from " + ChatColor.GOLD + "Burn " + RFormatter.tierToRoman(burnTier));
                    }
                } else {
                    h.halt = true;
                }
            }
        }, RTicks.seconds(1), h);
    }

    public void giveSlow(int durationSeconds, int tier) {
        int highestTier = tier;
        int remaining = 0;
        Player p = getPlayer();
        if (p == null)
            return;
        for (PotionEffect pe : p.getActivePotionEffects()) {
            if (pe.getType().equals(PotionEffectType.SLOW)) {
                remaining = pe.getDuration();
                int temp = pe.getAmplifier();
                if (temp > highestTier)
                    highestTier = temp;
            }
        }

        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, RTicks.seconds(durationSeconds) + (remaining / 2), highestTier), true);
    }

    public void givePoison(int durationSeconds, int tier) {
        if (poisonTicks > 0 && tier <= poisonTier) {
            int value = tier * durationSeconds;
            value /= poisonTier;
            poisonTicks += value;
        } else {
            poisonTicks = durationSeconds;
            poisonTier = tier;
        }
    }

    public void removePoison() {
        poisonTicks = 0;
    }

    public void giveBurn(int durationSeconds, int tier) {
        if (burnTicks > 0 && tier <= burnTicks) {
            int value = tier * durationSeconds;
            value /= burnTier;
            burnTicks += value;
        } else {
            burnTicks = durationSeconds;
            burnTier = tier;
        }
    }

    public void removeBurn() {
        burnTicks = 0;
    }

    public boolean isPVP() {
        if (!isPVE() || (region != null && region.dangerLevel <= 2))
            return false;
        return true;
    }

    public boolean isPVE() {
        Player p = getPlayer();
        if ((p.getGameMode() != GameMode.SURVIVAL && p.getGameMode() != GameMode.ADVENTURE) || dead || (region != null && region.dangerLevel <= 1))
            return false;
        return true;
    }

    public void knockback(Entity attacker, double knockbackMultiplier) {
        return;
        //        final Player p = getPlayer();
        //        if (p == null)
        //            return;
        //        if (attacker instanceof Player && !this.isPVP())
        //            return;
        //        else if (!this.isPVE())
        //            return;
        //        if (hasBuff(PowerStance.BUFF_ID) || hasBuff(ChantOfNecessarius.BUFF_ID))
        //            return;
        //        if (System.currentTimeMillis() - lastKnockback > 600) {
        //            lastKnockback = System.currentTimeMillis();
        //            Vector newVelocity = p.getLocation().toVector().subtract(attacker.getLocation().toVector()).normalize().multiply(knockbackMultiplier);
        //            // cap Y knockback
        //            if (Math.abs(newVelocity.getY()) > 0.01)
        //                newVelocity.setY(0.01 * Math.signum(newVelocity.getY()));
        //            // cap X knockback
        //            if (Math.abs(newVelocity.getX()) > 1)
        //                newVelocity.setX(1 * Math.signum(newVelocity.getX()));
        //            // cap Z knockback
        //            if (Math.abs(newVelocity.getZ()) > 1)
        //                newVelocity.setZ(1 * Math.signum(newVelocity.getZ()));
        //            if (p != null && p.isValid())
        //                p.setVelocity(newVelocity);
        //        }
    }

    public Projectile shootArrow() {
        //attempted fix for arrows crashing
        Player p = getPlayer();
        return p.launchProjectile(Arrow.class, p.getLocation().getDirection().normalize().multiply(1.5));
    }

    public void playCrit() {
        Player p = getPlayer();
        if (p == null || !p.isValid())
            return;
        RParticles.showWithOffset(ParticleEffect.CRIT, p.getEyeLocation(), 1, 10);
    }

    public int getDamage(boolean isSpell) {
        if (riding || dead || !isValid() || getPlayer().isGliding())
            return 0;
        int val = RMath.randInt(damageLow, damageHigh);
        if (hasBuff(SwordSpirit.BUFF_ID)) {
            val *= getBuffValue(SwordSpirit.BUFF_ID);
        }
        if (hasBuff(DarkBargain.BUFF_ID)) {
            val *= getBuffValue(DarkBargain.BUFF_ID);
        }
        if (hasBuff(MysteryDrink.DAMAGE_BUFF_ID)) {
            val *= getBuffValue(MysteryDrink.DAMAGE_BUFF_ID);
        }
        if (hasBuff(MysteryDrink.DAMAGE_DEBUFF_ID)) {
            val *= getBuffValue(MysteryDrink.DAMAGE_DEBUFF_ID);
        }
        if (hasBuff(WalkingSanctuary.BUFF_ID)) {
            val *= getBuffValue(WalkingSanctuary.BUFF_ID);
        }
        if (getSpellLevel(SpellbookCrusader.SWORD_MASTERY) > 0) {
            switch (getSpellLevel(SpellbookCrusader.SWORD_MASTERY)) {
                default:
                case 1:
                    val *= 1.02;
                    break;
                case 2:
                    val *= 1.04;
                    break;
                case 3:
                    val *= 1.06;
                    break;
                case 4:
                    val *= 1.08;
                    break;
                case 5:
                    val *= 1.10;
                    break;
            }
        }
        if (getSpellLevel(SpellbookArcher.BOW_MASTERY) > 0) {
            switch (getSpellLevel(SpellbookArcher.BOW_MASTERY)) {
                default:
                case 1:
                    val *= 1.02;
                    break;
                case 2:
                    val *= 1.04;
                    break;
                case 3:
                    val *= 1.06;
                    break;
                case 4:
                    val *= 1.08;
                    break;
                case 5:
                    val *= 1.10;
                    break;
            }
        }
        if (getSpellLevel(SpellbookAssassin.DAGGER_MASTERY) > 0) {
            switch (getSpellLevel(SpellbookAssassin.DAGGER_MASTERY)) {
                default:
                case 1:
                    val *= 1.02;
                    break;
                case 2:
                    val *= 1.04;
                    break;
                case 3:
                    val *= 1.06;
                    break;
                case 4:
                    val *= 1.08;
                    break;
                case 5:
                    val *= 1.10;
                    break;
            }
        }
        if (getSpellLevel(SpellbookPaladin.MACE_MASTERY) > 0) {
            switch (getSpellLevel(SpellbookPaladin.MACE_MASTERY)) {
                default:
                case 1:
                    val *= 1.02;
                    break;
                case 2:
                    val *= 1.04;
                    break;
                case 3:
                    val *= 1.06;
                    break;
                case 4:
                    val *= 1.08;
                    break;
                case 5:
                    val *= 1.10;
                    break;
            }
        }
        if (isSpell) {
            val *= spellDamage;
        } else {
            val *= attackDamage;
        }
        if (hasSetBonusDamage)
            val *= 1.15; // 5-set bonus damage
        return val;
    }

    public boolean attackPlayer(PlayerDataRPG other) {
        return attackPlayer(other, 0.5, -1, false);
    }

    public boolean attackPlayer(PlayerDataRPG other, int rpgDamage) {
        return attackPlayer(other, 0.5, rpgDamage, false);
    }

    public boolean attackPlayer(PlayerDataRPG other, int rpgDamage, boolean projectile) {
        return attackPlayer(other, 0.5, rpgDamage, projectile);
    }

    public boolean attackPlayer(PlayerDataRPG other, double knockback, int rpgDamage, boolean projectile) {
        Player p = getPlayer();
        if (riding)
            return false;
        if (p == null)
            return false;
        if (other == this)
            return false;
        int damage;
        if (rpgDamage > 0)
            damage = rpgDamage;
        else
            damage = getDamage(false);
        boolean crit = false;
        double critChanceTemp = critChance;
        if (hasBuff(Trinket.HAWKEYE_BUFF_ID)) {
            critChanceTemp += 0.20;
        }
        if (Math.random() < critChanceTemp) {
            crit = true;
            damage *= critDamage;
            other.playCrit();
        }
        boolean success = false;
        if (classType == ClassType.ASSASSIN) {
            if (hasBuff(ShadowStab.BUFF_ID)) {
                if (isStealthed()) {
                    damage *= getBuffValue(ShadowStab.BUFF_ID);
                    Spell.notify(p, "You deliver a powerful stab from the shadows.");
                }
                removeBuff(ShadowStab.BUFF_ID);
            }
            if (hasBuff(SinisterStrike.BUFF_ID)) {
                damage *= getBuffValue(SinisterStrike.BUFF_ID_DAMAGE);
                int duration = (int) getBuffValue(SinisterStrike.BUFF_ID);
                other.giveBuff(SinisterStrike.DEBUFF_ID, 0, duration * 1000);
                other.sendMessage(ChatColor.RED + "You have been crippled for " + ChatColor.YELLOW + duration + "s" + ChatColor.RED + " and have reduced healing.");
                Spell.notify(p, "You cripple your enemy.");
                removeBuff(SinisterStrike.BUFF_ID);
                removeBuff(SinisterStrike.BUFF_ID_DAMAGE);
            }
            if (hasBuff(DoubleStab.BUFF_ID) && rpgDamage <= 0) {
                damage *= getBuffValue(DoubleStab.BUFF_ID);
                success = other.damage(damage, p, DamageType.NORMAL, crit);
                other.damage(damage, p, DamageType.NORMAL_SPELL, crit);
                Spell.notify(p, "You stab twice at your enemy.");
                removeBuff(DoubleStab.BUFF_ID);
            } else {
                success = other.damage(damage, p, rpgDamage > 0 ? DamageType.NORMAL_SPELL : DamageType.NORMAL, crit);
            }
        } else {
            success = other.damage(damage, p, rpgDamage > 0 ? DamageType.NORMAL_SPELL : DamageType.NORMAL, crit);
        }
        if (success && !projectile && knockback > 0)
            other.knockback(p, knockback);
        if (success && isStealthed())
            removeStealth();
        return success;
    }

    public boolean attackMob(MobData other) {
        return attackMob(other, 0.5, -1, false);
    }

    public boolean attackMob(MobData other, int rpgDamage) {
        return attackMob(other, 0.5, rpgDamage, false);
    }

    public boolean attackMob(MobData other, int rpgDamage, boolean projectile) {
        return attackMob(other, 0.5, rpgDamage, projectile);
    }

    public boolean attackMob(MobData other, double knockback, int rpgDamage, boolean projectile) {
        if (riding)
            return false;
        Player p = getPlayer();
        if (p == null || other == null || System.currentTimeMillis() < other.invuln)
            return false;
        int damage;
        if (rpgDamage > 0)
            damage = rpgDamage;
        else
            damage = getDamage(false);
        boolean crit = false;
        double critChanceTemp = critChance;
        if (hasBuff(Trinket.HAWKEYE_BUFF_ID)) {
            critChanceTemp += 0.20;
        }
        if (Math.random() < critChanceTemp) {
            crit = true;
            damage *= critDamage;
            other.playCrit();
        }
        boolean success = false;
        if (classType == ClassType.ASSASSIN) {
            if (hasBuff(ShadowStab.BUFF_ID)) {
                if (isStealthed()) {
                    damage *= getBuffValue(ShadowStab.BUFF_ID);
                    Spell.notify(p, "You deliver a powerful stab from the shadows.");
                }
                removeBuff(ShadowStab.BUFF_ID);
            }
            if (hasBuff(SinisterStrike.BUFF_ID_DAMAGE)) {
                damage *= getBuffValue(SinisterStrike.BUFF_ID_DAMAGE);
                Spell.notify(p, "You cripple your enemy.");
                removeBuff(SinisterStrike.BUFF_ID);
                removeBuff(SinisterStrike.BUFF_ID_DAMAGE);
            }
            if (hasBuff(DoubleStab.BUFF_ID) && rpgDamage <= 0) {
                damage *= getBuffValue(DoubleStab.BUFF_ID);
                success = other.damage(damage, p, DamageType.NORMAL, crit);
                other.damage(damage, p, DamageType.NORMAL_SPELL, crit);
                Spell.notify(p, "You stab twice at your enemy.");
                removeBuff(DoubleStab.BUFF_ID);
            } else {
                success = other.damage(damage, p, rpgDamage > 0 ? DamageType.NORMAL_SPELL : DamageType.NORMAL, crit);
            }
        } else {
            success = other.damage(damage, p, rpgDamage > 0 ? DamageType.NORMAL_SPELL : DamageType.NORMAL, crit);
        }
        if (success && !projectile)
            other.knockback(p, knockback);
        if (success && isStealthed())
            removeStealth();
        return success;
    }

    private void checkEquipmentSet() {
        Player p = getPlayer();
        hasSetBonusDamage = false;
        hasSetBonusHP = false;
        if (!armorSetCounter.isEmpty()) {
            String currName = null;
            int currCount = 0;
            for (Entry<String, Integer> e : armorSetCounter.entrySet()) {
                if (e.getValue() >= 2 && e.getValue() > currCount) { //only message if there are at least two pieces
                    currName = e.getKey();
                    currCount = e.getValue();
                }
            }
            if (currName != null && (!currName.equals(lastMessagedSetName) || currCount != lastMessagedSetCount)) {
                lastMessagedSetName = currName;
                lastMessagedSetCount = currCount;
                if (getOption(SakiOption.SET_NOTIFICATION)) {
                    p.sendMessage("");
                    p.sendMessage(ChatColor.GRAY + ">> " + ChatColor.YELLOW + "You are currently using " + ChatColor.GREEN + currCount + " pieces of " + ChatColor.LIGHT_PURPLE + currName + ChatColor.YELLOW + " equipment.");
                    if (currCount < 4)
                        p.sendMessage(ChatColor.GRAY + ">> " + ChatColor.YELLOW + "Use " + ChatColor.AQUA + "4" + ChatColor.YELLOW + " pieces of the " + ChatColor.LIGHT_PURPLE + currName + ChatColor.YELLOW + " set for " + ChatColor.GOLD + "+10%" + ChatColor.YELLOW + " HP.");
                    else
                        p.sendMessage(ChatColor.GRAY + ">> " + ChatColor.GOLD + "You currently have " + ChatColor.YELLOW + "+10%" + ChatColor.GOLD + " HP from your " + ChatColor.LIGHT_PURPLE + currName + ChatColor.GOLD + " set.");
                    if (currCount < 5)
                        p.sendMessage(ChatColor.GRAY + ">> " + ChatColor.YELLOW + "Use " + ChatColor.AQUA + "5" + ChatColor.YELLOW + " pieces of the " + ChatColor.LIGHT_PURPLE + currName + ChatColor.YELLOW + " set for " + ChatColor.GOLD + "+15%" + ChatColor.YELLOW + " Damage.");
                    else
                        p.sendMessage(ChatColor.GRAY + ">> " + ChatColor.GOLD + "You currently have " + ChatColor.YELLOW + "+15%" + ChatColor.GOLD + " Damage from your " + ChatColor.LIGHT_PURPLE + currName + ChatColor.GOLD + " set.");
                }
            }
            if (currCount >= 4) {
                hasSetBonusHP = true;
            }
            if (currCount >= 5) {
                hasSetBonusDamage = true;
            }
        }
    }

    private void checkSpellStatEffects() {
        if (getSpellLevel(SpellbookCrusader.HEALTHY_DIET) > 0) {
            switch (getSpellLevel(SpellbookCrusader.HEALTHY_DIET)) {
                case 1:
                    maxHP += 50;
                    break;
                case 2:
                    maxHP += 75;
                    break;
                case 3:
                    maxHP += 125;
                    break;
                case 4:
                    maxHP += 200;
                    break;
                case 5:
                    maxHP += 300;
                    break;
            }
        }
        if (getSpellLevel(SpellbookArcher.KEEN_EYES) > 0) {
            switch (getSpellLevel(SpellbookArcher.KEEN_EYES)) {
                case 1:
                    critChance += 2;
                    break;
                case 2:
                    critChance += 4;
                    break;
                case 3:
                    critChance += 6;
                    break;
                case 4:
                    critChance += 8;
                    break;
                case 5:
                    critChance += 10;
                    break;
            }
        }
        if (getSpellLevel(SpellbookPaladin.DIVINITY) > 0) {
            switch (getSpellLevel(SpellbookPaladin.DIVINITY)) {
                case 1:
                    maxHPMultiplier += 2;
                    break;
                case 2:
                    maxHPMultiplier += 4;
                    break;
                case 3:
                    maxHPMultiplier += 6;
                    break;
                case 4:
                    maxHPMultiplier += 8;
                    break;
                case 5:
                    maxHPMultiplier += 10;
                    break;
            }
        }
    }

    public void updateEquipmentStats() {
        ItemStack item;
        Player p = getPlayer();
        if (p == null || !p.isValid())
            return;
        StatAccumulator.clearStats(this);
        armorSetCounter.clear();
        armor.clear();
        item = p.getEquipment().getItemInMainHand();
        if (item != null && EquipType.isWeapon(item)) {
            addStats(item, true);
            addSet(item);
            lastSetItem = item;
        } else if (lastSetItem != null) {
            addSet(lastSetItem);
        }
        armor.add(p.getEquipment().getHelmet());
        armor.add(p.getEquipment().getChestplate());
        armor.add(p.getEquipment().getLeggings());
        armor.add(p.getEquipment().getBoots());
        for (ItemStack i : armor) {
            addStats(i, false);
            addSet(i);
        }
        checkEquipmentSet();
        checkSpellStatEffects();
        if (hasSetBonusHP)
            maxHPMultiplier += 10;
        if (trinket == null) {
            p.getInventory().setItemInOffHand(null);
            lastTrinket = null;
        } else {
            if (lastTrinket == null || lastTrinket != trinket) {
                p.getInventory().setItemInOffHand(trinket.getEquipItem(this));
                lastTrinket = trinket;
            }
            int level = Trinket.getTrinketLevel(getTrinketExp(trinket));
            for (TrinketStat ts : trinket.stats) {
                ts.apply(p, this, level);
            }
            trinket.updateTrinket(this, p.getInventory().getItemInOffHand());
        }
        StatAccumulator.finalizeStats(this);
        finishedLoadEquips = true;
    }

    private void addSet(ItemStack item) {
        if (item == null)
            return;
        ItemMeta im = item.getItemMeta();
        if (im == null)
            return;
        if (!im.hasDisplayName())
            return;
        if (im.getDisplayName().startsWith("*"))
            return;
        String name = im.getDisplayName();
        for (String s : ItemBalance.SET_PREFIXES) {
            if (name.contains(s)) {
                if (armorSetCounter.containsKey(s))
                    armorSetCounter.put(s, armorSetCounter.get(s) + 1);
                else
                    armorSetCounter.put(s, 1);
                break;
            }
        }
    }

    private long lastEquipLevelMessage = 0;

    private void addStats(ItemStack item, boolean inHand) {
        if (item == null)
            return;
        ItemMeta im = item.getItemMeta();
        if (im == null)
            return;
        List<String> lore = im.getLore();
        if (lore == null)
            return;
        StatAccumulator sa = new StatAccumulator();
        for (String s : lore) {
            s = ChatColor.stripColor(s).trim();
            sa.parseAndAccumulate(s);
        }
        if (this.level < sa.level) {
            if (getOption(SakiOption.EQUIP_LEVEL) && System.currentTimeMillis() - lastEquipLevelMessage > 30000) {
                sendMessage("");
                sendMessage(ChatColor.GRAY + "> " + ChatColor.RED + "You are wearing equipment that is too high leveled!");
                sendMessage(ChatColor.GRAY + "> " + ChatColor.RED + "You will not receive any stats from that equipment.");
                sendMessage(ChatColor.GRAY + "> " + ChatColor.RED + "You can turn off this message in /options.");
                lastEquipLevelMessage = System.currentTimeMillis();
            }
            return;
        }
        sa.apply(this);
    }

    @Override
    public void setRank(Rank r) {
        rank = r;
        Team t = board.getEntryTeam(name);
        if (t != null)
            t.removeEntry(name);
        Team team = board.getTeam(rank.rankDisplayName);
        if (team == null) {
            team = board.registerNewTeam(rank.rankDisplayName);
            team.setPrefix(rank.nameColor + "");
            team.setAllowFriendlyFire(true);
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }
        team.addEntry(name);
        save();
    }

}
