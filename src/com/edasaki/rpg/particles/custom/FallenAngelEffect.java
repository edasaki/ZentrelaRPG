package com.edasaki.rpg.particles.custom;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.MathUtils;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.VectorUtils;

public class FallenAngelEffect extends Effect {

    /**
     * Particle to draw the image
     */
    public ParticleEffect particle = ParticleEffect.REDSTONE;

    /**
     * Each stepX pixel will be shown. Saves packets for high resolutions.
     */
    public int stepX = 1;

    /**
     * Each stepY pixel will be shown. Saves packets for high resolutions.
     */
    public int stepY = 1;

    /**
     * Scale the image down
     */
    public float size = (float) 1 / 10;

    /**
     * Image as BufferedImage
     */
    protected BufferedImage image = null;

    /**
     * Step counter
     */
    protected int step = 0;

    /**
     * Delay between steps
     */
    protected int delay = 0;

    public FallenAngelEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 5;
        iterations = -1;
    }

    public void loadFile(File file) {
        try {
            image = ImageIO.read(file);
        } catch (Exception ex) {
            ex.printStackTrace();
            image = null;
        }
    }

    @Override
    public void onRun() {
        if (image == null) {
            cancel();
            return;
        }
        Location location = getLocation();
        Vector dir = location.getDirection().normalize();
        dir.setY(0);
        location.subtract(dir.multiply(0.4));
        {
            Location loc_right = location.clone();
            Vector shift = dir.getCrossProduct(new Vector(0, 1, 0));
            shift = shift.normalize();
            loc_right.add(shift.multiply(2));
            for (int y = 0; y < image.getHeight(); y += stepY) {
                for (int x = 0; x < image.getWidth(); x += stepX) {
                    Vector v = new Vector((float) image.getWidth() / 2 - x, (float) image.getHeight() / 2 - y, 0).multiply(size);
                    VectorUtils.rotateAroundAxisY(v, -loc_right.getYaw() * MathUtils.degreesToRadians);
                    int r = (new Color(image.getRGB(x, y))).getRed();
                    int g = (new Color(image.getRGB(x, y))).getGreen();
                    int b = (new Color(image.getRGB(x, y))).getBlue();
                    loc_right.add(v);
                    if ((r == 0 && g == 0 && b == 0)) {
                        display(particle, loc_right, org.bukkit.Color.WHITE);
                    }
                    loc_right.subtract(v);
                }
            }
        }
        {
            Location loc_left = location.clone();
            Vector shift = dir.getCrossProduct(new Vector(0, 1, 0));
            shift = shift.normalize();
            loc_left.add(shift.multiply(-2));
            for (int y = 0; y < image.getHeight(); y += stepY) {
                for (int x = 0; x < image.getWidth(); x += stepX) {
                    Vector v = new Vector((float) image.getWidth() / 2 - x, (float) image.getHeight() / 2 - y, 0).multiply(size);
                    VectorUtils.rotateAroundAxisY(v, -loc_left.getYaw() * MathUtils.degreesToRadians);
                    int tempX = image.getWidth() - 1 - x;
                    Color clr = new Color(image.getRGB(tempX, y));
                    int r = clr.getRed();
                    int g = clr.getGreen();
                    int b = clr.getBlue();
                    loc_left.add(v);
                    if ((r == 0 && g == 0 && b == 0)) {
                        display(particle, loc_left, org.bukkit.Color.BLACK);
                    }
                    loc_left.subtract(v);
                }
            }
        }
    }

}