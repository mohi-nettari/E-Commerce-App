package com.example.ecommerce;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

public class LoadingDialog {
   private Activity activity;
    private AlertDialog alertDialog;

    public LoadingDialog(Activity activityl) {
        this.activity = activityl;
    }

    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_loading, null));

        alertDialog = builder.create();
        alertDialog.show();
    }
        public void dismisLoadingDialog(){
            alertDialog.dismiss();

        }


}
