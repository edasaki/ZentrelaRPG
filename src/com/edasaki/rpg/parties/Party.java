package com.edasaki.rpg.parties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.edasaki.core.chat.ChatFilter;
import com.edasaki.core.chat.ChatManager;
import com.edasaki.core.options.SakiOption;
import com.edasaki.core.players.Rank;
import com.edasaki.core.utils.RSound;
import com.edasaki.core.utils.fanciful.FancyMessage;
import com.edasaki.rpg.PlayerDataRPG;

public class Party {

    private HashMap<String, Long> lastInv = new HashMap<String, Long>();

    private static int ID = 1;

    public int id = ID++;

    private ArrayList<UUID> uuids = new ArrayList<UUID>();

    private UUID leaderUUID;
    private String leaderName;

    private ArrayList<Player> cachedPlayers = new ArrayList<Player>();
    private long lastPlayerUpdate = 0;
    private Scoreboard board;

    private boolean lootshare = false;

    public Party(Player p) {
        this.leaderName = p.getName();
        this.leaderUUID = p.getUniqueId();
        board = PartyManager.plugin.getServer().getScoreboardManager().getNewScoreboard();
        Objective objective = board.registerNewObjective("hpdisplay", "dummy");
        objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
        objective.setDisplayName(ChatColor.DARK_RED + "\u2764");
        Objective objective_side = board.registerNewObjective("partyhpside", "dummy");
        objective_side.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective_side.setDisplayName(ChatColor.YELLOW + "Party Member " + ChatColor.RED + "   HP" + ChatColor.RESET);
        addPlayer(p);
    }

    public void updatePlayer(String name, int hp, Rank rank, boolean inParty) {
        if (board == null)
            return;
        final Score score = board.getObjective("hpdisplay").getScore(name);
        score.setScore(hp);

        if (inParty) {
            final Score score_side = board.getObjective("partyhpside").getScore(name);
            score_side.setScore(hp);
        }

        Team team = board.getTeam(rank.rankDisplayName);
        if (team == null) {
            team = board.registerNewTeam(rank.rankDisplayName);
            team.setPrefix(rank.nameColor + "");
            team.setAllowFriendlyFire(true);
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }
        if (!team.hasEntry(name))
            team.addEntry(name);
    }

    public void sendInvite(Player p, PlayerDataRPG pd, Player inviter) {
        if (pd.party == this) {
            inviter.sendMessage(ChatColor.RED + p.getName() + " is already in the party!");
            return;
        }
        if (lastInv.containsKey(p.getName())) {
            if (System.currentTimeMillis() - lastInv.get(p.getName()) < 15000) {
                inviter.sendMessage(ChatColor.RED + p.getName() + " was invited to the party in the last 15 seconds. Please wait a bit before inviting them again.");
                return;
            }
        }
        lastInv.put(p.getName(), System.currentTimeMillis());
        RSound.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
        p.sendMessage(ChatColor.GRAY + ">> " + ChatColor.GREEN + "You have received a party invite from " + ChatColor.YELLOW + inviter.getName() + ChatColor.GREEN + "!");
        FancyMessage fm = new FancyMessage();
        fm.text(">> ");
        fm.color(ChatColor.GRAY);
        fm.then("Click Here");
        fm.color(ChatColor.YELLOW);
        fm.command("/party accept");
        fm.then(" to accept the party invitation!");
        fm.color(ChatColor.GREEN);
        fm.send(p);
        fm = new FancyMessage();
        fm.text(">> ");
        fm.color(ChatColor.GRAY);
        fm.then("To decline the invitation, ");
        fm.color(ChatColor.RED);
        fm.then("Click Here");
        fm.color(ChatColor.YELLOW);
        fm.command("/party decline");
        fm.then(".");
        fm.color(ChatColor.RED);
        fm.send(p);
        pd.invitedParty = this;
        sendMessage(p.getName() + " was invited to the party by " + inviter.getName() + ".");
    }

    public boolean isLeader(Player p) {
        return p.getName().equals(leaderName) && p.getUniqueId().equals(leaderUUID);
    }

    public void addPlayer(Player p) {
        if (isFull()) {
            p.sendMessage(ChatColor.RED + "You can't join the party because it's full!");
            sendMessage(p.getName() + " tried to join the party, but the party is full.");
            return;
        }
        lastPlayerUpdate = 0;
        p.setScoreboard(board);
        uuids.add(p.getUniqueId());
        PlayerDataRPG pd = PartyManager.plugin.getPD(p);
        if (pd != null) {
            pd.party = this;
        }
        sendMessage(p.getName() + " has joined " + this.leaderName + "'s party!");
        if (this.lootshare) {
            sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Warning: " + ChatColor.GREEN + ChatColor.BOLD + "Lootshare is ACTIVE.");
            sendMessage(ChatColor.GRAY + "- Others in your party can pick up your loot without delay.");
        } else {
            sendMessage(ChatColor.GRAY + "Lootshare is OFF.");
        }
    }

