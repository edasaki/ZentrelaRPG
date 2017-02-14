package com.edasaki.rpg.particles;

import org.bukkit.Color;
import org.bukkit.entity.Player;

import com.edasaki.rpg.particles.custom.AtomicEffect;
import com.edasaki.rpg.particles.custom.BloodHelixEffect;
import com.edasaki.rpg.particles.custom.ColoredHeartEffect;
import com.edasaki.rpg.particles.custom.ColoredImageEffect;
import com.edasaki.rpg.particles.custom.EmeraldStarEffect;
import com.edasaki.rpg.particles.custom.FlameBreathEffect;
import com.edasaki.rpg.particles.custom.InfernoTagEffect;
import com.edasaki.rpg.particles.custom.MusicalEffect;
import com.edasaki.rpg.particles.custom.RainbowHeartEffect;
import com.edasaki.rpg.particles.custom.RainyDayEffect;
import com.edasaki.rpg.particles.custom.SparkRingEffect;
import com.edasaki.rpg.particles.custom.SwirlyEffect;
import com.edasaki.rpg.particles.custom.WaterSpoutEffect;
import com.edasaki.rpg.particles.custom.WingsEffect;
import com.edasaki.rpg.particles.custom.misc.CustomWarpEffect;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;

public class EffectFactory {

    public static EffectManager em() {
        return ParticleManager.em;
    }

    public static CustomWarpEffect getWarpEffect(Player p, int time) {
        return new CustomWarpEffect(em(), p.getLocation(), time);
    }

    public static Effect getEffect(EffectName name, Player p) {
        switch (name) {
            case BLOOD_HELIX:
                return getBloodHelixEffect();
            case EMERALD_STAR:
                return getEmeraldStarEffect();
            case RAINBOW_HEART:
                return getRainbowHeartEffect();
            case INFERNO_TAG:
                return getInfernoTagEffect(p.getName());
            case WATER_SPOUT:
                return getWaterSpoutEffect();
            case SPARK_RING:
                return getSparkRingEffect();
            case FLAME_BREATH:
                return getFlameBreathEffect();
            case ATOMIC:
                return getAtomicEffect();
            case FALLEN_ANGEL:
                return getFallenAngelEffect();
            case FAIRY_WINGS:
                return getFairyWingsEffect();
            case ANGEL_WINGS:
                return getAngelWingsEffect();
            case PETITE_ANGEL_WINGS:
                return getPetiteAngelWingsEffect();
            case MUSICAL:
                return getMusicalEffect();
            case SWIRLY:
                return getSwirlyEffect();
            case PINK_HEART:
                return getPinkHeartEffect();
            case RED_HEART:
                return getRedHeartEffect();
            case BLACK_HEART:
                return getBlackHeartEffect();
            case RAINY_DAY:
                return getRainyDayEffect();
            case ZELLUMINATI:
                return getZelluminatiEffect();

        }
        return null;
    }

    private static Effect getBloodHelixEffect() {
        return new BloodHelixEffect(em());
    }

    private static Effect getEmeraldStarEffect() {
        return new EmeraldStarEffect(em());
    }

    private static Effect getRainbowHeartEffect() {
        return new RainbowHeartEffect(em());
    }

    private static Effect getInfernoTagEffect(String s) {
        return new InfernoTagEffect(em(), s);
    }

    private static Effect getWaterSpoutEffect() {
        return new WaterSpoutEffect(em());
    }

    private static Effect getSparkRingEffect() {
        return new SparkRingEffect(em());
    }

    private static Effect getFlameBreathEffect() {
        return new FlameBreathEffect(em());
    }

    private static Effect getAtomicEffect() {
        return new AtomicEffect(em());
    }

    private static Effect getFallenAngelEffect() {
        WingsEffect eff = new WingsEffect(em(), 1f / 10f, 0, 2f, true, true, Color.BLACK, Color.WHITE, 1);
        eff.loadFile("wings_1.png");
        return eff;
    }

    private static Effect getFairyWingsEffect() {
        WingsEffect eff = new WingsEffect(em(), 1f / 12f, 0, 1.2f, true, true, Color.LIME, Color.LIME, 1);
        eff.loadFile("wings_2.png");
        return eff;
    }

    private static Effect getAngelWingsEffect() {
        WingsEffect eff = new WingsEffect(em(), 1f / 30f, 0, 0.66f, true, true, Color.WHITE, Color.WHITE, 2);
        eff.loadFile("wings_3.png");
        return eff;
    }

    private static Effect getPetiteAngelWingsEffect() {
        WingsEffect eff = new WingsEffect(em(), 1f / 60f, 0, 0.35f, true, true, Color.WHITE, Color.WHITE, 2);
        eff.loadFile("wings_3.png");
        return eff;
    }

    private static Effect getMusicalEffect() {
        return new MusicalEffect(em());
    }

    private static Effect getSwirlyEffect() {
        return new SwirlyEffect(em());
    }

    private static Effect getPinkHeartEffect() {
        return new ColoredHeartEffect(em(), Color.FUCHSIA);
    }

    private static Effect getRedHeartEffect() {
        return new ColoredHeartEffect(em(), Color.RED);
    }

    private static Effect getBlackHeartEffect() {
        return new ColoredHeartEffect(em(), Color.BLACK);
    }

    private static Effect getRainyDayEffect() {
        return new RainyDayEffect(em());
    }

    private static Effect getZelluminatiEffect() {
        ColoredImageEffect eff = new ColoredImageEffect(em());
        eff.registerPriority(new java.awt.Color(0, 0, 0));
        eff.loadFile("image_1.png");
        return eff;
    }

}
