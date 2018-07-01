package com.zamanak.msglib.api.requests;

import android.content.Context;

import com.zamanak.msglib.MsgSdk;
import com.zamanak.msglib.api.base.BaseApi;
import com.zamanak.msglib.api.callbacks.ApiErrorCB;
import com.zamanak.msglib.api.callbacks.ApiSuccessCB;

import org.json.JSONException;

/**
 * Created by PirFazel on 2/22/2017.
 */

public class RequestGetMessages extends BaseApi {

    private int limit;
    private int offset;

    public RequestGetMessages(Context context, ApiSuccessCB outerSuccessCB,
                              ApiErrorCB outerErrorCB, String api_key, String url, int limit, int offset) {
        super(context, MsgSdk.URL_BASE, url, outerSuccessCB, outerErrorCB,
                true, false);
        this.limit = limit;
        this.offset = offset;
        this.api_key = api_key;
    }

    @Override
    protected void prepareRequest() throws JSONException {

        reqJson.put("limit", limit);
        reqJson.put("offset", offset);
    }
}