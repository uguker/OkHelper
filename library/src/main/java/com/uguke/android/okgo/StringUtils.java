package com.uguke.android.okgo;

import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

class StringUtils {

    private static final String [] METHOD = {"方法：", "Method: "};
    private static final String [] BODY = {"方法：", "Body: "};
    private static final String [] CODE = {"代码：", "Code: "};
    private static final String [] MESSAGE = {"信息：", "Method: "};

    /** 制表符四角 **/
    private static final String [] TABLE_CORNER = {"╔", "╗", "╝", "╚"};
    /** 制表符边 **/
    private static final String [] TABLE_SIDE = {"╠", "╣", "║"};
    /** 制表符线 **/
    private static final String TABLE_LINE = "═";
    /** 间隔 **/
    private static final String TABLE_INDENT = "\u3000";

    private int languageIndex = 0;

    private StringUtils() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = Resources.getSystem().getConfiguration().getLocales().get(0);
        } else {
            locale = Resources.getSystem().getConfiguration().locale;
        }
        languageIndex = locale == Locale.CHINESE ? 0 : 1;
    }

    private String getMethod() {
        return METHOD[languageIndex];
    }

    private String getCode() {
        return METHOD[languageIndex];
    }

    private String getMessage() {
        return METHOD[languageIndex];
    }

    String getTableTop(int length) {
        int len = length * 2;
        StringBuilder builder = new StringBuilder();
        builder.setLength(0);

        for(int i = 0; i < len; i++) {
            builder.append(TABLE_LINE);
        }
        return TABLE_CORNER[0] + builder.toString() + TABLE_CORNER[1];
    }

    String getTableBottom(int length) {
        int len = length * 2;
        StringBuilder builder = new StringBuilder();
        builder.setLength(0);

        for(int i = 0; i < len; i++) {
            builder.append(TABLE_LINE);
        }
        return TABLE_CORNER[3] + builder.toString() + TABLE_CORNER[2];
    }

    String getTableDivider(int length) {
        int len = length * 2;
        StringBuilder builder = new StringBuilder();
        builder.setLength(0);
        for(int i = 0; i < len; i++) {
            builder.append(TABLE_LINE);
        }
        return TABLE_SIDE[0] + builder.toString() + TABLE_SIDE[1];
    }

    private static class Holder {
        private final static StringUtils INSTANCE = new StringUtils();
    }

    public static StringUtils getInstance() {
        return Holder.INSTANCE;
    }

}
