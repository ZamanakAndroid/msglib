package com.zamanak.msglib.tools;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.zamanak.msglib.R;

/**
 * Created by PirFazel on 1/11/2017.
 */

public abstract class HandleNoConnectionError {
    public static int msgForVolleyError(VolleyError error) {
        int message = 0;
        if (error instanceof NoConnectionError) {
            message = R.string.plz_connect_to_the_internet;
        } else if (error instanceof TimeoutError) {
            message = R.string.plz_try_again;
        } else if (error instanceof NetworkError) {
            message = R.string.plz_connect_to_the_internet;
        }/* else if (error instanceof ServerError) {
            message = R.string.plz_try_again;
        } else if (error instanceof AuthFailureError) {
            message = R.string.plz_connect_to_the_internet;
        } else if (error instanceof ParseError) {
            message = R.string.plz_try_again;
        }*/
        return message;
    }
}
