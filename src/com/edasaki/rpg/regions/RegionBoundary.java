package com.edasaki.rpg.regions;

import java.util.Arrays;
import java.util.Collection;

public class RegionBoundary {
    private final RegionPoint[] points; // Points making up the boundary

    private long ymin = Long.MAX_VALUE, ymax = Long.MIN_VALUE;

    public static RegionBoundary create(Collection<RegionPoint> points) {
        return create(points.toArray(new RegionPoint[points.size()]));
    }

    public static RegionBoundary create(RegionPoint... points) {
        if (points.length < 2) {
            try {
                throw new Exception("Invalid region boundary (not enough points)");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        } else if (points.length == 2) {
            RegionPoint a = points[0];
            RegionPoint b = points[1];
            long x1 = a.x;
            long y1 = a.y;
            long z1 = a.z;
            long x2 = b.x;
            long y2 = b.y;
            long z2 = b.z;
            long temp = x1;
            if (x2 < x1) {
                x1 = x2;
                x2 = temp;
            }
            temp = y1;
            if (y2 < y1) {
                y1 = y2;
                y2 = temp;
            }
            temp = z1;
            if (z2 < z1) {
                z1 = z2;
                z2 = temp;
            }
            /*
             * x2 > x1, z2 > z1
             * 
             * x1, z1
             *         x2, z2
             */
            a = new RegionPoint(x1, y1, z1);
            b = new RegionPoint(x1, y1, z2);
            RegionPoint c = new RegionPoint(x2, y2, z2);
            RegionPoint d = new RegionPoint(x2, y2, z1);
            return new RegionBoundary(new RegionPoint[] { a, b, c, d, a });
        } else {
            byte delta = points[0].equals(points[points.length - 1]) ? (byte) 1 : (byte) 0;
            RegionPoint[] arr = new RegionPoint[points.length + delta];
            for (int k = 0; k < points.length; k++)
                arr[k] = points[k];
            if (delta == 1)
                arr[points.length] = arr[0];
            return new RegionBoundary(arr);
        }
    }

    private RegionBoundary(RegionPoint[] arr) {
        points = arr;
        for (RegionPoint p : points) {
            if (p.y < ymin)
                ymin = p.y;
            if (p.y > ymax)
                ymax = p.y;
        }
    }

    public boolean contains(RegionPoint p) {
        return contains(p.x, p.y, p.z);
    }

    public double area() {
        double sum = 0.0;
        for (int i = 0; i < points.length - 1; i++) {
            sum = sum + (points[i].x * points[i + 1].z) - (points[i].z * points[i + 1].x);
        }
        sum = Math.abs(sum);
        sum *= 0.5;
        sum *= (ymax - ymin);
        if (sum < 0) {
            return Double.MAX_VALUE - 1;
        }
        return sum;
    }

    public boolean contains(long x, long y, long z) {
        int i;
        int j;
        boolean result = false;
        for (i = 0, j = points.length - 1; i < points.length; j = i++) {
            if ((points[i].z > z) != (points[j].z > z) && (x < (points[j].x - points[i].x) * (z - points[i].z) / (points[j].z - points[i].z) + points[i].x)) {
                result = !result;
            }
        }
        if (y < ymin || y > ymax)
            return false;
        return result;
    }

    @Override
    public String toString() {
        return Arrays.toString(points);
    }
}
