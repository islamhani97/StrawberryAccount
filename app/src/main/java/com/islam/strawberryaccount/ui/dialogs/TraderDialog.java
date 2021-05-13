package com.islam.strawberryaccount.ui.dialogs;

import android.content.Context;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.islam.strawberryaccount.R;
import com.islam.strawberryaccount.databinding.DialogTraderBinding;
import com.islam.strawberryaccount.pojo.Trader;

public abstract class TraderDialog {

    private DialogTraderBinding binding;
    private Context context;
    private AlertDialog alertDialog;

    public TraderDialog(Context context, Trader trader, String actionText) {
        this.context = context;
        initDialog();
        binding.dialogTraderSave.setText(actionText);
        removeErrorWhenEdit(binding.dialogTraderTraderName);

        if (trader.getName() != null) {
            binding.dialogTraderTraderName.getEditText().setText(trader.getName());
        }


        binding.dialogTraderSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isErrorExist()) {
                    trader.setName(binding.dialogTraderTraderName.getEditText().getText().toString());
                    onSave(trader);
                    dismiss();
                }
            }
        });

        binding.dialogTraderCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }

    private void initDialog() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_trader, null, false);
        alertDialog = new MaterialAlertDialogBuilder(context, R.style.DialogTheme)
                .setView(binding.getRoot())
                .setCancelable(false)
                .setBackground(context.getDrawable(R.drawable.shape_dialog_background))
                .create();

    }

    public void show() {
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    private void dismiss() {
        if (alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    private boolean isErrorExist() {
        boolean error = false;

        if (binding.dialogTraderTraderName.getEditText().getText().toString().trim().isEmpty()) {
            binding.dialogTraderTraderName.setError(context.getString(R.string.error_field_required));
            error = true;
        } else if (binding.dialogTraderTraderName.getEditText().getText().toString().trim().length() < 3) {
            binding.dialogTraderTraderName.setError(context.getString(R.string.error_name_length));
            error = true;
        }
        return error;
    }

    private void removeErrorWhenEdit(TextInputLayout... textInputLayouts) {

        for (TextInputLayout inputLayout : textInputLayouts) {

            inputLayout.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    inputLayout.setErrorEnabled(false);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }

    }

    public abstract void onSave(Trader trader);

}
