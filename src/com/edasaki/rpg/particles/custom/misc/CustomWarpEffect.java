package com.edasaki.rpg.particles.custom.misc;

import org.bukkit.Location;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;

public class CustomWarpEffect extends Effect {

    /**
     * Radius of the spawned circles
     */
    public float radius = 0.7f;

    /**
     * Particles per circle
     */
    public int particles = 35;

    /**
     * Particle to display
     */
    public ParticleEffect particle = ParticleEffect.PORTAL;

    /**
     * Interval of the circles
     */
    public float grow = 0.02f;

    /**
     * Internal counter
     */
    protected int step = 0;

    private Location loc;

    public CustomWarpEffect(EffectManager effectManager, Location loc, int time) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 1;
        iterations = time * 20 / period;
        grow = 2.2f / iterations;
        this.loc = loc.add(0, -0.65, 0);
    }

    @Override
    public void onRun() {
        Location location = loc;
        double x, y, z;
        y = step * grow;
        location.add(0, y, 0);
        for (int i = 0; i < particles; i++) {
            double angle = (double) 2 * Math.PI * i / particles;
            x = Math.cos(angle) * radius;
            z = Math.sin(angle) * radius;
            location.add(x, 0, z);
            display(particle, location);
            location.subtract(x, 0, z);
        }
        location.subtract(0, y, 0);
        step++;
    }

}