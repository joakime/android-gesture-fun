package com.erdfelt.android.gestures;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.erdfelt.android.gestures.nav.Dir;
import com.erdfelt.android.gestures.nav.Navigator;
import com.erdfelt.android.gestures.nav.OnNavListener;

public class NavImageView extends ImageView implements OnNavListener {
    private static final String TAG        = NavImageView.class.getSimpleName();
    private Navigator           detector;
    private Bitmap              bitmap;
    private RectF               bitmapRect;

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

    @Override
    public void onCursorDirection(Dir dir) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCursorSelect() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onMultiMove(MotionEvent multi, Point center) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean onDrag(MotionEvent dragStart, MotionEvent dragNow, Dir dir, float distX, float distY) {
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
    public boolean onFlick(MotionEvent flickStart, MotionEvent flickEnd, Dir dir, double velocityX, double velocityY) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void onPress(MotionEvent motion) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLongPress(MotionEvent motion) {
        Log.d(TAG, "Reset matrix");
        recenter();
        invalidate();
    }

    private void recenter() {
        Matrix m = new Matrix();
        RectF bounds = new RectF(0, 0, getWidth(), getHeight());
        m.setRectToRect(bitmapRect, bounds, Matrix.ScaleToFit.FILL);
        setImageMatrix(m);
    }

    @Override
    public boolean onDoubleTap(MotionEvent motion) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean onTap(MotionEvent motion) {
        // Recenter to tap point.
        recenter(motion.getX(), motion.getY());
        invalidate();
        return true;
    }

    private void recenter(float x, float y) {
        Matrix m = getImageMatrix();
        float deltaX = x - (getWidth() / 2);
        float deltaY = y - (getHeight() / 2);
        m.postTranslate(-deltaX, -deltaY);
        setImageMatrix(m);
    }

    @Override
    public boolean onTouchUp(MotionEvent motion) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean onTouchDown(MotionEvent motion) {
        // TODO Auto-generated method stub
        return true;
    }
}
