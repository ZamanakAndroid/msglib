package com.zamanak.msglib.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.zamanak.msglib.R;

/**
 * Created by PirFazel on 2/4/2017.
 */

public abstract class NetworkUtils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static int msgForVolleyError(VolleyError error) {
        int message = 0;
        if (error instanceof NoConnectionError) {
            message = R.string.plz_connect_to_the_internet;
        } else if (error instanceof TimeoutError) {
            message = R.string.timeoutError;
        } else if (error instanceof NetworkError) {
            message = R.string.networkError;
        } else if (error instanceof ServerError) {
            message = R.string.serverError;
        } else if (error instanceof AuthFailureError) {
            message = R.string.authFailureError;
        } else if (error instanceof ParseError) {
            message = R.string.parseError;
        }
        return message;
    }

}
