package com.zamanak.msglib.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zamanak.msglib.R;


/**
 * Created by PIRI on 11/4/2017.
 */
public abstract class LoadMorRvAdapter<T> extends BaseRvAdapter<T> {

    private static final int TYPE_PROGRESS = 0xFFFF;
    private final Context context;
    private RetryLoadMoreListener mRetryLoadMoreListener;
    private boolean mOnLoadMoreFailed;
    private boolean mIsReachEnd;
    private String onReachEndCustomMsg = "";

    protected LoadMorRvAdapter(@NonNull Context context,
                               ItemClickListener itemClickListener,
                               @NonNull RetryLoadMoreListener retryLoadMoreListener) {

        super(context, itemClickListener);
        mRetryLoadMoreListener = retryLoadMoreListener;
        this.context = context;
    }

    public String getConcatenatedString(int str1, String str2) {

        return context.getString(str1) + ": " + str2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case TYPE_PROGRESS:
                View view = mInflater.inflate(R.layout.item_recyclerview_bottom, parent, false);
                return new BottomViewHolder(view, mRetryLoadMoreListener);
        }
        throw new RuntimeException("LoadMoreRecyclerViewAdapter: ViewHolder = null");
    }

    @Override
    public int getItemViewType(int position) {

        if (position == bottomItemPosition()) {
            return TYPE_PROGRESS;
        }
        return getCustomItemViewType(position);
    }

    private int bottomItemPosition() {
        return getItemCount() - 1;
    }

    protected abstract int getCustomItemViewType(int position);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof BottomViewHolder) {

            ((BottomViewHolder) holder).mTvNoMoreItem.setVisibility(
                    mIsReachEnd ? View.VISIBLE : View.GONE);
            ((BottomViewHolder) holder).mProgressBar.setVisibility(
                    mIsReachEnd ? View.GONE : mOnLoadMoreFailed ? View.GONE : View.VISIBLE);
            ((BottomViewHolder) holder).layoutRetry.setVisibility(
                    mIsReachEnd ? View.GONE : mOnLoadMoreFailed ? View.VISIBLE : View.GONE);

            if (mIsReachEnd) {
                if (onReachEndCustomMsg.equals("noMsg")) {
                    ((BottomViewHolder) holder).layout.setVisibility(View.GONE);
                } else {
                    ((BottomViewHolder) holder).mTvNoMoreItem.setText(onReachEndCustomMsg.isEmpty() ?
                            context.getString(R.string.no_more_item) : onReachEndCustomMsg);
                }
            }
        }
    }

    @Override
    public int getItemCount() {

        return mDataList.size() + 1;
        // +1 for progress
    }

    private static class BottomViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar mProgressBar;
        private TextView mTvNoMoreItem;
        private Button mBtnRetry;
        private View layoutRetry;
        private RetryLoadMoreListener mRetryLoadMoreListener;
        private RelativeLayout layout;

        BottomViewHolder(View itemView, RetryLoadMoreListener retryLoadMoreListener) {
            super(itemView);
            mRetryLoadMoreListener = retryLoadMoreListener;
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progress);
            layoutRetry = itemView.findViewById(R.id.layout_retry);
            mBtnRetry = (Button) itemView.findViewById(R.id.button_retry);
            mTvNoMoreItem = (TextView) itemView.findViewById(R.id.text_no_more_item);
            layout = (RelativeLayout) itemView.findViewById(R.id.layout);

            layoutRetry.setVisibility(View.GONE);
            mTvNoMoreItem.setVisibility(View.GONE);

            mBtnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRetryLoadMoreListener.onRetryLoadMore();
                }
            });
        }
    }

    public void startLoadMore() {

        mOnLoadMoreFailed = false;
        mIsReachEnd = false;
        notifyDataSetChanged();
    }

    public void onLoadMoreFailed() {

        mOnLoadMoreFailed = true;
        notifyItemChanged(bottomItemPosition());
    }

    public void onReachEnd(String customMSg) {

        this.onReachEndCustomMsg = customMSg;
        mIsReachEnd = true;
        notifyItemChanged(bottomItemPosition());
    }

    public void onReachEnd() {

        this.onReachEndCustomMsg = "";
        mIsReachEnd = true;
        notifyItemChanged(bottomItemPosition());
    }

    public void onReachEndNoMsg() {

        this.onReachEndCustomMsg = "noMsg";
        mIsReachEnd = true;
        notifyItemChanged(bottomItemPosition());
    }

    public interface RetryLoadMoreListener {

        void onRetryLoadMore();
    }
}
