package com.edasaki.rpg.guilds;

import java.util.HashMap;
import java.util.Map.Entry;

public class Guild {

    public String name;
    public String tag;
    public String leader;
    public String leaderUUID;
    public HashMap<String, GuildMember> uuidToMember = new HashMap<String, GuildMember>();

    private long lastAPUpdate = 0;
    private int lastAPValue = 0;
    
    public int getActivityPoints() {
        if(System.currentTimeMillis() - lastAPUpdate < 5000)
            return lastAPValue;
        lastAPUpdate = System.currentTimeMillis();
        int sum = 0;
        for (Entry<String, GuildMember> e : uuidToMember.entrySet()) {
            GuildMember gm = e.getValue();
            sum += gm.activityPoints;
        }
        return (lastAPValue = sum);
        
    }
    public Guild() {

    }

}
