package com.findmysalon.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import com.findmysalon.R;

public final class Helper {
    private static final String TAG = "Helper";

    public static void errorMsgDialog(@NonNull Context context, @StringRes int  msg)
    {
        AlertDialog d = new AlertDialog.Builder(context)
                .setTitle(R.string.warning)
                .setMessage(msg)
                .setPositiveButton(R.string.ok, null)
                .create();

        d.show();
    }
}
