package com.edasaki.rpg.particles.custom;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.RandomUtils;

public class RainyDayEffect extends Effect {

    /*
     * Particle of the cloud
     */
    public ParticleEffect cloudParticle = ParticleEffect.CLOUD;
    public Color cloudColor = null;

    /*
     * Particle of the rain/snow
     */
    public ParticleEffect mainParticle = ParticleEffect.DRIP_WATER;

    /*
     * Size of the cloud
     */
    public float cloudSize = .7f;

    /*
     * Radius of the rain/snow
     */
    public float particleRadius = cloudSize * 0.7f;

    public RainyDayEffect(EffectManager manager) {
        super(manager);
        type = EffectType.REPEATING;
        period = 5;
        iterations = -1;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        location.add(0, 1.0f, 0);
        for (int i = 0; i < 30; i++) {
            Vector v = RandomUtils.getRandomCircleVector().multiply(RandomUtils.random.nextDouble() * cloudSize);
            display(cloudParticle, location.add(v), cloudColor, 0, 3);
            location.subtract(v);
        }
        Location l = location.add(0, -0.05, 0);
        for (int i = 0; i < 15; i++) {
            int r = RandomUtils.random.nextInt(3);
            double x = RandomUtils.random.nextDouble() * particleRadius;
            double z = RandomUtils.random.nextDouble() * particleRadius;
            if (r == 1) {
                l.add(x, 0, z);
                display(mainParticle, l);
                l.subtract(x, 0, z);
                l.subtract(x, 0, z);
                display(mainParticle, l);
                l.add(x, 0, z);
            }
        }
    }

}