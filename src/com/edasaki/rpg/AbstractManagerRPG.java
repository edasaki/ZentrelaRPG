package com.edasaki.rpg;

import com.edasaki.core.AbstractManager;

public abstract class AbstractManagerRPG extends AbstractManager {

    public static SakiRPG plugin;

    public AbstractManagerRPG(SakiRPG pl) {
        super(pl);
        plugin = pl;
    }

}
