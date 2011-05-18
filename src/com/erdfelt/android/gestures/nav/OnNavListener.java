package com.erdfelt.android.gestures.nav;

import android.graphics.Point;
import android.view.MotionEvent;

public interface OnNavListener {
    /**
     * DPad, Trackball, and Arrow key action.
     * 
     * @param dir
     *            the direction of the keyboard event
     */
    void onCursorDirection(Dir dir);

    /**
     * DPad, Trackball, and Arrow key select was used.
     */
    void onCursorSelect();

    /**
     * The user double-tapped an area of the screen.
     * 
     * @param motion
     *            the double tap motion event details (of second tap)
     * @return true if handled (safe to always return true here)
     */
    boolean onDoubleTap(MotionEvent motion);

    /**
     * Active Drag Occuring.
     * 
     * @param dragStart
     *            the starting point of the drag
     * @param dragNow
     *            the current point of the drag
     * @param dir
     *            the direction of the drag
     * @return true if handled (safe to always return true here)
     */
    boolean onDrag(MotionEvent dragStart, MotionEvent dragNow, Dir dir, float deltaX, float deltaY);

    /**
     * The Drag has ended, the user has lifted their fingers off the surface.
     * 
     * @param dragStart
     *            the starting point of the drag
     * @param dragEnd
     *            the ending point of the drag
     * @return true if handled (safe to always return true here)
     */
    boolean onDragUp(MotionEvent dragStart, MotionEvent dragEnd);

    /**
     * A Flick event.
     * 
     * @param flickStart
     *            the start of the flick touch gesture
     * @param flickEnd
     *            the ending point of the flick touch gesture
     * @param dir
     *            the basic direction of the flick
     * @param velocityX
     *            the velocity X
     * @param velocityY
     *            the velocity Y
     * @return
     */
    boolean onFlick(MotionEvent flickStart, MotionEvent flickEnd, Dir dir, double velocityX, double velocityY);

    /**
     * The user long-pressed a spot on the screen.
     * 
     * @param motion
     *            the long press event details
     */
    void onLongPress(MotionEvent motion);

    /**
     * A multitouch (2+ fingers) event. Useful for determining Pinch/Spread/Rotate gestures.
     * 
     * @param multi
     *            the motion event for the multi-move.
     * @param center
     *            the centerpoint for the multi-move (only determines center point of first 2 fingers)
     * @return true if handled (safe to always return true here)
     */
    boolean onMultiMove(MotionEvent multi, Point center);

    /**
     * The user pressed the screen. (this is an event longer than {@link #onTap(MotionEvent)} but shorter than
     * {@link #onLongPress(MotionEvent)})
     * 
     * @param motion
     *            the event details
     */
    void onPress(MotionEvent motion);

    /**
     * User tapped the screen once.
     * 
     * @param motion
     *            the tap motion event details
     * @return true if handled (safe to always return true here)
     */
    boolean onTap(MotionEvent motion);

    /**
     * The user touched the screen. (usually indicates the start of other events)
     * 
     * @param motion
     *            the motion event for this event.
     * @return true if handled (safe to always return true here)
     */
    boolean onTouchDown(MotionEvent motion);

    /**
     * The user released their finger from the screen.
     * 
     * @param motion
     *            the touch up motion event details
     * @return true if handled (safe to always return true here)
     */
    boolean onTouchUp(MotionEvent motion);
}