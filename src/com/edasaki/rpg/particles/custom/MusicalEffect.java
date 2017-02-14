package com.edasaki.rpg.particles.custom;

import org.bukkit.Location;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;

public class MusicalEffect extends Effect {

    /**
     * Radials to spawn next note.
     */
    public double radialsPerStep = Math.PI / 8;

    /**
     * Radius of circle above head
     */
    public float radius = .4f;

    /**
     * Current step. Works as a counter
     */
    protected float step = 0;

    public MusicalEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        iterations = -1;
        period = 1;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        location.add(0, 1.0f, 0);
        location.add(Math.cos(radialsPerStep * step) * radius, 0, Math.sin(radialsPerStep * step) * radius);
        ParticleEffect.NOTE.display(location, visibleRange, 0, 0, 0, .5f, 1);
        step++;
    }

}