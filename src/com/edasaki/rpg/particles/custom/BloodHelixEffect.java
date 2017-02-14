package com.edasaki.rpg.particles.custom;

import org.bukkit.Location;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;

public class BloodHelixEffect extends Effect {
    private static final ParticleEffect particle = ParticleEffect.REDSTONE;
    public static int strands = 2;
    public static int particles = 120;
    public static float radius = 1.3F;
    public static float curve = 2.0F;
    public static double rotation = Math.PI / 4;

    private static final double START_Y = 1.5;

    private int step = 0;
    double currY = START_Y; // start height
    private static int perTick = 4;

    public BloodHelixEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 2;
        iterations = -1;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        for (int j = step; j <= step + perTick; j++) {
            currY -= 0.025d;
            for (int i = 1; i <= strands; i++) {
                float ratio = ((float) j) / particles;
                double angle = curve * ratio * 2.0f * Math.PI / strands + 2.0f * Math.PI * i / strands + rotation;
                double x = Math.cos(angle) * ratio * radius;
                double z = Math.sin(angle) * ratio * radius;
                double y = currY;
                location.add(x, y, z);
                display(particle, location);
                location.subtract(x, y, z);
            }
        }
        if (step > particles - perTick) {
            step = 0;
            currY = START_Y;
        } else {
            step += perTick;
        }
    }

}