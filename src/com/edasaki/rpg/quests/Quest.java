package com.edasaki.rpg.quests;

import java.util.ArrayList;
import java.util.HashMap;

import com.edasaki.rpg.SakiRPG;

public class Quest implements Comparable<Quest> {

    public static SakiRPG plugin = null;

    public boolean active = true;

    public HashMap<Integer, QuestVillager> npcs;

    public String identifier;

    public String name;
    public int reqLevel;
    public ArrayList<String> reqQuests;
    public ArrayList<Quest> reqQuestsProcessed = null;
    public ArrayList<String> totalRewards;
    public String desc;

    public int displayOrder = 0;

    public HashMap<Integer, QuestPart> parts;

    public Quest(String name, int reqLevel, ArrayList<String> reqQuests, String desc, ArrayList<String> totalRewards) {
        this.name = name;
        this.reqLevel = reqLevel;
        this.reqQuests = reqQuests;
        this.npcs = new HashMap<Integer, QuestVillager>();
        this.desc = desc;
        this.parts = new HashMap<Integer, QuestPart>();
        this.totalRewards = totalRewards;
    }

    @Override
    public int compareTo(Quest o) {
        if (this.reqLevel != o.reqLevel)
            return this.reqLevel - o.reqLevel;
        else if (this.displayOrder != o.displayOrder)
            return this.displayOrder - o.displayOrder;
        else
            return this.identifier.compareTo(o.identifier);
    }

    @Override
    public String toString() {
        return name;
    }

}
