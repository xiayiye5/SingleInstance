package com.example.singleinstance;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainService extends Service implements View.OnClickListener {

    private static final String TAG = "MainService";

    View toucherLayout;
    WindowManager.LayoutParams params;
    WindowManager windowManager;
    //状态栏高度.
    int statusBarHeight = -1;
    private RelativeLayout imageButton;

    //不与Activity进行绑定.
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "MainService Created");
        createToucher();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void createToucher() {
        //赋值WindowManager&LayoutParam.
        params = new WindowManager.LayoutParams();
        windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        //设置type.系统提示型窗口，一般都在应用程序窗口之上.
        //Android8.0行为变更，对8.0进行适配https://developer.android.google.cn/about/versions/oreo/android-8.0-changes#o-apps
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        //设置效果为背景透明.
        params.format = PixelFormat.RGBA_8888;
        //设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        //设置窗口初始停靠位置.
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 0;
        params.y = 0;

        //设置悬浮窗口长宽数据.
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = dip2px(40);

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局.
        toucherLayout = inflater.inflate(R.layout.toucherlayout, null);
        //添加toucherlayout
        windowManager.addView(toucherLayout, params);

        Log.i(TAG, "toucherlayout-->left:" + toucherLayout.getLeft());
        Log.i(TAG, "toucherlayout-->right:" + toucherLayout.getRight());
        Log.i(TAG, "toucherlayout-->top:" + toucherLayout.getTop());
        Log.i(TAG, "toucherlayout-->bottom:" + toucherLayout.getBottom());

        //主动计算出当前View的宽高信息.
        toucherLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        //用于检测状态栏高度.
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        Log.i(TAG, "状态栏高度为:" + statusBarHeight);

        //浮动窗口按钮.
        imageButton = (RelativeLayout) toucherLayout.findViewById(R.id.rl_horse_layout);
        ImageView ivHorseIcon = (ImageView) toucherLayout.findViewById(R.id.iv_horse_icon);
        ImageView ivCloseHorse = (ImageView) toucherLayout.findViewById(R.id.iv_close_horse);
        TextView tvHorseRaceLamp = (TextView) toucherLayout.findViewById(R.id.tv_horse_race_lamp);
        //开始文字滚动
        tvHorseRaceLamp.setSelected(true);
        ivHorseIcon.setOnClickListener(this);
        ivCloseHorse.setOnClickListener(this);
        imageButton.setOnClickListener(this);

        /*imageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                params.x = (int) event.getRawX() - 150;
                params.y = (int) event.getRawY() - 150 - statusBarHeight;
                windowManager.updateViewLayout(toucherLayout, params);
                return false;
            }
        });*/
    }

    @Override
    public void onDestroy() {
        if (imageButton != null) {
            windowManager.removeView(toucherLayout);
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        long[] hints = new long[2];
        if (v.getId() == R.id.iv_close_horse) {
            //关闭当前跑马灯
            stopSelf();
        } else if (v.getId() == R.id.iv_horse_icon) {
            Toast.makeText(getApplicationContext(), "点击了大喇叭", Toast.LENGTH_SHORT).show();
        } else {
            Log.i(TAG, "点击了");
            System.arraycopy(hints, 1, hints, 0, hints.length - 1);
            hints[hints.length - 1] = SystemClock.uptimeMillis();
            if (SystemClock.uptimeMillis() - hints[0] >= 700) {
                Log.i(TAG, "要执行");
                Toast.makeText(MainService.this, "连续点击两次以退出", Toast.LENGTH_SHORT).show();
            } else {
                Log.i(TAG, "即将关闭");
                stopSelf();
            }
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
