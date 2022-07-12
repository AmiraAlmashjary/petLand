package com.company.petLand.helpers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager;

import androidx.fragment.app.FragmentActivity;

import com.company.petLand.R;

public class LoadingDialog {
    private Dialog loadingDialog;
    private FragmentActivity activity_;


    public  LoadingDialog(FragmentActivity activity) {
        activity_ = activity;
        loadingDialog = new Dialog(activity);
        setUpLoadingDialog();

    }

    public void disableInteractions() {
        activity_.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void enableInteractions() {
        activity_.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void setUpLoadingDialog() {
        loadingDialog = new Dialog(activity_);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void showLoadingDialog() {
        loadingDialog.show();
        disableInteractions();


    }

    public  void hideLoadingDialog() {
        loadingDialog.dismiss();
        enableInteractions();

    }
}
