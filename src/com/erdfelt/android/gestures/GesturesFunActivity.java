package com.erdfelt.android.gestures;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class GesturesFunActivity extends Activity {
    private static final String TAG = GesturesFunActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Intent intent = getIntent();
        int layoutId = intent.getIntExtra("layout.id", R.layout.main_image);
        setContentView(layoutId);

        View v = findViewById(R.id.view);
        if (v != null) {
            v.setFocusable(true);
            v.requestFocus();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.layout_menu, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyDown(" + keyCode + ", " + event + ")");
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            case R.id.main_layout:
                intent = new Intent(this, GesturesFunActivity.class);
                intent.putExtra("layout.id", R.layout.main);
                startActivity(intent);
                finish();
                return true;
            case R.id.image_layout:
                intent = new Intent(this, GesturesFunActivity.class);
                intent.putExtra("layout.id", R.layout.main_image);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}