package com.example.singleinstance;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.Nullable;

/**
 * @author xiayiye5
 * 2020年7月10日16:09:23
 */
public class SlidingMenuActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注意 这个方法在使用的时候一定要放在setContentView前面
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sliding_menu);
    }
}
