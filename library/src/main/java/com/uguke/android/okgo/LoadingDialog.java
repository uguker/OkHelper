package com.uguke.android.okgo;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * 加载对话框
 * @author LeiJue
 */
public class LoadingDialog extends DialogFragment implements Loading<LoadingDialog> {

    private float arcMinAngle;
    private float arcAddAngle;

    private int arcCount;
    private float arcIntervalAngle;
    private float arcShakeRatio;
    private float arcStrokeWidth;
    private int [] arcColors;
    private int roundUseTime;

    private LoadingView loadingView;
    private float loadingSize;

    private int duration = 500;
    private boolean dimEnabled = false;
    private ValueAnimator showAnimator;
    private ValueAnimator dismissAnimator;
    private Window window;

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
        arcStrokeWidth = 4;
        arcMinAngle = 30;
        arcAddAngle = 270;
        roundUseTime = 100;
        arcColors = new int[]{};
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        window = dialog.getWindow();
        if (window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dimEnabled(dimEnabled);
        setCancelable(false);
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
                .setArcMinAngle(arcMinAngle)
                .setArcAddAngle(arcAddAngle)
                .setRoundUseTime(roundUseTime)
                .start();
        view.addView(loadingView);
        doShowAnimator();
        return dialog;
    }

    public LoadingDialog arcCount(@IntRange(from = 1) int count) {
        arcCount = count > 1 ? count : 1;
        if (loadingView != null) {
            loadingView.setArcCount(arcCount);
        }
        return this;
    }

    public LoadingDialog arcIntervalAngle(float angle) {
        arcIntervalAngle = angle;
        if (loadingView != null) {
            loadingView.setArcIntervalAngle(arcIntervalAngle);
        }
        return this;
    }

    public LoadingDialog arcShakeRatio(float ratio) {
        this.arcShakeRatio = ratio;
        if (loadingView != null) {
            loadingView.setArcShakeRatio(arcShakeRatio);
        }
        return this;
    }

    public LoadingDialog arcStrokeWidth(float width) {
        arcStrokeWidth = width;
        if (loadingView != null) {
            loadingView.setArcStrokeWidth(arcStrokeWidth);
        }
        return this;
    }

    public LoadingDialog arcColors(@ColorInt int... colors) {
        arcColors = colors;
        if (loadingView != null) {
            loadingView.setArcColors(arcColors);
        }
        return this;
    }

    /**
     * 设置最小角度（仅对叶数为1有效）
     * @param angle 最小角度
     */
    public LoadingDialog arcMinAngle(float angle) {
        arcMinAngle = angle;
        if (loadingView != null) {
            loadingView.setArcMinAngle(angle);
        }
        return this;
    }

    /**
     * 设置增量角度（仅对叶数为1有效）
     * @param angle 增量角度
     */
    public LoadingDialog arcAddAngle(float angle) {
        arcAddAngle = angle;
        if (loadingView != null) {
            loadingView.setArcAddAngle(angle);
        }
        return this;
    }

    /**
     * 设置转一圈需要时间
     * @param time 转一圈需要时间
     */
    public LoadingDialog roundUseTime(int time) {
        roundUseTime = time;
        if (loadingView != null) {
            loadingView.setRoundUseTime(time);
        }
        return this;
    }

    public LoadingDialog duration(int duration) {
        this.duration = duration;
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public LoadingDialog dimEnabled(boolean enabled) {
        dimEnabled = enabled;
        if (window != null) {
            if (enabled) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public LoadingDialog size(float size) {
        loadingSize = size;
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public LoadingDialog colors(int... color) {
        arcColors = color;
        return this;
    }

    @Override
    public void show(Activity activity, String tag) {
        show(activity.getFragmentManager(), tag);
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
                loadingView.setAlpha(value);
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
                loadingView.setAlpha(value);
                if (value == endValue) {
                    LoadingDialog.super.dismiss();
                }
            }
        });
        dismissAnimator.start();
    }

}
