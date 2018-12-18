package com.uguke.okgo;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.lzy.okgo.OkGo;

/**
 * 功能描述：
 * @author LeiJue
 * @date 2018/11/19
 */
public class LoadingStrategy implements Cloneable {

    private int mColor;
    private float mSize;
    private boolean mDimEnable;
    private String mText;
    private int mTextId = View.NO_ID;

    public LoadingStrategy() {
        mSize = 60;
        mDimEnable = false;
    }

    public LoadingStrategy setColor(int color) {
        mColor = color;
        return this;
    }

    public LoadingStrategy setColor(String color) {
        mColor = Color.parseColor(color);
        return this;
    }

    public LoadingStrategy setSize(float size) {
        mSize = size;
        return this;
    }

    public LoadingStrategy setDimEnable(boolean dimEnable) {
        mDimEnable = dimEnable;
        return this;
    }

    public LoadingStrategy setText(String text) {
        mText = text;
        return this;
    }

    public LoadingStrategy setTextId(int textId) {
        mTextId = textId;
        return this;
    }

    public int getColor() {
        if (mColor == 0) {
            mColor = ContextCompat.getColor(OkGo.getInstance().getContext(), R.color.colorAccent);
        }
        return mColor;
    }

    public float getSize() {
        return mSize;
    }

    public boolean isDimEnable() {
        return mDimEnable;
    }

    public String getText() {
        if (TextUtils.isEmpty(mText) && mTextId != View.NO_ID) {
            mText = OkGo.getInstance().getContext().getString(mTextId);
        }
        return mText;
    }

    @Override
    public LoadingStrategy clone() {
        try {
            return (LoadingStrategy) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