    public void leavePlayer(UUID uuid, String name) {
        if (uuid == null)
            return;
        board.resetScores(name);
        Player player = PartyManager.plugin.getServer().getPlayer(uuid);
        if (player != null && player.isOnline() && player.isValid() && ((CraftPlayer) player).getHandle() != null && ((CraftPlayer) player).getHandle().playerConnection != null) {
            if (!((CraftPlayer) player).getHandle().playerConnection.isDisconnected()) {
                try {
                    player.setScoreboard(PlayerDataRPG.board);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (this.leaderUUID != null && uuid == this.leaderUUID) {
            sendMessage(name + " has left the party.");
            if (player != null && player.isOnline()) {
                PlayerDataRPG pd = PartyManager.plugin.getPD(player);
                if (pd != null)
                    pd.party = null;
            }
            this.uuids.remove(uuid);
            lastPlayerUpdate = 0;
            if (this.uuids.size() == 0) {
                destroy();
            } else {
                for (int k = 0; k < uuids.size(); k++) {
                    UUID nu = uuids.get(k);
                    Player temp = PartyManager.plugin.getServer().getPlayer(nu);
                    if (temp != null && temp.isOnline()) {
                        sendMessage(temp.getName() + " is the new party leader.");
                        this.leaderUUID = nu;
                        this.leaderName = temp.getName();
                        break;
                    }
                }
            }
        } else {
            sendMessage(name + " has left the party.");
            if (player != null && player.isOnline()) {
                PlayerDataRPG pd = PartyManager.plugin.getPD(player);
                if (pd != null)
                    pd.party = null;
            }
            this.uuids.remove(uuid);
            lastPlayerUpdate = 0;
            if (this.uuids.size() == 0)
                destroy();
        }
    }

    public void kickPlayer(Player p) {
        if (p != null) {
            p.sendMessage(ChatColor.RED + "You were kicked from the party.");
            leavePlayer(p.getUniqueId(), p.getName());
            sendMessage(p.getName() + " was kicked from the party.");
        }
    }

    public boolean isFull() {
        if (this.uuids == null)
            return true;
        return this.uuids.size() >= PartyManager.MAX_SIZE;
    }

    public double getExpMultiplier() {
        int size = this.uuids.size();
        if (size < 0)
            size = 0;
        switch (size) {
            case 0:
            case 1:
                return 0.00;
            case 2:
                return 0.10;
            case 3:
                return 0.15;
            case 4:
                return 0.20;
            default:
            case 5:
                return 0.25;
        }
    }

    public void destroy() {
        for (UUID uuid : this.uuids) {
            if (PartyManager.plugin.getPD(uuid.toString()) != null) {
                PlayerDataRPG pd = PartyManager.plugin.getPD(uuid.toString());
                if (pd.party != null && pd.party == this)
                    pd.party = null;
            }
        }
        cachedPlayers.clear();
        cachedPlayers = null;
        uuids.clear();
        uuids = null;
        leaderName = null;
        leaderUUID = null;
        board = null;
        PartyManager.parties.remove(this);
    }

    public ArrayList<Player> getPlayers() {
        if (System.currentTimeMillis() - lastPlayerUpdate > 5000) {
            lastPlayerUpdate = System.currentTimeMillis();
            cachedPlayers.clear();
            ArrayList<UUID> toRemove = new ArrayList<UUID>();
            for (UUID uuid : uuids) {
                Player p = PartyManager.plugin.getServer().getPlayer(uuid);
                if (p != null && p.isOnline()) {
                    cachedPlayers.add(p);
                } else {
                    toRemove.add(uuid);
                }
            }
            uuids.removeAll(toRemove);
            return cachedPlayers;
        } else {
            for (int k = 0; k < cachedPlayers.size(); k++) {
                Player p = cachedPlayers.get(k);
                if (p == null || !p.isOnline()) {
                    cachedPlayers.remove(k);
                    k--;
                    if (k < 0)
                        k = 0;
                }
            }
            return cachedPlayers;
        }
    }

    public void sendMessage(Player sender, String s) {
        String msg = PartyManager.PREFIX + sender.getName() + ChatColor.WHITE + ": " + s;
        String msgFiltered = null;
        for (Player p : getPlayers()) {
            if (p == null || !p.isOnline())
                continue;
            try {
                if (PartyManager.plugin.getPD(p) != null && PartyManager.plugin.getPD(p).getOption(SakiOption.CHAT_FILTER)) {
                    if (msgFiltered == null)
                        msgFiltered = PartyManager.PREFIX + sender.getName() + ChatColor.WHITE + ": " + ChatFilter.getFiltered(s);
                    p.sendMessage(msgFiltered);
                } else {
                    p.sendMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (String m : ChatManager.monitors) {
            Player monitor = PartyManager.plugin.getServer().getPlayerExact(m);
            if (monitor != null && monitor.isOnline()) {
                PlayerDataRPG mpd = PartyManager.plugin.getPD(monitor);
                if (mpd != null && mpd.party != this) {
                    monitor.sendMessage(ChatColor.DARK_GRAY + "[MONITOR]");
                    monitor.sendMessage(ChatColor.GRAY + "ID " + id + " " + msg);
                }
            }
        }
    }

    public void sendMessage(String s) {
        String fMsg = PartyManager.PREFIX_SYSTEM + s;
        for (Player p : getPlayers()) {
            p.sendMessage(fMsg);
            RSound.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
        }
    }

    public String toString() {
        return uuids.size() + ": ID " + id;
    }

    public boolean isLootshareActive() {
        return lootshare;
    }

    public void toggleLootshare() {
        lootshare = !lootshare;
        if (lootshare) {
            sendMessage(ChatColor.AQUA + "Lootshare has been turned ON by the party leader!");
            sendMessage(ChatColor.RED + "- Others in your party can pick up your loot without delay.");
        } else {
            sendMessage(ChatColor.AQUA + "Lootshare has been turned OFF by the party leader!");
        }
    }

}
