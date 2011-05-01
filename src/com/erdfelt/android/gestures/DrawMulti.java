package com.erdfelt.android.gestures;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.text.TextPaint;

public class DrawMulti {
    private boolean   visible = false;
    private Point     center;
    private Point     touchA;
    private Point     touchB;

    private int       textSize;
    private float     radius  = 40.0f;
    private TextPaint paintText;
    private Paint     paintA;
    private Paint     paintB;
    private Paint     paintCenter;

    public DrawMulti() {
        textSize = (int) (radius * 0.75);

        paintA = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintA.setColor(Color.BLUE);
        paintA.setStyle(Paint.Style.FILL_AND_STROKE);

        paintB = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintB.setColor(Color.RED);
        paintB.setStyle(Paint.Style.FILL_AND_STROKE);

        paintCenter = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCenter.setColor(Color.MAGENTA);
        paintCenter.setStrokeWidth(radius / 5);
        paintCenter.setStyle(Paint.Style.FILL_AND_STROKE);

        paintText = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        paintText.setTextSize(textSize);
        paintText.setTypeface(Typeface.DEFAULT_BOLD);
        paintText.setColor(Color.WHITE);
        paintText.setTextAlign(Paint.Align.CENTER);
    }

    public void hide() {
        visible = false;
    }

    public void setMulti(Point touchA, Point touchB, Point center) {
        this.touchA = touchA;
        this.touchB = touchB;
        this.center = center;
        this.visible = true;
    }

    public void onDraw(Canvas canvas) {
        if (!visible) {
            return; // skip
        }

        canvas.drawLine(touchA.x, touchA.y, touchB.x, touchB.y, paintCenter);
        canvas.drawCircle(center.x, center.y, (int) (radius * 0.6), paintCenter);
        canvas.drawCircle(touchA.x, touchA.y, radius, paintA);
        canvas.drawCircle(touchB.x, touchB.y, radius, paintB);
        canvas.drawText("M", center.x, center.y + (textSize / 2), paintText);
        canvas.drawText("A", touchA.x, touchA.y + (textSize / 2), paintText);
        canvas.drawText("B", touchB.x, touchB.y + (textSize / 2), paintText);
    }
}
