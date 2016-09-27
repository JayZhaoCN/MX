package com.jay.mx.activities;

import android.os.Bundle;

import com.jay.mx.R;
import com.jay.mx.base.BaseTitleActivity;

/**
 * Created by Jay on 2016/9/27.
 * 自定义View
 */

public class TestViewActivity extends BaseTitleActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE.SINGLE_BACK, R.color.rippelColor);
        this.setContentView(R.layout.test_view);
    }
}
