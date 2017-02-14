package com.edasaki.rpg.commands;

import com.edasaki.core.SakiCore;
import com.edasaki.core.commands.mod.WalkSpeedCommand;
import com.edasaki.core.players.Rank;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.commands.admin.SwapCommand;
import com.edasaki.rpg.commands.beta.BaseMobCommand;
import com.edasaki.rpg.commands.beta.ClearNearbyCommand;
import com.edasaki.rpg.commands.beta.EffectsCommand;
import com.edasaki.rpg.commands.beta.GamemodeCommand;
import com.edasaki.rpg.commands.beta.MobsCommand;
import com.edasaki.rpg.commands.beta.NightVisionCommand;
import com.edasaki.rpg.commands.beta.TestEquipsCommand;
import com.edasaki.rpg.commands.beta.TestPotionsCommand;
import com.edasaki.rpg.commands.builder.BlockLocCommand;
import com.edasaki.rpg.commands.builder.BuilderCommand;
import com.edasaki.rpg.commands.builder.SakiReplaceCommand;
import com.edasaki.rpg.commands.builder.TerraformCommand;
import com.edasaki.rpg.commands.builder.VoxelSniperCommand;
import com.edasaki.rpg.commands.builder.WorldEditCommand;
import com.edasaki.rpg.commands.console.AdRewardCommand;
import com.edasaki.rpg.commands.console.BuycraftRewardCommand;
import com.edasaki.rpg.commands.gm.GMCommand;
import com.edasaki.rpg.commands.gm.NPCCommand;
import com.edasaki.rpg.commands.member.BankCommand;
import com.edasaki.rpg.commands.member.ClassCommand;
import com.edasaki.rpg.commands.member.ClearCommand;
import com.edasaki.rpg.commands.member.FlightCommand;
import com.edasaki.rpg.commands.member.GrappleCommand;
import com.edasaki.rpg.commands.member.GuildCommand;
import com.edasaki.rpg.commands.member.HelpCommand;
import com.edasaki.rpg.commands.member.HorseCommand;
import com.edasaki.rpg.commands.member.IgnoreCommand;
import com.edasaki.rpg.commands.member.InfoCommand;
import com.edasaki.rpg.commands.member.LocationCommand;
import com.edasaki.rpg.commands.member.LookupCommand;
import com.edasaki.rpg.commands.member.OnlineCommand;
import com.edasaki.rpg.commands.member.OptionCommand;
import com.edasaki.rpg.commands.member.PartyCommand;
import com.edasaki.rpg.commands.member.PingCommand;
import com.edasaki.rpg.commands.member.PlayTimeCommand;
import com.edasaki.rpg.commands.member.QuestCommand;
import com.edasaki.rpg.commands.member.RegionCommand;
import com.edasaki.rpg.commands.member.RenameCommand;
import com.edasaki.rpg.commands.member.ReplyCommand;
import com.edasaki.rpg.commands.member.ResetQuestsCommand;
import com.edasaki.rpg.commands.member.ResetSPCommand;
import com.edasaki.rpg.commands.member.RewardsCommand;
import com.edasaki.rpg.commands.member.RollCommand;
import com.edasaki.rpg.commands.member.SalvageCommand;
import com.edasaki.rpg.commands.member.ShardCommand;
import com.edasaki.rpg.commands.member.SpawnCommand;
import com.edasaki.rpg.commands.member.Spell1Command;
import com.edasaki.rpg.commands.member.Spell2Command;
import com.edasaki.rpg.commands.member.Spell3Command;
import com.edasaki.rpg.commands.member.Spell4Command;
import com.edasaki.rpg.commands.member.SpellCommand;
import com.edasaki.rpg.commands.member.TopCommand;
import com.edasaki.rpg.commands.member.TradeCommand;
import com.edasaki.rpg.commands.member.TrinketCommand;
import com.edasaki.rpg.commands.member.VoteCommand;
import com.edasaki.rpg.commands.member.WhisperCommand;
import com.edasaki.rpg.commands.member.WorldBossArenaCommand;
import com.edasaki.rpg.commands.mod.SetWarpCommand;
import com.edasaki.rpg.commands.mod.WarpCommand;
import com.edasaki.rpg.commands.owner.AnnounceCommand;
import com.edasaki.rpg.commands.owner.BurnCommand;
import com.edasaki.rpg.commands.owner.CompleteQuestCommand;
import com.edasaki.rpg.commands.owner.CrashCommand;
import com.edasaki.rpg.commands.owner.DBCommand;
import com.edasaki.rpg.commands.owner.DeopCommand;
import com.edasaki.rpg.commands.owner.EditLoreCommand;
import com.edasaki.rpg.commands.owner.EditNameCommand;
import com.edasaki.rpg.commands.owner.FindDupesCommand;
import com.edasaki.rpg.commands.owner.FindItemCommand;
import com.edasaki.rpg.commands.owner.GenerateItemsCommand;
import com.edasaki.rpg.commands.owner.GiveItemCommand;
import com.edasaki.rpg.commands.owner.HealCommand;
import com.edasaki.rpg.commands.owner.KillCommand;
import com.edasaki.rpg.commands.owner.LevelCommand;
import com.edasaki.rpg.commands.owner.LoadWorldCommand;
import com.edasaki.rpg.commands.owner.ManaCommand;
import com.edasaki.rpg.commands.owner.MobSpawnCommand;
import com.edasaki.rpg.commands.owner.MonitorCommand;
import com.edasaki.rpg.commands.owner.MotdCommand;
import com.edasaki.rpg.commands.owner.OpCommand;
import com.edasaki.rpg.commands.owner.PoisonCommand;
import com.edasaki.rpg.commands.owner.ReflectCommand;
import com.edasaki.rpg.commands.owner.ReflectGetCommand;
import com.edasaki.rpg.commands.owner.ReloadCommand;
import com.edasaki.rpg.commands.owner.ReloadDungeonsCommand;
import com.edasaki.rpg.commands.owner.ReloadGenericsCommand;
import com.edasaki.rpg.commands.owner.ReloadHayCommand;
import com.edasaki.rpg.commands.owner.ReloadHologramsCommand;
import com.edasaki.rpg.commands.owner.ReloadHorsesCommand;
import com.edasaki.rpg.commands.owner.ReloadItemsCommand;
import com.edasaki.rpg.commands.owner.ReloadMobsCommand;
import com.edasaki.rpg.commands.owner.ReloadQuestsCommand;
import com.edasaki.rpg.commands.owner.ReloadRegionsCommand;
import com.edasaki.rpg.commands.owner.ReloadShopsCommand;
import com.edasaki.rpg.commands.owner.ReloadWarpsCommand;
import com.edasaki.rpg.commands.owner.RerollCommand;
import com.edasaki.rpg.commands.owner.MisakaCommand;
import com.edasaki.rpg.commands.owner.SakiCommand;
import com.edasaki.rpg.commands.owner.SeekItemCommand;
import com.edasaki.rpg.commands.owner.SetBankCommand;
import com.edasaki.rpg.commands.owner.SetSpawnCommand;
import com.edasaki.rpg.commands.owner.ShadowMuteCommand;
import com.edasaki.rpg.commands.owner.ShutdownCommand;
import com.edasaki.rpg.commands.owner.SpawnMobCommand;
import com.edasaki.rpg.commands.owner.TPHideCommand;
import com.edasaki.rpg.commands.owner.ViewBankCommand;

