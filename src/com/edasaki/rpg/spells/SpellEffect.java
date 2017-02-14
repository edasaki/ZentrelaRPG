package com.edasaki.rpg.spells;

import java.util.function.IntToDoubleFunction;

import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;

public abstract class SpellEffect {
    protected IntToDoubleFunction[] functions;

    public abstract boolean cast(Player p, PlayerDataRPG pd, int level);
}
