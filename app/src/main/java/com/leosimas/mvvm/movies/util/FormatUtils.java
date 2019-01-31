package com.leosimas.mvvm.movies.util;

import android.content.Context;
import android.text.format.DateUtils;

import java.util.Date;

public class FormatUtils {

    public static String formatDate(Context context, Date date) {
        if (date == null) {
            return "";
        }
        return DateUtils.formatDateTime(context, date.getTime(),DateUtils.FORMAT_SHOW_DATE
                | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_WEEKDAY);
    }

}
