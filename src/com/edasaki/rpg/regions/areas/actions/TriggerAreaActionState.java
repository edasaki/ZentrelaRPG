package com.edasaki.rpg.regions.areas.actions;

import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.regions.areas.TriggerAreaAction;

public class TriggerAreaActionState extends TriggerAreaAction {

    private String state = "";

    public TriggerAreaActionState(String state) {
        this.state = state;
    }

    @Override
    public void act(Player p, PlayerDataRPG pd) {
        pd.addState(state);
    }

}
