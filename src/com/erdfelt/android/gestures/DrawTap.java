package com.erdfelt.android.gestures;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.view.MotionEvent;

public class DrawTap {
    public static enum Type {
        NONE, TAP, DOUBLE_TAP, LONG_PRESS;
    }

    public DrawTap.Type type   = DrawTap.Type.NONE;
    public Point        point  = new Point();

    private float       radius = 40.0f;
    private Paint       paintTap;
    private Paint       paintDoubleTap;
    private Paint       paintLongPress;
    private Paint       paintCircle;
    private TextPaint   paintText;
    private int         textSize;

    public DrawTap() {
        textSize = (int) (radius * 0.75);

        paintText = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        paintText.setTextSize(textSize);
        paintText.setTypeface(Typeface.DEFAULT_BOLD);
        paintText.setColor(Color.WHITE);
        paintText.setTextAlign(Paint.Align.CENTER);

        paintTap = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTap.setColor(Color.BLUE);
        paintTap.setStyle(Paint.Style.FILL_AND_STROKE);

        paintDoubleTap = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintDoubleTap.setColor(Color.CYAN);
        paintDoubleTap.setStyle(Paint.Style.FILL_AND_STROKE);

        paintLongPress = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintLongPress.setColor(Color.MAGENTA);
        paintLongPress.setStyle(Paint.Style.FILL_AND_STROKE);

        paintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCircle.setStyle(Paint.Style.STROKE);
        paintCircle.setStrokeWidth(radius / 7);
    }

    public void hide() {
        type = DrawTap.Type.NONE;
    }

    public void onDraw(Canvas canvas) {
        switch (type) {
            case TAP:
                canvas.drawCircle(point.x, point.y, radius, paintTap);
                paintCircle.setColor(paintTap.getColor());
                canvas.drawCircle(point.x, point.y, (float) (radius + (radius * 1.3)), paintCircle);
                canvas.drawText("T", point.x, point.y + (textSize / 2), paintText);
                break;
            case DOUBLE_TAP:
                canvas.drawCircle(point.x, point.y, radius, paintDoubleTap);
                paintCircle.setColor(paintDoubleTap.getColor());
                canvas.drawCircle(point.x, point.y, (float) (radius + (radius * 1.3)), paintCircle);
                canvas.drawText("DT", point.x, point.y + (textSize / 2), paintText);
                break;
            case LONG_PRESS:
                canvas.drawCircle(point.x, point.y, radius, paintLongPress);
                paintCircle.setColor(paintLongPress.getColor());
                canvas.drawCircle(point.x, point.y, (float) (radius + (radius * 1.3)), paintCircle);
                canvas.drawText("LP", point.x, point.y + (textSize / 2), paintText);
        }
    }

    public void show(DrawTap.Type lt, MotionEvent ev) {
        type = lt;
        point.x = (int) ev.getX();
        point.y = (int) ev.getY();
    }
}