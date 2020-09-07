package com.example.singleinstance;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Administrator
 */
public class RefreshListView extends ListView implements OnScrollListener {
    private int downY;
    /**
     * 头布局的高度
     */
    private int headerHeight;
    /**
     * 头布局
     */
    private View mheaderView;
    /**
     * 代表下拉刷新状态 ctrl + shift + X
     */
    private final int PULL_REFRESH_STATE = 0;
    // 释放刷新状态
    private final int RELEASE_STATE = 1;
    // 正在刷新状态
    private final int RELEASEING = 2;
    /**
     * 默认当前是下拉刷新状态
     */
    private int pullRefreshState = PULL_REFRESH_STATE;
    /**
     * 向上旋转的动画
     */
    private RotateAnimation upAnimation;
    /**
     * 向下旋转的动画
     */
    private RotateAnimation downAnimation;
    private ProgressBar pbProgress;
    private TextView tvUpdateState;
    private TextView tvLastUpdateTime;
    private ImageView ivArrow;
    /**
     * 默认没有滑动到底部
     */
    private boolean isStop = false;
    private OnRefreshListener mOnRefreshListener;
    private OnLoadingMoreListener mOnLoadingMoreListener;
    private View mfootView;
    //脚布局高度
    private int footHeight;

    public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initHeader();
        initFootView();
    }

    //初始化脚布局
    private void initFootView() {
        //[1]通过打气筒把一个布局转换成一个view对象
        mfootView = View.inflate(getContext(), R.layout.list_footer_view, null);
        mfootView.measure(0, 0);
        footHeight = mfootView.getMeasuredHeight();
        //[1]默认情况隐藏脚布局
        mfootView.setPadding(0, -footHeight, 0, 0);

        //[2]添加脚布局
        this.addFooterView(mfootView);
        //[3]给listview设置滑动监听
        this.setOnScrollListener(this);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public RefreshListView(Context context) {
        this(context, null);
    }

    /**
     * 添加listview的头
     */
    private void initHeader() {
        // [1]把我们画的自定义头布局转换成一个View
        mheaderView = View.inflate(getContext(), R.layout.list_header_view, null);
        //[1.1]找到关心的控件
        pbProgress = (ProgressBar) mheaderView.findViewById(R.id.pb_progress);
        tvUpdateState = (TextView) mheaderView.findViewById(R.id.tv_updat_state);
        tvLastUpdateTime = (TextView) mheaderView.findViewById(R.id.tv_last_update_time);
        ivArrow = (ImageView) mheaderView.findViewById(R.id.iv_arrow);
        //[1.1.1]初始化设置一下 时间
        tvLastUpdateTime.setText(getCurrentTimer());
        // [2]获取mheaderview控件的高度
        // int height = mheaderView.getHeight(); //此方法是当控件完全显示到屏幕后 才可以获取高度
        // [3]对viwe进行测量
        // 让系统帮助我们去测量
        mheaderView.measure(0, 0);
        headerHeight = mheaderView.getMeasuredHeight();
        // System.out.println("测量后的高度:"+headerHeight);
        mheaderView.setPadding(0, -headerHeight, 0, 0);
        //[4]添加头布局
        this.addHeaderView(mheaderView);
        //[5]初始化 向上 向下图片旋转动画
        initAnim();
    }

    /**
     * 初始化头布局 图片旋转的动画
     */
    private void initAnim() {
        //向上旋转的动画
        upAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //设置动画执行的时长
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true);
        //向下旋转的动画
        downAnimation = new RotateAnimation(-180, -360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //设置动画执行的时长
        downAnimation.setDuration(500);
        downAnimation.setFillAfter(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            // 按下
            case MotionEvent.ACTION_DOWN:
                // [1]获取按下的Y轴的位置
                downY = (int) ev.getY();
                break;
            //移动
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getY();
                // [2]计算移动的距离
                int diffY = moveY - downY;
                // [3]当listview处于第一个条目索引 并且 移动的距离>0才显示头布局
                if (getFirstVisiblePosition() == 0 && diffY > 0) {
                    // [4]算出paddinTop
                    int paddingTop = -headerHeight + diffY / 2;

                    // [4.1]判断paddingTop 来更新头布局
                    if (paddingTop > 0 && pullRefreshState != RELEASE_STATE) {
                        // System.out.println("进入释放刷新状态");
                        pullRefreshState = RELEASE_STATE;
                        // [4.2]更新头布局状态
                        updateHeaderView();
                    } else if (paddingTop < 0
                            && pullRefreshState != PULL_REFRESH_STATE) {
                        // System.out.println("~~~~下拉刷新状态~~~~");
                        pullRefreshState = PULL_REFRESH_STATE;
                        updateHeaderView();
                    }
                    // [5]设置头布局
                    mheaderView.setPadding(0, paddingTop, 0, 0);
                    // 让当前view处理事件
                    return true;
                }

                break;
            //手抬起事件
            case MotionEvent.ACTION_UP:
                if (pullRefreshState == PULL_REFRESH_STATE) {
                    updateHeaderView();
                } else if (pullRefreshState == RELEASE_STATE) {
                    //[1]把正在刷新状态 赋值给 当前状态
                    pullRefreshState = RELEASEING;
                    //[2]调用更新头布局的方法
                    updateHeaderView();
                    //[3]更新为下拉状态
//				header_current_state =PULL_REFRESH_STATE;
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onRefresh();
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 更新头布局的方法
     */
    private void updateHeaderView() {
        switch (pullRefreshState) {
            //下拉刷新状态
            case PULL_REFRESH_STATE:
                //[1]更改iv状态
                ivArrow.startAnimation(downAnimation);
                //[2]设置 tv_update 状态
                tvUpdateState.setText("下拉刷新");
                //[3]隐藏头布局
                mheaderView.setPadding(0, -headerHeight, 0, 0);
                break;
            //释放刷新状态
            case RELEASE_STATE:
                ivArrow.startAnimation(upAnimation);
                tvUpdateState.setText("释放刷新");
                break;
            //正在刷新的状态
            case RELEASEING:
                //[1]把动画图片隐藏
                ivArrow.setVisibility(View.INVISIBLE);
                ivArrow.clearAnimation();
                //[2]显示进度条
                pbProgress.setVisibility(View.VISIBLE);
                //[3]刷新状态的文字改为 正在刷新
                tvUpdateState.setText("正在刷新ing");
                //[4]设置头布局回到屏幕顶部
                mheaderView.setPadding(0, 0, 0, 0);
                //[5]设置上次更新时间
                //tv_last_update_time.setText(getCurrentTime());
                break;
            default:
                break;
        }
    }

    /**
     * 设置刷新的监听
     *
     * @param listener 监听
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefreshListener = listener;

    }

    public void setOnLoadingMoreListener(OnLoadingMoreListener l) {
        mOnLoadingMoreListener = l;
    }


    public interface OnLoadingMoreListener {
        public void onLoadingMore();
    }

    /**
     * 刷新数据的接口
     */
    public interface OnRefreshListener {
        public void onRefresh();
    }

    public String getCurrentTimer() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    /**
     * 设置刷新数据后的逻辑
     */
    @SuppressLint("SimpleDateFormat")
    public void setOnLoadFinish() {
        //[0]进度条隐藏
        pbProgress.setVisibility(View.INVISIBLE);
        //[2]在这里更新时间
        tvLastUpdateTime.setText(getCurrentTimer());
        //[3]隐藏头布局
        mheaderView.setPadding(0, -headerHeight, 0, 0);
        //[4]把状态置为下拉状态
        pullRefreshState = PULL_REFRESH_STATE;
    }

    //当状态发生改变的时候
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
            //判断LisView 是否滑动到了底部
            if (getLastVisiblePosition() == getCount() - 1 && !isStop) {
                System.out.println("listview滑动到了底部");
                isStop = true;
                //[1]把脚布局显示出来
                mfootView.setPadding(0, 0, 0, 0);
                if (mOnLoadingMoreListener != null) {
                    mOnLoadingMoreListener.onLoadingMore();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    /**
     * 加载更多完成 需要处理的逻辑
     */
    public void setOnLoadIngMoreFinish() {
        //[1]把加载更多脚布局隐藏
        mfootView.setPadding(0, -footHeight, 0, 0);
        isStop = false;
    }
}
