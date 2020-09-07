package com.example.singleinstance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

public class RoundActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_image);
    }

    public void showImage(View view) {
        startActivity(new Intent(this,CircleImageViewActivity.class));
    }
}
