package com.erdfelt.android.gestures;

import android.graphics.Matrix;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.ImageView;

public class ImageMatrixAnimController {
    private static final String TAG     = "IMAnimController";
    private ImageView           mView;
    private Animation           mAnimation;
    private long                mMaxDelay;
    private long                mDuration;
    private Transformation      mTransformation;
    private boolean             mActive = false;
    private Matrix              baseMatrix;
    private Matrix              animMatrix;

    public ImageMatrixAnimController(ImageView view) {
        mView = view;
    }

    public void start(Animation anim) {
        baseMatrix = mView.getImageMatrix();
        mAnimation = anim;
        mAnimation.setFillBefore(true);
        mTransformation = new Transformation();
        mDuration = mAnimation.getDuration();
        mMaxDelay = 0;
        mAnimation.setStartTime(Animation.START_ON_FIRST_FRAME);
        mAnimation.initialize(mView.getWidth(), mView.getHeight(), mView.getWidth(), mView.getHeight());
        Log.d(TAG, "start(" + anim.getClass().getSimpleName() + ")");
        Log.d(TAG, "       base = " + toTranslateString(baseMatrix));
        animMatrix = new Matrix();
        mActive = true;
    }

    public void onStep() {
        long now = AnimationUtils.currentAnimationTimeMillis();
        mActive = mAnimation.getTransformation(now, mTransformation);
        // Log.d(TAG, "onStep() - base = " + toTranslateString(baseMatrix) + " ~");
        animMatrix.set(baseMatrix);
        // Log.d(TAG, "          xform = " + toTranslateString(mTransformation.getMatrix()) + " :");
        animMatrix.postConcat(mTransformation.getMatrix());
        // Log.d(TAG, "           anim = " + toTranslateString(animMatrix) + " #");
        mView.setImageMatrix(animMatrix);
        mView.invalidate();
    }
    
    float dbg[] = new float[9];
    
    private String toTranslateString(Matrix matrix) {
        matrix.getValues(dbg);
        return String.format("x:%7.3f y:%7.3f", dbg[Matrix.MTRANS_X], dbg[Matrix.MTRANS_Y]);
    }

    public boolean isDone() {
        return AnimationUtils.currentAnimationTimeMillis() > mAnimation.getStartTime() + mMaxDelay + mDuration;
    }

    public boolean isActive() {
        return mActive;
    }
}
