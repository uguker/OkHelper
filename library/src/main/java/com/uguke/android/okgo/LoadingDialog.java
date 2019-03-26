package com.uguke.android.okgo;


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

    private Window mWindow;
    private View contentView;
    private TextView loadingText;
    private ProgressBar loadingView;

    public LoadingDialog() {
        super();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    //    public LoadingDialog(@NonNull Context context) {
//        super(context, R.style.OkLoadingTheme);
//        // 设置无标题
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//        Window window = getWindow();
//        if (window != null) {
//            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        }
//        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, height);
//        contentView = View.inflate(getContext(), R.layout.dlg_ok_loading, null);
//        // 设置布局
//        setContentView(contentView, params);
//        setCanceledOnTouchOutside(false);
//        setCancelable(false);
//        // 获取控件
//        loadingView = findViewById(R.id.ok_loading);
//        loadingText = findViewById(R.id.ok_text);
//        loadingText.setTextColor(Color.parseColor("#F0F0F0"));
//        dimEnable(true);
//    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.OkLoadingTheme);
        //super(context, R.style.OkLoadingTheme);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window = getActivity().getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, height);
        contentView = View.inflate(getActivity(), R.layout.dlg_ok_loading, null);

        dialog.setContentView(contentView, params);
        // 设置布局
        //setCanceledOnTouchOutside(false);
        //contentView.setLayoutParams(params);
        setCancelable(false);
        // 获取控件
        loadingView = contentView.findViewById(R.id.ok_loading);
        loadingText = contentView.findViewById(R.id.ok_text);
        loadingText.setTextColor(Color.parseColor("#F0F0F0"));
        //dimEnable(true);
        //builder.setView(contentView);

        //AlertDialog dialog = builder.create();

        return dialog;
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

    public void superDismiss() {
        super.dismiss();
    }

    @Override
    public void dismiss() {

        loadingText.postDelayed(new Runnable() {
            @Override
            public void run() {
                superDismiss();
            }
        }, 1500);


    }
}
