package com.edasaki.rpg.mobs;

import java.util.ArrayList;

import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RTicks;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.mobs.spells.MobSpell;

public class MobSpellTicker {

    private boolean started = false;

    public void start() {
        for (MobSpellWrapper wrapper : this.spells) {
            RScheduler.schedule(SakiRPG.plugin, wrapper, RTicks.fromMS(wrapper.spell.getCastDelay()));
        }
    }

    public void addSpell(MobSpell sp, MobData md) {
        if (started) {
            try {
                throw new Exception("Tried addSpell after spellticker started.");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        spells.add(new MobSpellWrapper(sp, md));
    }

    public void cleanup() {
        for (MobSpellWrapper msw : spells) {
            msw.stopped = true;
        }
        this.spells.clear();
        this.spells = null;
    }

    private ArrayList<MobSpellWrapper> spells = new ArrayList<MobSpellWrapper>();

    private class MobSpellWrapper implements Runnable {
        private boolean stopped = false;
        private MobSpell spell;
        private MobData md;

        public MobSpellWrapper(MobSpell ms, MobData md) {
            this.spell = ms;
            this.md = md;
        }

        @Override
        public void run() {
            if (stopped)
                return;
            if (md != null && md.entity != null && md.entity.isValid()) {
                if (md.ai != null && md.ai.target != null && md.ai.target.isValid() && md.ai.target.isOnline() && md.ai.target.getWorld().equals(md.entity.getWorld())) {
                    spell.castSpell(md.entity, md, md.ai.target);
                }
                int convert = RTicks.fromMS(spell.getCastDelay()) + (int) (Math.random() * 2);
                RScheduler.schedule(SakiRPG.plugin, this, convert);
            }
        }

    }

}