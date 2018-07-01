package com.zamanak.msglib.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.zamanak.msglib.MsgSdk;
import com.zamanak.msglib.R;
import com.zamanak.msglib.adapter.ViewHolderMessageList;
import com.zamanak.msglib.api.base.BaseApi;
import com.zamanak.msglib.api.callbacks.ApiErrorCB;
import com.zamanak.msglib.api.callbacks.ApiSuccessCB;
import com.zamanak.msglib.api.requests.RequestSeen;
import com.zamanak.msglib.base.BaseFragment;
import com.zamanak.msglib.model.Message;
import com.zamanak.msglib.tools.FontUtils;
import com.zamanak.msglib.tools.MessageData;

/**
 * Created by PirFazel on 1/29/2017.
 */

public class FragmentMessageDetail extends BaseFragment {

    Message message;
    TextView title;
    TextView date;
    TextView desc;
    Button notifLink;

    private void getMessage() {
        try {
            Bundle bundle = getArguments();
            message = (Message) bundle.getSerializable(MsgSdk.KEY_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected int getToolbarIcon() {
        return 0;
    }

    @Override
    protected int getToolbarTitle() {
        return R.string.messages;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_message_detail;
    }

    @Override
    protected int getToolbarMenuLayout() {
        return 0;
    }

    @Override
    protected boolean hasToolbarBackPressedBtn() {
        return true;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        try {
            title = getViewById(R.id.title);
            desc = getViewById(R.id.desc);
            date = getViewById(R.id.date);
            notifLink = getViewById(R.id.notifLink);
            notifLink.setBackgroundColor(MsgSdk.ACCENT_COLOR);

            Toolbar toolbar = new Toolbar(mActivity);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            toolbar.setLayoutParams(layoutParams);
            toolbar.setPopupTheme(R.style.AppTheme);
            toolbar.setBackgroundColor(MsgSdk.PRIMARY_COLOR);
            toolbar.setTitle(FontUtils.textWithFont(mActivity, R.string.messages));
            toolbar.setVisibility(View.VISIBLE);
            LinearLayout ll = mContentView.findViewById(R.id.linear);
            ll.addView(toolbar, 0);
            mActivity.setSupportActionBar(toolbar);

            MessageData data = new Gson().fromJson(message.getData().toString(), MessageData.class);
            if (data.getType().equals("link")) {
                String link = (String) ((LinkedTreeMap) (data.getContent())).get("link");
                notifLink.setVisibility(View.VISIBLE);
                notifLink.setText(FontUtils.textWithFont(mActivity, link));
            } else {
                notifLink.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            notifLink.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initToolbarOnFragmentDestroy() {
        /*mActivity.initToolbar(mActivity.getString(R.string.messages),
                0,
                true);*/
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        getMessage();
        addData();
        /*if (!message.isSeen()) {
            msgSeen();
        }*/
        mActivity.initToolbar(mActivity.getString(R.string.messages),
                0,
                true);
    }

    private void addData() {

        title.setText(FontUtils.textWithFont(mActivity, message.getTitle()));
        desc.setText(FontUtils.textWithFont(mActivity, message.getMessage()));
        date.setText(FontUtils.textWithFont(mActivity, message.getCreatedAtP()));
    }

    private void msgSeen() {
        new RequestSeen(getContext(), new ApiSuccessCB() {
            @Override
            public void onSuccess(BaseApi service) {
                Log.v("msgSeen", service.resJson.toString());
            }
        }, new ApiErrorCB() {
            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                Log.e("msgSeen", e.getMessage());
            }
        }, MsgSdk.API_KEY_BASE, message.getId()).execute();
    }
}
