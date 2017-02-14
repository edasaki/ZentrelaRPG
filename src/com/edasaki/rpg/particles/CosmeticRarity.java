package com.edasaki.rpg.particles;

import org.bukkit.ChatColor;

public enum CosmeticRarity {
    COMMON(ChatColor.WHITE + ChatColor.ITALIC.toString() + "Common Effect"),
    RARE(ChatColor.AQUA + ChatColor.ITALIC.toString() + "Rare Effect"),
    EPIC(ChatColor.LIGHT_PURPLE + ChatColor.ITALIC.toString() + "Epic Effect"),
    LEGENDARY(ChatColor.GOLD + ChatColor.ITALIC.toString() + "Legendary Effect"),

    ;
    public String display;

    CosmeticRarity(String display) {
        this.display = display;
    }
}