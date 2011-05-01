package com.erdfelt.android.gestures;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.erdfelt.android.gestures.nav.Dir;
import com.erdfelt.android.gestures.nav.Navigator;
import com.erdfelt.android.gestures.nav.OnNavListener;

public class NavDebugView extends View implements OnNavListener {
    private static final String TAG             = NavDebugView.class.getSimpleName();

    private Navigator           detector;
    private DrawDir             drawdir         = new DrawDir();
    private DrawLine            drawdrag        = new DrawLine();
    private DrawTap             drawtap         = new DrawTap();
    private DrawMulti           drawmulti       = new DrawMulti();
    private Paint               backgroundPaint = new Paint();
    private Point               lastPoint       = new Point();

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

    private void init() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.WHITE);
        backgroundPaint.setStyle(Paint.Style.FILL);

        setBackgroundColor(R.color.white);

        detector = new Navigator(getContext(), this);
    }

    @Override
    public void onCursorDirection(Dir dir) {
        Log.i(TAG, "onCursorDirection(" + dir + ")");
        drawdir.setDir(DrawDir.Type.CURSOR, dir);
        invalidate();
    }

    @Override
    public void onCursorSelect() {
        Log.i(TAG, "onCursorSelect()");
        // TODO: Show Select!
    }

    @Override
    public boolean onDoubleTap(MotionEvent motion) {
        Log.i(TAG, "onDoubleTap(" + motion + ")");
        drawtap.show(DrawTap.Type.DOUBLE_TAP, motion);
        invalidate();
        return true;
    }

    @Override
    public boolean onDrag(MotionEvent dragStart, MotionEvent dragNow, Dir dir) {
        // Log.i(TAG, "onDrag(" + dragStart + ", " + dragNow + ", " + dir + ")");
        drawdrag.updatePointB(dragNow);
        drawdir.setDir(DrawDir.Type.DRAG, dir);
        updateLastPoint(dragNow);
        invalidate();
        return true;
    }

    @Override
    public boolean onDragUp(MotionEvent dragStart, MotionEvent dragEnd) {
        Log.i(TAG, "onDragUp(" + dragStart + ", " + dragEnd + ")");
        drawdrag.hide();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth(), getHeight(), backgroundPaint);

        drawdir.onDraw(canvas);
        drawdrag.onDraw(canvas);
        drawtap.onDraw(canvas);
        drawmulti.onDraw(canvas);
    }

    @Override
    public boolean onFlick(MotionEvent e1, MotionEvent e2, Dir dir, double velocityX, double velocityY) {
        Log.i(TAG, "onFlick(" + e1 + ", " + e2 + ", " + dir + ", " + velocityX + ", " + velocityY + ")");
        drawdir.setDir(DrawDir.Type.FLICK, dir);
        invalidate();
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        drawdir.updateSize(right - left, bottom - top);
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void onLongPress(MotionEvent motion) {
        Log.i(TAG, "onLongPress(" + motion + ")");
        drawtap.show(DrawTap.Type.LONG_PRESS, motion);
        invalidate();
    }

    @Override
    public void onPress(MotionEvent e) {
        Log.i(TAG, "onPress(" + e + ")");
        // TODO: Show Press?
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        drawdir.updateSize(w, h);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onMultiMove(MotionEvent multi, Point center) {
        Log.i(TAG, "onMultiMove(" + multi + ", " + center + ")");

        Point touchA = new Point();
        touchA.x = (int) multi.getX(0);
        touchA.y = (int) multi.getY(0);
        Point touchB = new Point();
        touchB.x = (int) multi.getX(1);
        touchB.y = (int) multi.getY(1);
        drawmulti.setMulti(touchA, touchB, center);

        invalidate();
        return true;
    }

    @Override
    public boolean onTap(MotionEvent motion) {
        Log.i(TAG, "onTap(" + motion + ")");
        drawtap.show(DrawTap.Type.TAP, motion);
        invalidate();
        return true;
    }

    @Override
    public boolean onTouchDown(MotionEvent e) {
        Log.i(TAG, "onTouchDown(" + e + ")");
        drawdrag.updatePointA(e);
        drawdrag.hide();
        drawdir.hide();
        drawtap.hide();
        drawmulti.hide();
        updateLastPoint(e);
        invalidate();
        return true;
    }

    /**
     * Pass Touch Into Detector
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    /**
     * Pass Keyboard Into Detector
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return detector.onKeyDown(keyCode, event);
    }

    /**
     * Pass Trackball / DPad Into Detector
     */
    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        return detector.onTrackballEvent(event);
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
