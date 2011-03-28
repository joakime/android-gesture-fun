package com.erdfelt.android.gestures.nav;

public enum Dir {
    NONE, NORTH_WEST, NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST;

    private static final Dir zones[] = new Dir[] { Dir.EAST, Dir.NORTH_EAST, Dir.NORTH, Dir.NORTH_WEST, Dir.WEST, Dir.SOUTH_WEST, Dir.SOUTH, Dir.SOUTH_EAST };

    public static Dir asDir(double deltaX, double deltaY) {
        double degrees = calcDegrees(deltaX, deltaY);
        int n = (int) (degrees + (45 / 2));
        int z = (n / 45);
        if (z == 8) {
            z = 0;
        }
        // Log.i("Dir", String.format("asDir(%.1f, %.1f) = %.1f degrees = %s", deltaX, deltaY, degrees, zones[z]));
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
