package com.zamanak.msglib.tools;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

/**
 * Created by PirFazel on 11/21/2016.
 */

public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String tag;
    private Context context;

    public BaseException(String tag, String msg, Context context) {

        super(msg);
        this.tag = tag;
        this.context = context;
        if (this.context instanceof Activity) {
            Log.w("BaseException", " from : "
                    + context.getPackageName());
            DispatchHandler();
        } else {
            Log.e("BaseException", "Tag: " + tag + " msg: " + msg);
        }
    }


    private void DispatchHandler() {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //TODO : handle error
            }
        });
    }
}
