package com.example.singleinstance;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class SlidingMenu extends ViewGroup {

    private int downX;
    //代表左侧菜单
    private final int LEFT_MENU = 0;
    //代表右侧内容
    private final int RIGHT_CONTENT = 1;

    private int currentScreen = RIGHT_CONTENT;
    private Scroller msScroller;
    private int pressX;
    private int pressY;

    public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        //创建sclroller对象
        msScroller = new Scroller(context);

    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SlidingMenu(Context context) {
        this(context, null);
    }

    // 对子控件进行测量

    /**
     * widthMeasureSpec 根屏幕一样宽 heightMeasureSpec 跟 屏幕一样高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // [1]找到左侧菜单和右侧 内容
        View leftMenu = getChildAt(0);
        View rightContent = getChildAt(1);
        // [2]对子控件进行测量
        leftMenu.measure(leftMenu.getLayoutParams().width, heightMeasureSpec);
        rightContent.measure(widthMeasureSpec, heightMeasureSpec);

    }

    // 对子控件进行排版

    /**
     * l:代表slidingmenu的左边线 t:上边线 r:右边线 : 屏幕的宽 b:地边线 :屏幕的高
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // [1]找到左侧菜单和右侧 内容
        View leftMenu = getChildAt(0);
        View rightContent = getChildAt(1);
        // [2]对子控件进行排版
        leftMenu.layout(-leftMenu.getMeasuredWidth(), 0, 0, b);
        rightContent.layout(l, t, r, b);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pressX = (int) ev.getX();
                pressY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();
                int diffX = Math.abs(moveX - pressX);
                int diffY = Math.abs(moveY - pressY);
                if (diffX > diffY && diffX > 10) {
                    //说明 侧滑
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                //[1]手指按下的downx
                downX = (int) event.getX();

                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                //[2]计算偏移量
                int diffX = downX - moveX;

                //[3]边界值进行判断
                //获取X轴指向内容的偏移量
                int currentScrollX = getScrollX() + diffX;
//			System.out.println("scrollX:"+currentScrollX);
                if (currentScrollX < -getChildAt(0).getWidth()) {
                    scrollTo(-getChildAt(0).getWidth(), 0);
                } else if (currentScrollX > 0) {
                    scrollTo(0, 0);
                } else {

                    scrollBy(diffX, 0);
                }
                downX = moveX;


                break;

            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                int center = -getChildAt(0).getWidth() / 2;

                if (scrollX > center) {
//				System.out.println("切换到右侧内容 ");
                    currentScreen = RIGHT_CONTENT;
                    SwitchScreen();

                } else {

                    currentScreen = LEFT_MENU;
//				System.out.println("切换到菜单页面");
                    SwitchScreen();
                }
                break;
            default:
                break;

        }
        //让当前控件处理此事件
        return true;

    }

    //切换屏幕
    private void SwitchScreen() {
        int startX = getScrollX();
        // dx = 目标 - startX
        int dx = 0;
        if (currentScreen == LEFT_MENU) {
//			scrollTo(-getChildAt(0).getWidth(), 0);
            dx = -getChildAt(0).getWidth() - startX;
        } else if (currentScreen == RIGHT_CONTENT) {
//			scrollTo(0, 0);
            dx = 0 - startX;
        }
        //开始模拟数据
        msScroller.startScroll(startX, 0, dx, 0, Math.abs(dx) * 10);
        // drawChild->child.draw-->computeScroll
        invalidate();
    }

    @Override
    public void computeScroll() {
        //取出我们模拟的数据
        if (msScroller.computeScrollOffset()) {
            int currX = msScroller.getCurrX();
            scrollTo(currX, 0);
            invalidate();
        }
    }
}
