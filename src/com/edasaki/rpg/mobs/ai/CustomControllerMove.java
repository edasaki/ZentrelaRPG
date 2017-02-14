package com.edasaki.rpg.mobs.ai;

import net.minecraft.server.v1_10_R1.ControllerMove;
import net.minecraft.server.v1_10_R1.EntityInsentient;
import net.minecraft.server.v1_10_R1.GenericAttributes;
import net.minecraft.server.v1_10_R1.MathHelper;
import net.minecraft.server.v1_10_R1.NavigationAbstract;
import net.minecraft.server.v1_10_R1.PathType;
import net.minecraft.server.v1_10_R1.PathfinderAbstract;

public class CustomControllerMove extends ControllerMove {
    protected final EntityInsentient a;
    protected double b;
    protected double c;
    protected double d;
    protected double e;
    protected float f;
    protected float g;
    protected Operation h = Operation.WAIT;

    public CustomControllerMove(EntityInsentient paramEntityInsentient) {
        super(paramEntityInsentient);
        this.a = paramEntityInsentient;
    }

    public boolean a() {
        return this.h == Operation.MOVE_TO;
    }

    public double b() {
        return this.e;
    }

    public void a(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
        this.b = paramDouble1;
        this.c = paramDouble2;
        this.d = paramDouble3;
        this.e = paramDouble4;
        this.h = Operation.MOVE_TO;
    }

    public void a(float paramFloat1, float paramFloat2) {
        this.h = Operation.STRAFE;
        this.f = paramFloat1;
        this.g = paramFloat2;
        this.e = 0.25D;
    }

    public void a(CustomControllerMove paramControllerMove) {
        this.h = paramControllerMove.h;
        this.b = paramControllerMove.b;
        this.c = paramControllerMove.c;
        this.d = paramControllerMove.d;
        this.e = Math.max(paramControllerMove.e, 1.0D);
        this.f = paramControllerMove.f;
        this.g = paramControllerMove.g;
    }

    public void c() {
        float f9;
        if (this.h == Operation.STRAFE) {
            float f1 = (float) this.a.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue();
            float f2 = (float) this.e * f1;

            float f3 = this.f;
            float f4 = this.g;
            float f5 = MathHelper.c(f3 * f3 + f4 * f4);
            if (f5 < 1.0F) {
                f5 = 1.0F;
            }
            f5 = f2 / f5;
            f3 *= f5;
            f4 *= f5;

            float f6 = MathHelper.sin(this.a.yaw * 0.017453292F);
            float f7 = MathHelper.cos(this.a.yaw * 0.017453292F);
            float f8 = f3 * f7 - f4 * f6;
            f9 = f4 * f7 + f3 * f6;

            NavigationAbstract localNavigationAbstract = this.a.getNavigation();
            if (localNavigationAbstract != null) {
                PathfinderAbstract localPathfinderAbstract = localNavigationAbstract.q();
                if ((localPathfinderAbstract != null) && (localPathfinderAbstract.a(this.a.world, MathHelper.floor(this.a.locX + f8), MathHelper.floor(this.a.locY), MathHelper.floor(this.a.locZ + f9)) != PathType.WALKABLE)) {
                    this.f = 1.0F;
                    this.g = 0.0F;
                    f2 = f1;
                }
            }
            this.a.l(f2);
            this.a.o(this.f);
            this.a.p(this.g);

            this.h = Operation.WAIT;
        } else if (this.h == Operation.MOVE_TO) {
            this.h = Operation.WAIT;

            double d1 = this.b - this.a.locX;
            double d2 = this.d - this.a.locZ;
            double d3 = this.c - this.a.locY;
            double d4 = d1 * d1 + d3 * d3 + d2 * d2;
            if (d4 < 2.500000277905201E-7D) {
                this.a.o(0.0F);
                return;
            }
            f9 = (float) (MathHelper.b(d2, d1) * 57.2957763671875D) - 90.0F;

            this.a.yaw = a(this.a.yaw, f9, 90.0F);
            this.a.l((float) (this.e * this.a.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue()));
            if ((d3 > this.a.P) && (d1 * d1 + d2 * d2 < Math.max(1.0F, this.a.width))) {
                this.a.getControllerJump().a();
            }
        } else {
            this.a.o(0.0F);
        }
    }

    protected float a(float paramFloat1, float paramFloat2, float paramFloat3) {
        float f1 = MathHelper.g(paramFloat2 - paramFloat1);
        if (f1 > paramFloat3) {
            f1 = paramFloat3;
        }
        if (f1 < -paramFloat3) {
            f1 = -paramFloat3;
        }
        float f2 = paramFloat1 + f1;
        if (f2 < 0.0F) {
            f2 += 360.0F;
        } else if (f2 > 360.0F) {
            f2 -= 360.0F;
        }
        return f2;
    }

    public double d() {
        return this.b;
    }

    public double e() {
        return this.c;
    }

    public double f() {
        return this.d;
    }

}
