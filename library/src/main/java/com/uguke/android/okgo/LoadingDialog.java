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
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
/**
 * 加载对话框
 * @author LeiJue
 */
public class LoadingDialog extends DialogFragment implements Loading<LoadingDialog> {

    private TextView loadingText;
    private LoadingView loadingView;

    private int duration = 2000;
    private boolean waitingForDismiss;
    private ValueAnimator showAnimator;
    private ValueAnimator dismissAnimator;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            //doDismissAnimator();
            return false;
        }
    });

    public LoadingDialog() {
        super();
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
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, height);
        //View view = View.inflate(getActivity(), R.layout.dlg_ok_loading, null);
        FrameLayout view = new FrameLayout(getContext());

        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(120, 120);
        params2.gravity = Gravity.CENTER;
        // 获取控件
        loadingView = new LoadingView(getActivity());
        loadingView.setLayoutParams(params2);
        view.addView(loadingView);
        dialog.setContentView(view, params);
        //loadingView.setColors(Color.BLACK, Color.BLUE);
        loadingView.setColors(Color.BLACK, Color.BLUE, Color.RED);

        loadingView.setLeafCount(1);
        loadingView.setLeafSpace(30);
        loadingView.setArcWidth(5);
        loadingView.setRotateSpeed(100);
        //loadingView.setColors(Color.BLACK);
        loadingView.start();
        //color(Color.BLACK);
        doShowAnimator();
        return dialog;
    }

    public LoadingDialog dimEnable(boolean enable) {
        if (enable) {
            //mWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            //contentView.setBackgroundColor(Color.parseColor("#22888888"));
            loadingText.setTextColor(Color.parseColor("#F0F0F0"));
        } else {
            //mWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            //contentView.setBackgroundColor(Color.TRANSPARENT);
            loadingText.setTextColor(Color.parseColor("#999999"));
        }
        return this;
    }

//    @Override
//    public LoadingDialog color(@ColorInt int color) {
//        int temp = color;
//        if (color == Color.TRANSPARENT) {
//            temp = ContextCompat.getColor(getActivity(), R.color.colorAccent);
//        }
//        //DrawableCompat.setTint(loadingView.getIndeterminateDrawable(), temp);
//        return this;
//    }

    public LoadingDialog text(CharSequence text) {
        loadingText.setText(text);
        return this;
    }

    public LoadingDialog text(@StringRes int resId) {
        loadingText.setText(resId);
        return this;
    }

    public LoadingDialog size(float size) {
        float density = getActivity().getResources().getDisplayMetrics().density;
        loadingView.getLayoutParams().width = (int) (size * density);
        loadingView.getLayoutParams().height = (int) (size * density);
        loadingText.setTextSize(size / 4);
        loadingText.setPadding(0, (int) (size / 10 * density), 0, 0);
        return this;
    }

    @Override
    public void show(Context context) {

    }

    @Override
    public void dismiss() {
        //doDismissAnimator();
    }

    @Override
    public <T> T color(int color) {
                int temp = color;
        if (color == Color.TRANSPARENT) {
            temp = ContextCompat.getColor(getActivity(), R.color.colorAccent);
        }
        //DrawableCompat.setTint(loadingView.getIndeterminateDrawable(), temp);

        return (T) this;
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
