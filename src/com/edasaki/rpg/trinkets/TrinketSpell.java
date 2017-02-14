package com.edasaki.rpg.trinkets;

import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;

public abstract class TrinketSpell {

    public abstract String getName();
    
    public abstract String getDescription();
    
    public abstract int getCooldown();
    
    public abstract boolean cast(Player p, PlayerDataRPG pd);
    
}
