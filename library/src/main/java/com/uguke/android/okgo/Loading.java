package com.uguke.android.okgo;

import android.content.Context;

/**
 * Created by 31081 on 2019/3/27.
 */

public interface Loading<T extends Loading> {

    void show(Context context);

    void dismiss();

    <T> T color(int color);


}
