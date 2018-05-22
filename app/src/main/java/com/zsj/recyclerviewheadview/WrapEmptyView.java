package com.zsj.recyclerviewheadview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 *
 * @author shengjun.zhu1
 * @date 2018/5/22
 */

public class WrapEmptyView extends LinearLayout {
    private static final String TAG = "TAG";
    // 下拉刷新的辅助类
    private RefreshViewCreator mRefreshCreator;
    // 下拉刷新头部的高度
    private int mRefreshViewHeight = 0;
    // 下拉刷新的头部View
    private View mRefreshView;
    // 手指按下的Y位置
    private int mFingerDownY;
    // 手指拖拽的阻力指数
    private float mDragIndex = 0.5f;
    // 当前是否正在拖动
    private boolean mCurrentDrag = false;
    // 当前的状态
    private int mCurrentRefreshStatus;
    // 默认状态
    public int REFRESH_STATUS_NORMAL = 0x0011;
    // 下拉刷新状态
    public int REFRESH_STATUS_PULL_DOWN_REFRESH = 0x0022;
    // 松开刷新状态
    public int REFRESH_STATUS_LOOSEN_REFRESHING = 0x0033;
    // 正在刷新状态
    public int REFRESH_STATUS_REFRESHING = 0x0033;
    //空布局
    private View mEmptyView;

    public WrapEmptyView(Context context) {
        super(context);
        init(context);
    }

    public WrapEmptyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WrapEmptyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
    }

    // 先处理下拉刷新，同时考虑刷新列表的不同风格样式，确保这个项目还是下一个项目都能用
    // 所以我们不能直接添加View，需要利用辅助类
    public void addRefreshViewCreator(RefreshViewCreator refreshCreator) {
        this.mRefreshCreator = refreshCreator;
        addRefreshView();
    }


    /**
     * 添加头部的刷新View
     */
    public void addRefreshView() {
        if (mRefreshCreator != null) {
            // 添加头部的刷新View
            View refreshView = mRefreshCreator.getRefreshView(getContext(), this);
            if (refreshView != null) {
                this.mRefreshView = refreshView;
                addView(mRefreshView);
            }
        }
    }

    /**
     * 添加空白的布局
     * @param emptyView
     */
    public void addEmptyView(View emptyView){
        mEmptyView = emptyView;
        addView(emptyView);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            if (mRefreshView != null && mRefreshViewHeight <= 0) {
                // 获取头部刷新View的高度
                mRefreshViewHeight = mRefreshView.getMeasuredHeight();
                if (mRefreshViewHeight > 0) {
                    // 隐藏头部刷新的View  marginTop  多留出1px防止无法判断是不是滚动到头部问题
//                    TODO
                    setPullRefreshViewMarginTop(-mRefreshViewHeight + 1);
                }
            }
        }
    }

    /**
     * 设置刷新View的marginTop
     */
    public void setPullRefreshViewMarginTop(int marginTop) {
        MarginLayoutParams params = (MarginLayoutParams) mRefreshView.getLayoutParams();
        //如果头布局底部了.就不能再继续往下拉了
        if (marginTop < -mRefreshViewHeight + 1) {
            marginTop = -mRefreshViewHeight + 1;
        }else if (marginTop >=0){
            marginTop = 0;
        }
        params.topMargin = marginTop;
        mRefreshView.setLayoutParams(params);
    }

    //自定义ViewGroup监听 onTouchEvent
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent e) {
//        switch (e.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                break;
//            case MotionEvent.ACTION_MOVE:
//                //必须要在MOVE中return才有效果，在这里return后UP事件也会被拦截
//                return true;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                break;
//        }
//        return super.onInterceptTouchEvent(e);
//    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录手指按下的位置 ,之所以写在dispatchTouchEvent那是因为如果我们处理了条目点击事件，
                // 那么就不会进入onTouchEvent里面，所以只能在这里获取
                mFingerDownY = (int) ev.getRawY();
                break;

            case MotionEvent.ACTION_UP:
                if (mCurrentDrag) {
                    restorePullRefreshView();
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // 获取手指触摸拖拽的距离
                int distanceY = (int) ((e.getRawY() - mFingerDownY) * mDragIndex);
                Log.d(TAG, "onTouchEvent: distanceY =" +distanceY +", mFingerDownY ="+mFingerDownY +"getRawY ="+e.getRawY());
                // 如果是已经到达头部，并且不断的向下拉，那么不断的改变refreshView的marginTop的值
                if (distanceY > 0) {
                    int marginTop = distanceY - mRefreshViewHeight;
                    setPullRefreshViewMarginTop(marginTop);
                    updateRefreshStatus(marginTop);
                    mCurrentDrag = true;
                    return false;
                }
                break;
            default:
                break;
        }

        //让LinearLayout捕获触摸事件
//        return super.onTouchEvent(e);
        return true;
    }



    /**
     * 更新刷新的状态
     */
    private void updateRefreshStatus(int marginTop) {
        if (marginTop <= -mRefreshViewHeight) {
            mCurrentRefreshStatus = REFRESH_STATUS_NORMAL;
        } else if (marginTop < 0) {
            mCurrentRefreshStatus = REFRESH_STATUS_PULL_DOWN_REFRESH;
        } else {
            mCurrentRefreshStatus = REFRESH_STATUS_LOOSEN_REFRESHING;
        }

        if (mRefreshCreator != null) {
            mRefreshCreator.onPull(marginTop, mRefreshViewHeight, mCurrentRefreshStatus);
        }
    }


    /**
     * 重置当前刷新状态状态
     */
    public void restorePullRefreshView() {
        int currentTopMargin = ((MarginLayoutParams) mRefreshView.getLayoutParams()).topMargin;
        int finalTopMargin = -mRefreshViewHeight + 1;
        Log.d("zsj", "restorePullRefreshView: currentTopMargin ="+currentTopMargin+" ,finalTopMargin ="+finalTopMargin);
        if (mCurrentRefreshStatus == REFRESH_STATUS_LOOSEN_REFRESHING) {
            finalTopMargin = 0;
            mCurrentRefreshStatus = REFRESH_STATUS_REFRESHING;
            if (mRefreshCreator != null) {
                mRefreshCreator.onRefreshing();
            }
            if (mListener != null) {
                mListener.onRefresh();
            }
        }

        int distance = currentTopMargin - finalTopMargin;

        // 回弹到指定位置
        ValueAnimator animator = ObjectAnimator.ofFloat(currentTopMargin, finalTopMargin).setDuration(distance);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentTopMargin = (float) animation.getAnimatedValue();
                setPullRefreshViewMarginTop((int) currentTopMargin);
            }
        });
        animator.start();
        mCurrentDrag = false;
    }

    // 处理刷新回调监听
    private WrapRecyclerView.OnRefreshListener mListener;

    public void setOnRefreshListener(WrapRecyclerView.OnRefreshListener listener) {
        this.mListener = listener;
    }

    public interface OnRefreshListener {
        void onRefresh();
    }


    /**
     * 停止刷新
     */
    public void onStopPullRefresh() {
        mCurrentRefreshStatus = REFRESH_STATUS_NORMAL;
        restorePullRefreshView();
        if (mRefreshCreator != null) {
            mRefreshCreator.onStopRefresh();
        }
    }


}
