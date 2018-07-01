package com.zamanak.msglib.api.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zamanak.msglib.MsgSdk;
import com.zamanak.msglib.R;
import com.zamanak.msglib.api.callbacks.ApiErrorCB;
import com.zamanak.msglib.api.callbacks.ApiSuccessCB;
import com.zamanak.msglib.base.BaseActivity;
import com.zamanak.msglib.listeners.OnDismiss;
import com.zamanak.msglib.tools.BaseException;
import com.zamanak.msglib.tools.CustomProgressDialog;
import com.zamanak.msglib.tools.ErrorTags;
import com.zamanak.msglib.tools.NetworkUtils;

import static com.zamanak.msglib.tools.JsonUtils.trimMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class BaseApi implements OnDismiss {

    protected Context context;
    protected JSONObject reqJson = new JSONObject();
    public JSONObject resJson;
    protected String phone;
    protected String token;
    protected String api_key;
    protected String url;
    protected String path;
    protected String email;
    protected String code;
    protected ApiSuccessCB apiSuccessCB;
    protected ApiErrorCB apiErrorCB;
    private boolean isPost;
    private boolean hasProgressDialog;
    private CustomProgressDialog customProgressDialog = null;
    private JsonObjectRequest jsObjRequest;
    protected String endPoint = null;

    public BaseApi(Context context, String url, String path,
                   ApiSuccessCB apiSuccessCB, ApiErrorCB apiErrorCB,
                   boolean isPost, boolean hasProgressDialog) {

        this.context = context;
        this.isPost = isPost;
        this.hasProgressDialog = hasProgressDialog;
        this.url = url;
        this.path = path;
        this.apiSuccessCB = apiSuccessCB;
        this.apiErrorCB = apiErrorCB;
        instantiateProgressDialog();
        handleToken();
    }

    private void handleToken() {
        if (token != null) {
            token = token.toLowerCase();
        }
    }

    private void instantiateProgressDialog() {
        if (hasProgressDialog) {
            customProgressDialog =
                    new CustomProgressDialog(context, this);
        }
    }

    protected void prepareRequest() throws JSONException {
    }

    public void execute() {

        try {
            if (isProgressEnable()) {
                customProgressDialog.showProgressDialogWithCancelButton();
            }
            prepareRequest();
            if (endPoint == null) {
                url += path;
            } else {
                url = endPoint;
            }
        } catch (JSONException e) {
            BaseException baseException = new BaseException(ErrorTags.ERROR_SEND_REQUEST,
                    e.getMessage(), context);
            baseException.setStackTrace(e.getStackTrace());
            throw baseException;
        }
        if (isPost) {
            sendRequest(Request.Method.POST);
            return;
        }
        sendRequest(Request.Method.GET);
    }

    protected Response.ErrorListener onError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

            try {
                Log.e("VolleyError", error.toString());
                if (isProgressEnable()) {
                    customProgressDialog.getProgressDialog().dismiss();
                    int msg = NetworkUtils.msgForVolleyError(error);
                    if (msg != 0) {
                        ((BaseActivity) context).onError(context.getString(msg));
                    }
                }
                handleErrorResponse(error);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void handleErrorResponse(VolleyError error) {

        if (customProgressDialog != null) {
            customProgressDialog.getProgressDialog().dismiss();
        }
        NetworkResponse response = error.networkResponse;
        if (response != null && response.data != null) {
            String jsonObj = new String(response.data);
            String json = trimMessage(jsonObj, "drug");
            Log.v("statusCode", "" + response.statusCode);
            if (json != null) {
                apiErrorCB.onError(new BaseException(response.statusCode + "", json, context));
            } else {
                Log.v("statusCode", "" + response.statusCode);
                if (isProgressEnable()) {
                    ((BaseActivity) context).onError(context.getString(R.string.plz_try_again));
                }
                apiErrorCB.onError(error);
            }
        } else {
            apiErrorCB.onError(error);
        }
    }

    private void sendRequest(int method) {

        JSONObject jsonRequest = null;
        if (method == Request.Method.POST) {
            jsonRequest = reqJson;
        }
        jsObjRequest = new JsonObjectRequest(method, url, jsonRequest, onResponse, onError) {

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                return BaseApi.this.getHeaders();
            }
        };
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,
                2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    private Map<String, String> getHeaders() {

        Map<String, String> params = new HashMap<>();
        token = MsgSdk.TOKEN;
        if (token != null) {
            params.put(MsgSdk.HEADER_TOKEN, token);
            Log.v("session_token", token);
        }
        if (api_key != null) {
            params.put(MsgSdk.HEADER_API_KEY, api_key);
            Log.v("api_key", api_key);
        }
        return params;
    }

    private Response.Listener onResponse = new Response.Listener<JSONObject>() {

        @Override
        public void onResponse(JSONObject response) {
            if (isProgressEnable()) {
                customProgressDialog.getProgressDialog().dismiss();
            }
            resJson = response;
            successOnResponse();
        }
    };

    private void successOnResponse() {
        if (apiSuccessCB != null) {
            apiSuccessCB.onSuccess(this);
        }
    }

    private boolean isProgressEnable() {
        return hasProgressDialog && customProgressDialog != null;
    }

    @Override
    public void onDismiss() {
        jsObjRequest.cancel();
    }
}