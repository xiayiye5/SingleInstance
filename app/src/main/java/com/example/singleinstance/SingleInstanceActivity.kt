package com.example.singleinstance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

class SingleInstanceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_instance)
        Log.e("生命周期_SingleInstance", "onCreate");
    }

    override fun onRestart() {
        super.onRestart()
        Log.e("生命周期_SingleInstance", "onRestart");
    }

    override fun onStart() {
        super.onStart()
        Log.e("生命周期_SingleInstance", "onStart");
    }

    override fun onResume() {
        super.onResume()
        Log.e("生命周期_SingleInstance", "onResume");
    }

    override fun onPause() {
        super.onPause()
        Log.e("生命周期_SingleInstance", "onPause");
    }

    override fun onStop() {
        super.onStop()
        Log.e("生命周期_SingleInstance", "onStop");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("生命周期_SingleInstance", "onDestroy");
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.e("生命周期_SingleInstance", "onNewIntent");
    }

    fun thirdPage(view: View) {
        startActivity(Intent(this, OtherActivity::class.java))
    }
}