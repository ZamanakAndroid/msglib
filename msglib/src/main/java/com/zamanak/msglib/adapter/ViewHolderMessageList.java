package com.zamanak.msglib.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zamanak.msglib.R;


/**
 * Created by PirFazel on 1/29/2017.
 */

public class ViewHolderMessageList extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView date;
    public TextView desc;
    public LinearLayout linearLayout;
    public TextView notifLink;

    public ViewHolderMessageList(View itemView) {

        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        date = (TextView) itemView.findViewById(R.id.date);
        desc = (TextView) itemView.findViewById(R.id.desc);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.lin);
        notifLink = (TextView) itemView.findViewById(R.id.notifLink);
    }
}
