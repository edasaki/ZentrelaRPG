package com.edasaki.rpg.fakes;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.entity.Player;

public class Fake {

    private HashSet<UUID> active = new HashSet<UUID>();
    // each Fake has two states, pre and post
    public FakeBlock[][] pre, post;

    public void toggle(Player p) {
        UUID uuid = p.getUniqueId();
        if (active.contains(uuid))
            active.remove(uuid);
        else
            active.add(uuid);
    }

}
