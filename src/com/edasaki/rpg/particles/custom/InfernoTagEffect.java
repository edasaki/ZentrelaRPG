package com.edasaki.rpg.particles.custom;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.MathUtils;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.StringParser;
import de.slikey.effectlib.util.VectorUtils;

public class InfernoTagEffect extends Effect {

    public ParticleEffect particle = ParticleEffect.FLAME;

    /**
     * Each stepX pixel will be shown. Saves packets for lower fontsizes.
     */
    public int stepX = 1;
    public int stepY = 1;

    /**
     * Scale the font down
     */
    public float size = (float) 1 / 5;

    /**
     * Font to create the Text
     */
    public Font font;
    protected BufferedImage image = null;

    private String text;
    
    private boolean lockedYaw = false;
    private float yaw;
    private Vector dir;

    public InfernoTagEffect(EffectManager effectManager, String text) {
        super(effectManager);
        this.text = text;
        this.font = new Font("Tahoma", Font.PLAIN, 9);
        type = EffectType.REPEATING;
        period = 10;
        iterations = -1;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    @Override
    public void onRun() {
        if (font == null) {
            cancel();
            return;
        }
        Location location = getLocation();
        location.add(0, 1.2, 0);
        if(!lockedYaw) {
            yaw = -location.getYaw();
            dir = location.getDirection();
            dir = dir.normalize().setY(0);
            lockedYaw = true;
        }
        location.add(dir);
        try {
            if (image == null) {
                image = StringParser.stringToBufferedImage(font, text);
            }
            for (int y = 0; y < image.getHeight(); y += stepY) {
                for (int x = 0; x < image.getWidth(); x += stepX) {
                    int clr = image.getRGB(image.getWidth() - 1 - x, y);
                    if (clr != Color.black.getRGB())
                        continue;
                    Vector v = new Vector((float) image.getWidth() / 2 - x, (float) image.getHeight() / 2 - y, 0).multiply(size);
                    VectorUtils.rotateAroundAxisY(v, yaw * MathUtils.degreesToRadians);
                    display(particle, location.add(v));
                    location.subtract(v);
                }
            }
        } catch (Exception ex) {
            cancel(true);
        }
    }
}