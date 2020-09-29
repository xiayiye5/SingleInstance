package com.example.singleinstance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_other.*

class OtherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other)
        vtThird.run {
            text = "三江集团"
            setTextColor(ContextCompat.getColor(this@OtherActivity, R.color.colorAccent))
            textSize = 18.0f
        }
        vtThird.apply {
            text = "三江集团"
            setTextColor(ContextCompat.getColor(this@OtherActivity, R.color.colorAccent))
            textSize = 18.0f
        }
    }
}