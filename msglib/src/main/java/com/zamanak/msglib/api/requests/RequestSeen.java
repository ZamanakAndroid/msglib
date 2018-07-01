package com.zamanak.msglib.api.requests;

import android.content.Context;

import com.zamanak.msglib.MsgSdk;
import com.zamanak.msglib.api.base.BaseApi;
import com.zamanak.msglib.api.callbacks.ApiErrorCB;
import com.zamanak.msglib.api.callbacks.ApiSuccessCB;

import org.json.JSONException;

/**
 * Created by PirFazel on 1/29/2017.
 */

public class RequestSeen extends BaseApi {

    private String inboxId;

    public RequestSeen(Context context, ApiSuccessCB outerSuccessCB,
                       ApiErrorCB outerErrorCB, String api_key, String inboxId) {
        super(context, MsgSdk.URL_BASE, MsgSdk.URL_SEEN, outerSuccessCB,
                outerErrorCB, true, false);
        this.inboxId = inboxId;
        this.api_key = api_key;
    }

    @Override
    protected void prepareRequest() throws JSONException {
        reqJson.put(MsgSdk.KEY_INBOX_ID, inboxId);
    }
}