package com.zcwipe.photoselector.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;

import com.zcwipe.photoselector.R;


/**
 * Created by Administrator on 2017/3/7/007.
 */

public class CustomProgressDialog extends Dialog {

    private Context context;
    private static CustomProgressDialog dialog = null;

    public CustomProgressDialog(Context context) {
        super(context);
        this.context = context;
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public static CustomProgressDialog createDialog(Context context) {
        dialog = new CustomProgressDialog(context, R.style.CustomProgressDialog);
        dialog.setContentView(R.layout.dialog_progress);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        return dialog;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
    }
}
