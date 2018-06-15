package com.uguke.okgo;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 功能描述：Loading对话框
 * @author  雷珏
 * @date    2018/06/15
 */
public class LoadingDialog extends Dialog {

    private Window window;
    private View contentView;
    private TextView loadingText;
    private ProgressBar loading;
    
    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.OkLoadingTheme);
        window = getWindow();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        setContentView(R.layout.ok_loading_dialog);
        contentView = window.getDecorView();
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        loading = findViewById(R.id.ok_loading_progress);
        loadingText = findViewById(R.id.ok_loading_text);
        loadingText.setTextColor(Color.parseColor("#F0F0F0"));
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        if (window == null) {
            super.dismiss();
            return;
        }

        ValueAnimator animator = ValueAnimator.ofFloat(1f, 0.4f)
                .setDuration(250);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                float value = (float) animation.getAnimatedValue();
                contentView.setScaleX(value);
                contentView.setScaleY(value);
                contentView.setAlpha(value);
                if (fraction == 1) {
                    superDismiss();
                }
            }
        });
        animator.start();

    }

    public LoadingDialog dimEnabled(boolean dimEnabled) {
        if (dimEnabled) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            loadingText.setTextColor(Color.parseColor("#F0F0F0"));
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            loadingText.setTextColor(Color.parseColor("#999999"));
        }
        return this;
    }

    public LoadingDialog color(@ColorInt int color) {
        DrawableCompat.setTint(loading.getIndeterminateDrawable(), color);
        return this;
    }

    public LoadingDialog color(String color) {
        DrawableCompat.setTint(loading.getIndeterminateDrawable(), Color.parseColor(color));
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

    private void superDismiss() {
        super.dismiss();
    }
}
