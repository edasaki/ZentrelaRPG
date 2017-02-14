package com.edasaki.rpg.particles.custom;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.RandomUtils;

public class WaterSpoutEffect extends Effect {

    /**
     * Particle of the fountain
     */
    public ParticleEffect particle = ParticleEffect.WATER_SPLASH;

    /**
     * Amount of strands (10)
     */
    public int strands = 4;

    /**
     * Particles per iteration per strand (100)
     */
    public int particlesStrand = 50;

    /**
     * Particles per iteration in the spout
     */
    public int particlesSpout = 20;

    /**
     * Radius of strands in blocks
     */
    public float radius = 2;

    /**
     * Radius of spout as a fraction of the fountain (0.1)
     */
    public float radiusSpout = .1f;

    /**
     * Height of the fountain
     */
    public float height = 1.5f;

    /**
     * Height of the spout in blocks
     */
    public float heightSpout = 2;

    /**
     * Rotation of the fountain on the Y-Axis (Fraction of PI)
     */
    public double rotation = Math.PI / 4;

    public WaterSpoutEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 5;
        iterations = -1;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        for (int i = 1; i <= strands; i++) {
            double angle = 2 * i * Math.PI / strands + rotation;
            for (int j = 1; j <= particlesStrand; j++) {
                float ratio = (float) j / particlesStrand;
                double x, y, z;
                x = Math.cos(angle) * radius * ratio;
                y = Math.sin(Math.PI * j / particlesStrand) * height;
                z = Math.sin(angle) * radius * ratio;
                location.add(x, y, z);
                display(particle, location);
                location.subtract(x, y, z);
            }
        }
        for (int i = 0; i < particlesSpout; i++) {
            Vector v = RandomUtils.getRandomCircleVector().multiply(RandomUtils.random.nextFloat() * radius * radiusSpout);
            v.setY(RandomUtils.random.nextFloat() * heightSpout);
            location.add(v);
            display(particle, location);
            location.subtract(v);
        }
    }

}