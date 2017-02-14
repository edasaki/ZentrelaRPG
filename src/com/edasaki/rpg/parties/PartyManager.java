package com.edasaki.rpg.parties;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.edasaki.core.players.Rank;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RTicks;
import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;

public class PartyManager extends AbstractManagerRPG {

    public static final int MAX_SIZE = 10;

    protected static final String PREFIX = ChatColor.GRAY + "[Party] " + ChatColor.YELLOW;
    protected static final String PREFIX_SYSTEM = ChatColor.GRAY + "[Party] " + ChatColor.DARK_AQUA;

    protected static ArrayList<Party> parties;

    public PartyManager(SakiRPG plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {
        parties = new ArrayList<Party>();
        RScheduler.scheduleRepeating(plugin, new Runnable() {
            public void run() {
                for (int k = 0; k < parties.size(); k++) {
                    Party p = parties.get(k);
                    if (p.getPlayers().size() == 0) {
                        parties.remove(k);
                        if (p != null)
                            p.destroy();
                        k--;
                        if (k < 0)
                            k = 0;
                    }
                }
            }
        }, RTicks.seconds(30));
    }

    public static void updatePlayerForAll(String name, int hp, Rank rank, Party party) {
        if (parties != null) {
            for (Party p : parties) {
                if (p == null)
                    continue;
                p.updatePlayer(name, hp, rank, party != null && party == p);
            }
        }

    }

    public static void createParty(Player p, PlayerDataRPG pd) {
        if (pd.party != null) {
            p.sendMessage(ChatColor.RED + " You are already in a party!");
            p.sendMessage(ChatColor.RED + " Leave your current party to create a new one.");
        } else {
            Party party = new Party(p);
            pd.party = party;
            parties.add(party);
            p.sendMessage(ChatColor.GRAY + ">> " + ChatColor.GREEN + "You have created a new party!");
        }

    }

    public static boolean sameParty(Player p, Player p2) {
        PlayerDataRPG pd = plugin.getPD(p);
        PlayerDataRPG pd2 = plugin.getPD(p2);
        if (pd == null || pd2 == null)
            return false;
        if (pd.party == null || pd2.party == null)
            return false;
        return pd.party == pd2.party;
    }

}
