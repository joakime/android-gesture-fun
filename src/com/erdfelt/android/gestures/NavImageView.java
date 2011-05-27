package com.erdfelt.android.gestures;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.erdfelt.android.gestures.nav.Dir;
import com.erdfelt.android.gestures.nav.Navigator;
import com.erdfelt.android.gestures.nav.OnNavListener;

public class NavImageView extends ImageView implements OnNavListener {
    private static class LastPoint extends Point {
        private boolean valid = false;

        public boolean isValid() {
            return valid;
        }

        public void reset() {
            super.set(0, 0);
            valid = false;
        }

        public void setPoint(Point point) {
            super.x = point.x;
            super.y = point.y;
            valid = true;
        }
    }

    private static class LastScale {
        private boolean valid = false;
        private float   distance;

        public float calcScale(Point center, MotionEvent multi) {
            float distA = (float) distance(center.x, center.y, multi.getX(0), multi.getY(0));
            float distB = (float) distance(center.x, center.y, multi.getX(1), multi.getY(1));

            float distN = Math.min(distA, distB);
            return (distN / distance);
        }

        private double distance(double x1, double y1, double x2, double y2) {
            return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        }

        public boolean isValid() {
            return valid;
        }

        public void reset() {
            this.distance = 0.0f;
            this.valid = false;
        }

        public void setScale(Point center, MotionEvent multi) {
            float distA = (float) distance(center.x, center.y, multi.getX(0), multi.getY(0));
            float distB = (float) distance(center.x, center.y, multi.getX(1), multi.getY(1));

            distance = Math.min(distA, distB);
            this.valid = true;
        }
    }

    private static final String TAG = NavImageView.class.getSimpleName();

    private static final void close(Closeable c) {
        if (c == null) {
            return;
        }
        try {
            c.close();
        } catch (IOException ignore) {
            /* ignore */
        }
    }

    private static final Bitmap loadBitmap(AssetManager assets, String filename) {
        InputStream in = null;
        try {
            in = assets.open(filename);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inScaled = false;
            return BitmapFactory.decodeStream(in, null, opts);
        } catch (IOException e) {
            Log.e(TAG, "Unable to open image: " + filename, e);
            return null;
        } finally {
            close(in);
        }
    }

    private Navigator                 detector;
    private Bitmap                    bitmap;
    private RectF                     bitmapRect;
    private LastPoint                 lastMultiPoint = new LastPoint();
    private LastScale                 lastMultiScale = new LastScale();
    private ImageMatrixAnimController animController;

    public NavImageView(Context context) {
        this(context, null);
    }

    public NavImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setBackgroundColor(Color.BLACK);

