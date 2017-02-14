package com.edasaki.rpg.regions;

public class RegionPoint {

    public long x, y, z;

    public RegionPoint(long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof RegionPoint))
            return false;
        if (this == other)
            return true;
        RegionPoint rp = (RegionPoint) other;
        return this.x == rp.x && this.y == rp.y && this.z == rp.z;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }

}
