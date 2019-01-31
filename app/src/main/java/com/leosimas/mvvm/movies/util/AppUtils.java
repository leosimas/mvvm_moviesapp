package com.leosimas.mvvm.movies.util;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.StringRes;

public class AppUtils {

    private static Toast toast;

    public static void showKeyboard(Context context, EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideKeyboard(Context context, EditText editText) {
        editText.clearFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void showToastShort(Context context, @StringRes int stringId) {
        cancelToast();
        toast = Toast.makeText(context, stringId, Toast.LENGTH_SHORT);
        toast.show();
    }

    private static void cancelToast() {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }

}
