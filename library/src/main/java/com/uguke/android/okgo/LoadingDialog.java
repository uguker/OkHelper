package com.uguke.android.okgo;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.uguke.okgo.R;

/**
 * 功能描述：Loading对话框
 * @author LeiJue
 */
public class LoadingDialog extends Dialog {

    private Window mWindow;
    private View contentView;
    private TextView loadingText;
    private ProgressBar loadingView;

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.OkLoadingTheme);
        // 设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, height);
        contentView = View.inflate(getContext(), R.layout.dlg_ok_loading, null);
        // 设置布局
        setContentView(contentView, params);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        // 获取控件
        loadingView = findViewById(R.id.ok_loading);
        loadingText = findViewById(R.id.ok_text);
        loadingText.setTextColor(Color.parseColor("#F0F0F0"));
        dimEnable(true);
    }

    public LoadingDialog dimEnable(boolean enable) {
        if (enable) {
            //mWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            contentView.setBackgroundColor(Color.parseColor("#22888888"));
            loadingText.setTextColor(Color.parseColor("#F0F0F0"));
        } else {
            //mWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            contentView.setBackgroundColor(Color.TRANSPARENT);
            loadingText.setTextColor(Color.parseColor("#999999"));
        }
        return this;
    }

    public LoadingDialog color(@ColorInt int color) {
        int temp = color;
        if (color == Color.TRANSPARENT) {
            temp = ContextCompat.getColor(getContext(), R.color.colorAccent);
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
        float density = getContext().getResources().getDisplayMetrics().density;
        loadingView.getLayoutParams().width = (int) (size * density);
        loadingView.getLayoutParams().height = (int) (size * density);
        loadingText.setTextSize(size / 4);
        loadingText.setPadding(0, (int) (size / 10 * density), 0, 0);
        return this;
    }

}
