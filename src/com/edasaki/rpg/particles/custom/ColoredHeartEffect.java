package com.edasaki.rpg.particles.custom;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.MathUtils;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.VectorUtils;

/**
 * Creates a 2D Heart in 3D space. Thanks to the author for sharing it!
 *
 * @author <a href="http://forums.bukkit.org/members/qukie.90952701/">Qukie</a>
 */
public class ColoredHeartEffect extends Effect {

    /**
     * ParticleType of spawned particle
     */
    public ParticleEffect particle = ParticleEffect.REDSTONE;

    /**
     * Heart-particles per interation (100)
     */
    public int particles = 50;

    /**
     * Strech/Compress factor along the x or y axis (1, 1)
     */
    public double yFactor = 1, zFactor = 1;

    /**
     * Defines the size of the that inner sting (0.8) \/
     */
    public double factorInnerSpike = 0.8;

    /**
     * Compresses the heart along the y axis. (2)
     */
    public double compressYFactorTotal = 2.5;

    /**
     * Compilation of the heart. (2)
     */
    public float compilation = 2F;
    
    private org.bukkit.Color clr;

    public ColoredHeartEffect(EffectManager effectManager, org.bukkit.Color clr) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 5;
        iterations = -1;
        this.clr = clr;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        location.add(0, 0.5, 0);
        for (int i = 0; i < particles; i++) {
            float alpha = ((MathUtils.PI / compilation) / particles) * i;
            double phi = Math.pow(Math.abs(MathUtils.sin(2 * compilation * alpha)) + factorInnerSpike * Math.abs(MathUtils.sin(compilation * alpha)), 1 / compressYFactorTotal);

            Vector vector = new Vector();
            vector.setY(phi * (MathUtils.sin(alpha) + MathUtils.cos(alpha)) * yFactor);
            vector.setZ(phi * (MathUtils.cos(alpha) - MathUtils.sin(alpha)) * zFactor);

            VectorUtils.rotateVector(vector, 0, -location.getYaw() * MathUtils.degreesToRadians + (Math.PI / 2f), 0);
            display(particle, location.add(vector), this.clr);
            location.subtract(vector);
        }
    }

}