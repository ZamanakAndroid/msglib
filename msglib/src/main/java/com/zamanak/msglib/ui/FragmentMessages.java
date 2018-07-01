package com.zamanak.msglib.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.zamanak.msglib.MsgSdk;
import com.zamanak.msglib.R;
import com.zamanak.msglib.adapter.AdapterMessagesList;
import com.zamanak.msglib.api.base.BaseApi;
import com.zamanak.msglib.api.callbacks.ApiErrorCB;
import com.zamanak.msglib.api.callbacks.ApiSuccessCB;
import com.zamanak.msglib.api.requests.RequestGetMessages;
import com.zamanak.msglib.base.BaseFragment;
import com.zamanak.msglib.listeners.EndlessRvScrollListener;
import com.zamanak.msglib.listeners.OnInboxRowListener;
import com.zamanak.msglib.model.Message;
import com.zamanak.msglib.model.ResultModelMessages;
import com.zamanak.msglib.rows.MessageRowModel;
import com.zamanak.msglib.tools.FontUtils;
import com.zamanak.msglib.tools.MessageData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PirFazel on 1/29/2017.
 */

public class FragmentMessages extends BaseFragment implements
        AdapterMessagesList.ItemClickListener, AdapterMessagesList.RetryLoadMoreListener,
        OnInboxRowListener {

    int messages_count;
    ResultModelMessages resultMessages;
    RecyclerView recyclerView;
    private List<MessageRowModel> list = new ArrayList<>();
    int limit = 8;
    int offset = 0;
    AdapterMessagesList adapter;
    List<Message> messages = new ArrayList<>();
    private int currentSize = 0;

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
        return R.layout.fragment_inbox;
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

        /*mActivity.initToolbar(mActivity.getString(R.string.messages),
                0,
                true);*/
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
        recyclerView = getViewById(R.id.recyclerView);
    }

    @Override
    protected void initToolbarOnFragmentDestroy() {
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

        createLists();
        initRvData();
        //FirebaseLogUtils.logEvent(mActivity, "messages_visited");
    }

    private void createLists() {

        list = new ArrayList<>();
        messages = new ArrayList<>();
    }

    private void getMessages() {

        new RequestGetMessages(mActivity, new ApiSuccessCB() {
            @Override
            public void onSuccess(BaseApi service) {
                try {
                    Log.v("service_msg", service.resJson.toString());
                    messages_count = service.resJson.getJSONArray("result").length();
                    currentSize = messages_count + currentSize;
                    Log.v("result", String.valueOf(messages_count));
                    if (messages_count == 0) {
                        adapter.onReachEnd();
                    } else {
                        resultMessages = new Gson().fromJson(service.resJson.toString(),
                                ResultModelMessages.class);
                        Log.v("resultMessages", resultMessages.toString());
                        setData(resultMessages);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    adapter.onLoadMoreFailed();
                }
            }
        }, new ApiErrorCB() {
            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                //Log.v("service_msg", e.getMessage());
                adapter.onLoadMoreFailed();
            }
        }, MsgSdk.API_KEY, MsgSdk.URL_MESSAGES, limit, offset).execute();
    }

    private void initRvData() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new AdapterMessagesList(list, mActivity,
                this, this, this);
        recyclerView.setAdapter(adapter);
        EndlessRvScrollListener scrollListener =
                new EndlessRvScrollListener(linearLayoutManager) {
                    @Override
                    public void onLoadMore(int page) {
                        Log.v("message_page", page + "");
                        loadMore(page);
                    }
                };
        recyclerView.addOnScrollListener(scrollListener);
    }

    private void startDetailMessageFragment(int position) {

        try {
            if (mActivity.getTopFragment() instanceof FragmentMessages) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(MsgSdk.KEY_MESSAGE, messages.get(position));
                mActivity.addFragment(FragmentMessageDetail.class,
                        bundle, R.id.fragment_msg, true);
            }
            /*FirebaseLogUtils.logIdEvent(mActivity, "one_message_visited",
                    Long.parseLong(messages.get(position).getId()));*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setData(ResultModelMessages resultMessages) {

        for (int i = 0; i < messages_count; i++) {
            MessageRowModel model = new MessageRowModel(
                    resultMessages.result.get(i).getTitle(),
                    resultMessages.result.get(i).getMessage(),
                    resultMessages.result.get(i).getCreatedAtP(),
                    false);
            list.add(model);
            messages.add(resultMessages.result.get(i));
        }
        updateView();
    }

    private void updateView() {

        offset += limit;
        mContentView.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        }, 100);
    }

    public void loadMore(int page) {

        Log.v("loadMorePage", String.valueOf(page));
        adapter.startLoadMore();
        if (currentSize == offset) {
            getMessages();
        } else {
            adapter.onReachEnd();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
    }

    @Override
    public void onRetryLoadMore() {
        getMessages();
    }

    @Override
    public void onLinkClick(int position) {
        try {
            MessageData data = new Gson().fromJson(messages.get(position).getData().toString(), MessageData.class);
            if (data.getType().equals("link")) {
                String link = (String) ((LinkedTreeMap) (data.getContent())).get("link");
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(browserIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInboxRowClick(int position) {
        startDetailMessageFragment(position);
    }
}
