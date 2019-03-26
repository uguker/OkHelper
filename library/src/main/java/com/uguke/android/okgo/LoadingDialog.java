package com.uguke.android.okgo;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 加载对话框
 * @author LeiJue
 */
public class LoadingDialog extends DialogFragment {

    private TextView loadingText;
    private ProgressBar loadingView;

    private int duration = 300;
    private boolean waitingForDismiss;
    private ValueAnimator showAnimator;
    private ValueAnimator dismissAnimator;

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
        View view = View.inflate(getActivity(), R.layout.dlg_ok_loading, null);
        dialog.setContentView(view, params);
        // 获取控件
        loadingView = view.findViewById(R.id.ok_loading);
        loadingText = view.findViewById(R.id.ok_text);
        loadingText.setTextColor(Color.parseColor("#F0F0F0"));
        loadingText.setText("加载中");
        color(Color.BLACK);
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

    public LoadingDialog color(@ColorInt int color) {
        int temp = color;
        if (color == Color.TRANSPARENT) {
            temp = ContextCompat.getColor(getActivity(), R.color.colorAccent);
        }
        DrawableCompat.setTint(loadingView.getIndeterminateDrawable(), temp);
        return this;
    }

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
    public void dismiss() {
        waitingForDismiss = showAnimator != null;
        doDismissAnimator();
    }

    private void doShowAnimator() {
        final float startValue = 0.5f;
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
                refreshView(loadingText, value);
                if (value == endValue && waitingForDismiss) {
                    showAnimator = null;
                    dismiss();
                }
            }
        });
        showAnimator.start();
    }

    private void doDismissAnimator() {
        if (waitingForDismiss) {
            return;
        }
        final float startValue = 1f;
        final float endValue = 0.5f;
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
                refreshView(loadingText, value);
                if (value == endValue) {
                    LoadingDialog.super.dismiss();
                }
            }
        });
        dismissAnimator.start();
    }

    private void refreshView(View view, float value) {
        view.setAlpha(value);
        view.setScaleX(value);
        view.setScaleY(value);
    }


}
