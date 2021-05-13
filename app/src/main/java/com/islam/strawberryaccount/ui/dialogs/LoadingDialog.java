package com.islam.strawberryaccount.ui.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.islam.strawberryaccount.R;

public class LoadingDialog {

    private AlertDialog alertDialog;

    public LoadingDialog(Context context) {
        alertDialog = new MaterialAlertDialogBuilder(context)
                .setView(LayoutInflater.from(context).inflate(R.layout.dialog_loading, null, false))
                .setCancelable(false)
                .setBackground(new ColorDrawable(Color.TRANSPARENT))
                .create();
    }

    public boolean show() {
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
        return true;
    }

    public boolean dismiss() {
        if (alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        return false;
    }


}
