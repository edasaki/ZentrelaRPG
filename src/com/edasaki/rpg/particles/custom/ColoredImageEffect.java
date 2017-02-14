package com.edasaki.rpg.particles.custom;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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

public class ColoredImageEffect extends Effect {

    public ParticleEffect particle = ParticleEffect.REDSTONE;
    public double density = 0.2;
    public float size = (float) 1 / 20;
    public boolean enableRotation = true;
    public Plane plane = Plane.Y;
    public double angularVelocityX = 0;//Math.PI / 200;
    public double angularVelocityY = Math.PI / 170;
    public double angularVelocityZ = 0;

    protected BufferedImage image = null;
    protected int currStep = 0;
    protected int currRotationStep = 0;

    private float yaw;
    private boolean lockedYaw = false;

    private ArrayList<Color> priority = new ArrayList<Color>();

    public ColoredImageEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 5;
        iterations = -1;
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

    public void loadFile(File file) {
        try {
            image = ImageIO.read(file);
        } catch (Exception ex) {
            ex.printStackTrace();
            image = null;
        }
    }

    public void registerPriority(Color clr) {
        priority.add(clr);
    }

    @Override
    public void onRun() {
        if (image == null) {
            cancel();
            return;
        }
        Location location = getLocation();
        location.add(0, 2, 0);
        if (!lockedYaw) {
            yaw = location.getYaw();
            lockedYaw = true;
        }

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                Color clr = (new Color(rgb));
                boolean prio = false;
                for (Color c : priority) {
                    if (c.getRed() == clr.getRed() && c.getGreen() == clr.getGreen() && c.getBlue() == clr.getBlue()) {
                        prio = true;
                        break;
                    }
                }
                if (!prio & Math.random() > density)
                    continue;
                Vector v = new Vector((float) image.getWidth() / 2 - x, (float) image.getHeight() / 2 - y, 0).multiply(size);
                VectorUtils.rotateAroundAxisY(v, -yaw * MathUtils.degreesToRadians);
                if (enableRotation) {
                    double rotX = 0;
                    double rotY = 0;
                    double rotZ = 0;
                    switch (plane) {
                        case X:
                            rotX = angularVelocityX * currRotationStep;
                            break;
                        case Y:
                            rotY = angularVelocityY * currRotationStep;
                            break;
                        case Z:
                            rotZ = angularVelocityZ * currRotationStep;
                            break;
                        case XY:
                            rotX = angularVelocityX * currRotationStep;
                            rotY = angularVelocityY * currRotationStep;
                            break;
                        case XZ:
                            rotX = angularVelocityX * currRotationStep;
                            rotZ = angularVelocityZ * currRotationStep;
                            break;
                        case XYZ:
                            rotX = angularVelocityX * currRotationStep;
                            rotY = angularVelocityY * currRotationStep;
                            rotZ = angularVelocityZ * currRotationStep;
                            break;
                        case YZ:
                            rotY = angularVelocityY * currRotationStep;
                            rotZ = angularVelocityZ * currStep;
                            break;
                    }
                    VectorUtils.rotateVector(v, rotX, rotY, rotZ);
                }
                int r = clr.getRed();
                int g = clr.getGreen();
                int b = clr.getBlue();
                location.add(v);
                if (!(r == 123 && g == 123 && b == 123)) {
                    display(particle, location, org.bukkit.Color.fromRGB(r, g, b));
                }
                location.subtract(v);
            }
        }
        currRotationStep++;
    }

    public enum Plane {
        X, Y, Z, XY, XZ, XYZ, YZ;
    }

}