package com.mor.sa.android.activities;

import com.checker.sa.android.helper.Helper;
import com.mor.sa.android.activities.R;
import com.org.touchview.TouchImageView;

import android.app.Activity;
import android.os.Bundle;


public class TouchImageViewActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoomin_image_view);
        TouchImageView img = (TouchImageView) findViewById(R.id.snoop);
        img.setImageBitmap(Helper.thisImage);
        img.setMaxZoom(4f);
    }
}