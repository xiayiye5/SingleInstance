package com.example.singleinstance;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiayiye5
 * 2020年7月10日15:27:44
 * 下拉刷新的 Activity
 */
public class RefreshActivity extends Activity implements RefreshListView.OnRefreshListener, RefreshListView.OnLoadingMoreListener {

    private List<String> dataLists;
    private MyAdapter myAdapter;
    private RefreshListView refreshListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
        //[1]找到我们关心的控件
        refreshListView = (RefreshListView) findViewById(R.id.refreshListView1);

        dataLists = new ArrayList<String>();
        for (int i = 0; i < 25; i++) {
            dataLists.add("这是一条微博数据" + i);

        }
        //[3]设置数据适配器
        myAdapter = new MyAdapter();
        refreshListView.setAdapter(myAdapter);
        //[4]设置刷新监听
        refreshListView.setOnRefreshListener(this);
        //[5]设置加载更多数据
        refreshListView.setOnLoadingMoreListener(this);

    }


    //定义listview的数据适配器
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataLists.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv;
            if (convertView == null) {
                tv = new TextView(getApplicationContext());
            } else {
                tv = (TextView) convertView;
            }
            tv.setText(dataLists.get(position));
            tv.setTextSize(20);
            tv.setTextColor(Color.BLACK);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            return tv;
        }

    }


    @Override
    public void onRefresh() {
        new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(3000);
                dataLists.add(0, "中午请大家吃饭");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //更新数据适配器
                        myAdapter.notifyDataSetChanged();

                        //设置加载完成后的逻辑
                        refreshListView.setOnLoadFinish();
                    }
                });
            }
        }.start();
    }


    @Override
    public void onLoadingMore() {
        new Thread() {
            @Override
            public void run() {

                SystemClock.sleep(3000);
                dataLists.add("中午请大家吃饭 是真的");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //更新数据适配器
                        myAdapter.notifyDataSetChanged();

                        //设置加载完成后的逻辑
                        refreshListView.setOnLoadIngMoreFinish();
                    }
                });
            }
        }.start();
    }
}
