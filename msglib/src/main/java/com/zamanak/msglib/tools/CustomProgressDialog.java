package com.zamanak.msglib.tools;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.zamanak.msglib.R;
import com.zamanak.msglib.listeners.OnDismiss;


/**
 * Created by PirFazel on 12/14/2016.
 */

public class CustomProgressDialog extends ProgressDialog implements OnDismiss {
    private Context context;
    private CustomProgressDialog progressDialog;
    private int theme;
    private OnDismiss onDismiss = null;

    public CustomProgressDialog(Context context) {
        super(context);
        this.context = context;
        this.theme = R.style.AppTheme_Dark_Dialog;
    }

    public CustomProgressDialog(Context context, OnDismiss onDismiss) {
        super(context);
        this.onDismiss = onDismiss;
        this.context = context;
        this.theme = R.style.AppTheme_Dark_Dialog;
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        this.theme = theme;
    }

    public void showProgressDialog(String msg) {
        progressDialog = new CustomProgressDialog(context,
                theme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(msg);
        progressDialog.show();

    }

    public void showProgressDialog(int msg) {
        progressDialog = new CustomProgressDialog(context,
                theme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(context.getString(msg));
        progressDialog.show();
    }

    public void showProgressDialog(int msg, boolean cancelable) {
        progressDialog = new CustomProgressDialog(context,
                theme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(context.getString(msg));
        progressDialog.setCancelable(cancelable);
        progressDialog.setCanceledOnTouchOutside(cancelable);
        progressDialog.show();
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
        if (onDismiss != null) {
            onDismiss.onDismiss();
        }
    }


    public void showProgressDialogWithCancelButton() {
        progressDialog = new CustomProgressDialog(context,
                theme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(context.getString(R.string.plz_wait));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.cancel), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onDismiss();
            }
        });
        progressDialog.show();
    }

    public void showProgressDialog() {
        progressDialog = new CustomProgressDialog(context,
                theme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(context.getString(R.string.plz_wait));
        progressDialog.show();

        progressDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                onDismiss();
            }
        });
    }


    public CustomProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void onDismiss() {
        if (onDismiss != null) {
            onDismiss.onDismiss();
        }
    }
}
