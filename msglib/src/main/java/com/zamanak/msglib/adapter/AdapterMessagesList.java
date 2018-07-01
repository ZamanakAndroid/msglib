package com.zamanak.msglib.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.zamanak.msglib.MsgSdk;
import com.zamanak.msglib.R;
import com.zamanak.msglib.base.LoadMorRvAdapter;
import com.zamanak.msglib.listeners.OnInboxRowListener;
import com.zamanak.msglib.rows.MessageRowModel;
import com.zamanak.msglib.tools.FontUtils;
import com.zamanak.msglib.tools.MessageData;

import java.util.List;

public class AdapterMessagesList extends LoadMorRvAdapter<MessageRowModel> {

    private Context context;
    private static final int TYPE_ITEM = 1;
    private OnInboxRowListener onInboxRowListener;

    public AdapterMessagesList(List<MessageRowModel> list,
                               @NonNull Context context,
                               ItemClickListener itemClickListener,
                               @NonNull LoadMorRvAdapter.RetryLoadMoreListener retryLoadMoreListener,
                               OnInboxRowListener onInboxRowListener) {

        super(context, itemClickListener, retryLoadMoreListener);
        mDataList = list;
        this.onInboxRowListener = onInboxRowListener;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = mInflater.inflate(R.layout.row_messages, parent, false);
            return new ViewHolderMessageList(v);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,
                                 @SuppressLint("RecyclerView") final int position) {

        if (holder instanceof ViewHolderMessageList) {
            try {
                ((ViewHolderMessageList) holder).notifLink.setBackgroundColor(MsgSdk.ACCENT_COLOR);
                MessageRowModel message = mDataList.get(position);
                ((ViewHolderMessageList) holder).title.setText(FontUtils.textWithFont(context, message.title));
                ((ViewHolderMessageList) holder).desc.setText(FontUtils.textWithFont(context, message.desc));
                ((ViewHolderMessageList) holder).date.setText(FontUtils.textWithFont(context, message.date));
                /*if (message.seen) {
                    ((ViewHolderMessageList) holder).linearLayout.setBackgroundColor(Color.parseColor("#e9e9e9"));
                    ((ViewHolderMessageList) holder).title.setTypeface(null, Typeface.NORMAL);
                } else {*/
                ((ViewHolderMessageList) holder).linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
                ((ViewHolderMessageList) holder).title.setTypeface(null, Typeface.BOLD);
                // }
                ((ViewHolderMessageList) holder).linearLayout.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onInboxRowListener.onInboxRowClick(position);
                            }
                        });
                MessageData data = new Gson().fromJson(mDataList.get(position).data.toString(), MessageData.class);
                if (data.getType().equals("link")) {
                    String link = (String) ((LinkedTreeMap) (data.getContent())).get("link");
                    ((ViewHolderMessageList) holder).notifLink.setVisibility(View.VISIBLE);
                    ((ViewHolderMessageList) holder).notifLink.setText(FontUtils.textWithFont(context, link));
                    ((ViewHolderMessageList) holder).notifLink.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onInboxRowListener.onLinkClick(position);
                                }
                            });
                } else {
                    ((ViewHolderMessageList) holder).notifLink.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ((ViewHolderMessageList) holder).notifLink.setVisibility(View.GONE);
            }
        }
        super.onBindViewHolder(holder, position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    protected int getCustomItemViewType(int position) {
        return TYPE_ITEM;
    }
}
