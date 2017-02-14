package com.edasaki.rpg.regions.areas;

import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.regions.areas.actions.TriggerAreaActionDelay;
import com.edasaki.rpg.regions.areas.actions.TriggerAreaActionMessage;
import com.edasaki.rpg.regions.areas.actions.TriggerAreaActionPing;
import com.edasaki.rpg.regions.areas.actions.TriggerAreaActionState;

public abstract class TriggerAreaAction {
    public static TriggerAreaAction parse(String s) {
        String prefix = s.split(" ")[0];
        if (s.indexOf(' ') > -1)
            s = s.substring(s.indexOf(' ')).trim();
        switch (prefix.toLowerCase()) {
            case "message":
                return new TriggerAreaActionMessage(s);
            case "state":
                return new TriggerAreaActionState(s);
            case "ping":
                return new TriggerAreaActionPing();
            case "delay":
                return new TriggerAreaActionDelay(Long.parseLong(s));

        }
        return null;

    }

    public abstract void act(Player p, PlayerDataRPG pd);

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
