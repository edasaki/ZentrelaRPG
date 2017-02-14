package com.edasaki.rpg.particles.custom.misc;

import org.bukkit.Color;
import org.bukkit.Location;

import com.edasaki.core.utils.RMath;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;

public class FlightLaunchEffect extends Effect {

    private Location loc;

    public FlightLaunchEffect(EffectManager effectManager, Location loc) {
        super(effectManager);
        type = EffectType.INSTANT;
        this.loc = loc;
        setLocation(loc);
    }

    @Override
    public void onRun() {
        double x, y, z;
        for (int i = 0; i < 50; i++) {
            int count = 20;
            do {
                count--;
                x = RMath.randDouble(0, 2);
                y = RMath.randDouble(0, 2);
                z = RMath.randDouble(0, 2);
            } while (count >= 0 && x * x + y * y + z * z > 4);
            x -= 1;
            z -= 1;
            loc.add(x, y, z);
            display(ParticleEffect.REDSTONE, loc, Color.WHITE);
            loc.subtract(x, y, z);
        }
    }

}