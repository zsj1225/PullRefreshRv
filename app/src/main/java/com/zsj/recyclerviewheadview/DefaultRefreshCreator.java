package com.zsj.recyclerviewheadview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author 朱胜军
 * @date 2018/5/20
 * 描述	      默认样式的头部刷新 如淘宝、京东、不同的样式可以自己去实现
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */
public class DefaultRefreshCreator extends RefreshViewCreator {
    private static final String TAG = "DefaultRefreshCreator";
    // 加载数据的ImageView
    private View mRefreshIv;

    @Override
    public View getRefreshView(Context context, ViewGroup parent) {
        View refreshView = LayoutInflater.from(context).inflate(R.layout.layout_refresh_header_view, parent, false);
        mRefreshIv = refreshView.findViewById(R.id.refresh_iv);
        return refreshView;
    }

    @Override
    public void onPull(int currentDragHeight, int refreshViewHeight, int currentRefreshStatus) {
//        Log.d(TAG, "onPull: currentDragHeight =" + currentDragHeight + " , refreshViewHeight =" + refreshViewHeight);
        float rotate = 1.0f + ((float) currentDragHeight) / refreshViewHeight;
//        Log.d(TAG, "onPull rotate =" + rotate);
        if (rotate >= 0.6f) {
            // 不断下拉的过程中不断的旋转图片
            float alpha = (refreshViewHeight +currentDragHeight- (refreshViewHeight*0.4f) )/ (refreshViewHeight*0.6f);
//            Log.d(TAG, "onPull: left ="+(refreshViewHeight +currentDragHeight- (refreshViewHeight*0.4f)));
//            Log.d(TAG, "onPull: right ="+((refreshViewHeight*0.6f)));
//            Log.d(TAG, "onPull: alpha ="+alpha);
            mRefreshIv.setAlpha(alpha);
        }else {
            mRefreshIv.setAlpha(0.0f);
        }
    }

    @Override
    public void onRefreshing() {
        // 刷新的时候不断旋转
//        RotateAnimation animation = new RotateAnimation(0, 720,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        animation.setRepeatCount(-1);
//        animation.setDuration(1000);
//        mRefreshIv.startAnimation(animation);
    }

    @Override
    public void onStopRefresh() {
        // 停止加载的时候清除动画
        mRefreshIv.setRotation(0);
        mRefreshIv.clearAnimation();
    }
}