package com.uguke.okgo.reflect;

import android.annotation.TargetApi;
import android.os.Build;

/**
 * 功能描述：类型异常
 * @author  雷珏
 * @date    2018/06/14
 */
public class TypeException extends RuntimeException {

    public TypeException() {}

    public TypeException(String message) {
        super(message);
    }

    public TypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TypeException(Throwable cause) {
        super(cause);
    }

    @TargetApi(Build.VERSION_CODES.N)
    public TypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
