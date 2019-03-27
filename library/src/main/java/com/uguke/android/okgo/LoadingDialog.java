package com.uguke.android.okgo;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
/**
 * 加载对话框
 * @author LeiJue
 */
public class LoadingDialog extends DialogFragment implements Loading<LoadingDialog> {

    private float minAngle;
    private float addAngle;

    private int arcCount;
    private float arcIntervalAngle;
    private float arcShakeRatio;
    private float arcStrokeWidth;
    private int [] arcColors;
    private int roundUseTime;

    private LoadingView loadingView;
    private float loadingSize;

    private int duration = 500;
    private ValueAnimator showAnimator;
    private ValueAnimator dismissAnimator;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            doDismissAnimator();
            return false;
        }
    });

    public LoadingDialog() {
        super();
        loadingSize = 40;
        arcCount = 1;
        arcIntervalAngle = 30;
        arcShakeRatio = 0.1f;
        arcStrokeWidth = 3;
        minAngle = 30;
        addAngle = 270;
        roundUseTime = 100;
        arcColors = new int[]{};
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.OkLoadingTheme);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        if (window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        // 设置布局控件
        FrameLayout view = new FrameLayout(getActivity());
        dialog.setContentView(view, new ViewGroup.LayoutParams(width, height));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                (int) (dm.density * loadingSize), (int) (dm.density * loadingSize));
        params.gravity = Gravity.CENTER;
        // 获取控件
        loadingView = new LoadingView(getActivity());
        loadingView.setLayoutParams(params);
        loadingView.setArcColors(arcColors)
                .setArcCount(arcCount)
                .setArcIntervalAngle(arcIntervalAngle)
                .setArcShakeRatio(arcShakeRatio)
                .setArcStrokeWidth(arcStrokeWidth)
                .setMinAngle(minAngle)
                .setAddAngle(addAngle)
                .setRoundUseTime(roundUseTime)
                .start();
        view.addView(loadingView);
        doShowAnimator();
        return dialog;
    }

    public LoadingDialog arcCount(@IntRange(from = 1) int count) {
        arcCount = count > 1 ? count : 1;
        return this;
    }

    public LoadingDialog arcIntervalAngle(float angle) {
        arcIntervalAngle = angle;
        return this;
    }

    public LoadingDialog arcShakeRatio(float ratio) {
        this.arcShakeRatio = ratio;
        return this;
    }

    public LoadingDialog arcStrokeWidth(float width) {
        arcStrokeWidth = width;
        return this;
    }

    public LoadingDialog arcColors(@ColorInt int... colors) {
        arcColors = colors;
        return this;
    }

    /**
     * 设置最小角度（仅对叶数为1有效）
     * @param min 最小角度
     */
    public LoadingDialog minAngle(float min) {
        minAngle = min;
        return this;
    }

    /**
     * 设置增量角度（仅对叶数为1有效）
     * @param add 增量角度
     */
    public LoadingDialog addAngle(float add) {
        addAngle = add;
        return this;
    }

    /**
     * 设置转一圈需要时间
     * @param time 转一圈需要时间
     */
    public LoadingDialog roundUseTime(int time) {
        roundUseTime = time;
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T size(float size) {
        loadingSize = size;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T colors(int... color) {
        arcColors = color;
        return (T) this;
    }

    @Override
    public void show(FragmentActivity activity, String tag) {
        show(activity.getSupportFragmentManager(), tag);
    }

    @Override
    public void dismiss() {
        doDismissAnimator();
    }

    private void doShowAnimator() {
        final float startValue = 0.3f;
        final float endValue = 1f;
        showAnimator = ValueAnimator.ofFloat(startValue, endValue).setDuration(duration);
        showAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (loadingView == null) {
                    showAnimator.cancel();
                    return;
                }
                float value = (float) animation.getAnimatedValue();
                refreshView(loadingView, value);
            }
        });
        showAnimator.start();
    }

    private void doDismissAnimator() {
        if (showAnimator.isRunning()) {
            handler.sendEmptyMessageDelayed(0, duration);
            return;
        }
        final float startValue = 1f;
        final float endValue = 0.0f;
        dismissAnimator = ValueAnimator.ofFloat(startValue, endValue).setDuration(duration);
        dismissAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (loadingView == null) {
                    dismissAnimator.cancel();
                    return;
                }
                float value = (float) animation.getAnimatedValue();
                refreshView(loadingView, value);
                if (value == endValue) {
                    LoadingDialog.super.dismiss();
                }
            }
        });
        dismissAnimator.start();
    }

    private void refreshView(View view, float value) {
        view.setAlpha(value);
        //view.setScaleX(value);
        //view.setScaleY(value);
    }


}
