package com.edasaki.rpg.regions.areas.actions;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.edasaki.core.utils.RSound;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.regions.areas.TriggerAreaAction;

public class TriggerAreaActionPing extends TriggerAreaAction {

    @Override
    public void act(Player p, PlayerDataRPG pd) {
        RSound.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
    }

}
