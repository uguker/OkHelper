package com.uguke.android.okgo;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

/**
 *
 * @author 31081
 * @date 2019/3/27
 */
public interface Loading<T extends Loading> {

    <T> T size(float size);

    <T> T colors(int... color);

    void show(FragmentActivity activity, String tag);

    void dismiss();


}
