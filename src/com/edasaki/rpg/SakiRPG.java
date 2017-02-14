package com.edasaki.rpg;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Entity;

import com.edasaki.core.PlayerData;
import com.edasaki.core.SakiCore;
import com.edasaki.core.options.SakiOption;
import com.edasaki.core.pets.PetManager;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RTicks;
import com.edasaki.rpg.buffs.BuffManager;
import com.edasaki.rpg.buycraft.BuycraftManager;
import com.edasaki.rpg.chat.ChatManager;
import com.edasaki.rpg.combat.CombatManager;
import com.edasaki.rpg.commands.CommandManager;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.drops.DropManager;
import com.edasaki.rpg.dungeons.DungeonManager;
import com.edasaki.rpg.economy.EconomyManager;
import com.edasaki.rpg.economy.ShardManager;
import com.edasaki.rpg.fakes.FakeManager;
import com.edasaki.rpg.fun.GrappleManager;
import com.edasaki.rpg.general.EnvironmentManager;
import com.edasaki.rpg.general.SchematicManager;
import com.edasaki.rpg.general.StealthManager;
import com.edasaki.rpg.guilds.GuildManager;
import com.edasaki.rpg.haybales.HaybaleManager;
import com.edasaki.rpg.help.HelpManager;
import com.edasaki.rpg.holograms.HologramManager;
import com.edasaki.rpg.horses.HorseManager;
import com.edasaki.rpg.items.ItemManager;
import com.edasaki.rpg.mobs.EntityRegistrar;
import com.edasaki.rpg.mobs.MobData;
import com.edasaki.rpg.mobs.MobManager;
import com.edasaki.rpg.music.MusicManager;
import com.edasaki.rpg.npcs.NPCManager;
import com.edasaki.rpg.npcs.generics.GenericNPCManager;
import com.edasaki.rpg.particles.ParticleManager;
import com.edasaki.rpg.parties.PartyManager;
import com.edasaki.rpg.quests.QuestManager;
import com.edasaki.rpg.rebirths.RebirthManager;
import com.edasaki.rpg.regions.RegionManager;
import com.edasaki.rpg.rewards.RewardsManager;
import com.edasaki.rpg.shops.ShopManager;
import com.edasaki.rpg.skills.SkillManager;
import com.edasaki.rpg.soaring.SoaringManager;
import com.edasaki.rpg.spells.SpellManager;
import com.edasaki.rpg.tips.TipManager;
import com.edasaki.rpg.top.TopManager;
import com.edasaki.rpg.trades.TradeManager;
import com.edasaki.rpg.trinkets.TrinketManager;
import com.edasaki.rpg.utils.gson.RPGItemAdapter;
import com.edasaki.rpg.vip.BoostManager;
import com.edasaki.rpg.votes.VoteManager;
import com.edasaki.rpg.warps.WarpManager;
import com.edasaki.rpg.worldboss.WorldBossManager;

public class SakiRPG extends SakiCore {

    public static final String GAME_WORLD = "main";
    public static final String LOBBY_WORLD = "lobby";
    public static final String TUTORIAL_WORLD = "tutorial";
    public static final String BOSS_WORLD = "worldboss";

    public static SakiRPG plugin;

    private static Location tutorialSpawnLoc = null;

    public static Location getTutorialSpawn() {
        if (tutorialSpawnLoc != null)
            return tutorialSpawnLoc;
        World w = plugin.getServer().getWorld(SakiRPG.TUTORIAL_WORLD);
        return tutorialSpawnLoc = new Location(w, -270.5, 63.0, -372.5, 0f, 0f);
    }

