package com.erdfelt.android.gestures;

import android.util.Log;
import android.view.ViewConfiguration;


public enum Dir {
    NONE, NW, N, NE, E, SE, S, SW, W;
    
    public static Dir asDir(float deltaX, float deltaY, boolean sloppy) {
        int x = Math.round(deltaX);
        int y = Math.round(deltaY);

        if (sloppy) {
            if (Math.abs(x) < ViewConfiguration.getTouchSlop()) {
                x = 0;
            }
            if (Math.abs(y) < ViewConfiguration.getTouchSlop()) {
                y = 0;
            }
        }
        
        Dir dir = NONE;

        if (y > 0) {
            dir = S;
        } else if (y < 0) {
            dir = N;
        }

        if (x > 0) {
            if (dir == S) {
                dir = SE;
            } else if (dir == N) {
                dir = NE;
            } else {
                dir = E;
            }
        } else if (x < 0) {
            if (dir == S) {
                dir = SW;
            } else if (dir == N) {
                dir = NW;
            } else {
                dir = W;
            }
        }
        
        // Log.i("Dir", "asDir(x=" + deltaX + "->" + x + ", y=" + deltaY + "->" + y + ") = " + dir);

        return dir;
    }

}