        bitmap = loadBitmap(context.getResources().getAssets(), "lion.png");
        setImageBitmap(bitmap);
        bitmapRect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());

        detector = new Navigator(context, this);
        animController = new ImageMatrixAnimController(this);
    }

    public void animRecenter() {
        Matrix toM = new Matrix();
        RectF bounds = new RectF(0, 0, getWidth(), getHeight());
        toM.setRectToRect(bitmapRect, bounds, Matrix.ScaleToFit.CENTER);

        RectF brect = getBitmapRect();
        Log.d(TAG, "Current bitmap rect: " + brect);
        Log.d(TAG, "toM : " + toM);

        float from[] = new float[9];
        getImageMatrix().getValues(from);
        float to[] = new float[9];
        toM.getValues(to);
        // float deltaX = from[Matrix.MTRANS_X] - to[Matrix.MTRANS_X];
        // float deltaY = from[Matrix.MTRANS_Y] - to[Matrix.MTRANS_Y];

        float deltaX = to[Matrix.MTRANS_X] - brect.left;
        float deltaY = to[Matrix.MTRANS_Y] - brect.top;
        Log.d(TAG, "animRecenter() - x:" + deltaX + ", y:" + deltaY);
        TranslateAnimation anim = new TranslateAnimation(0, deltaX, 0, deltaY);
        anim.setDuration(500);
        animController.start(anim);
        try {
            invalidate();
            Thread.sleep(20);
        } catch (Throwable ignore) {
            /* ignore */
        }
    }

    private RectF getBitmapRect() {
        RectF rect = new RectF(bitmapRect);
        Matrix m = new Matrix(getImageMatrix());
        m.mapRect(rect);
        return rect;
    }

    @Override
    public void onCursorDirection(Dir dir) {
        int dist = 30;
        switch (dir) {
            case NORTH_WEST:
                translate(-dist, -dist);
                break;
            case NORTH:
                translate(0, -dist);
                break;
            case NORTH_EAST:
                translate(dist, -dist);
                break;
            case EAST:
                translate(dist, 0);
                break;
            case SOUTH_EAST:
                translate(dist, dist);
                break;
            case SOUTH:
                translate(0, dist);
                break;
            case SOUTH_WEST:
                translate(-dist, dist);
                break;
            case WEST:
                translate(-dist, 0);
                break;
        }
    }

    private void translate(int x, int y) {
        Matrix m = getImageMatrix();
        m.postTranslate(x, y);
        setImageMatrix(m);
        invalidate();
    }

    @Override
    public void onCursorSelect() {
        Log.d(TAG, "Recenter Animation");
        animRecenter();
    }

    @Override
    public boolean onDoubleTap(MotionEvent motion) {
        // Zoom In
        Matrix m = getImageMatrix();
        float x = motion.getX();
        float y = motion.getY();
        m.postScale(1.20f, 1.20f, x, y);

        setImageMatrix(m);
        invalidate();
        return true;
    }

    @Override
    public boolean onDrag(MotionEvent dragStart, MotionEvent dragNow, Dir dir, float distX, float distY) {
        if (lastMultiPoint.isValid()) {
            return true;
        }
        Matrix m = getImageMatrix();
        m.postTranslate(-distX, -distY);
        setImageMatrix(m);
        invalidate();
        return true;
    }

    @Override
    public boolean onDragUp(MotionEvent dragStart, MotionEvent dragEnd) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Log.d(TAG, "onDraw()");
        if (animController.isActive()) {
            animController.onStep();
            invalidate();
        }
    }

    @Override
    public boolean onFlick(MotionEvent flickStart, MotionEvent flickEnd, Dir dir, double velocityX, double velocityY) {
        // TODO Auto-generated method stub
        return true;
    }

    /**
     * Pass Keyboard Into Detector
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return detector.onKeyDown(keyCode, event);
    }

    @Override
    public void onLongPress(MotionEvent motion) {
    }

    @Override
    public boolean onMultiMove(MotionEvent multi, Point center) {
        if (lastMultiScale.isValid() && lastMultiPoint.isValid()) {
            // Perform Scale
            float scale = lastMultiScale.calcScale(center, multi);
            if ((scale > 0.01f) || (scale < -0.01f)) {
                Matrix m = getImageMatrix();
                float deltaX = lastMultiPoint.x - center.x;
                float deltaY = lastMultiPoint.y - center.y;
                m.postTranslate(-deltaX, -deltaY);
                m.postScale(scale, scale, center.x, center.y);
                setImageMatrix(m);
                invalidate();
            }
        }
        lastMultiScale.setScale(center, multi);
        lastMultiPoint.setPoint(center);

        return true;
    }

    @Override
    public void onPress(MotionEvent motion) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onTap(MotionEvent motion) {
        Log.d(TAG, "onTap()");
        // Recenter to tap point.
        recenter(motion.getX(), motion.getY());
        RectF brect = getBitmapRect();
        Log.d(TAG, "Current bitmap rect: " + brect);
        invalidate();
        return true;
    }

    @Override
    public boolean onTouchDown(MotionEvent motion) {
        // TODO Auto-generated method stub
        return true;
    }

    /**
     * Pass Touch Into Detector
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onTouchUp(MotionEvent motion) {
        // TODO Auto-generated method stub

        lastMultiPoint.reset();
        lastMultiScale.reset();

        return true;
    }

    /**
     * Pass Trackball / DPad Into Detector
     */
    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        return detector.onTrackballEvent(event);
    }

    protected void recenter() {
        Matrix m = new Matrix();
        RectF bounds = new RectF(0, 0, getWidth(), getHeight());
        m.setRectToRect(bitmapRect, bounds, Matrix.ScaleToFit.FILL);
        setImageMatrix(m);
    }

    private void recenter(float x, float y) {
        Matrix m = getImageMatrix();
        float deltaX = x - (getWidth() / 2);
        float deltaY = y - (getHeight() / 2);
        m.postTranslate(-deltaX, -deltaY);
        setImageMatrix(m);
    }
}
