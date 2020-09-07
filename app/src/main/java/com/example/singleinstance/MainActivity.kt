package com.example.singleinstance

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : CheckPermissionsActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //动态设置按钮的宽高
       /* val dm = DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        val widthPixels = dm.widthPixels// 取得屏幕bai的宽度du
        val heightPixels = dm.heightPixels// 取得屏幕的高度
        Log.e("打印像素", "$widthPixels==$heightPixels")
        val btWidth = widthPixels - dip2px(50f) * 2
        val layoutParams = btOpen.layoutParams
        layoutParams.height = btWidth;
        btOpen.layoutParams = layoutParams;*/
    }

    fun startPage(view: View) {
        startActivity(Intent(this, FloatActivity::class.java))
    }

    override fun onRestart() {
        super.onRestart()
        Log.e("生命周期_MainActivity", "onRestart");
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.e("生命周期_MainActivit", "onNewIntent");
    }

    override fun onStop() {
        super.onStop()
        Log.e("生命周期_MainActivity", "onStop");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("生命周期_MainActivity", "onDestroy");
    }

    override fun onResume() {
        super.onResume()
        Log.e("生命周期_MainActivity", "onResume");
    }

    override fun onPause() {
        super.onPause()
        Log.e("生命周期_MainActivity", "onPause");
    }

    fun goAnimator(view: View) {
        startActivity(Intent(this, AnimatorActivity::class.java))
    }

    fun goRefreshPage(view: View) {
        startActivity(Intent(this, RefreshActivity::class.java))
    }

    fun goSlidePage(view: View) {
        startActivity(Intent(this, SlidingMenuActivity::class.java))
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2px(dpValue: Float): Int {
        val scale: Float = getResources().getDisplayMetrics().density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun openRoundImage(view: View) {
        startActivity(Intent(this, RoundActivity::class.java))
    }

    fun trendsID(view: View) {
        startActivity(Intent(this,SingleInstanceActivity::class.java))
    }
}