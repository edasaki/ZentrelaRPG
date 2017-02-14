package com.edasaki.rpg.shield;

import java.util.List;

import com.edasaki.core.SakiCore;
import com.edasaki.core.shield.SakiShield;
import com.edasaki.core.shield.ShieldCheck;
import com.edasaki.rpg.SakiRPG;

public class SakiShieldRPG extends SakiShield {

    @Override
    public SakiCore getPlugin() {
        return SakiRPG.plugin;
    }

    @Override
    public List<ShieldCheck> getChecks() {
        return null;
    }

    @Override
    public void halt() {
        //        getPlugin()
    }

}
