package com.erdfelt.android.gestures;

import android.app.Activity;
import android.os.Bundle;

public class GesturesFunActivity extends Activity {
    private GestureDebugView view;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        view = (GestureDebugView) findViewById(R.id.debug);
    }
}