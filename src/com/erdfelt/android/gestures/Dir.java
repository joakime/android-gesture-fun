package com.erdfelt.android.gestures;

public enum Dir {
    NONE, NW, N, NE, E, SE, S, SW, W;

    private static final Dir zones[] = new Dir[] { Dir.E, Dir.NE, Dir.N, Dir.NW, Dir.W, Dir.SW, Dir.S, Dir.SE };

    public static Dir asDir(double deltaX, double deltaY) {
        double degrees = calcDegrees(deltaX, deltaY);
        int n = (int) (degrees + (45 / 2));
        int z = (n / 45);
        if (z == 8) {
            z = 0;
        }
        return zones[z];
    }

    public static double calcDegrees(double x, double y) {
        double radians = Math.atan2(y, x);
        double degrees = (radians * (180 / Math.PI));
        if (degrees < 0.0) {
            degrees = 360 - ((-1) * degrees);
        }
        return degrees;
    }
}
