package com.erdfelt.android.gestures.nav;

import android.graphics.Point;
import android.view.MotionEvent;

public interface OnNavListener {
    /**
     * DPad, Trackball, and Arrow key action.
     * @param dir the direction of the keyboard event
     */
    void onCursorDirection(Dir dir);

    /**
     * DPad, Trackball, and Arrow key select was used.
     */
    void onCursorSelect();

    void onPinch(Point fingerA, Point fingerB, Point center);

    void onSpread(Point fingerA, Point fingerB, Point center);

    boolean onDrag(MotionEvent dragStart, MotionEvent dragNow, Dir dir);

    boolean onDragUp(MotionEvent dragStart, MotionEvent dragEnd);

    boolean onFlick(MotionEvent flickStart, MotionEvent flickEnd, Dir dir, double velocityX, double velocityY);

    void onPress(MotionEvent motion);

    void onLongPress(MotionEvent motion);

    boolean onDoubleTap(MotionEvent motion);

    boolean onTap(MotionEvent motion);

    boolean onTouchUp(MotionEvent motion);

    boolean onTouchDown(MotionEvent motion);
}