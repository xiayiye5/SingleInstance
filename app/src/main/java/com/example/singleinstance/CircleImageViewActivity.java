package com.example.singleinstance;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CircleImageViewActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        ImageView rvImg = findViewById(R.id.iv_img);


        //动态设置广告图片的大小
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int widthPixels = dm.widthPixels;
        int heightPixels = dm.heightPixels;
        Log.e("打印像素", widthPixels + "==" + heightPixels);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rvImg.getLayoutParams();
        //动态设置广告图片的宽高保持一致,右边就是图片去除左右边距50dp后图片的宽度减去44是减去下面的两个22像素边距
        layoutParams.height = widthPixels - dip2px(50f) * 2;
        layoutParams.width = widthPixels - dip2px(50f) * 2;
        layoutParams.leftMargin = dip2px(27);
        layoutParams.rightMargin = dip2px(27);
        layoutParams.topMargin = dip2px(27);
        layoutParams.bottomMargin = dip2px(27);
        rvImg.setLayoutParams(layoutParams);
        showBitmap(rvImg,"http://ws.gfan.com/upload/1/2020/07/03/09/1593741311887.jpg",this);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     * 自己写的加载网络图片的方法
     *
     * @param welcomeImg 图片要显示在这个控件上面
     * @param imgUrl     图片的网址
     * @param activity   要显示的activity
     */
    public void showBitmap(final ImageView welcomeImg, final String imgUrl, final Activity activity) {
        int corePollSize = Runtime.getRuntime().availableProcessors();
        int maxSize = corePollSize * 2 + 1;
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                corePollSize, maxSize, 1000,
                TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(10)
                , Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        //开启线程池请求网络图片
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(imgUrl);
                    HttpURLConnection uct = (HttpURLConnection) url.openConnection();
                    uct.setRequestMethod("GET");
                    uct.setReadTimeout(10000);
                    uct.setConnectTimeout(10000);
                    int responseCode = uct.getResponseCode();
                    if (responseCode == 200) {
                        //子线程更新UI（设置显示网络图片）？是否会阻塞线程？出现ANR？？
                        InputStream inputStream = uct.getInputStream();
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                welcomeImg.setImageBitmap(bitmap);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
