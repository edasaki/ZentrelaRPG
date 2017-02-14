package com.edasaki.rpg.npcs.generics;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.edasaki.core.chat.ChatFilter;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.npcs.NPCEntity;
import com.edasaki.rpg.npcs.NPCType;

public class GenericNPC extends NPCEntity {

    public ArrayList<String> lines = new ArrayList<String>();
    private ArrayList<String> processed = null;

    public int leash;

    private transient int last = -1;

    public GenericNPC(int id, String name, NPCType type, Location loc, int leash) {
        super(id, name, type, loc);
        this.leash = leash;
    }

    public GenericNPC(int id, String name, NPCType type, double x, double y, double z, String world, int leash) {
        super(id, name, type, x, y, z, world);
        this.leash = leash;
    }

    @Override
    public void interact(Player p, PlayerDataRPG pd) {
        if (processed == null && lines != null) {
            processed = new ArrayList<String>();
            for (String s : lines) {
                try {
                    s = ChatFilter.getFiltered(s);
                    if (s.charAt(0) == '*' && s.charAt(s.length() - 1) == '*')
                        processed.add(ChatColor.GRAY.toString() + ChatColor.ITALIC + s);
                    else if (s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"')
                        processed.add(ChatColor.GRAY + s.substring(1, s.length() - 1));
                    else
                        processed.add(s);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error reading NPC line: " + s);
                }
            }
        }
        if (processed != null && !processed.isEmpty()) {
            int temp = (int) (Math.random() * processed.size());
            if (temp == last) {
                temp++;
                if (temp >= processed.size())
                    temp = 0;
            }
            last = temp;
            this.say(p, ChatColor.GRAY + processed.get(temp));
        }
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.YELLOW;
    }

}
