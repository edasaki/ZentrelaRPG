package com.edasaki.rpg.regions.areas.actions;

import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.regions.areas.TriggerAreaAction;

public class TriggerAreaActionDelay extends TriggerAreaAction {

    public long delay;

    public TriggerAreaActionDelay(long delay) {
        this.delay = delay;
    }

    @Override
    public void act(Player p, PlayerDataRPG pd) {
    }

}
