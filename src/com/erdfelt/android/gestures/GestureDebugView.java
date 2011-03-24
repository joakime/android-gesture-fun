package com.erdfelt.android.gestures;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public class GestureDebugView extends View implements OnGestureListener {
    private static final String TAG = GestureDebugView.class.getSimpleName();

    public GestureDebugView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public GestureDebugView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GestureDebugView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setBackgroundColor(R.color.white);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        calculateTextLocations(right - left, bottom - top);
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        calculateTextLocations(w, h);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private Path pathNE;
    private Path pathN;
    private Path pathNW;
    private Path pathW;
    private Path pathSW;
    private Path pathS;
    private Path pathSE;
    private Path pathE;

    private Dir  activeDir = Dir.NONE;

    private void calculateTextLocations(int width, int height) {
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

    private Paint highlightPaint  = new Paint();
    private Paint backgroundPaint = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth(), getHeight(), backgroundPaint);

        switch (activeDir) {
            case E:
                canvas.drawPath(pathE, highlightPaint);
                break;
            case W:
                canvas.drawPath(pathW, highlightPaint);
                break;
            case NE:
                canvas.drawPath(pathNE, highlightPaint);
                break;
            case NW:
                canvas.drawPath(pathNW, highlightPaint);
                break;
            case SE:
                canvas.drawPath(pathSE, highlightPaint);
                break;
            case SW:
                canvas.drawPath(pathSW, highlightPaint);
                break;
            case N:
                canvas.drawPath(pathN, highlightPaint);
                break;
            case S:
                canvas.drawPath(pathS, highlightPaint);
                break;
        }

        super.onDraw(canvas);
    }

    private String dump(MotionEvent ev) {
        StringBuilder dump = new StringBuilder();
        dump.append("[MotionEvent:");
        dump.append("x=").append(ev.getX());
        dump.append(",y=").append(ev.getY());
        dump.append("]");
        return dump.toString();
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.i(TAG, "onDown(" + dump(e) + ")");
        this.activeDir = Dir.NONE;
        invalidate();
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.i(TAG, "onShowPress(" + dump(e) + ")");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.i(TAG, "onSingleTapUp(" + dump(e) + ")");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.i(TAG, "onScroll(" + dump(e1) + ", " + dump(e2) + ", " + distanceX + ", " + distanceY + ")");
        this.activeDir = Dir.asDir(distanceX * (-1), distanceY * (-1), false);
        invalidate();
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.i(TAG, "onLongPress(" + dump(e) + ")");

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.i(TAG, "onFling(" + dump(e1) + ", " + dump(e2) + ", " + velocityX + ", " + velocityY + ")");
        this.activeDir = Dir.asDir(velocityX, velocityY, true);
        invalidate();
        return false;
    }
}
