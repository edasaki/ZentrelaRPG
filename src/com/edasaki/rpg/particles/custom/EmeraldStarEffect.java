package com.edasaki.rpg.particles.custom;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.MathUtils;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.RandomUtils;
import de.slikey.effectlib.util.VectorUtils;

public class EmeraldStarEffect extends Effect {

    /**
     * Particles to create the star
     */
    public ParticleEffect particle = ParticleEffect.VILLAGER_HAPPY;

    /**
     * Particles per spike
     */
    public int particles = 12;

    /**
     * Height of the spikes in blocks
     */
    public float spikeHeight = 1.0f;

    /**
     * Half amount of spikes. Creation is only done half and then mirrored.
     */
    public int spikesHalf = 3;

    /**
     * Inner radius of the star. (0.5)
     */
    public float innerRadius = 0.1f;

    public EmeraldStarEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 25;
        iterations = -1;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        location.add(0, 2, 0);
        float radius = 3 * innerRadius / MathUtils.SQRT_3;
        for (int i = 0; i < spikesHalf * 2; i++) {
            double xRotation = i * Math.PI / spikesHalf;
            for (int x = 0; x < particles; x++) {
                double angle = 2 * Math.PI * x / particles;
                float height = RandomUtils.random.nextFloat() * spikeHeight;
                Vector v = new Vector(Math.cos(angle), 0, Math.sin(angle));
                v.multiply((spikeHeight - height) * radius / spikeHeight);
                v.setY(innerRadius + height);
                VectorUtils.rotateAroundAxisX(v, xRotation);
                location.add(v);
                display(particle, location);
                location.subtract(v);
                VectorUtils.rotateAroundAxisX(v, Math.PI);
                VectorUtils.rotateAroundAxisY(v, Math.PI / 2);
                location.add(v);
                display(particle, location);
                location.subtract(v);
            }
        }
    }

}