public class CommandManager extends com.edasaki.core.commands.CommandManager {

    public CommandManager(SakiCore plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {
        super.initialize();
        // Member
        register(Rank.MEMBER, new OnlineCommand("online", "players"));
        register(Rank.MEMBER, new WhisperCommand("w", "whisper", "tell", "pm", "message", "msg"));
        register(Rank.MEMBER, new ReplyCommand("r", "reply"));
        register(Rank.MEMBER, new ClassCommand("class", "classes"));
        register(Rank.MEMBER, new SpellCommand("spell", "spells", "magic", "sp", "spl", "spls"));
        register(Rank.MEMBER, new InfoCommand("info", "information", "details", "detail", "spy"));
        register(Rank.MEMBER, new ClearCommand("clear", "clearinventory"));
        register(Rank.MEMBER, new ResetSPCommand("resetsp"));
        register(Rank.MEMBER, new OptionCommand("option", "options", "opt", "o"));
        register(Rank.MEMBER, new HelpCommand("help", "commands", "command", "?"));
        register(Rank.MEMBER, new Spell1Command("spell1", "1", "s1"));
        register(Rank.MEMBER, new Spell2Command("spell2", "2", "s2"));
        register(Rank.MEMBER, new Spell3Command("spell3", "3", "s3"));
        register(Rank.MEMBER, new Spell4Command("spell4", "4", "s4"));
        register(Rank.MEMBER, new ShardCommand("shard", "shards", "eco", "econ", "economy", "bal", "balance", "gold", "money"));
        register(Rank.MEMBER, new LocationCommand("loc", "location"));
        register(Rank.MEMBER, new RegionCommand("region", "reg"));
        register(Rank.MEMBER, new QuestCommand("quest", "q", "quests"));
        //        register(Rank.MEMBER, new ResetQuestsCommand("resetquests", "resetquest"));
        register(Rank.MEMBER, new GuildCommand("guild", "g", "guilds"));
        register(Rank.MEMBER, new TradeCommand("trade"));
        register(Rank.MEMBER, new PartyCommand("party", "p"));
        register(Rank.MEMBER, new TrinketCommand("trinket", "t"));
        register(Rank.MEMBER, new LookupCommand("lookup"));
        register(Rank.MEMBER, new PlayTimeCommand("playtime", "timeplayed", "playedtime", "played"));
        register(Rank.MEMBER, new RewardsCommand("rewards", "reward", "votepoints", "votepoint", "rewardshop"));
        //        register(Rank.MEMBER, new TeleportAcceptCommand("tpa")); // removed 2.0.1
        register(Rank.MEMBER, new EffectsCommand("e", "effect", "effects", "particle", "particles"));
        register(Rank.MEMBER, new VoteCommand("vote", "votes", "voting"));
        register(Rank.MEMBER, new SpawnCommand("spawn"));
        register(Rank.MEMBER, new SalvageCommand("salvage", "sell"));
        register(Rank.MEMBER, new BankCommand("bank"));
        register(Rank.MEMBER, new RollCommand("roll", "rtd"));
        register(Rank.MEMBER, new HorseCommand("horse", "h"));
        register(Rank.MEMBER, new RenameCommand("rename"));
        register(Rank.MEMBER, new IgnoreCommand("ignore"));
        register(Rank.MEMBER, new PingCommand("ping"));
        register(Rank.MEMBER, new FlightCommand("soaring", "flight", "soar"));
        register(Rank.MEMBER, new TopCommand("top", "leader", "tops", "leaders", "leaderboard", "toplist"));

        register(Rank.MEMBER, new WorldBossArenaCommand("worldboss", "worldbossarena"));

        // Beta

        // Helper

        // Gamemaster
        register(Rank.GAMEMASTER, new NPCCommand("npc"));
        register(Rank.GAMEMASTER, new GMCommand("makegm"));

        // Builder
        register(Rank.BUILDER, new BuilderCommand("builder"));
        register(Rank.BUILDER, new VoxelSniperCommand("vxme"));
        register(Rank.BUILDER, new TerraformCommand("terraform", "tf"));
        register(Rank.BUILDER, new SakiReplaceCommand("sakireplace"));
        register(Rank.BUILDER, new NightVisionCommand("nightvision"));
        register(SakiRPG.TEST_REALM ? Rank.MEMBER : Rank.BUILDER, new GamemodeCommand("gamemode", "gm"));
        register(Rank.BUILDER, new WorldEditCommand("weme"));

        // Mod
        register(SakiCore.TEST_REALM ? Rank.MEMBER : Rank.MOD, new SetWarpCommand("setwarp"));
        register(SakiCore.TEST_REALM ? Rank.MEMBER : Rank.MOD, new WarpCommand("warp"));

        // Admin
        register(Rank.ADMIN, new ClearNearbyCommand("clearnearby"));
        register(Rank.ADMIN, new SwapCommand("swap"));

        // Owner
        register(Rank.OWNER, new GrappleCommand("grapple"));
        register(Rank.OWNER, new DBCommand("db"));
        register(Rank.OWNER, new LoadWorldCommand("loadworld"));
        register(Rank.OWNER, new SetSpawnCommand("setspawn"));
        register(Rank.OWNER, new ReflectCommand("reflect"));
        register(Rank.OWNER, new MotdCommand("motd"));
        register(Rank.OWNER, new ReflectGetCommand("reflectget"));
        register(Rank.OWNER, new MisakaCommand("misaka"));
        register(Rank.OWNER, new ReloadMobsCommand("reloadmobs"));
        register(Rank.OWNER, new AnnounceCommand("announce"));
        register(SakiRPG.TEST_REALM ? Rank.MEMBER : Rank.OWNER, new GiveItemCommand("giveitem", "item", "items"));
        register(Rank.OWNER, new ReloadItemsCommand("reloaditems"));
        register(Rank.OWNER, new OpCommand("op"));
        register(Rank.OWNER, new DeopCommand("deop"));
        register(Rank.OWNER, new ReloadCommand("reload", "rl"));
        register(Rank.OWNER, new KillCommand("kill"));
        register(Rank.OWNER, new MonitorCommand("monitor"));
        register(Rank.OWNER, new ReloadWarpsCommand("reloadwarps"));
        register(Rank.OWNER, new TPHideCommand("tphide"));
        register(Rank.OWNER, new ManaCommand("mana"));
        register(Rank.OWNER, new CrashCommand("crash"));
        register(Rank.OWNER, new BurnCommand("burn"));
        register(Rank.OWNER, new PoisonCommand("poison"));
        register(Rank.OWNER, new ReloadRegionsCommand("reloadregions"));
        register(Rank.OWNER, new ReloadQuestsCommand("reloadquests"));
        register(Rank.OWNER, new ReloadGenericsCommand("reloadgenerics"));
        register(Rank.OWNER, new MobSpawnCommand("mobspawn", "ms"));
        register(Rank.OWNER, new ShadowMuteCommand("shadowmute"));
        register(Rank.OWNER, new SakiCommand("sakibot", "saki"));
        register(Rank.OWNER, new ReloadHologramsCommand("reloadholograms", "reloadholos"));
        register(Rank.OWNER, new ReloadDungeonsCommand("reloaddungeons"));
        register(Rank.OWNER, new ReloadShopsCommand("reloadshops"));
        register(Rank.OWNER, new WalkSpeedCommand("walkspeed"));
        register(Rank.OWNER, new TestPotionsCommand("testpotions", "testpots", "testpotion"));
        register(Rank.OWNER, new TestEquipsCommand("testequips"));
        register(Rank.OWNER, new MobsCommand("mobs"));
        register(SakiRPG.TEST_REALM ? Rank.MEMBER : Rank.OWNER, new GenerateItemsCommand("generateitems", "generate", "generateitem"));
        register(SakiRPG.TEST_REALM ? Rank.MEMBER : Rank.OWNER, new SeekItemCommand("seekitems", "seekitem", "seek"));
        register(SakiRPG.TEST_REALM ? Rank.MEMBER : Rank.OWNER, new SpawnMobCommand("spawnmob"));
        register(Rank.OWNER, new ViewBankCommand("viewbank", "checkbank", "seebank"));
        register(Rank.OWNER, new BlockLocCommand("blockloc"));
        //        register(Rank.OWNER, new MaintenanceCommand("maintenance"));
        register(SakiRPG.TEST_REALM ? Rank.MEMBER : Rank.OWNER, new ResetQuestsCommand("resetquests", "resetquest"));
        register(Rank.OWNER, new ReloadHayCommand("reloadhay", "reloadhaybales"));
        register(Rank.OWNER, new ReloadHorsesCommand("reloadstables", "reloadhorses"));
        register(Rank.OWNER, new ShutdownCommand("shutdown"));
        register(Rank.OWNER, new FindDupesCommand("finddupes"));
        register(Rank.OWNER, new FindItemCommand("finditem"));
        register(Rank.OWNER, new SetBankCommand("setbank"));
        register(Rank.OWNER, new EditLoreCommand("editlore"));
        register(SakiRPG.TEST_REALM ? Rank.MEMBER : Rank.OWNER, new BaseMobCommand("basemob"));
        register(SakiRPG.TEST_REALM ? Rank.MEMBER : Rank.OWNER, new LevelCommand("level"));
        register(SakiRPG.TEST_REALM ? Rank.MEMBER : Rank.OWNER, new HealCommand("heal"));
        register(Rank.OWNER, new RerollCommand("reroll"));
        register(Rank.OWNER, new EditNameCommand("editname"));
        register(SakiRPG.TEST_REALM ? Rank.MEMBER : Rank.OWNER, new CompleteQuestCommand("completequest"));

        // Console - these commands are meant to be run by console, but can be used (carefully) by owner rank
        register(Rank.OWNER, new AdRewardCommand("adreward"));
        register(Rank.OWNER, new BuycraftRewardCommand("buycraftreward"));
    }
}
