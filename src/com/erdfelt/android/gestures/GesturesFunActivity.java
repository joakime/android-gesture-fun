package com.erdfelt.android.gestures;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class GesturesFunActivity extends Activity {
    private GestureDebugView view;
    private GestureDetector detector;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        view = (GestureDebugView) findViewById(R.id.debug);
        detector = new GestureDetector(this, view);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}