package com.example.singleinstance

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class TrendsIdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_trends_id)
        setContentView(TrendsIdUtils.getIdFromLayout(this, "activity_trends_id"))
    }
}