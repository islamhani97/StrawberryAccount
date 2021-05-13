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
import com.islam.strawberryaccount.databinding.DialogShowPackageBinding;
import com.islam.strawberryaccount.pojo.Package;

public abstract class ShowPackageDialog {

    private DialogShowPackageBinding binding;
    private AlertDialog alertDialog;
    private Context context;

    private Animation rotateOpenAnim, rotateCloseAnim, showingAnim, hidingAnim;
    private boolean active;

    public ShowPackageDialog(Context context, Package aPackage,String language) {
        this.context = context;
        initDialog();
        binding.setLanguage(language);
        binding.setAPackage(aPackage);

        rotateOpenAnim = AnimationUtils.loadAnimation(context, R.anim.button_rotate_open);
        rotateCloseAnim = AnimationUtils.loadAnimation(context, R.anim.button_rotate_close);
        showingAnim = AnimationUtils.loadAnimation(context, R.anim.button_show_top);
        hidingAnim = AnimationUtils.loadAnimation(context, R.anim.button_hide_top);
        active = false;

        binding.dialogShowPackageOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        binding.dialogShowPackageFloatingActions.actionsGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configAction();
            }
        });


        binding.dialogShowPackageFloatingActions.actionEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEdit(aPackage);
                dismiss();

            }
        });

        binding.dialogShowPackageFloatingActions.actionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new MaterialAlertDialogBuilder(context, R.style.DialogTheme)
                        .setMessage(R.string.message_delete)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onDelete(aPackage);
                                ;
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
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_show_package, null, false);
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


    public abstract void onEdit(Package aPackage);

    public abstract void onDelete(Package aPackage);


    private void configAction() {
        active = !active;
        setVisibility(active);
        setAnimation(active);
        setClickable(active);
    }

    private void setAnimation(boolean active) {

        if (active) {
            binding.dialogShowPackageFloatingActions.actionsGroup.startAnimation(rotateOpenAnim);
            binding.dialogShowPackageFloatingActions.actionEdit.startAnimation(showingAnim);
            binding.dialogShowPackageFloatingActions.actionDelete.startAnimation(showingAnim);

        } else {
            binding.dialogShowPackageFloatingActions.actionsGroup.startAnimation(rotateCloseAnim);
            binding.dialogShowPackageFloatingActions.actionEdit.startAnimation(hidingAnim);
            binding.dialogShowPackageFloatingActions.actionDelete.startAnimation(hidingAnim);

        }

    }

    private void setVisibility(boolean active) {
        if (active) {
            binding.dialogShowPackageFloatingActions.actionEdit.setVisibility(View.VISIBLE);
            binding.dialogShowPackageFloatingActions.actionDelete.setVisibility(View.VISIBLE);
        } else {

            binding.dialogShowPackageFloatingActions.actionEdit.setVisibility(View.INVISIBLE);
            binding.dialogShowPackageFloatingActions.actionDelete.setVisibility(View.INVISIBLE);
        }


    }

    private void setClickable(boolean active) {

        if (active) {
            binding.dialogShowPackageFloatingActions.actionEdit.setClickable(true);
            binding.dialogShowPackageFloatingActions.actionEdit.setFocusable(true);
            binding.dialogShowPackageFloatingActions.actionDelete.setClickable(true);
            binding.dialogShowPackageFloatingActions.actionDelete.setFocusable(true);
        } else {

            binding.dialogShowPackageFloatingActions.actionEdit.setClickable(false);
            binding.dialogShowPackageFloatingActions.actionEdit.setFocusable(false);
            binding.dialogShowPackageFloatingActions.actionDelete.setClickable(false);
            binding.dialogShowPackageFloatingActions.actionDelete.setFocusable(false);
        }
    }


}