    @Override
    public void onEnable() {
        plugin = this;
        AbstractManagerRPG.plugin = this;
        RPGAbstractCommand.plugin = this;
        setPlayerdataClass(PlayerDataRPG.class);
        RPGItemAdapter.register();

        new DropManager(this); // before loadWorlds
        loadWorlds();
        MobData.plugin = this;
        EntityRegistrar.registerEntities();

        File f = getDataFolder();
        if (!f.exists())
            f.mkdirs();

        getServer().createWorld(new WorldCreator(LOBBY_WORLD));
        getServer().createWorld(new WorldCreator(GAME_WORLD));
        getServer().createWorld(new WorldCreator(TUTORIAL_WORLD));
        getServer().createWorld(new WorldCreator("dungeon"));

        new EnvironmentManager(this);
        new CommandManager(this);

        this.unloadManager(com.edasaki.core.chat.ChatManager.class); // use custom chat manager for RPG
        new ChatManager(this);
        new CombatManager(this);
        new ItemManager(this); //must be before mobmanager
        new MobManager(this);
        new GrappleManager(this);
        new SpellManager(this);
        new WarpManager(this);
        new StealthManager(this);
        new EconomyManager(this);
        new ShardManager(this);
        new SchematicManager(this);
        //        new MobBossManager(this);
        new RegionManager(this);
        new NPCManager(this);
        new QuestManager(this);
        //        new PacketManager(this);
        new BuffManager(this);
        new GuildManager(this);
        new TradeManager(this);
        new PartyManager(this);
        new TipManager(this);
        new ShopManager(this);
        new TrinketManager(this);
        new ParticleManager(this);
        new HologramManager(this);
        if (!SakiRPG.TEST_REALM)
            new VoteManager(this);
        new RewardsManager(this);
        new BuycraftManager(this);
        new HelpManager(this);
        new DungeonManager(this);
        new HaybaleManager(this);
        new HorseManager(this);
        new BoostManager(this);
        new RebirthManager(this);
        new MusicManager(this);
        new GenericNPCManager(this);
        new SkillManager(this);
        new PetManager(this);
        new FakeManager(this);
        new SoaringManager(this);
        new WorldBossManager(this);
        new TopManager(this);

        System.out.println("Successfully loaded SakiRPG.");

        postLoad();
    }

    public void postLoad() {
        RScheduler.schedule(plugin, new Runnable() {
            public void run() {
                for (World w : getServer().getWorlds()) {
                    for (Chunk chunk : w.getLoadedChunks()) {
                        NPCManager.handleChunk(chunk);
                        DungeonManager.handleChunk(chunk);
                        HologramManager.handleChunk(chunk);
                        EnvironmentManager.handleChunk(chunk);
                    }
                }
            }
        }, RTicks.seconds(1));
        RScheduler.schedule(plugin, new Runnable() {
            public void run() {
                for (PlayerData pd : getAllPlayerData()) {
                    if (!(pd instanceof PlayerDataRPG))
                        continue;
                    PlayerDataRPG pdr = (PlayerDataRPG) pd;
                    if (pdr.getOption(SakiOption.SOFT_LAUNCH)) {
                        pd.sendMessage(ChatColor.GRAY + "> The world is incomplete and we are still building stuff!");
                        pd.sendMessage(ChatColor.GRAY + "> We are working hard to get more things out soon!");
                        pd.sendMessage(ChatColor.GRAY + "> Keep an eye on the forums at zentrela.net for updates.");
                        pd.sendMessage(ChatColor.GRAY + "> Hide this message in /options (end of the second row).");
                    }
                }
                RScheduler.schedule(plugin, this, RTicks.seconds(300));
            }
        }, RTicks.seconds(5));
    }

    @Override
    public void onDisable() {
        try {
            WarpManager.saveWarps();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            EntityRegistrar.unregisterEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ParticleManager.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadWorlds() {
        for (World w : getServer().getWorlds()) {
            EnvironmentManager.despawnEntities(w.getEntities().toArray(new Entity[w.getEntities().size()]));
            w.setThunderDuration(0);
            w.setWeatherDuration(0);
            w.setStorm(false);
            w.setThundering(false);
            if (!w.getName().equals(BOSS_WORLD))
                w.setTime(0);
        }
    }

    @Override
    public PlayerDataRPG getPD(Object o) {
        return (PlayerDataRPG) super.getPD(o);
    }
}
