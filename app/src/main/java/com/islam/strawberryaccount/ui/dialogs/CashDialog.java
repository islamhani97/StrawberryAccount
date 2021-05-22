package com.islam.strawberryaccount.ui.dialogs;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.islam.strawberryaccount.R;
import com.islam.strawberryaccount.databinding.DialogCashBinding;
import com.islam.strawberryaccount.pojo.Cash;
import com.islam.strawberryaccount.utils.Constants;

import java.util.Calendar;

public abstract class CashDialog {

    private DialogCashBinding binding;
    private Context context;
    private AlertDialog alertDialog;
    private Calendar calendar;


    public CashDialog(Context context, Cash cash, String actionText, String language) {
        this.context = context;
        calendar = Calendar.getInstance();
        initDialog();
        binding.dialogCashSave.setText(actionText);
        removeErrorWhenEdit(binding.dialogCashValue);

        if (cash.getValue() != null) {
            binding.dialogCashValue.getEditText().setText(cash.getValue().toString());
        }

        if (cash.getDate() == null) {
            cash.setDate(calendar.getTime());
        }
        binding.dialogCashDate.getEditText().setText(Constants.showDate(cash.getDate(), language));


        binding.dialogCashDate.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar setCalendar = Calendar.getInstance();
                        setCalendar.set(year,
                                month,
                                dayOfMonth,
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                calendar.get(Calendar.SECOND));

                        cash.setDate(setCalendar.getTime());
                        binding.dialogCashDate.getEditText().setText(Constants.showDate(cash.getDate(), language));

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        binding.dialogCashSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isErrorExist()) {
                    cash.setValue(Double.parseDouble(binding.dialogCashValue.getEditText().getText().toString()));
                    onSave(cash);
                    dismiss();
                }
            }
        });

        binding.dialogCashCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }

    private void initDialog() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_cash, null, false);
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

        if (binding.dialogCashValue.getEditText().getText().toString().trim().isEmpty()) {
            binding.dialogCashValue.setError(context.getString(R.string.error_field_required));
            error = true;
        } else if (!isLogicNumber(binding.dialogCashValue.getEditText().getText().toString().trim())) {
            binding.dialogCashValue.setError(context.getString(R.string.error_logic_number));
            error = true;
        }


        return error;
    }

    private boolean isLogicNumber(String number) {
        boolean logic = true;
        int dotsNumber = 0;

        for (int i = 0; i < number.length(); i++) {
            if (number.charAt(i) == '.') {
                dotsNumber++;
            }
            if (dotsNumber > 1) {
                logic = false;
                break;
            }
        }
        return logic;
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

    public abstract void onSave(Cash cash);

}
