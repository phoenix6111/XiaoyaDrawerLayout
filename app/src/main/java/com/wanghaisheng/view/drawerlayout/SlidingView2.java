package com.wanghaisheng.view.drawerlayout;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.wanghaisheng.util.DestyUtil;
import com.wanghaisheng.xiaoyadrawerlayout.R;

/**
 * Author: sheng on 2016/9/29 13:20
 * Email: 1392100700@qq.com
 */

public class SlidingView2 extends HorizontalScrollView {

    public static final int DEFAULT_MENU_RIGHTPADDING = 100;

    //最外层包裹的ViewGroup
    private LinearLayout mViewWrapper;

    //菜单区域
    private ViewGroup mMenuView;
    //内容区域
    private ViewGroup mContentView;

    //菜单全部显示时与右边的padding，即是内容显示的宽度
    private int mMenuRightPadding;
    //屏幕的宽度
    private int mScreenWidth;
    //左边菜单的宽度
    private int mMenuWidth;

    //是否已经测量过的标志
    private boolean hasMeasured;

    //菜单是否打开的标志
    private boolean isMenuOpened;

    public SlidingView2(Context context) {
        this(context,null);
    }

    public SlidingView2(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlidingView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取自定义属性值
        getAttrValue(attrs);

        //获取屏幕的宽度
        mScreenWidth = DestyUtil.getScreenWidth(getContext());

    }

    /**
     * 获取自定义属性值
     * @param attrs
    */
    private void getAttrValue(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.SlidingView);
        mMenuRightPadding = (int) ta.getDimension(R.styleable.SlidingView_menu_right_padding,DestyUtil.dp2px(getContext(),DEFAULT_MENU_RIGHTPADDING));

        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(!hasMeasured) {
            //获取最外层包裹的ViewGroup
            mViewWrapper = (LinearLayout) getChildAt(0);

            //获取Menu
            mMenuView = (ViewGroup) mViewWrapper.getChildAt(0);
            //设置菜单的宽度为屏幕宽度减去右边的padding
            mMenuWidth = mMenuView.getLayoutParams().width = mScreenWidth - mMenuRightPadding;

            //获取Content
            mContentView = (ViewGroup) mViewWrapper.getChildAt(1);
            //设置内容区域的宽度为屏幕的宽度
            mContentView.getLayoutParams().width  = mScreenWidth;

            hasMeasured = true;
        }

    }

    /**
     * 初始状态将menu隐藏
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if(changed) {
            scrollTo(mMenuWidth,0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        //只用处理手指抬起时的状态，当手指抬起时，如果菜单滑动距离大于菜单内容的一半，则显示菜单，否则隐藏菜单
        int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_UP:
                //scroolX为菜单隐藏的宽度
                int scrollX = getScrollX();
                if(scrollX >= mMenuWidth/2) {
                    //隐藏菜单
                    smoothScrollTo(mMenuWidth,0);
                    isMenuOpened = false;
                } else {
                    smoothScrollTo(0,0);
                    isMenuOpened = true;
                }

                return true;
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 打开菜单
     */
    public void openMenu() {
        if(!isMenuOpened) {
            smoothScrollTo(0,0);
            isMenuOpened = true;
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if(isMenuOpened) {
            smoothScrollTo(mMenuWidth,0);
            isMenuOpened = false;
        }
    }

    /**
     * 切换菜单的状态
     */
    public void toggleMenu() {
       if(isMenuOpened) {
           closeMenu();
       } else {
           openMenu();
       }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        float scale = 1.0f-l*1.0f/mMenuWidth;
        ObjectAnimator.ofFloat(mMenuView,"translationX",l*(0.7f+0.3f*scale)).setDuration(0).start();
    }
}
