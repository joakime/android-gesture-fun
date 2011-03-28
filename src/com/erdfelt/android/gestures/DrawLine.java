package com.erdfelt.android.gestures;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

public class DrawLine {
    public Point    pointA = new Point(0, 0);
    public Point    pointB = new Point(0, 0);
    private boolean show   = false;
    private Paint   paint;

    public DrawLine() {
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(3.0f);
    }

    public void hide() {
        show = false;
    }

    public void onDraw(Canvas canvas) {
        if (show) {
            canvas.drawLine(pointA.x, pointA.y, pointB.x, pointB.y, paint);
        }
    }

    public void updatePointA(MotionEvent e) {
        pointA.x = (int) e.getX();
        pointA.y = (int) e.getY();
    }

    public void updatePointB(MotionEvent e) {
        pointB.x = (int) e.getX();
        pointB.y = (int) e.getY();
        show = true;
    }
}