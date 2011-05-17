package com.erdfelt.android.gestures;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class GesturesFunActivity extends Activity {
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Intent intent = getIntent();
        int layoutId = intent.getIntExtra("layout.id", R.layout.main_image);
        setContentView(layoutId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.layout_menu, menu);
        return true;
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