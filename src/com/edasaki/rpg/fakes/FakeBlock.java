package com.edasaki.rpg.fakes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class FakeBlock {
    public Location loc;
    public Material mat;
    public byte data;

    public FakeBlock(Location loc, Material mat, byte data) {
        this.loc = loc;
        this.mat = mat;
        this.data = data;
    }

    public void send(Player p) {
        p.sendBlockChange(loc, mat, data);
    }
}
