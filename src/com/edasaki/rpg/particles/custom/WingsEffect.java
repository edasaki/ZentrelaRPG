package com.edasaki.rpg.particles.custom;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.edasaki.rpg.particles.ParticleManager;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.MathUtils;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.VectorUtils;

public class WingsEffect extends Effect {

    private static final Vector VERTICAL_VECTOR = new Vector(0, 1, 0);

    public ParticleEffect particle = ParticleEffect.REDSTONE;
    public float size = (float) 1 / 12;
    protected BufferedImage image = null;

    private float yAdjust;
    private float xzAdjust;
    private boolean leftWing;
    private boolean rightWing;
    private int step;

    private org.bukkit.Color leftColor, rightColor;

    private org.bukkit.Color[] leftColorArr = null, rightColorArr = null;

    public WingsEffect(EffectManager effectManager, float scaling, float yAdjust, float xzAdjust, boolean leftWing, boolean rightWing, org.bukkit.Color leftColor, org.bukkit.Color rightColor, int step) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 5;
        iterations = -1;
        this.size = scaling;
        this.yAdjust = yAdjust;
        this.xzAdjust = xzAdjust;
        this.leftWing = leftWing;
        this.rightWing = rightWing;
        this.leftColor = leftColor;
        this.rightColor = rightColor;
        this.step = step;
    }

    public WingsEffect(EffectManager effectManager, float scaling, float yAdjust, float xzAdjust, boolean leftWing, boolean rightWing, org.bukkit.Color[] leftColorArr, org.bukkit.Color[] rightColorArr, int step) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 5;
        iterations = -1;
        this.size = scaling;
        this.yAdjust = yAdjust;
        this.xzAdjust = xzAdjust;
        this.leftWing = leftWing;
        this.rightWing = rightWing;
        this.leftColorArr = leftColorArr;
        this.rightColorArr = rightColorArr;
        this.step = step;
    }

    public void loadFile(String name) {
        File localFile = new File(ParticleManager.plugin.getDataFolder() + "/effects/" + name);
        localFile.getParentFile().mkdirs();
        if (!localFile.exists()) {
            try {
                localFile.createNewFile();
                Object localObject = ParticleManager.plugin.getResource(name);
                byte[] arrayOfByte = new byte[((InputStream) localObject).available()];
                ((InputStream) localObject).read(arrayOfByte);
                ((InputStream) localObject).close();
                FileOutputStream localFileOutputStream = new FileOutputStream(localFile);
                localFileOutputStream.write(arrayOfByte);
                localFileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loadFile(localFile);
    }
    
    public void setParticle(ParticleEffect particle) { 
        this.particle = particle;
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
        location.add(0, yAdjust, 0);
        Vector dir = location.getDirection().normalize();
        dir.setY(0);
        location.subtract(dir.multiply(0.4));

        if (leftWing) {
            Location loc_left = location.clone();
            Vector shift = dir.getCrossProduct(VERTICAL_VECTOR);
            shift = shift.normalize();
            loc_left.add(shift.multiply(-xzAdjust));
            for (int y = 0; y < image.getHeight(); y += step) {
                for (int x = 0; x < image.getWidth(); x += step) {
                    Vector v = new Vector((float) image.getWidth() / 2 - x, (float) image.getHeight() / 2 - y, 0).multiply(size);
                    VectorUtils.rotateAroundAxisY(v, -loc_left.getYaw() * MathUtils.degreesToRadians);
                    int tempX = image.getWidth() - 1 - x;
                    Color clr = new Color(image.getRGB(tempX, y));
                    loc_left.add(v);
                    if (clr.getRed() == 0 && clr.getGreen() == 0 && clr.getBlue() == 0) {
                        if (leftColorArr != null)
                            display(particle, loc_left, leftColorArr[(int) (Math.random() * leftColorArr.length)]);
                        else
                            display(particle, loc_left, leftColor);
                    }
                    loc_left.subtract(v);
                }
            }
        }

        if (rightWing) {
            Location loc_right = location.clone();
            Vector shift = dir.getCrossProduct(VERTICAL_VECTOR);
            shift = shift.normalize();
            loc_right.add(shift.multiply(xzAdjust));
            for (int y = 0; y < image.getHeight(); y += step) {
                for (int x = 0; x < image.getWidth(); x += step) {
                    Vector v = new Vector((float) image.getWidth() / 2 - x, (float) image.getHeight() / 2 - y, 0).multiply(size);
                    VectorUtils.rotateAroundAxisY(v, -loc_right.getYaw() * MathUtils.degreesToRadians);
                    Color clr = new Color(image.getRGB(x, y));
                    loc_right.add(v);
                    if (clr.getRed() == 0 && clr.getGreen() == 0 && clr.getBlue() == 0) {
                        if (rightColorArr != null)
                            display(particle, loc_right, rightColorArr[(int) (Math.random() * rightColorArr.length)]);
                        else
                            display(particle, loc_right, rightColor);
                    }
                    loc_right.subtract(v);
                }
            }
        }
    }

}