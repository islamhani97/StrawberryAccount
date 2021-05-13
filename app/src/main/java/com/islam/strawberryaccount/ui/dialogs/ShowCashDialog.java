package com.islam.strawberryaccount.ui.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.islam.strawberryaccount.R;
import com.islam.strawberryaccount.databinding.DialogShowCashBinding;
import com.islam.strawberryaccount.pojo.Cash;

public abstract class ShowCashDialog {

    private DialogShowCashBinding binding;
    private AlertDialog alertDialog;
    private Context context;

    private Animation rotateOpenAnim, rotateCloseAnim, showingAnim, hidingAnim;
    private boolean active;

    public ShowCashDialog(Context context, Cash cash, String language) {
        this.context = context;
        initDialog();
        binding.setLanguage(language);
        binding.setCash(cash);

        rotateOpenAnim = AnimationUtils.loadAnimation(context, R.anim.button_rotate_open);
        rotateCloseAnim = AnimationUtils.loadAnimation(context, R.anim.button_rotate_close);
        showingAnim = AnimationUtils.loadAnimation(context, R.anim.button_show_top);
        hidingAnim = AnimationUtils.loadAnimation(context, R.anim.button_hide_top);
        active = false;


        binding.dialogShowCashOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.dialogShowCashFloatingActions.actionsGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configAction();
            }
        });


        binding.dialogShowCashFloatingActions.actionEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onEdit(cash);
                dismiss();

            }
        });

        binding.dialogShowCashFloatingActions.actionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new MaterialAlertDialogBuilder(context, R.style.DialogTheme)
                        .setMessage(R.string.message_delete)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onDelete(cash);
                                dismiss();
                            }
                        })
                        .setNegativeButton(R.string.no, null)
                        .setCancelable(false)
                        .setBackground(context.getDrawable(R.drawable.shape_dialog_background))
                        .show();

            }
        });

    }

    private void initDialog() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_show_cash, null, false);
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

    public abstract void onEdit(Cash cash);

    public abstract void onDelete(Cash cash);


    private void configAction() {
        active = !active;
        setVisibility(active);
        setAnimation(active);
        setClickable(active);
    }

    private void setAnimation(boolean active) {

        if (active) {
            binding.dialogShowCashFloatingActions.actionsGroup.startAnimation(rotateOpenAnim);
            binding.dialogShowCashFloatingActions.actionEdit.startAnimation(showingAnim);
            binding.dialogShowCashFloatingActions.actionDelete.startAnimation(showingAnim);

        } else {
            binding.dialogShowCashFloatingActions.actionsGroup.startAnimation(rotateCloseAnim);
            binding.dialogShowCashFloatingActions.actionEdit.startAnimation(hidingAnim);
            binding.dialogShowCashFloatingActions.actionDelete.startAnimation(hidingAnim);

        }

    }

    private void setVisibility(boolean active) {
        if (active) {
            binding.dialogShowCashFloatingActions.actionEdit.setVisibility(View.VISIBLE);
            binding.dialogShowCashFloatingActions.actionDelete.setVisibility(View.VISIBLE);
        } else {

            binding.dialogShowCashFloatingActions.actionEdit.setVisibility(View.INVISIBLE);
            binding.dialogShowCashFloatingActions.actionDelete.setVisibility(View.INVISIBLE);
        }


    }

    private void setClickable(boolean active) {

        if (active) {
            binding.dialogShowCashFloatingActions.actionEdit.setClickable(true);
            binding.dialogShowCashFloatingActions.actionEdit.setFocusable(true);
            binding.dialogShowCashFloatingActions.actionDelete.setClickable(true);
            binding.dialogShowCashFloatingActions.actionDelete.setFocusable(true);
        } else {

            binding.dialogShowCashFloatingActions.actionEdit.setClickable(false);
            binding.dialogShowCashFloatingActions.actionEdit.setFocusable(false);
            binding.dialogShowCashFloatingActions.actionDelete.setClickable(false);
            binding.dialogShowCashFloatingActions.actionDelete.setFocusable(false);
        }
    }

}
