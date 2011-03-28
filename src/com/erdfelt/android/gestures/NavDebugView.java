package com.erdfelt.android.gestures;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.erdfelt.android.gestures.nav.Dir;
import com.erdfelt.android.gestures.nav.Navigator;
import com.erdfelt.android.gestures.nav.OnNavListener;

public class NavDebugView extends View implements OnNavListener {
    public static class Line {
        public Point    pointA = new Point(0, 0);
        public Point    pointB = new Point(0, 0);
        private boolean show   = false;
        private Paint   paint;

        public Line() {
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

    public static class Loc {
        public LocType    type   = LocType.NONE;
        public Point      point  = new Point();

        private float     radius = 40.0f;
        private Paint     paintTap;
        private Paint     paintDoubleTap;
        private Paint     paintLongPress;
        private Paint     paintCircle;
        private TextPaint paintText;
        private int       textSize;

        public Loc() {
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
            type = LocType.NONE;
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

        public void show(LocType lt, MotionEvent ev) {
            type = lt;
            point.x = (int) ev.getX();
            point.y = (int) ev.getY();
        }
    }

    public static enum LocType {
        NONE, TAP, DOUBLE_TAP, LONG_PRESS;
    }

    private static final String TAG             = NavDebugView.class.getSimpleName();

    private Navigator           detector;
    private Path                pathNE;
    private Path                pathN;
    private Path                pathNW;
    private Path                pathW;
    private Path                pathSW;
    private Path                pathS;
    private Path                pathSE;
    private Path                pathE;
    private Dir                 activeDir       = Dir.NONE;
    private Paint               highlightPaint  = new Paint();
    private Paint               backgroundPaint = new Paint();
    private Line                dragline        = new Line();
    private Point               lastPoint       = new Point();
    private Loc                 highlightLoc    = new Loc();

    public NavDebugView(Context context) {
        super(context);
        init();
    }

    public NavDebugView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NavDebugView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void calculateNavIndicatorLocations(int width, int height) {
        int size = 50;
        int midX = width / 2;
        int midY = height / 2;

        pathNE = new Path();
        pathNE.moveTo(width, 0);
        pathNE.lineTo(width, size);
        pathNE.lineTo(width - size, 0);
        pathNE.lineTo(width, 0);
        pathNE.close();

        pathN = new Path();
        pathN.moveTo(midX, 0);
        pathN.lineTo(midX + size, size);
        pathN.lineTo(midX - size, size);
        pathN.lineTo(midX, 0);
        pathN.close();

        pathNW = new Path();
        pathNW.moveTo(0, 0);
        pathNW.lineTo(size, 0);
        pathNW.lineTo(0, size);
        pathNW.lineTo(0, 0);
        pathNW.close();

        pathW = new Path();
        pathW.moveTo(0, midY);
        pathW.lineTo(size, midY - size);
        pathW.lineTo(size, midY + size);
        pathW.lineTo(0, midY);
        pathW.close();

        pathSW = new Path();
        pathSW.moveTo(0, height);
        pathSW.lineTo(0, height - size);
        pathSW.lineTo(size, height);
        pathSW.lineTo(0, height);
        pathSW.close();

        pathS = new Path();
        pathS.moveTo(midX, height);
        pathS.lineTo(midX + size, height - size);
        pathS.lineTo(midX - size, height - size);
        pathS.lineTo(midX, height);
        pathS.close();

        pathSE = new Path();
        pathSE.moveTo(width, height);
        pathSE.lineTo(width, height - size);
        pathSE.lineTo(width - size, height);
        pathSE.lineTo(width, height);
        pathSE.close();

        pathE = new Path();
        pathE.moveTo(width, midY);
        pathE.lineTo(width - size, midY - size);
        pathE.lineTo(width - size, midY + size);
        pathE.lineTo(width, midY);
        pathE.close();

        highlightPaint = new Paint();
        highlightPaint.setColor(Color.RED);
        highlightPaint.setStyle(Paint.Style.FILL);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.WHITE);
        backgroundPaint.setStyle(Paint.Style.FILL);
    }

    private void init() {
        setBackgroundColor(R.color.white);
        detector = new Navigator(getContext(), this);
    }

    @Override
    public void onCursorDirection(Dir dir) {
        Log.i(TAG, "onCursorDirection(" + dir + ")");

    }

    @Override
    public void onCursorSelect() {
        Log.i(TAG, "onCursorSelect()");

    }

    @Override
    public boolean onDoubleTap(MotionEvent motion) {
        Log.i(TAG, "onDoubleTap(" + motion + ")");
        highlightLoc.show(LocType.DOUBLE_TAP, motion);
        invalidate();
        return true;
    }

    @Override
    public boolean onDrag(MotionEvent dragStart, MotionEvent dragNow, Dir dir) {
        Log.i(TAG, "onDrag(" + dragStart + ", " + dragNow + ", " + dir + ")");
        dragline.updatePointB(dragNow);
        activeDir = dir;
        updateLastPoint(dragNow);
        invalidate();
        return true;
    }

    @Override
    public boolean onDragUp(MotionEvent dragStart, MotionEvent dragEnd) {
        Log.i(TAG, "onDragUp(" + dragStart + ", " + dragEnd + ")");
        dragline.hide();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth(), getHeight(), backgroundPaint);

        switch (activeDir) {
            case EAST:
                canvas.drawPath(pathE, highlightPaint);
                break;
            case WEST:
                canvas.drawPath(pathW, highlightPaint);
                break;
            case NORTH_EAST:
                canvas.drawPath(pathNE, highlightPaint);
                break;
            case NORTH_WEST:
                canvas.drawPath(pathNW, highlightPaint);
                break;
            case SOUTH_EAST:
                canvas.drawPath(pathSE, highlightPaint);
                break;
            case SOUTH_WEST:
                canvas.drawPath(pathSW, highlightPaint);
                break;
            case NORTH:
                canvas.drawPath(pathN, highlightPaint);
                break;
            case SOUTH:
                canvas.drawPath(pathS, highlightPaint);
                break;
        }

        dragline.onDraw(canvas);
        highlightLoc.onDraw(canvas);

        super.onDraw(canvas);
    }

    @Override
    public boolean onFlick(MotionEvent e1, MotionEvent e2, Dir dir, double velocityX, double velocityY) {
        Log.i(TAG, "onFlick(" + e1 + ", " + e2 + ", " + dir + ", " + velocityX + ", " + velocityY + ")");
        activeDir = dir;
        this.highlightPaint.setColor(Color.BLUE);
        invalidate();
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        calculateNavIndicatorLocations(right - left, bottom - top);
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void onLongPress(MotionEvent motion) {
        Log.i(TAG, "onLongPress(" + motion + ")");
        highlightLoc.show(LocType.LONG_PRESS, motion);
        invalidate();
    }

    @Override
    public void onPinch(Point fingerA, Point fingerB, Point center) {
        Log.i(TAG, "onPinch(" + fingerA + ", " + fingerB + ", " + center + ")");

    }

    @Override
    public void onPress(MotionEvent e) {
        Log.i(TAG, "onPress(" + e + ")");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        calculateNavIndicatorLocations(w, h);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void onSpread(Point fingerA, Point fingerB, Point center) {
        Log.i(TAG, "onSpread(" + fingerA + ", " + fingerB + ", " + center + ")");

    }

    @Override
    public boolean onTap(MotionEvent motion) {
        Log.i(TAG, "onTap(" + motion + ")");
        highlightLoc.show(LocType.TAP, motion);
        invalidate();
        return true;
    }

    @Override
    public boolean onTouchDown(MotionEvent e) {
        Log.i(TAG, "onTouchDown(" + e + ")");
        activeDir = Dir.NONE;
        dragline.updatePointA(e);
        dragline.hide();
        highlightPaint.setColor(Color.RED);
        highlightLoc.hide();
        updateLastPoint(e);
        invalidate();
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onTouchUp(MotionEvent motion) {
        Log.i(TAG, "onTouchUp(" + motion + ")");
        return true;
    }

    private void updateLastPoint(MotionEvent e) {
        lastPoint.x = (int) e.getX();
        lastPoint.y = (int) e.getY();
    }
}
